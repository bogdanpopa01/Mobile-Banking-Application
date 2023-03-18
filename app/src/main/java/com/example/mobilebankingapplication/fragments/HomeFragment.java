package com.example.mobilebankingapplication.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mobilebankingapplication.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class HomeFragment extends Fragment {
    FloatingActionButton fabSeeDeposits, fabAddDeposit;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_home, container, false);

        fabSeeDeposits = view.findViewById(R.id.fabSeeDeposits);
        fabSeeDeposits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getChildFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.depositsFragment,new DepositsFragment());
                fragmentTransaction.commit();
            }
        });
        fabAddDeposit = view.findViewById(R.id.fabAddDeposit);
        fabAddDeposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getChildFragmentManager();
                AddDepositFragment addDepositFragment = new AddDepositFragment();
                addDepositFragment.show(fragmentManager,"AddDepositFragment");
            }
        });

        return view;
    }

}