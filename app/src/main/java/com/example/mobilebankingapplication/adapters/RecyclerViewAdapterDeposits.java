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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_deposit_design, parent, false);
        return new DepositViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DepositViewHolder holder, int position) {
        holder.tvDepositName.setText(arrayListDeposits.get(position).getDepositName());
        holder.tvCurrency.setText("RON");
        holder.tvDepositAmount.setText(String.valueOf(arrayListDeposits.get(position).getDepositAmount()));
        holder.tvInterestRateValue.setText(String.valueOf(arrayListDeposits.get(position).getDepositInterestRate()));
        holder.tvTimeLeftValue.setText("da");
        holder.imageViewDeposit.setImageResource(R.drawable.baseline_deposit_24);
    }

    @Override
    public int getItemCount() {
        return arrayListDeposits.size();
    }

    public class DepositViewHolder extends RecyclerView.ViewHolder {
        private TextView tvDepositName, tvDepositAmount, tvCurrency, tvInterestRateValue,tvTimeLeftValue;
        private ImageView imageViewDeposit;

        public DepositViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDepositName = itemView.findViewById(R.id.tvDepositName);
            tvDepositAmount = itemView.findViewById(R.id.tvDepositAmount);
            tvCurrency = itemView.findViewById(R.id.tvCurrency);
            tvInterestRateValue = itemView.findViewById(R.id.tvInterestRateValue);
            tvTimeLeftValue = itemView.findViewById(R.id.tvTimeLeftValue);
            imageViewDeposit = itemView.findViewById(R.id.imageViewDeposit);
        }
    }
}
