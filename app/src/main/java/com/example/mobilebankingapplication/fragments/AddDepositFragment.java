package com.example.mobilebankingapplication.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
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
import com.example.mobilebankingapplication.classes.User;
import com.example.mobilebankingapplication.database.DatabaseConstants;
import com.example.mobilebankingapplication.database.RequestHandler;
import com.example.mobilebankingapplication.utils.ConverterUUID;
import com.example.mobilebankingapplication.utils.DateConverter;
import com.example.mobilebankingapplication.utils.RandomUuidGenerator;
import com.example.mobilebankingapplication.utils.SharedViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
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

    public AddDepositFragment() {
        // Required empty public constructor
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
                switch (position){
                    case 0:
                        tvInterestRateValue.setText("0.04");
                        tvTimeLeftValue.setText("1 month");
                        break;
                    case 1:
                        tvInterestRateValue.setText("0.045");
                        tvTimeLeftValue.setText("2 months");
                        break;
                    case 2:
                        tvInterestRateValue.setText("0.05");
                        tvTimeLeftValue.setText("3 months");
                        break;
                    case 3:
                        tvInterestRateValue.setText("0.055");
                        tvTimeLeftValue.setText("6 months");
                        break;
                    case 4:
                        tvInterestRateValue.setText("0.08");
                        tvTimeLeftValue.setText("12 months");
                        break;
                    case 5:
                        tvInterestRateValue.setText("0.09");
                        tvTimeLeftValue.setText("24 months");
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
                                params.put("depositAmount", String.valueOf(deposit.getDepositAmount()) );
                                params.put("depositPeriod",String.valueOf(deposit.getDepositPeriod()));
                                params.put("depositInterestRate", String.valueOf(deposit.getDepositInterestRate()));
                                params.put("depositTimeLeft", DateConverter.timestampToString(deposit.getDepositTimeLeft()));
                                params.put("userId",  ConverterUUID.UUIDtoString(deposit.getUserId()));
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

    private Deposit createDeposit(){
        UUID depositId = RandomUuidGenerator.generateRandomUuid();
        String depositName = etDepositName.getText().toString();
        double depositAmount = Double.parseDouble(etDepositAmount.getText().toString());
        String depositPeriodString = tvTimeLeftValue.getText().toString();
        int depositPeriod=1;
        switch (depositPeriodString){
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
        UUID userId = user.getUserId();

        Deposit deposit = new Deposit(depositId,depositName,depositAmount,depositPeriod,depositInterestRate,depositDate,userId);

        if(deposit != null){
            return deposit;
        }
        try {
            throw new Exception("Deposit is null!");
        } catch (Exception e) {
            throw new RuntimeException("Deposit is null!");
        }
    }

    private boolean validation(){
        boolean isValid = true;

        if(etDepositName.getText() == null || etDepositName.getText().toString().trim().isEmpty()){
            etDepositName.setError("The name field cannot be empty!");
            isValid = false;
        }

        if(etDepositAmount.getText() == null || etDepositAmount.getText().toString().trim().isEmpty()){
            etDepositAmount.setError("The amount field cannot be empty!");
            isValid = false;
        }

        return isValid;
    }

    // to ensure that the user enters only numbers
    private void depositAmountValidation(){

        etDepositAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 0 && !isValidDepositAmount(s.toString())){
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

    private String validString(String string){
        return string.replaceAll("[^0-9.]", "");
    }

    private void closeAddDepositFragment(){
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(AddDepositFragment.this);
        fragmentTransaction.commit();
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