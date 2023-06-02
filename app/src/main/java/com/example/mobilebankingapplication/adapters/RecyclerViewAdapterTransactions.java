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
        holder.tvTransactionType.setText(arrayListTransactions.get(position).getTransactionType().toString());
        holder.tvTransactionDate.setText(DateConverter.dateToString(arrayListTransactions.get(position).getTransactionDate()));
        holder.tvTransactionCurrency.setText("RON");
        if(arrayListTransactions.get(position).getTransactionAmount()>0f && !arrayListTransactions.get(position).getTransactionType().equals(TransactionType.TRANSFER)){
            int greenColor = ContextCompat.getColor(holder.itemView.getContext(), R.color.green);
            holder.tvTransactionAmount.setTextColor(greenColor);
        } else {
            int redColor = ContextCompat.getColor(holder.itemView.getContext(), R.color.red);
            holder.tvTransactionAmount.setTextColor(redColor);
        }

        if (arrayListTransactions.get(position).getTransactionType().equals(TransactionType.TRANSFER)) {
            holder.tvTransactionAmount.setText(String.valueOf(arrayListTransactions.get(position).getTransactionAmount()));
            int redColor = ContextCompat.getColor(holder.itemView.getContext(), R.color.red);
            holder.tvTransactionAmount.setTextColor(redColor);
        } else {
            holder.tvTransactionAmount.setText(String.valueOf(arrayListTransactions.get(position).getTransactionAmount()));
        }

    }

    @Override
    public int getItemCount() {
        return arrayListTransactions.size();
    }

    public class TransactionViewHolder extends RecyclerView.ViewHolder{
        private TextView tvTransactionType, tvTransactionDate, tvTransactionCurrency, tvTransactionAmount;

        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTransactionType = itemView.findViewById(R.id.tvTransactionType);
            tvTransactionDate = itemView.findViewById(R.id.tvTransactionDate);
            tvTransactionCurrency = itemView.findViewById(R.id.tvTransactionCurrency);
            tvTransactionAmount = itemView.findViewById(R.id.tvTransactionAmount);
        }
    }
}
