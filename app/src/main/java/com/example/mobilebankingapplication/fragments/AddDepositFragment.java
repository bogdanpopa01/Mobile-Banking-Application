package com.example.mobilebankingapplication.fragments;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

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



}