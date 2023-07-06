package com.example.mobilebankingapplication.fragments;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
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

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;


public class AccountStatementFragment extends DialogFragment {
    private View view;
    private User user;
    private SharedViewModel sharedViewModel;
    private Button btnConfirm, btnCancel, btnFrom, btnTo;
    private TextView tvStartDate, tvEndDate;
    private Date startDate = null, endDate = null;
    private ArrayList<Transaction> arrayListTransactions = new ArrayList<>();

    public AccountStatementFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getUser();

        String url = DatabaseConstants.URL_GET_TRANSACTIONS_BY_USER + "?userId=" + user.getUserId();
        JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(Request.Method.GET, url, null,
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
                                Date date = new Date(transactionDate.getTime());
                                TransactionType transactionType = TransactionType.valueOf(jsonObject.getString("transactionType").trim());
                                UUID userId = ConverterUUID.stringToUUID(jsonObject.getString("userId").trim());

                                Transaction transaction = new Transaction(transactionId, transactionName, transactionAmount, transactionDate, transactionType, userId);
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
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("transfers");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                UUID transferId = ConverterUUID.stringToUUID(jsonObject.getString("transferId"));
                                String transferPayee = jsonObject.getString("transferPayee");
                                double transferAmount = jsonObject.getDouble("transferAmount");
                                UUID userId = ConverterUUID.stringToUUID(jsonObject.getString("userId"));
                                Timestamp transferDate = DateConverter.stringToTimestamp(jsonObject.getString("transferDate"));

                                Transaction transaction = new Transaction(transferId, "Transfer " + transferPayee, transferAmount, transferDate, TransactionType.TRANSFER, userId);
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

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.fragment_account_statement);

        // Set the dialog window width dynamically
        Window window = dialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(layoutParams);
        }

        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_account_statement, container, false);
        initializeComponents();

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Objects.requireNonNull(getDialog()).dismiss();
            }
        });

        btnFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDay) {
                                Calendar selectedDate = Calendar.getInstance();
                                selectedDate.set(selectedYear, selectedMonth, selectedDay, 0, 0, 0);
                                selectedDate.set(Calendar.MILLISECOND, 0);

                                Calendar currentDate = Calendar.getInstance();

                                if (selectedDate.compareTo(currentDate) <= 0) {
                                    if (endDate != null) {
                                        if (selectedDate.getTime().compareTo(endDate) < 0) {
                                            startDate = selectedDate.getTime();
                                            tvStartDate.setText(DateConverter.dateToString(startDate));
                                        } else {
                                            Toast.makeText(getContext(), "The start date cannot be later than the end date!", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        startDate = selectedDate.getTime();
                                        tvStartDate.setText(DateConverter.dateToString(startDate));
                                    }
                                } else {
                                    Toast.makeText(requireContext(), "The date cannot be in the future!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        year, month, day);

                // Show the DatePickerDialog
                datePickerDialog.show();
            }
        });


        btnTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDay) {
                                Calendar selectedDate = Calendar.getInstance();
                                selectedDate.set(selectedYear, selectedMonth, selectedDay, 23, 59, 59);
                                selectedDate.set(Calendar.MILLISECOND, 999);

                                Calendar currentDate = Calendar.getInstance();

                                if (selectedDate.compareTo(currentDate) <= 0) {
                                    if (startDate != null) {
                                        if (selectedDate.getTime().compareTo(startDate) > 0) {
                                            endDate = selectedDate.getTime();
                                            tvEndDate.setText(DateConverter.dateToString(endDate));
                                        } else {
                                            Toast.makeText(getContext(), "The end date cannot be sooner than the start date!", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        endDate = selectedDate.getTime();
                                        tvEndDate.setText(DateConverter.dateToString(endDate));
                                    }
                                } else {
                                    Toast.makeText(requireContext(), "The date cannot be in the future!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        year, month, day);

                // Show the DatePickerDialog
                datePickerDialog.show();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (endDate != null && startDate != null) {
                    List<Transaction> filteredTransactions = new ArrayList<>();
                    for (Transaction transaction : arrayListTransactions) {
                        Timestamp transactionTimestamp = transaction.getTransactionDate();
                        Date transactionDate = new Date(transactionTimestamp.getTime());

                        if (transactionDate.after(startDate) && transactionDate.before(endDate)) {
                            filteredTransactions.add(transaction);
                        }
                    }

                    if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 200);
                    } else {
                        try {
                            Workbook workbook = new HSSFWorkbook();
                            Sheet sheet = workbook.createSheet("AccountStatement");

                            // Create header row
                            Row headerRow = sheet.createRow(0);
                            headerRow.createCell(0).setCellValue("Name");
                            headerRow.createCell(1).setCellValue("Type");
                            headerRow.createCell(2).setCellValue("Date");
                            headerRow.createCell(3).setCellValue("Amount");

                            int rowNum = 1;
                            for (Transaction transaction : filteredTransactions) {
                                Row row = sheet.createRow(rowNum);
                                row.createCell(0).setCellValue(transaction.getTransactionName());
                                row.createCell(1).setCellValue(transaction.getTransactionType().toString());
                                row.createCell(2).setCellValue(transaction.getTransactionDate().toString());
                                row.createCell(3).setCellValue(transaction.getTransactionAmount());
                                rowNum++;
                            }

                            // Save the workbook to a file in the Downloads directory
                            String fileName = "AccountStatement.xlsx";
                            File downloadsDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                            File file = new File(downloadsDirectory, fileName);

                            if (file.exists()) {
                                file.delete();
                            }

                            FileOutputStream fileOutputStream = new FileOutputStream(file);
                            workbook.write(fileOutputStream);
                            fileOutputStream.close();

                            Toast.makeText(getContext(), "Account Statement generated successfully!", Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Error generating the Account Statement", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(getContext(), "End date or start date missing!", Toast.LENGTH_SHORT).show();
                }
                Objects.requireNonNull(getDialog()).dismiss();
            }
        });


        return view;
    }

    private void initializeComponents() {
        btnConfirm = view.findViewById(R.id.btnConfirmAccStmt);
        btnCancel = view.findViewById(R.id.btnCancelAccStmt);
        btnTo = view.findViewById(R.id.btnTo);
        btnFrom = view.findViewById(R.id.btnFrom);
        tvEndDate = view.findViewById(R.id.tvEndDate);
        tvStartDate = view.findViewById(R.id.tvStartDate);
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
}