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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.mobilebankingapplication.R;
import com.example.mobilebankingapplication.adapters.RecyclerViewAdapterDeposits;
import com.example.mobilebankingapplication.classes.Deposit;
import com.example.mobilebankingapplication.classes.User;
import com.example.mobilebankingapplication.database.DatabaseConstants;
import com.example.mobilebankingapplication.utils.ConverterUUID;
import com.example.mobilebankingapplication.utils.DateConverter;
import com.example.mobilebankingapplication.utils.SharedViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.UUID;


public class DepositsFragment extends Fragment {
    private RecyclerView recyclerViewDeposits;
    private RecyclerViewAdapterDeposits recyclerViewAdapterDeposits;
    private ArrayList<Deposit> arrayListDeposits = new ArrayList<>();
    private SharedViewModel sharedViewModel;
    private User user;

    public DepositsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getUser();

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        String url = DatabaseConstants.URL_GET_DEPOSITS_BY_USER + "?userId=" + user.getUserId();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("deposits");
                            for(int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                UUID depositId = ConverterUUID.stringToUUID(jsonObject.getString("depositId"));
                                String depositName = jsonObject.getString("depositName");
                                double depositAmount = jsonObject.getDouble("depositAmount");
                                int depositPeriod = jsonObject.getInt("depositPeriod");
                                double depositInterestRateValue = jsonObject.getDouble("depositInterestRate");
                                Timestamp depositTimeLeftValue = DateConverter.stringToTimestamp(jsonObject.getString("depositTimeLeft"));
                                UUID userId = ConverterUUID.stringToUUID(jsonObject.getString("userId"));

                                Deposit deposit = new Deposit(depositId, depositName, depositAmount, depositPeriod, depositInterestRateValue, depositTimeLeftValue, userId);
                                arrayListDeposits.add(deposit);
                            }

                            // sort the deposits array by time left in descending order
                            Deposit[] deposits = arrayListDeposits.toArray(new Deposit[arrayListDeposits.size()]);
                            Arrays.sort(deposits, new Comparator<Deposit>() {
                                @Override
                                public int compare(Deposit deposit1, Deposit deposit2) {
                                    return deposit2.getDepositTimeLeft().compareTo(deposit1.getDepositTimeLeft());
                                }
                            });

                            // clear the arrayListDeposits and add the sorted deposits back to it
                            arrayListDeposits.clear();
                            arrayListDeposits.addAll(Arrays.asList(deposits));
                            recyclerViewAdapterDeposits.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(jsonObjectRequest);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_deposits, container, false);

        recyclerViewDeposits = view.findViewById(R.id.recyclerViewDeposits);
        recyclerViewDeposits.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerViewAdapterDeposits = new RecyclerViewAdapterDeposits(arrayListDeposits,getContext());
        recyclerViewDeposits.setAdapter(recyclerViewAdapterDeposits);

        return view;
    }

    private void getUser() {
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        user = sharedViewModel.getUser().getValue();
        if (user == null) {
            try {
                throw new Exception("The user in null in DepositsFragment!");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}