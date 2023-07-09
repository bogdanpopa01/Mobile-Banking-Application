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

import org.apache.poi.ss.formula.functions.T;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class TransactionsFragment extends Fragment {
    private RecyclerView recyclerViewTransactions;
    private RecyclerViewAdapterTransactions recyclerViewAdapterTransactions;
    private List<Transaction> arrayListTransactions = new ArrayList<>();
    private ArrayList<Transaction> filteredTransactions = new ArrayList<>();
    private SharedViewModel sharedViewModel;
    private User user;
    private View view;
    private SearchView searchViewTransactions;
    private Button btnAllTransactions, btnTransfers, btnDate, btnIncomes, btnExpenses, btnAccountStatement;
    private Button btnSort, btnFilter;
    private Button btnSortAscending, btnSortDescending;

    private LinearLayout filterLayout1, filterLayout2, sortLayout;
    private Calendar selectedDate = Calendar.getInstance();
    private String selectedFilter;
    private boolean isSortHidden = true;
    private boolean isFilterHidden = true;
    private boolean isSortAscending = false;
    private boolean isSortDescending = false;
    private boolean isSortDateDesc = true;

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

        hideFilterLayout();
        hideSortLayout();

        searchMethod();

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
                if (isSortHidden) {
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
                if (isFilterHidden) {
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
                selectedFilter = "all";
                isSortDateDesc = true;
                isSortAscending = false;
                isSortDescending = false;
                filterTransactions(searchViewTransactions.getQuery().toString());
            }
        });

        btnTransfers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedFilter = "transfers";
                isSortDateDesc = true;
                isSortAscending = false;
                isSortDescending = false;
                filterTransactions(searchViewTransactions.getQuery().toString());
            }
        });

        btnIncomes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedFilter = "incomes";
                isSortDateDesc = true;
                isSortAscending = false;
                isSortDescending = false;
                filterTransactions(searchViewTransactions.getQuery().toString());
            }
        });

        btnExpenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedFilter = "expenses";
                isSortDateDesc = true;
                isSortAscending = false;
                isSortDescending = false;
                filterTransactions(searchViewTransactions.getQuery().toString());
            }
        });

        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterDate();
                selectedFilter = "date";
                isSortDateDesc = true;
                isSortAscending = false;
                isSortDescending = false;
                filterTransactions(searchViewTransactions.getQuery().toString());
            }
        });


        btnSortAscending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedFilter == null){
                    Toast.makeText(getContext(), "Select All button to sort all the transactions", Toast.LENGTH_SHORT).show();
                }
                isSortAscending = true;
                isSortDescending = false;
                isSortDateDesc = false;
                sortFilteredTransactions();
                recyclerViewAdapterTransactions.notifyDataSetChanged();
            }
        });

        btnSortDescending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedFilter == null){
                    Toast.makeText(getContext(), "Select All button to sort all the transactions", Toast.LENGTH_SHORT).show();
                }
                isSortAscending = false;
                isSortDescending = true;
                isSortDateDesc = false;
                sortFilteredTransactions();
                recyclerViewAdapterTransactions.notifyDataSetChanged();
            }
        });

        recyclerViewTransactions.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerViewAdapterTransactions = new RecyclerViewAdapterTransactions(arrayListTransactions, getContext());
        recyclerViewTransactions.setAdapter(recyclerViewAdapterTransactions);

        return view;
    }

    private void filterTransactions(String searchTerm) {
        filteredTransactions.clear();

        for (Transaction t : arrayListTransactions) {
            if (isTransactionTypeMatch(t) && isTransactionNameMatch(t, searchTerm)) {
                if (selectedFilter.equals("date")) {
                    Timestamp transactionTimestamp = t.getTransactionDate();
                    long transactionTimeInMillis = transactionTimestamp.getTime();

                    Calendar transactionCalendar = Calendar.getInstance();
                    transactionCalendar.setTimeInMillis(transactionTimeInMillis);

                    if (transactionCalendar.get(Calendar.YEAR) == selectedDate.get(Calendar.YEAR) &&
                            transactionCalendar.get(Calendar.MONTH) == selectedDate.get(Calendar.MONTH) &&
                            transactionCalendar.get(Calendar.DAY_OF_MONTH) == selectedDate.get(Calendar.DAY_OF_MONTH)) {
                        filteredTransactions.add(t);
                    }
                } else {
                    filteredTransactions.add(t);
                }
            }
        }

        sortFilteredTransactions();
        recyclerViewAdapterTransactions.setTransactions(filteredTransactions);
        recyclerViewAdapterTransactions.notifyDataSetChanged();
    }

    private void sortFilteredTransactions() {
        if (isSortAscending) {
            sortTransactionsByAmountAsc();
        } else if (isSortDescending){
            sortTransactionsByAmountDesc();
        } else if(isSortDateDesc) {
            sortTransactionsByDateDesc();
        }
    }

    private boolean isTransactionTypeMatch(Transaction transaction) {
        if (selectedFilter.equals("all")) {
            return true;
        } else if (selectedFilter.equals("transfers")) {
            return transaction.getTransactionType() == TransactionType.TRANSFER;
        } else if (selectedFilter.equals("incomes")) {
            return transaction.getTransactionAmount() > 0;
        } else if (selectedFilter.equals("expenses")) {
            return transaction.getTransactionAmount() < 0;
        } else if(selectedFilter.equals("date")){
            return true;
        }
        return false;
    }

    private boolean isTransactionNameMatch(Transaction transaction, String searchTerm) {
        return searchTerm.isEmpty() || transaction.getTransactionName().toLowerCase().startsWith(searchTerm.toLowerCase());
    }

    private void sortTransactionsByDateDesc() {
        Collections.sort(filteredTransactions, new Comparator<Transaction>() {
            @Override
            public int compare(Transaction transaction1, Transaction transaction2) {
                return transaction2.getTransactionDate().compareTo(transaction1.getTransactionDate());
            }
        });
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
        filteredTransactions.clear();

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

        sortFilteredTransactions();
        recyclerViewAdapterTransactions.setTransactions(filteredTransactions);
        recyclerViewAdapterTransactions.notifyDataSetChanged();
    }

    private void sortTransactionsByAmountAsc() {
        Collections.sort(filteredTransactions, new Comparator<Transaction>() {
            @Override
            public int compare(Transaction transaction1, Transaction transaction2) {
                return Double.compare(transaction1.getTransactionAmount(), transaction2.getTransactionAmount());
            }
        });
    }

    private void sortTransactionsByAmountDesc() {
        Collections.sort(filteredTransactions, new Comparator<Transaction>() {
            @Override
            public int compare(Transaction transaction1, Transaction transaction2) {
                return Double.compare(transaction2.getTransactionAmount(), transaction1.getTransactionAmount());
            }
        });
    }


    private void searchMethod() {
        searchViewTransactions.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                filterTransactions(s);
                return false;
            }
        });
    }

    private void initializeComponents() {
        recyclerViewTransactions = view.findViewById(R.id.recyclerViewTransactions);
        searchViewTransactions = view.findViewById(R.id.searchViewTransactions);

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

    private void hideFilterLayout() {
        filterLayout1.setVisibility(View.GONE);
        filterLayout2.setVisibility(View.GONE);
        btnFilter.setText(R.string.btnFilterDynamic);
    }

    private void showFilterLayout() {
        filterLayout1.setVisibility(View.VISIBLE);
        filterLayout2.setVisibility(View.VISIBLE);
        btnFilter.setText(R.string.btnHide);
    }

    private void hideSortLayout() {
        sortLayout.setVisibility(View.GONE);
        btnSort.setText(R.string.btnSortDynamic);
    }

    private void showSortLayout() {
        sortLayout.setVisibility(View.VISIBLE);
        btnSort.setText(R.string.btnHide);
    }
}