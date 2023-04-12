package com.example.mobilebankingapplication.fragments;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

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

import com.example.mobilebankingapplication.R;
import com.example.mobilebankingapplication.classes.Deposit;
import com.example.mobilebankingapplication.utils.DateConverter;

import java.util.Objects;


public class EditDepositFragment extends DialogFragment {
    public static final String KEY_SEND_DEPOSIT_TO_EDIT = "sendDepositToEdit";
    private EditText etDepositName, etDepositAmount;
    private Button btnCancel, btnEditDeposit, btnDeleteDeposit;
    private Spinner spinnerPeriodEditDepositFragment;
    private TextView tvInterestRateValue, tvTimeLeftValue;
    private View view;


    public EditDepositFragment() {
        // Required empty public constructor
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
        depositAmountValidation();

        spinnerPeriodEditDepositFragment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Objects.requireNonNull(getDialog()).dismiss();
            }
        });

        Bundle bundle = getArguments();
        populateFields(bundle);

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

    private void populateFields(Bundle bundle){
        if (bundle != null) {
            Deposit selectedDeposit = bundle.getParcelable(KEY_SEND_DEPOSIT_TO_EDIT);
            if(selectedDeposit!=null){
                etDepositAmount.setText(String.valueOf(selectedDeposit.getDepositAmount()));
                etDepositName.setText(selectedDeposit.getDepositName());
                double interestRateValue = selectedDeposit.getDepositInterestRate();
                tvInterestRateValue.setText(String.valueOf(interestRateValue));
                tvTimeLeftValue.setText(DateConverter.dateToString(selectedDeposit.getDepositTimeLeft()));
                int priorSpinnerSelectedValue = 0;
                double epsilone = 0.0001;
                if(Math.abs(interestRateValue - 0.045) < epsilone){
                    priorSpinnerSelectedValue = 1;
                } else if(Math.abs(interestRateValue - 0.05) < epsilone){
                    priorSpinnerSelectedValue = 2;
                } else if(Math.abs(interestRateValue - 0.055) < epsilone){
                    priorSpinnerSelectedValue = 3;
                } else if(Math.abs(interestRateValue - 0.08) < epsilone){
                    priorSpinnerSelectedValue = 4;
                } else if(Math.abs(interestRateValue - 0.09) < epsilone){
                    priorSpinnerSelectedValue = 5;
                }
                spinnerPeriodEditDepositFragment.setSelection(priorSpinnerSelectedValue);
            } else {
                Toast.makeText(getContext(),"The deposit wasn't sent!",Toast.LENGTH_SHORT).show();
            }
        }
    }
}