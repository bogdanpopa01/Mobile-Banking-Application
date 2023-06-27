package com.example.mobilebankingapplication.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.mobilebankingapplication.R;
import com.example.mobilebankingapplication.classes.Transaction;
import com.example.mobilebankingapplication.classes.User;
import com.example.mobilebankingapplication.database.DatabaseConstants;
import com.example.mobilebankingapplication.database.RequestHandler;
import com.example.mobilebankingapplication.enums.TransactionType;
import com.example.mobilebankingapplication.utils.ConverterUUID;
import com.example.mobilebankingapplication.utils.DateConverter;
import com.example.mobilebankingapplication.utils.SharedViewModel;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;


public class ReportsFragment extends Fragment {
    private View view;
    private PieChart pieChart;
    private ArrayList<Transaction> arrayListTransactions = new ArrayList<>();
    private SharedViewModel sharedViewModel;
    private User user;
    private Button btnGeneralPrediction, btnMonthlyPrediction;
    private int selectedMonth;


    public ReportsFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getUser();
        loadTransactions();

    }

    private void displayPieChart() {
        // Create a list to store the pie chart entries
        ArrayList<PieEntry> pieEntries = new ArrayList<>();

        // Calculate the total amount for each transaction type
        Map<TransactionType, Double> typeAmountMap = new HashMap<>();
        for (Transaction transaction : arrayListTransactions) {
            TransactionType transactionType = transaction.getTransactionType();
            double transactionAmount = transaction.getTransactionAmount();
            if (typeAmountMap.containsKey(transactionType)) {
                double currentAmount = typeAmountMap.get(transactionType);
                typeAmountMap.put(transactionType, currentAmount + Math.abs(transactionAmount)); // Make the amount positive
            } else {
                typeAmountMap.put(transactionType, Math.abs(transactionAmount)); // Make the amount positive
            }
        }

        // Convert the transaction type and amount map to pie chart entries
        for (Map.Entry<TransactionType, Double> entry : typeAmountMap.entrySet()) {
            TransactionType transactionType = entry.getKey();
            double transactionAmount = entry.getValue();
            PieEntry pieEntry = new PieEntry((float) transactionAmount, transactionType.toString());
            pieEntries.add(pieEntry);
        }

        // Create the data set for the pie chart
        PieDataSet pieDataSet = new PieDataSet(pieEntries, "");
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieDataSet.setValueTextColor(Color.WHITE);
        pieDataSet.setValueTextSize(12f);
        pieDataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.format("%.2f", value); // Format the value to display with 2 decimal places
            }
        });

        // Create the pie data using the data set
        PieData pieData = new PieData(pieDataSet);

        // Get a reference to the PieChart view
        PieChart pieChart = view.findViewById(R.id.pieChart);

        // Customize the pie chart appearance
        pieChart.setUsePercentValues(false); // Disable percentage values
        pieChart.getDescription().setEnabled(false);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.TRANSPARENT);
        pieChart.setHoleRadius(50f);
        pieChart.setTransparentCircleRadius(55f);
        pieChart.setDrawEntryLabels(false); // Disable entry labels


        pieChart.setCenterText("Expenses filter by type");
        pieChart.setCenterTextSize(16f);


        // Customize the legend
        Legend legend = pieChart.getLegend();
        legend.setEnabled(true);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setDrawInside(false);
        legend.setXEntrySpace(10f);
        legend.setYEntrySpace(0f);
        legend.setYOffset(10f);

        // Set the data for the pie chart
        pieChart.setData(pieData);

        // Refresh the chart to update its display
        pieChart.invalidate();


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_reports, container, false);
        initializeComponents();

        btnGeneralPrediction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayPieChart();
            }
        });

        btnMonthlyPrediction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMonthPickerDialog();
            }
        });

        return view;
    }

    private void initializeComponents() {
        pieChart = view.findViewById(R.id.pieChart);
        btnGeneralPrediction = view.findViewById(R.id.btnGeneralPrediction);
        btnMonthlyPrediction = view.findViewById(R.id.btnMonthlyPrediction);
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

    private void loadTransactions() {
        final int NUM_REQUESTS = 2;  // Number of requests to complete
        final AtomicInteger completedRequests = new AtomicInteger(0);  // Counter for completed requests

        String urlTransactions = DatabaseConstants.URL_GET_TRANSACTIONS_BY_USER + "?userId=" + user.getUserId();
        JsonObjectRequest jsonObjectRequestTransactions = new JsonObjectRequest(Request.Method.GET, urlTransactions, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("transactions");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                UUID transactionId = ConverterUUID.stringToUUID(jsonObject.getString("transactionId").trim());
                                String transactionName = jsonObject.getString("transactionName");
                                double transactionAmount = jsonObject.getDouble("transactionAmount");
                                Timestamp transactionDate = DateConverter.stringToTimestamp(jsonObject.getString("transactionDate"));
                                TransactionType transactionType = TransactionType.valueOf(jsonObject.getString("transactionType").trim());
                                UUID userId = ConverterUUID.stringToUUID(jsonObject.getString("userId").trim());

                                Transaction transaction = new Transaction(transactionId, transactionName, transactionAmount, transactionDate, transactionType, userId);
                                if (transactionAmount < 0) {
                                    arrayListTransactions.add(transaction);
                                }
                            }

                            // Increment the counter for completed requests
                            int completed = completedRequests.incrementAndGet();
                            if (completed == NUM_REQUESTS) {
                                // All requests completed, process the data
                                processTransactions();
                            }
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

        String urlTransfers = DatabaseConstants.URL_GET_TRANSFER_BY_USER + "?userId=" + user.getUserId();
        JsonObjectRequest jsonObjectRequestTransfers = new JsonObjectRequest(Request.Method.GET, urlTransfers, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("transfers");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                UUID transferId = ConverterUUID.stringToUUID(jsonObject.getString("transferId"));
                                double transferAmount = jsonObject.getDouble("transferAmount");
                                UUID userId = ConverterUUID.stringToUUID(jsonObject.getString("userId"));
                                Timestamp transferDate = DateConverter.stringToTimestamp(jsonObject.getString("transferDate"));

                                Transaction transaction = new Transaction(transferId, "Transfer", transferAmount, transferDate, TransactionType.TRANSFER, userId);
                                arrayListTransactions.add(transaction);
                            }

                            // Increment the counter for completed requests
                            int completed = completedRequests.incrementAndGet();
                            if (completed == NUM_REQUESTS) {
                                // All requests completed, process the data
                                processTransactions();
                            }
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

        RequestHandler.getInstance(getContext()).addToRequestQueue(jsonObjectRequestTransactions);
        RequestHandler.getInstance(getContext()).addToRequestQueue(jsonObjectRequestTransfers);
    }

    private void processTransactions() {
        // Sort the transactions array by date in descending order
        Transaction[] transactions = arrayListTransactions.toArray(new Transaction[arrayListTransactions.size()]);
        Arrays.sort(transactions, new Comparator<Transaction>() {
            @Override
            public int compare(Transaction transaction1, Transaction transaction2) {
                return transaction2.getTransactionDate().compareTo(transaction1.getTransactionDate());
            }
        });

        // Clear the arrayListTransactions and add the sorted transactions back to it
        arrayListTransactions.clear();
        arrayListTransactions.addAll(Arrays.asList(transactions));

        // Call the method to display the pie chart or perform any other actions
        displayPieChart();
    }

    private void showMonthPickerDialog() {
        // Define an array of month names to display in the picker
        final String[] months = {
                "January", "February", "March", "April",
                "May", "June", "July", "August",
                "September", "October", "November", "December"
        };

        // Create a dialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Select Month");

        // Set the month items and handle the item click event
        builder.setItems(months, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Store the selected month
                selectedMonth = which;

                // Filter the transactions for the selected month
                ArrayList<Transaction> filteredTransactions = filterTransactionsByMonth(arrayListTransactions, selectedMonth);

                // Update the pie chart with the filtered transactions
                updatePieChart(filteredTransactions);

            }
        });

        // Create and show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private ArrayList<Transaction> filterTransactionsByMonth(ArrayList<Transaction> transactions, int month) {
        ArrayList<Transaction> filteredTransactions = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        for (Transaction transaction : transactions) {
            calendar.setTime(transaction.getTransactionDate());
            int transactionMonth = calendar.get(Calendar.MONTH);
            if (transactionMonth == month) {
                if (transaction.getTransactionAmount() < 0) {
                    filteredTransactions.add(transaction);
                }
            }
        }

        return filteredTransactions;
    }

    private void updatePieChart(ArrayList<Transaction> transactions) {
        // Create a list to store the pie chart entries
        ArrayList<PieEntry> pieEntries = new ArrayList<>();

        // Calculate the total amount for each transaction type
        Map<TransactionType, Double> typeAmountMap = new HashMap<>();
        for (Transaction transaction : transactions) {
            if (transaction.getTransactionAmount() < 0) {
                TransactionType transactionType = transaction.getTransactionType();
                double transactionAmount = transaction.getTransactionAmount();
                if (typeAmountMap.containsKey(transactionType)) {
                    double currentAmount = typeAmountMap.get(transactionType);
                    typeAmountMap.put(transactionType, currentAmount + Math.abs(transactionAmount)); // Make the amount positive
                } else {
                    typeAmountMap.put(transactionType, Math.abs(transactionAmount)); // Make the amount positive
                }
            }
        }

        // Convert the transaction type and amount map to pie chart entries
        for (Map.Entry<TransactionType, Double> entry : typeAmountMap.entrySet()) {
            TransactionType transactionType = entry.getKey();
            double transactionAmount = entry.getValue();
            PieEntry pieEntry = new PieEntry((float) transactionAmount, transactionType.toString());
            pieEntries.add(pieEntry);
        }

        // Create the data set for the pie chart
        PieDataSet pieDataSet = new PieDataSet(pieEntries, "");
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieDataSet.setValueTextColor(Color.WHITE);
        pieDataSet.setValueTextSize(12f);
        pieDataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.format(Locale.getDefault(), "%.1f%%", value);
            }
        });

        // Create the data for the pie chart
        PieData pieData = new PieData(pieDataSet);
        pieData.setValueTextColor(Color.WHITE);
        pieData.setValueTextSize(12f);
        
        // Set the data for the pie chart
        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);
        pieChart.setEntryLabelColor(Color.WHITE);
        pieChart.invalidate(); // Refresh the chart
    }


}