package com.example.mobilebankingapplication.fragments;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.mobilebankingapplication.R;
import com.example.mobilebankingapplication.classes.Deposit;
import com.example.mobilebankingapplication.classes.User;
import com.example.mobilebankingapplication.database.DatabaseConstants;
import com.example.mobilebankingapplication.database.RequestHandler;
import com.example.mobilebankingapplication.utils.SharedViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;
import java.util.UUID;


public class PopUpDeletionFragment extends DialogFragment {
    public static final String KEY_POP_UP_DELETION_FRAGMENT = "sentToPopUpDeletionFragment";
    private View view;
    private TextView tvDepositName;
    private Button btnYes, btnNo;
    private SharedViewModel sharedViewModel;
    private User user;

    public PopUpDeletionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view =  inflater.inflate(R.layout.fragment_pop_deletion, container, false);
        initializeComponenets();
        getUser();

        Bundle bundle = getArguments();
        if (bundle != null) {
            Deposit deposit = bundle.getParcelable(KEY_POP_UP_DELETION_FRAGMENT);
            if (deposit != null) {
                tvDepositName.setText(deposit.getDepositName());
            }
        }

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteDeposit(bundle);
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Objects.requireNonNull(getDialog()).dismiss();
            }
        });


        return view;
    }

    private void initializeComponenets(){
        tvDepositName = view.findViewById(R.id.tvPopUpDeletion4);
        btnYes = view.findViewById(R.id.btnYesPopUpDeletion);
        btnNo = view.findViewById(R.id.btnNoPopUpDeletion);
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

    private void deleteDeposit(Bundle bundle) {
        if (bundle != null) {
            Deposit selectedDeposit = bundle.getParcelable(KEY_POP_UP_DELETION_FRAGMENT);
            if (selectedDeposit != null) {

                UUID depositId = selectedDeposit.getDepositId();
                String url = DatabaseConstants.URL_DELETE_DEPOSIT + "?depositId=" + depositId;
                StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String message = jsonObject.getString("message");
                            Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                            closePopUpDeletionFragment();
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                RequestHandler.getInstance(getContext()).addToRequestQueue(stringRequest);
            }
        } else {
            Toast.makeText(getContext(), "The bundle is null!", Toast.LENGTH_LONG).show();
        }
    }

    private void closePopUpDeletionFragment() {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(PopUpDeletionFragment.this);
        fragmentTransaction.commit();
    }

}