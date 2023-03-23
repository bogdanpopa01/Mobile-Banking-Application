package com.example.mobilebankingapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobilebankingapplication.R;
import com.example.mobilebankingapplication.classes.Deposit;
import com.example.mobilebankingapplication.utils.DateConverter;

import java.util.ArrayList;

public class RecyclerViewAdapterDeposits extends RecyclerView.Adapter<RecyclerViewAdapterDeposits.DepositViewHolder>{
    private ArrayList<Deposit> arrayListDeposits;
    private Context context;

    public RecyclerViewAdapterDeposits(ArrayList<Deposit> arrayListDeposits, Context context) {
        this.arrayListDeposits = arrayListDeposits;
        this.context = context;
    }

    @NonNull
    @Override
    public DepositViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_deposit_design2, parent, false);
        return new RecyclerViewAdapterDeposits.DepositViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DepositViewHolder holder, int position) {
        holder.tvDepositName2.setText(arrayListDeposits.get(position).getDepositName());
    }

    @Override
    public int getItemCount() {
        return arrayListDeposits.size();
    }

    public class DepositViewHolder extends RecyclerView.ViewHolder {
        private TextView tvDepositName2;

        public DepositViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDepositName2 = itemView.findViewById(R.id.tvDepositName2);
        }
    }
}