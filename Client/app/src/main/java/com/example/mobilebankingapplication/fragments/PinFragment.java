package com.example.mobilebankingapplication.fragments;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobilebankingapplication.R;
import com.example.mobilebankingapplication.classes.User;
import com.example.mobilebankingapplication.utils.SharedViewModel;

import org.apache.poi.ss.formula.functions.T;


public class PinFragment extends DialogFragment {
    private SharedViewModel sharedViewModel;
    private User user;
    private TextView tvPin;
    private Button btnConfirm;
    private EditText etPassword;
    private View view;
    private int wrongPasswordCount = 0;

    public PinFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_pin, container, false);
        initilizeComponents();


        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etPassword.getText() != null && !etPassword.getText().toString().trim().isEmpty()) {
                    if (etPassword.getText().toString().equals(user.getPassword())) {
                        tvPin.setText(user.getCardCvv());
                    } else {
                        wrongPasswordCount++;
                        if (wrongPasswordCount == 3) {
                            getActivity().finish();
                        } else {
                            Toast.makeText(getContext(), "Wrong password! Remaining attempts: " + String.valueOf(3 - wrongPasswordCount), Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(getContext(), "The password field is empty!", Toast.LENGTH_SHORT).show();
                }
            }
        });


        return view;
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

    private void initilizeComponents() {
        tvPin = view.findViewById(R.id.tvPinValue);
        etPassword = view.findViewById(R.id.etPinPassword);
        btnConfirm = view.findViewById(R.id.btnConfirmPasswordForPin);
    }
}