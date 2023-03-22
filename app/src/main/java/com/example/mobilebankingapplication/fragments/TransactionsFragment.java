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
import com.example.mobilebankingapplication.adapters.RecyclerViewAdapterTransactions;
import com.example.mobilebankingapplication.classes.Deposit;
import com.example.mobilebankingapplication.classes.Transaction;
import com.example.mobilebankingapplication.enums.TransactionType;

import java.util.ArrayList;
import java.util.Date;

public class TransactionsFragment extends Fragment {
    private RecyclerView recyclerViewTransactions;
    private RecyclerViewAdapterTransactions recyclerViewAdapterTransactions;
    private ArrayList<Transaction> arrayListTransactions = new ArrayList<>();

    public TransactionsFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Transaction transaction = new Transaction(1000L,"Tran",12.3,new Date(), TransactionType.MARE,1000);
        arrayListTransactions.add(transaction);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_transactions, container, false);



        recyclerViewTransactions = view.findViewById(R.id.recyclerViewTransactions);
        recyclerViewTransactions.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerViewAdapterTransactions = new RecyclerViewAdapterTransactions(arrayListTransactions,getContext());
        recyclerViewTransactions.setAdapter(recyclerViewAdapterTransactions);

        return view;
    }
}