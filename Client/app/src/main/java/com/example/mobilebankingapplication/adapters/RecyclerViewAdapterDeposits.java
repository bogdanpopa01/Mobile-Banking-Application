package com.example.mobilebankingapplication.adapters;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobilebankingapplication.R;
import com.example.mobilebankingapplication.classes.Deposit;
import com.example.mobilebankingapplication.fragments.AddDepositFragment;
import com.example.mobilebankingapplication.fragments.EditDepositFragment;
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
        return new RecyclerViewAdapterDeposits.DepositViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DepositViewHolder holder, int position) {
        holder.tvDepositName.setText(arrayListDeposits.get(position).getDepositName());
        holder.tvDepositAmount.setText(String.valueOf(arrayListDeposits.get(position).getDepositAmount()));
        holder.tvDepositCurrency.setText("RON");
        try {
            holder.tvDepositTimeLeftValue.setText(DateConverter.dateToString(arrayListDeposits.get(position).getDepositTimeLeft()));
        } catch (Exception e) {
            Log.e("Date conversion error", e.getMessage());
            holder.tvDepositTimeLeftValue.setText("N/A");
        }
        holder.tvDepositInterestRateValue.setText(String.valueOf(arrayListDeposits.get(position).getDepositInterestRate()));

        // to be able to modify the deposit
        holder.rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                if(position != RecyclerView.NO_POSITION){
                    Deposit selectedDeposit = arrayListDeposits.get(position);
                    FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                    EditDepositFragment editDepositFragment = new EditDepositFragment();

                    Bundle bundle = new Bundle();
                    bundle.putParcelable(EditDepositFragment.KEY_SEND_DEPOSIT_TO_EDIT, selectedDeposit);
                    editDepositFragment.setArguments(bundle);

                    editDepositFragment.show(fragmentManager,"EditDepositFragment");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayListDeposits.size();
    }

    public class DepositViewHolder extends RecyclerView.ViewHolder {
        private TextView tvDepositName, tvDepositAmount, tvDepositCurrency, tvDepositTimeLeftValue, tvDepositInterestRateValue;
        private ConstraintLayout rootLayout;

        public DepositViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDepositName = itemView.findViewById(R.id.tvDepositName);
            tvDepositAmount = itemView.findViewById(R.id.tvDepositAmount);
            tvDepositCurrency = itemView.findViewById(R.id.tvDepositCurrency);
            tvDepositTimeLeftValue = itemView.findViewById(R.id.tvDepositTimeLeftValue);
            tvDepositInterestRateValue = itemView.findViewById(R.id.tvDepositInterestRateValue);
            rootLayout = itemView.findViewById(R.id.rootLayout);
        }
    }
}