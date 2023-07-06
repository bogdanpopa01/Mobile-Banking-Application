package com.example.mobilebankingapplication.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
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
import com.example.mobilebankingapplication.classes.User;
import com.example.mobilebankingapplication.database.DatabaseConstants;
import com.example.mobilebankingapplication.database.RequestHandler;
import com.example.mobilebankingapplication.interfaces.DepositsUpdateCallback;
import com.example.mobilebankingapplication.utils.ConverterUUID;
import com.example.mobilebankingapplication.utils.DateConverter;
import com.example.mobilebankingapplication.utils.RandomUuidGenerator;
import com.example.mobilebankingapplication.utils.SharedViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class AddDepositFragment extends DialogFragment {
    private EditText etDepositName, etDepositAmount;
    private Button btnClose, btnOpenDeposit;
    private Spinner spinnerPeriodAddDepositFragment;
    private TextView tvInterestRateValue, tvTimeLeftValue;
    private View view;
    private SharedViewModel sharedViewModel;
    private User user;
    private Context contex;
    private DepositsUpdateCallback depositsUpdateCallback;

    public void setDepositsUpdateCallback(DepositsUpdateCallback callback) {
        this.depositsUpdateCallback = callback;
    }

    public AddDepositFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.contex = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_add_deposit, container, false);
        initializeComponents();
        getUser();
        depositAmountValidation();

        spinnerPeriodAddDepositFragment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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


        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Objects.requireNonNull(getDialog()).dismiss();
            }
        });

        btnOpenDeposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validation()) {

                    // balance validation

                    BigDecimal depositAmountSafe = new BigDecimal(etDepositAmount.getText().toString());
                    BigDecimal userBalance = new BigDecimal(user.getBalance());

                    int comparisonResult = depositAmountSafe.compareTo(userBalance);

                    if (comparisonResult > 0) {
                        Toast.makeText(contex, R.string.INSUFFICIENT_BALANCE, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Deposit deposit = createDeposit();
                    if (deposit != null) {
                        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                                DatabaseConstants.URL_REGISTER_DEPOSIT,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            Toast.makeText(getContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();

                                            // to update the user
                                            double newBalance = user.getBalance() - deposit.getDepositAmount();
                                            String urlUpdateUser = DatabaseConstants.URL_UPDATE_USER + "?userId=" + user.getUserId() + "&balance=" + newBalance;

                                            StringRequest stringRequestUserBalanceAddDeposit = new StringRequest(Request.Method.PUT, urlUpdateUser, new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    try {
                                                        JSONObject jsonObject = new JSONObject(response);
//                                                        Toast.makeText(contex, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                                    } catch (JSONException e) {
                                                        throw new RuntimeException(e);
                                                    }
                                                }
                                            }, new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    Toast.makeText(contex, error.getMessage(), Toast.LENGTH_SHORT).show();
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
                                            RequestHandler.getInstance(contex).addToRequestQueue(stringRequestUserBalanceAddDeposit);
                                            user.setBalance(newBalance);
                                            sharedViewModel.setUser(user);
                                            closeAddDepositFragment();
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
                    }
                }
            }
        });

        return view;
    }

    private void initializeComponents() {
        etDepositName = view.findViewById(R.id.etDepositNameEditDepositFragment);
        etDepositAmount = view.findViewById(R.id.etDepositAmountEditDepositFragment);

        btnClose = view.findViewById(R.id.btnCancelEditDepositFragment);
        btnOpenDeposit = view.findViewById(R.id.btnEditDepositEditDepositFragment);
        spinnerPeriodAddDepositFragment = view.findViewById(R.id.spinnerDepositPeriodEditDepositFragment);

        ArrayAdapter<CharSequence> arrayAdapterSpinnerPeriod = ArrayAdapter.createFromResource(getContext(), R.array.array_add_deposits_period, android.R.layout.simple_spinner_item);
        arrayAdapterSpinnerPeriod.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPeriodAddDepositFragment.setAdapter(arrayAdapterSpinnerPeriod);

        tvInterestRateValue = view.findViewById(R.id.tvInterestRateValueEditDepositFragment);
        tvTimeLeftValue = view.findViewById(R.id.tvTimeLeftValueEditDepositFragment);
    }

    private Deposit createDeposit() {
        UUID depositId = RandomUuidGenerator.generateRandomUuid();
        String depositName = etDepositName.getText().toString();
        double depositAmount = Double.parseDouble(etDepositAmount.getText().toString());
        String depositPeriodString = tvTimeLeftValue.getText().toString();
        int depositPeriod = 1;
        switch (depositPeriodString) {
            case "2 months":
                depositPeriod = 2;
                break;
            case "3 months":
                depositPeriod = 3;
                break;
            case "6 months":
                depositPeriod = 6;
                break;
            case "12 months":
                depositPeriod = 12;
                break;
            case "24 months":
                depositPeriod = 24;
                break;

        }
        double depositInterestRate = Double.parseDouble(tvInterestRateValue.getText().toString());
        Timestamp depositDate = new Timestamp(System.currentTimeMillis());

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(depositDate);
        calendar.add(Calendar.MONTH, depositPeriod);

        Timestamp depositEndDate = new Timestamp(calendar.getTimeInMillis());

        UUID userId = user.getUserId();

        Deposit deposit = new Deposit(depositId, depositName, depositAmount, depositPeriod, depositInterestRate, depositEndDate, userId);

        if (deposit != null) {
            return deposit;
        }
        try {
            throw new Exception("Deposit is null!");
        } catch (Exception e) {
            throw new RuntimeException("Deposit is null!");
        }
    }

    private boolean validation() {
        boolean isValid = true;

        if (etDepositName.getText() == null || etDepositName.getText().toString().trim().isEmpty()) {
            etDepositName.setError("The name field cannot be empty!");
            isValid = false;
        }

        if (etDepositAmount.getText() == null || etDepositAmount.getText().toString().trim().isEmpty()) {
            etDepositAmount.setError("The amount field cannot be empty!");
            isValid = false;
        }

        if (etDepositAmount.getText().toString().length() == 1 && etDepositAmount.getText().toString().charAt(0) == '.') {
            etDepositAmount.setError("The amount field cannot contain just a dot!");
            isValid = false;
        }

        String amountString = etDepositAmount.getText().toString();
        if (amountString.matches(".+\\.0$") && Double.parseDouble(amountString)==0) {
            etDepositAmount.setError("The amount field cannot have trailing '.0'!");
            isValid = false;
        }

        if (!amountString.isEmpty() && !amountString.equals(".")) {
            double amountValue = Double.parseDouble(amountString);
            boolean isDecimalZero = amountString.matches(".*\\.0$");
            boolean isIntegerZero = amountString.matches("0+");

            if (amountValue == 0.0 && (isDecimalZero || isIntegerZero)) {
                etDepositAmount.setError("The amount field cannot be zero!");
                isValid = false;
            }
        }

        return isValid;
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

    private void closeAddDepositFragment() {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(AddDepositFragment.this);
        fragmentTransaction.commit();
        if (depositsUpdateCallback != null) {
            depositsUpdateCallback.onUpdateDeposits();
        }
    }

    private void getUser() {
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        user = sharedViewModel.getUser().getValue();
        if (user == null) {
            try {
                throw new Exception("The user in null in AddDepositFragment!");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

}