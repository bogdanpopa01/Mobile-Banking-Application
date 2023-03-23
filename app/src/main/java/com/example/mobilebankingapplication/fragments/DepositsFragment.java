package com.example.mobilebankingapplication.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mobilebankingapplication.R;
import com.example.mobilebankingapplication.adapters.RecyclerViewAdapterDeposits;
import com.example.mobilebankingapplication.classes.Deposit;

import java.util.ArrayList;
import java.util.Date;


public class DepositsFragment extends Fragment {
    private RecyclerView recyclerViewDeposits;
    private RecyclerViewAdapterDeposits recyclerViewAdapterDeposits;
    private ArrayList<Deposit> arrayListDeposits = new ArrayList<>();

    public DepositsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Deposit exampleDeposit = new Deposit(1000L,"Vacation",1230.50d,6,0.1,new Date(),1000);
        Deposit exampleDeposit2 = new Deposit(1000L,"Car",3230.50d,6,0.1,new Date(),1000);
        arrayListDeposits.add(exampleDeposit);
        arrayListDeposits.add(exampleDeposit2);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_deposits, container, false);



        recyclerViewDeposits = view.findViewById(R.id.recyclerViewDeposits);
        recyclerViewDeposits.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerViewAdapterDeposits = new RecyclerViewAdapterDeposits(arrayListDeposits,getContext());
        recyclerViewDeposits.setAdapter(recyclerViewAdapterDeposits);

        return view;
    }
}