package com.example.mobilebankingapplication.fragments;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.mobilebankingapplication.R;

import java.util.Objects;

public class AddDepositFragment extends DialogFragment {
    EditText etDepositName, etDepositAmount;
    Button btnClose, btnOpenDeposit;

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

        View view = inflater.inflate(R.layout.fragment_add_deposit, container, false);

        etDepositName = view.findViewById(R.id.etDepositNameAddDepositFragment);
        etDepositAmount = view.findViewById(R.id.etDepositAmountAddDepositFragment);
        btnClose = view.findViewById(R.id.btnCloseAddDepositFragment);
        btnOpenDeposit = view.findViewById(R.id.btnOpenDepositAddDepositFragment);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Objects.requireNonNull(getDialog()).dismiss();
            }
        });

        btnOpenDeposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Objects.requireNonNull(getDialog()).dismiss();
            }
        });

        return view;
    }
}