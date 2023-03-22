package com.example.mobilebankingapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobilebankingapplication.R;
import com.example.mobilebankingapplication.classes.Deposit;
import com.example.mobilebankingapplication.classes.Transaction;

import java.util.ArrayList;

public class RecyclerViewAdapterTransactions extends RecyclerView.Adapter<RecyclerViewAdapterTransactions.TransactionViewHolder>{
    private ArrayList<Transaction> arrayListTransactions;
    private Context context;

    public RecyclerViewAdapterTransactions(ArrayList<Transaction> arrayListDeposits, Context context) {
        this.arrayListTransactions = arrayListDeposits;
        this.context = context;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_transaction_design, parent, false);
        return new RecyclerViewAdapterTransactions.TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        holder.tvTransactionName.setText(arrayListTransactions.get(position).getTransactionName());
    }

    @Override
    public int getItemCount() {
        return arrayListTransactions.size();
    }

    public class TransactionViewHolder extends RecyclerView.ViewHolder{
        private TextView tvTransactionName;

        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTransactionName = itemView.findViewById(R.id.tvTransactionName);
        }
    }
}
