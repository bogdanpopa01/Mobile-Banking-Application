package com.example.mobilebankingapplication.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.mobilebankingapplication.R;
import com.example.mobilebankingapplication.classes.Deposit;
import com.example.mobilebankingapplication.classes.DepositDeleteEvent;
import com.example.mobilebankingapplication.classes.DepositUpdatedEvent;
import com.example.mobilebankingapplication.classes.User;
import com.example.mobilebankingapplication.database.DatabaseConstants;
import com.example.mobilebankingapplication.database.RequestHandler;
import com.example.mobilebankingapplication.interfaces.DeletionCallback;
import com.example.mobilebankingapplication.utils.ConverterUUID;
import com.example.mobilebankingapplication.utils.DateConverter;
import com.example.mobilebankingapplication.utils.SharedViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import io.reactivex.rxjava3.subjects.PublishSubject;


public class EditDepositFragment extends DialogFragment implements DeletionCallback {
    public static final String KEY_SEND_DEPOSIT_TO_EDIT = "sendDepositToEdit";
    private EditText etDepositName, etDepositAmount;
    private Button btnCancel, btnEditDeposit, btnDeleteDeposit;
    private Spinner spinnerPeriodEditDepositFragment;
    private TextView tvInterestRateValue, tvTimeLeftValue;
    private SharedViewModel sharedViewModel;
    private User user;
    private View view;
    private Context context;
    public static PublishSubject<DepositUpdatedEvent> updateEventSubject = PublishSubject.create();

    public EditDepositFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_edit_deposit, container, false);
        initializeComponents();
        getUser();
        depositAmountValidation();

        spinnerPeriodEditDepositFragment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        tvInterestRateValue.setText(getString(R.string.oneMonthValue));
                        tvTimeLeftValue.setText(getString(R.string.oneMonth));
                        break;
                    case 1:
                        tvInterestRateValue.setText(getText(R.string.twoMonthValue));
                        tvTimeLeftValue.setText(getText(R.string.twoMonth));
                        break;
                    case 2:
                        tvInterestRateValue.setText(getText(R.string.threeMonthValue));
                        tvTimeLeftValue.setText(getText(R.string.twoMonth));
                        break;
                    case 3:
                        tvInterestRateValue.setText(getText(R.string.sixMonthValue));
                        tvTimeLeftValue.setText(getText(R.string.twoMonth));
                        break;
                    case 4:
                        tvInterestRateValue.setText(getText(R.string.oneYearValue));
                        tvTimeLeftValue.setText(getText(R.string.oneYear));
                        break;
                    case 5:
                        tvInterestRateValue.setText(getText(R.string.twoYearValue));
                        tvTimeLeftValue.setText(getText(R.string.twoYear));
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerPeriodEditDepositFragment.setEnabled(false);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Objects.requireNonNull(getDialog()).dismiss();
            }
        });

        Bundle bundle = getArguments();
        populateFields(bundle);

        btnDeleteDeposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Deposit deposit = bundle.getParcelable(KEY_SEND_DEPOSIT_TO_EDIT);
                if (deposit != null) {
                    FragmentManager fragmentManager = getChildFragmentManager();

                    PopUpDeletionFragment popUpFragmentDeletion = new PopUpDeletionFragment();
                    Bundle deletionBundle = new Bundle();
                    deletionBundle.putParcelable(PopUpDeletionFragment.KEY_POP_UP_DELETION_FRAGMENT, deposit);
                    popUpFragmentDeletion.setArguments(deletionBundle);
                    popUpFragmentDeletion.setDeletionCallback(EditDepositFragment.this); // Set the callback
                    popUpFragmentDeletion.show(fragmentManager, "PopUpDeletionFragment");
                }
            }
        });

        btnEditDeposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateDeposit(bundle);
                updateEventSubject.onNext(new DepositUpdatedEvent(true));
            }
        });

        return view;
    }

    private void initializeComponents() {
        etDepositName = view.findViewById(R.id.etDepositNameEditDepositFragment);
        etDepositAmount = view.findViewById(R.id.etDepositAmountEditDepositFragment);
        btnDeleteDeposit = view.findViewById(R.id.btnDeleteDepositEditDepositFragment);

        btnCancel = view.findViewById(R.id.btnCancelEditDepositFragment);
        btnEditDeposit = view.findViewById(R.id.btnEditDepositEditDepositFragment);
        spinnerPeriodEditDepositFragment = view.findViewById(R.id.spinnerDepositPeriodEditDepositFragment);

        ArrayAdapter<CharSequence> arrayAdapterSpinnerPeriod = ArrayAdapter.createFromResource(getContext(), R.array.array_add_deposits_period, android.R.layout.simple_spinner_item);
        arrayAdapterSpinnerPeriod.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPeriodEditDepositFragment.setAdapter(arrayAdapterSpinnerPeriod);

        tvInterestRateValue = view.findViewById(R.id.tvInterestRateValueEditDepositFragment);
        tvTimeLeftValue = view.findViewById(R.id.tvTimeLeftValueEditDepositFragment);
    }

    // to ensure that the user enters only numbers
    private void depositAmountValidation() {

        etDepositAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0 && !isValidDepositAmount(s.toString())) {
                    String string = validString(s.toString());
                    etDepositAmount.setText(string);
                    etDepositAmount.setSelection(string.length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private boolean isValidDepositAmount(String string) {
        return string.matches("[0-9.]*");
    }

    private String validString(String string) {
        return string.replaceAll("[^0-9.]", "");
    }

    private void populateFields(Bundle bundle) {
        if (bundle != null) {
            Deposit selectedDeposit = bundle.getParcelable(KEY_SEND_DEPOSIT_TO_EDIT);
            if (selectedDeposit != null) {
                etDepositAmount.setText(String.valueOf(selectedDeposit.getDepositAmount()));
                etDepositName.setText(selectedDeposit.getDepositName());
                double interestRateValue = selectedDeposit.getDepositInterestRate();
                tvInterestRateValue.setText(String.valueOf(interestRateValue));
                tvTimeLeftValue.setText(DateConverter.dateToString(selectedDeposit.getDepositTimeLeft()));
                int priorSpinnerSelectedValue = 0;
                double epsilon = 0.0001;
                if (Math.abs(interestRateValue - 0.045) < epsilon) {
                    priorSpinnerSelectedValue = 1;
                } else if (Math.abs(interestRateValue - 0.05) < epsilon) {
                    priorSpinnerSelectedValue = 2;
                } else if (Math.abs(interestRateValue - 0.055) < epsilon) {
                    priorSpinnerSelectedValue = 3;
                } else if (Math.abs(interestRateValue - 0.08) < epsilon) {
                    priorSpinnerSelectedValue = 4;
                } else if (Math.abs(interestRateValue - 0.09) < epsilon) {
                    priorSpinnerSelectedValue = 5;
                }
                spinnerPeriodEditDepositFragment.setSelection(priorSpinnerSelectedValue);
            } else {
                Toast.makeText(getContext(), "The deposit wasn't sent!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void closeEditDepositFragment() {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(EditDepositFragment.this);
        fragmentTransaction.commit();
    }

    private void updateDeposit(Bundle bundle) {
        if (bundle != null) {
            Deposit selectedDeposit = bundle.getParcelable(KEY_SEND_DEPOSIT_TO_EDIT);
            if (selectedDeposit != null) {
                double oldDepositAmount = selectedDeposit.getDepositAmount();
                UUID depositId = selectedDeposit.getDepositId();
                String depositName = etDepositName.getText().toString();
                double depositAmount = Double.parseDouble(etDepositAmount.getText().toString());

                // balance validation
                BigDecimal depositAmountSafe = BigDecimal.valueOf(depositAmount);
                BigDecimal userBalance = BigDecimal.valueOf(user.getBalance());
                int comparisonResult = depositAmountSafe.compareTo(userBalance);

                if (comparisonResult > 0) {
                    Toast.makeText(getContext(), R.string.INSUFFICIENT_BALANCE, Toast.LENGTH_SHORT).show();
                    return;
                }
                int depositPeriod = selectedDeposit.getDepositPeriod();
                double depositInterestRate = selectedDeposit.getDepositInterestRate();
                Timestamp depositTimeLeft = selectedDeposit.getDepositTimeLeft();
                UUID userId = selectedDeposit.getUserId();

                if (!depositName.equals(selectedDeposit.getDepositName()) || depositAmount != selectedDeposit.getDepositAmount()) {

                    Deposit deposit = new Deposit(depositId, depositName, depositAmount, depositPeriod, depositInterestRate, depositTimeLeft, userId);

                    String url = DatabaseConstants.URL_UPDATE_DEPOSIT + "?depositId=" + depositId;
                    StringRequest stringRequest = new StringRequest(Request.Method.PUT, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        Toast.makeText(getContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();

                                        // to update the user
                                        double depositAmountDifference = depositAmount - oldDepositAmount;
                                        double newBalance = user.getBalance() - depositAmountDifference;
                                        String urlUpdateUser = DatabaseConstants.URL_UPDATE_USER + "?userId=" + user.getUserId() + "&balance=" + newBalance;
                                        StringRequest stringRequestEditDeposit = new StringRequest(Request.Method.PUT, urlUpdateUser, new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                try {
                                                    JSONObject jsonObject = new JSONObject(response);
//                                                    Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                                } catch (JSONException e) {
                                                    throw new RuntimeException(e);
                                                }
                                            }
                                        }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }) {
                                            @Nullable
                                            @Override
                                            protected Map<String, String> getParams() throws AuthFailureError {
                                                Map<String, String> params = new HashMap<>();
                                                params.put("userId", ConverterUUID.UUIDtoString(user.getUserId()));
                                                params.put("balance", String.valueOf(newBalance));
                                                return params;
                                            }
                                        };
                                        RequestHandler.getInstance(context).addToRequestQueue(stringRequestEditDeposit);
                                        user.setBalance(newBalance);
                                        sharedViewModel.setUser(user);
                                        closeEditDepositFragment();
                                    } catch (JSONException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }) {
                        @Nullable
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("depositId", ConverterUUID.UUIDtoString(deposit.getDepositId()));
                            params.put("depositName", deposit.getDepositName());
                            params.put("depositAmount", String.valueOf(deposit.getDepositAmount()));
                            params.put("depositPeriod", String.valueOf(deposit.getDepositPeriod()));
                            params.put("depositInterestRate", String.valueOf(deposit.getDepositInterestRate()));
                            params.put("depositTimeLeft", DateConverter.timestampToString(deposit.getDepositTimeLeft()));
                            params.put("userId", ConverterUUID.UUIDtoString(deposit.getUserId()));
                            return params;
                        }
                    };
                    RequestHandler.getInstance(getContext()).addToRequestQueue(stringRequest);
                } else {
                    Toast.makeText(getContext(), "The name field is the same!", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getContext(), "The selectedDeposit is null!", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getContext(), "The bundle is null!", Toast.LENGTH_LONG).show();
        }
    }

    private void getUser() {
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        user = sharedViewModel.getUser().getValue();
        if (user == null) {
            try {
                throw new Exception("The user in null in HomeFragment!");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void onDeleteConfirmed() {
        closeEditDepositFragment();
    }
}