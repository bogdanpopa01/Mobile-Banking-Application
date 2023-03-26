package com.example.mobilebankingapplication.fragments;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextUtils;
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

import com.example.mobilebankingapplication.R;
import com.example.mobilebankingapplication.classes.Deposit;
import com.example.mobilebankingapplication.utils.RandomLongGenerator;

import java.util.Date;
import java.util.Objects;

public class AddDepositFragment extends DialogFragment {
    private EditText etDepositName, etDepositAmount;
    private Button btnClose, btnOpenDeposit;
    private Spinner spinnerPeriodAddDepositFragment;
    private TextView tvInterestRateValue, tvTimeLeftValue;
    private View view;

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
        depositAmountValidation();

        spinnerPeriodAddDepositFragment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        tvInterestRateValue.setText("0.4");
                        tvTimeLeftValue.setText("1 month");
                        break;
                    case 1:
                        tvInterestRateValue.setText("0.45");
                        tvTimeLeftValue.setText("2 months");
                        break;
                    case 2:
                        tvInterestRateValue.setText("0.5");
                        tvTimeLeftValue.setText("3 months");
                        break;
                    case 3:
                        tvInterestRateValue.setText("0.55");
                        tvTimeLeftValue.setText("6 months");
                        break;
                    case 4:
                        tvInterestRateValue.setText("0.8");
                        tvTimeLeftValue.setText("12 months");
                        break;
                    case 5:
                        tvInterestRateValue.setText("0.9");
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
                if(validation()){
                    Deposit deposit = createDeposit();
                    Toast.makeText(getContext(),"The deposit was created!",Toast.LENGTH_SHORT).show();
                    if(deposit!=null){
                        Bundle bundle = new Bundle();
                        bundle.putParcelable(DepositsFragment.KEY_SEND_DEPOSIT,deposit);
                        DepositsFragment depositsFragment = new DepositsFragment();
                        depositsFragment.setArguments(bundle);
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.depositsFragment, depositsFragment).commit();
                    }
                    if(deposit!=null){
                        Objects.requireNonNull(getDialog()).dismiss();
                    }
                }

            }

        });
        return view;
    }

    private void initializeComponents() {
        etDepositName = view.findViewById(R.id.etDepositNameAddDepositFragment);
        etDepositAmount = view.findViewById(R.id.etDepositAmountAddDepositFragment);

        btnClose = view.findViewById(R.id.btnCloseAddDepositFragment);
        btnOpenDeposit = view.findViewById(R.id.btnOpenDepositAddDepositFragment);
        spinnerPeriodAddDepositFragment = view.findViewById(R.id.spinnerDepositPeriodAddDepositFragment);

        ArrayAdapter<CharSequence> arrayAdapterSpinnerPeriod = ArrayAdapter.createFromResource(getContext(), R.array.array_add_deposits_period, android.R.layout.simple_spinner_item);
        arrayAdapterSpinnerPeriod.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPeriodAddDepositFragment.setAdapter(arrayAdapterSpinnerPeriod);

        tvInterestRateValue = view.findViewById(R.id.tvInterestRateValueAddDepositFragment);
        tvTimeLeftValue = view.findViewById(R.id.tvTimeLeftValueAddDepositFragment);
    }

    private Deposit createDeposit(){
        long depositId = RandomLongGenerator.generateLong();
        String depositName = etDepositName.getText().toString();
        double depositAmount = Double.parseDouble(etDepositAmount.getText().toString());
        String depositPeriodString = tvTimeLeftValue.getText().toString();
        int depositPeriod=1;
        switch (depositPeriodString){
            case "1 month":
                depositPeriod = 1;
                break;
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
        Date depositDate = new Date();
        long userId = RandomLongGenerator.generateLong();

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




}