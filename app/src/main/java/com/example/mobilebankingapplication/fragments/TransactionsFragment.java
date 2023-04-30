package com.example.mobilebankingapplication.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mobilebankingapplication.R;
import com.example.mobilebankingapplication.adapters.RecyclerViewAdapterDeposits;
import com.example.mobilebankingapplication.adapters.RecyclerViewAdapterTransactions;
import com.example.mobilebankingapplication.classes.Deposit;
import com.example.mobilebankingapplication.classes.Transaction;
import com.example.mobilebankingapplication.classes.Transfer;
import com.example.mobilebankingapplication.classes.User;
import com.example.mobilebankingapplication.database.Constants;
import com.example.mobilebankingapplication.enums.TransactionType;
import com.example.mobilebankingapplication.utils.ConverterUUID;
import com.example.mobilebankingapplication.utils.DateConverter;
import com.example.mobilebankingapplication.utils.RandomLongGenerator;
import com.example.mobilebankingapplication.utils.SharedViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class TransactionsFragment extends Fragment {
    public static final String KEY_SEND_TRANSACTION = "sendTransaction";
    private RecyclerView recyclerViewTransactions;
    private RecyclerViewAdapterTransactions recyclerViewAdapterTransactions;
    private ArrayList<Transaction> arrayListTransactions = new ArrayList<>();
    public TransactionsFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        String url = Constants.URL_GET_ALL_TRANSACTIONS;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for(int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                UUID transactionId = ConverterUUID.stringToUUID(jsonObject.getString("transactionId").trim());
                                String transactionName = jsonObject.getString("transactionName");
                                double transactionAmount = jsonObject.getDouble("transactionAmount");
                                Timestamp transactionDate = DateConverter.stringToTimestamp(jsonObject.getString("transactionDate"));
                                TransactionType transactionType = TransactionType.valueOf(jsonObject.getString("transactionType"));
                                UUID userId = ConverterUUID.stringToUUID(jsonObject.getString("userId").trim());

                                Transaction transaction = new Transaction(transactionId,transactionName,transactionAmount,transactionDate,transactionType,userId);
                                arrayListTransactions.add(transaction);
                            }

                            // sort the transactions array by date in descending order
                            Transaction[] transactions = arrayListTransactions.toArray(new Transaction[arrayListTransactions.size()]);
                            Arrays.sort(transactions, new Comparator<Transaction>() {
                                @Override
                                public int compare(Transaction transaction1, Transaction transaction2) {
                                    return transaction2.getTransactionDate().compareTo(transaction1.getTransactionDate());
                                }
                            });

                            // clear the arrayListTransactions and add the sorted transactions back to it
                            arrayListTransactions.clear();
                            arrayListTransactions.addAll(Arrays.asList(transactions));
                            recyclerViewAdapterTransactions.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        String url2 = Constants.URL_GET_ALL_TRANSFERS;
        StringRequest stringRequest2 = new StringRequest(Request.Method.GET, url2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for(int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                UUID transferId = ConverterUUID.stringToUUID(jsonObject.getString("transferId"));
                                double transferAmount = jsonObject.getDouble("transferAmount");
                                UUID userId = ConverterUUID.stringToUUID(jsonObject.getString("userId"));
                                Timestamp transferDate = DateConverter.stringToTimestamp(jsonObject.getString("transferDate"));

                                Transaction transaction = new Transaction(transferId,"Transfer",transferAmount,transferDate,TransactionType.TRANSFER,userId);
                                arrayListTransactions.add(transaction);
                            }

                            // sort the transactions array by date in descending order
                            Transaction[] transactions = arrayListTransactions.toArray(new Transaction[arrayListTransactions.size()]);
                            Arrays.sort(transactions, new Comparator<Transaction>() {
                                @Override
                                public int compare(Transaction transaction1, Transaction transaction2) {
                                    return transaction2.getTransactionDate().compareTo(transaction1.getTransactionDate());
                                }
                            });

                            // clear the arrayListTransactions and add the sorted transactions back to it
                            arrayListTransactions.clear();
                            arrayListTransactions.addAll(Arrays.asList(transactions));
                            recyclerViewAdapterTransactions.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(stringRequest);
        requestQueue.add(stringRequest2);

//        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
//        Long transferId = sharedViewModel.getData();
//        if (transferId != null) {
//            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
//            String url = Constants.URL_GET_TRANSFER + "?transferId=" + transferId;
//
//            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                    new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String response) {
//                            if (transferId != null) {
//                                try {
//                                    JSONObject jsonObject = new JSONObject(response);
//                                    if (jsonObject.has("transferId")) {
//                                        Transfer transfer = new Transfer(
//                                                jsonObject.getLong("transferId"),
//                                                jsonObject.getDouble("transferAmount"),
//                                                jsonObject.getString("transferPayee"),
//                                                jsonObject.getString("transferIBAN"),
//                                                jsonObject.getString("transferDescription"),
//                                                jsonObject.getLong("userId"),
//                                                DateConverter.stringToDate(jsonObject.getString("transferDate"))
//                                        );
//                                        Transaction transaction = new Transaction(transfer.getTransferId(), "Transfer", transfer.getTransferAmount(), new Date(), TransactionType.TRANSFER, transfer.getUserId());
//                                        arrayListTransactions.add(transaction);
//                                        recyclerViewAdapterTransactions.notifyDataSetChanged();
//                                    }
//                                } catch (JSONException e) {
//                                    throw new RuntimeException(e);
//                                }
//                            }
//                        }
//                    },
//                    new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            // Handle error
//                        }
//                    });
//
//            requestQueue.add(stringRequest);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_transactions, container, false);

        recyclerViewTransactions = view.findViewById(R.id.recyclerViewTransactions);
        recyclerViewTransactions.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerViewAdapterTransactions = new RecyclerViewAdapterTransactions(arrayListTransactions, getContext());
        recyclerViewTransactions.setAdapter(recyclerViewAdapterTransactions);

        recyclerViewAdapterTransactions.notifyDataSetChanged();

        return view;
    }
}