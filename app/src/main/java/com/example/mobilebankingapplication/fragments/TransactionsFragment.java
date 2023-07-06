package com.example.mobilebankingapplication.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mobilebankingapplication.R;
import com.example.mobilebankingapplication.adapters.RecyclerViewAdapterTransactions;
import com.example.mobilebankingapplication.classes.Transaction;
import com.example.mobilebankingapplication.classes.User;
import com.example.mobilebankingapplication.database.DatabaseConstants;
import com.example.mobilebankingapplication.database.RequestHandler;
import com.example.mobilebankingapplication.enums.TransactionType;
import com.example.mobilebankingapplication.utils.ConverterUUID;
import com.example.mobilebankingapplication.utils.DateConverter;
import com.example.mobilebankingapplication.utils.SharedViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.UUID;

public class TransactionsFragment extends Fragment {
    private RecyclerView recyclerViewTransactions;
    private RecyclerViewAdapterTransactions recyclerViewAdapterTransactions;
    private ArrayList<Transaction> arrayListTransactions = new ArrayList<>();
    private SharedViewModel sharedViewModel;
    private User user;
    private View view;
    private SearchView searchViewTransactions;
    private Button btnAllTransactions, btnTransfers, btnDate, btnIncomes, btnExpenses, btnAccountStatement;
    private Button btnSort, btnFilter;
    private Button btnSortAscending, btnSortDescending;

    private LinearLayout filterLayout1, filterLayout2, sortLayout;
    private String selectedFilter = "all";
    private String currentSearchText = "";
    private Calendar selectedDate = Calendar.getInstance();
    private boolean isSortHidden = true;
    private boolean isFilterHidden = true;

    public TransactionsFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getUser();

        String url = DatabaseConstants.URL_GET_TRANSACTIONS_BY_USER + "?userId=" + user.getUserId();
        JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(Request.Method.GET, url,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("transactions");
                            for(int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                UUID transactionId = ConverterUUID.stringToUUID(jsonObject.getString("transactionId").trim());
                                String transactionName = jsonObject.getString("transactionName");
                                double transactionAmount = jsonObject.getDouble("transactionAmount");
                                Timestamp transactionDate = DateConverter.stringToTimestamp(jsonObject.getString("transactionDate"));
                                TransactionType transactionType = TransactionType.valueOf(jsonObject.getString("transactionType").trim());
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
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        String url2 = DatabaseConstants.URL_GET_TRANSFER_BY_USER + "?userId=" + user.getUserId();
        JsonObjectRequest jsonObjectRequest2 = new JsonObjectRequest(Request.Method.GET, url2, null,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("transfers");
                            for(int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                UUID transferId = ConverterUUID.stringToUUID(jsonObject.getString("transferId"));
                                String transferPayee = jsonObject.getString("transferPayee");
                                double transferAmount = jsonObject.getDouble("transferAmount");
                                UUID userId = ConverterUUID.stringToUUID(jsonObject.getString("userId"));
                                Timestamp transferDate = DateConverter.stringToTimestamp(jsonObject.getString("transferDate"));

                                Transaction transaction = new Transaction(transferId,"Transfer " + transferPayee,transferAmount,transferDate,TransactionType.TRANSFER,userId);
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
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestHandler.getInstance(getContext()).addToRequestQueue(jsonObjectRequest1);
        RequestHandler.getInstance(getContext()).addToRequestQueue(jsonObjectRequest2);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_transactions, container, false);

        initializeComponents();
        searchingMethod();

        hideFilterLayout();
        hideSortLayout();

        btnAccountStatement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getChildFragmentManager();
                AccountStatementFragment accountStatementFragment = new AccountStatementFragment();
                accountStatementFragment.show(fragmentManager, "AccountStatementFragment");
            }
        });


        btnSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isSortHidden){
                    isSortHidden = false;
                    showSortLayout();
                } else {
                    isSortHidden = true;
                    hideSortLayout();
                }
            }
        });

        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isFilterHidden){
                    isFilterHidden = false;
                    showFilterLayout();
                } else {
                    isFilterHidden = true;
                    hideFilterLayout();
                }
            }
        });

        btnAllTransactions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterAllMethod();
            }
        });

        btnTransfers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterTransfersOnlyMethod();
            }
        });

        btnIncomes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterIncomes();
            }
        });

        btnExpenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterExpenses();
            }
        });

        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterDate();
            }
        });

        btnSortAscending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrayListTransactions.sort(Transaction.amountAscending);
                RecyclerViewAdapterTransactions adapterTransactions = new RecyclerViewAdapterTransactions(arrayListTransactions, getContext());
                recyclerViewTransactions.setAdapter(adapterTransactions);
            }
        });

        btnSortDescending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrayListTransactions.sort(Transaction.amountAscending);
                Collections.reverse(arrayListTransactions);
                RecyclerViewAdapterTransactions adapterTransactions = new RecyclerViewAdapterTransactions(arrayListTransactions, getContext());
                recyclerViewTransactions.setAdapter(adapterTransactions);
            }
        });

        recyclerViewTransactions.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerViewAdapterTransactions = new RecyclerViewAdapterTransactions(arrayListTransactions, getContext());
        recyclerViewTransactions.setAdapter(recyclerViewAdapterTransactions);

        recyclerViewAdapterTransactions.notifyDataSetChanged();

        return view;
    }

    private void filterMethod(String status){
        selectedFilter = status;
        ArrayList<Transaction>  filteredTransactions = new ArrayList<>();

        for(Transaction transaction : arrayListTransactions){
            if(transaction.getTransactionType().toString().toLowerCase().contains(status)){
                // to make sure the search option shows items that are specific to the type that is toggled, and not items that just match the words in the search option, regardless of their type
                if(currentSearchText.equals("")) {
                    filteredTransactions.add(transaction);
                } else {
                    if(transaction.getTransactionType().toString().toLowerCase().contains(currentSearchText.toLowerCase())){
                        filteredTransactions.add(transaction);
                    }
                }
            }
        }
        RecyclerViewAdapterTransactions adapterTransactions = new RecyclerViewAdapterTransactions(filteredTransactions,getContext());
        recyclerViewTransactions.setAdapter(adapterTransactions);
    }

    private void filterAllMethod(){
        selectedFilter = "all";
        // it clears any existing query text that might be present
        searchViewTransactions.setQuery("",false);
        // removes the keyboard or any active focus
        searchViewTransactions.clearFocus();
        RecyclerViewAdapterTransactions adapterTransactions = new RecyclerViewAdapterTransactions(arrayListTransactions,getContext());
        recyclerViewTransactions.setAdapter(adapterTransactions);
    }

    private void filterTransfersOnlyMethod(){
        filterMethod("transfer");
    }

    private void filterIncomes() {
        selectedFilter = "incomes";
        ArrayList<Transaction> filteredTransactions = new ArrayList<>();

        for (Transaction transaction : arrayListTransactions) {
            if (!transaction.getTransactionType().equals(TransactionType.TRANSFER) && transaction.getTransactionAmount() >= 0) {
                if (currentSearchText.equals("")) {
                    filteredTransactions.add(transaction);
                } else {
                    if (transaction.getTransactionType().toString().toLowerCase().contains(currentSearchText.toLowerCase())) {
                        filteredTransactions.add(transaction);
                    }
                }
            }
        }

        RecyclerViewAdapterTransactions adapterTransactions = new RecyclerViewAdapterTransactions(filteredTransactions, getContext());
        recyclerViewTransactions.setAdapter(adapterTransactions);
    }

    private void filterExpenses() {
        selectedFilter = "expenses";
        ArrayList<Transaction> filteredTransactions = new ArrayList<>();

        for (Transaction transaction : arrayListTransactions) {
            if (transaction.getTransactionType().equals(TransactionType.TRANSFER) || transaction.getTransactionAmount() < 0) {
                if (currentSearchText.equals("")) {
                    filteredTransactions.add(transaction);
                } else {
                    if (transaction.getTransactionType().toString().toLowerCase().contains(currentSearchText.toLowerCase())) {
                        filteredTransactions.add(transaction);
                    }
                }
            }
        }
        RecyclerViewAdapterTransactions adapterTransactions = new RecyclerViewAdapterTransactions(filteredTransactions, getContext());
        recyclerViewTransactions.setAdapter(adapterTransactions);
    }

    private void filterDate() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                selectedDate.set(Calendar.YEAR, year);
                selectedDate.set(Calendar.MONTH, monthOfYear);
                selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                applyDateFilter();
            }
        };

        // Show the date picker dialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), dateSetListener,
                selectedDate.get(Calendar.YEAR), selectedDate.get(Calendar.MONTH),
                selectedDate.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }


    private void applyDateFilter() {
        selectedFilter = "date";
        ArrayList<Transaction> filteredTransactions = new ArrayList<>();

        for (Transaction transaction : arrayListTransactions) {
            Timestamp transactionTimestamp = transaction.getTransactionDate();
            long transactionTimeInMillis = transactionTimestamp.getTime();

            Calendar transactionCalendar = Calendar.getInstance();
            transactionCalendar.setTimeInMillis(transactionTimeInMillis);

            if (transactionCalendar.get(Calendar.YEAR) == selectedDate.get(Calendar.YEAR) &&
                    transactionCalendar.get(Calendar.MONTH) == selectedDate.get(Calendar.MONTH) &&
                    transactionCalendar.get(Calendar.DAY_OF_MONTH) == selectedDate.get(Calendar.DAY_OF_MONTH)) {
                filteredTransactions.add(transaction);
            }
        }

        RecyclerViewAdapterTransactions adapterTransactions = new RecyclerViewAdapterTransactions(filteredTransactions, getContext());
        recyclerViewTransactions.setAdapter(adapterTransactions);
    }

    private void initializeComponents(){
        recyclerViewTransactions = view.findViewById(R.id.recyclerViewTransactions);
        searchViewTransactions  = view.findViewById(R.id.searchViewTransactions);

        btnAllTransactions = view.findViewById(R.id.btnAllTransactions);
        btnDate = view.findViewById(R.id.btnDate);
        btnExpenses = view.findViewById(R.id.btnExpenses);
        btnTransfers = view.findViewById(R.id.btnTransfersOnly);
        btnIncomes = view.findViewById(R.id.btnIncomes);
        btnAccountStatement = view.findViewById(R.id.btnAccountStatement);

        btnSort = view.findViewById(R.id.btnSort);
        btnFilter = view.findViewById(R.id.btnFilter);

        filterLayout1 = view.findViewById(R.id.filterLayout1);
        filterLayout2 = view.findViewById(R.id.filterLayout2);
        sortLayout = view.findViewById(R.id.sortLayout);

        btnSortAscending = view.findViewById(R.id.btnSortAscending);
        btnSortDescending = view.findViewById(R.id.btnSortDescending);
    }

    private void getUser() {
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        user = sharedViewModel.getUser().getValue();
        if (user == null) {
            try {
                throw new Exception("The user in null in TransactionsFragment!");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void searchingMethod() {
        searchViewTransactions.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                currentSearchText = newText;
                ArrayList<Transaction> filteredTransactions = new ArrayList<>();

                // Filter transactions based on search text, selected filter, and date
                for (Transaction transaction : arrayListTransactions) {
                    boolean matchesSearchText = transaction.getTransactionType().toString().toLowerCase().startsWith(newText.toLowerCase());
                    boolean matchesFilter = selectedFilter.equals("all")
                            || (selectedFilter.equals("incomes") && !transaction.getTransactionType().equals(TransactionType.TRANSFER) && transaction.getTransactionAmount() >= 0)
                            || (selectedFilter.equals("expenses") && (transaction.getTransactionType().equals(TransactionType.TRANSFER) || transaction.getTransactionAmount() < 0))
                            || (selectedFilter.equals("transfer") && transaction.getTransactionType().equals(TransactionType.TRANSFER));
                    boolean matchesDate = selectedFilter.equals("date") && isTransactionOnSelectedDate(transaction);

                    if (matchesSearchText && (matchesFilter || matchesDate)) {
                        filteredTransactions.add(transaction);
                    }
                }

                RecyclerViewAdapterTransactions adapterTransactions = new RecyclerViewAdapterTransactions(filteredTransactions, getContext());
                recyclerViewTransactions.setAdapter(adapterTransactions);
                return false;
            }
        });
    }

    private boolean isTransactionOnSelectedDate(Transaction transaction) {
        Calendar transactionCalendar = Calendar.getInstance();
        transactionCalendar.setTimeInMillis(transaction.getTransactionDate().getTime());

        return transactionCalendar.get(Calendar.YEAR) == selectedDate.get(Calendar.YEAR)
                && transactionCalendar.get(Calendar.MONTH) == selectedDate.get(Calendar.MONTH)
                && transactionCalendar.get(Calendar.DAY_OF_MONTH) == selectedDate.get(Calendar.DAY_OF_MONTH);
    }

    private void hideFilterLayout(){
        filterLayout1.setVisibility(View.GONE);
        filterLayout2.setVisibility(View.GONE);
        btnFilter.setText(R.string.btnFilterDynamic);
    }

    private void showFilterLayout(){
        filterLayout1.setVisibility(View.VISIBLE);
        filterLayout2.setVisibility(View.VISIBLE);
        btnFilter.setText(R.string.btnHide);
    }

    private void hideSortLayout(){
        sortLayout.setVisibility(View.GONE);
        btnSort.setText(R.string.btnSortDynamic);
    }

    private void showSortLayout(){
        sortLayout.setVisibility(View.VISIBLE);
        btnSort.setText(R.string.btnHide);
    }

}