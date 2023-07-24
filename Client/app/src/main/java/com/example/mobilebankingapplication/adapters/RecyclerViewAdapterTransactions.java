package com.example.mobilebankingapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobilebankingapplication.R;
import com.example.mobilebankingapplication.classes.Transaction;
import com.example.mobilebankingapplication.enums.TransactionType;
import com.example.mobilebankingapplication.utils.DateConverter;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapterTransactions extends RecyclerView.Adapter<RecyclerViewAdapterTransactions.TransactionViewHolder> {
    private List<Transaction> transactions;
    private Context context;

    public RecyclerViewAdapterTransactions(List<Transaction> transactions, Context context) {
        this.transactions = transactions;
        this.context = context;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_transaction_design, parent, false);
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        Transaction transaction = transactions.get(position);
        holder.tvTransactionName.setText(transaction.getTransactionName());
        holder.tvTransactionDate.setText(DateConverter.dateToString(transaction.getTransactionDate()));
        holder.tvTransactionCurrency.setText("RON");

        if (transaction.getTransactionAmount() > 0f && !transaction.getTransactionType().equals(TransactionType.TRANSFER)) {
            int greenColor = ContextCompat.getColor(holder.itemView.getContext(), R.color.green);
            holder.tvTransactionAmount.setTextColor(greenColor);
        } else {
            int redColor = ContextCompat.getColor(holder.itemView.getContext(), R.color.red);
            holder.tvTransactionAmount.setTextColor(redColor);
        }

        if (transaction.getTransactionType().equals(TransactionType.TRANSFER)) {
            holder.tvTransactionAmount.setText(String.valueOf(transaction.getTransactionAmount()));
            int redColor = ContextCompat.getColor(holder.itemView.getContext(), R.color.red);
            holder.tvTransactionAmount.setTextColor(redColor);
        } else {
            holder.tvTransactionAmount.setText(String.valueOf(transaction.getTransactionAmount()));
        }
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public static class TransactionViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTransactionName, tvTransactionDate, tvTransactionCurrency, tvTransactionAmount;

        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTransactionName = itemView.findViewById(R.id.tvTransactionName);
            tvTransactionDate = itemView.findViewById(R.id.tvTransactionDate);
            tvTransactionCurrency = itemView.findViewById(R.id.tvTransactionCurrency);
            tvTransactionAmount = itemView.findViewById(R.id.tvTransactionAmount);
        }
    }
}
