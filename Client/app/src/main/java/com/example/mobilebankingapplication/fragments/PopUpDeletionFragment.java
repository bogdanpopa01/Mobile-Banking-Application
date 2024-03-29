package com.example.mobilebankingapplication.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.mobilebankingapplication.R;
import com.example.mobilebankingapplication.classes.Deposit;
import com.example.mobilebankingapplication.classes.DepositDeleteEvent;
import com.example.mobilebankingapplication.classes.User;
import com.example.mobilebankingapplication.database.DatabaseConstants;
import com.example.mobilebankingapplication.database.RequestHandler;
import com.example.mobilebankingapplication.interfaces.DeletionCallback;
import com.example.mobilebankingapplication.utils.ConverterUUID;
import com.example.mobilebankingapplication.utils.SharedViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import io.reactivex.rxjava3.subjects.PublishSubject;


public class PopUpDeletionFragment extends DialogFragment {
    public static final String KEY_POP_UP_DELETION_FRAGMENT = "sentToPopUpDeletionFragment";
    private View view;
    private TextView tvDepositName;
    private Button btnYes, btnNo;
    private SharedViewModel sharedViewModel;
    private User user;
    private DeletionCallback deletionCallback;
    private Context context;
    public static PublishSubject<DepositDeleteEvent> deleteEventSubject = PublishSubject.create();

    public void setDeletionCallback(DeletionCallback deletionCallback) {
        this.deletionCallback = deletionCallback;
    }

    public PopUpDeletionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
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
                if (deletionCallback != null) {
                    deletionCallback.onDeleteConfirmed();
                }
                deleteEventSubject.onNext(new DepositDeleteEvent(true));
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
                            Toast.makeText(context, message, Toast.LENGTH_LONG).show();

                            // to update the user
                            double newBalance = user.getBalance() + selectedDeposit.getDepositAmount();
                            String urlUpdateUser = DatabaseConstants.URL_UPDATE_USER + "?userId=" + user.getUserId() + "&balance=" + newBalance;
                            StringRequest stringRequestDeleteDeposit = new StringRequest(Request.Method.PUT, urlUpdateUser, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
//                                        Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                    } catch (JSONException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }) {
                                @Nullable
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String, String> params = new HashMap<>();
                                    params.put("userId", ConverterUUID.UUIDtoString(user.getUserId()));
                                    params.put("balance", String.valueOf(newBalance));
                                    return params;
                                }
                            };
                            RequestHandler.getInstance(context).addToRequestQueue(stringRequestDeleteDeposit);
                            user.setBalance(newBalance);
                            sharedViewModel.setUser(user);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                RequestHandler.getInstance(context).addToRequestQueue(stringRequest);
            }
        } else {
            Toast.makeText(context, "The bundle is null!", Toast.LENGTH_LONG).show();
        }
    }
}