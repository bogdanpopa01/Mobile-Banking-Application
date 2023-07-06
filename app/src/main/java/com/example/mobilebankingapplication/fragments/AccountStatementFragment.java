package com.example.mobilebankingapplication.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobilebankingapplication.R;
import com.example.mobilebankingapplication.classes.User;
import com.example.mobilebankingapplication.utils.DateConverter;
import com.example.mobilebankingapplication.utils.SharedViewModel;

import org.w3c.dom.Text;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;


public class AccountStatementFragment extends DialogFragment {
    private View view;
    private User user;
    private SharedViewModel sharedViewModel;
    private Button btnConfirm, btnCancel, btnFrom, btnTo;
    private TextView tvStartDate, tvEndDate;
    private Date startDate = null, endDate = null;

    public AccountStatementFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getUser();
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
                                selectedDate.set(selectedYear, selectedMonth, selectedDay);

                                Calendar currentDate = Calendar.getInstance();

                                if (selectedDate.compareTo(currentDate) <= 0) {
                                    if (endDate != null) {
                                        if (selectedDate.getTime().getTime() < endDate.getTime()) {
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
                                selectedDate.set(selectedYear, selectedMonth, selectedDay);

                                Calendar currentDate = Calendar.getInstance();

                                if (selectedDate.compareTo(currentDate) <= 0) {
                                    if (startDate != null) {
                                        if (selectedDate.getTime().getTime() > startDate.getTime()) {
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