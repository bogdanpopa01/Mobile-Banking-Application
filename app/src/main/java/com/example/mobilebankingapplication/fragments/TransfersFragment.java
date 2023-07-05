package com.example.mobilebankingapplication.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.mobilebankingapplication.R;
import com.example.mobilebankingapplication.classes.Transfer;
import com.example.mobilebankingapplication.classes.User;
import com.example.mobilebankingapplication.database.DatabaseConstants;
import com.example.mobilebankingapplication.database.RequestHandler;
import com.example.mobilebankingapplication.utils.ConverterUUID;
import com.example.mobilebankingapplication.utils.DateConverter;
import com.example.mobilebankingapplication.utils.RandomUuidGenerator;
import com.example.mobilebankingapplication.utils.SharedViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TransfersFragment extends Fragment {
    private EditText etAmountTranfersFragment, etPayeeTransfersFragment, etIBANTransfersFragment, etDescriptionTransfersActivity;
    private Button btnSend;
    private View view;
    private SharedViewModel sharedViewModel;
    private User user;

    public TransfersFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_transfers, container, false);
        initializeComponents();
        getUser();
        transferAmountValidation();
        transferIBANValidation();
        enforceLettersOnly(etPayeeTransfersFragment);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validation()) {
                    double transferAmount = Double.parseDouble(etAmountTranfersFragment.getText().toString());

                    // balance validation
                    BigDecimal transferAmountSafe = BigDecimal.valueOf(transferAmount);
                    BigDecimal userBalance = BigDecimal.valueOf(user.getBalance());
                    int comparisonResult = transferAmountSafe.compareTo(userBalance);

                    if (comparisonResult > 0) {
                        Toast.makeText(getContext(), R.string.INSUFFICIENT_BALANCE, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Transfer transfer = createTransfer();
                    if (transfer != null) {
                        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                                DatabaseConstants.URL_REGISTER_TRANSFER,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            Toast.makeText(getContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                                            // the update method for the user
                                            double newBalance = user.getBalance() - transfer.getTransferAmount();
                                            String urlUpdateUser = DatabaseConstants.URL_UPDATE_USER + "?userId=" + user.getUserId() + "&balance=" + newBalance;

                                            StringRequest stringRequestUserBalance = new StringRequest(Request.Method.PUT, urlUpdateUser, new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    try {
                                                        JSONObject jsonObject = new JSONObject(response);
//                                                        Toast.makeText(getContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                                    } catch (JSONException e) {
                                                        throw new RuntimeException(e);
                                                    }
                                                }
                                            }, new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
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
                                            RequestHandler.getInstance(getContext()).addToRequestQueue(stringRequestUserBalance);
                                            user.setBalance(newBalance);

                                            // clearing the fields
                                            etAmountTranfersFragment.setText("");
                                            etIBANTransfersFragment.setText("");
                                            etPayeeTransfersFragment.setText("");
                                            etDescriptionTransfersActivity.setText("");

                                        } catch (JSONException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }) {
                            @Nullable
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<>();
                                params.put("transferId", ConverterUUID.UUIDtoString(transfer.getTransferId()));
                                params.put("transferAmount", String.valueOf((-1) * transfer.getTransferAmount()));
                                params.put("transferPayee", transfer.getTransferPayee());
                                params.put("transferIBAN", transfer.getTransferIBAN());
                                params.put("transferDescription", transfer.getTransferDescription());
                                params.put("userId", ConverterUUID.UUIDtoString(transfer.getUserId()));
                                params.put("transferDate", DateConverter.timestampToString(transfer.getTransferDate()));
                                return params;
                            }
                        };
                        RequestHandler.getInstance(getContext()).addToRequestQueue(stringRequest);
                    }
                }
            }
        });
        return view;
    }

    private void initializeComponents() {
        etAmountTranfersFragment = view.findViewById(R.id.etAmountTransfersFragment);
        etIBANTransfersFragment = view.findViewById(R.id.etIBANTransfersFragment);
        etPayeeTransfersFragment = view.findViewById(R.id.etPayeeTransfersFragment);
        etDescriptionTransfersActivity = view.findViewById(R.id.etDescriptionTransfersFragment);
        btnSend = view.findViewById(R.id.btnSendTransfersFragment);
    }

    private Transfer createTransfer() {
        UUID transferId = RandomUuidGenerator.generateRandomUuid();
        double transferAmount = Double.parseDouble(etAmountTranfersFragment.getText().toString());
        String transferPayee = etPayeeTransfersFragment.getText().toString();
        String transferIBAN = etIBANTransfersFragment.getText().toString().replace(" ", "");
        String transferDescription = etDescriptionTransfersActivity.getText().toString();
        UUID userId = user.getUserId();
        Timestamp transferDate = new Timestamp(System.currentTimeMillis());

        Transfer transfer = new Transfer(transferId, transferAmount, transferPayee, transferIBAN, transferDescription, userId, transferDate);

        if (transfer != null) {
            return transfer;
        }

        try {
            throw new Exception("Transfer is null!");
        } catch (Exception e) {
            throw new RuntimeException("Transfer is null!");
        }
    }

    private boolean validation() {
        boolean isValid = true;

        if (etAmountTranfersFragment.getText() == null || etAmountTranfersFragment.getText().toString().trim().isEmpty()) {
            etAmountTranfersFragment.setError("The amount field cannot be empty!");
            isValid = false;
        }

        if (etPayeeTransfersFragment.getText() == null || etPayeeTransfersFragment.getText().toString().trim().isEmpty()) {
            etPayeeTransfersFragment.setError("The payee field cannot be empty!");
            isValid = false;

            if (etPayeeTransfersFragment.getText().length() < 2) {
                etPayeeTransfersFragment.setError("At least 2 letters are required!");
                isValid = false;
            }
        }

        if (etIBANTransfersFragment.getText() == null || etIBANTransfersFragment.getText().toString().trim().isEmpty()) {
            etIBANTransfersFragment.setError("The IBAN field cannot be empty!");
            isValid = false;
        }

        if (etAmountTranfersFragment.getText().toString().length() == 1 && etAmountTranfersFragment.getText().toString().charAt(0) == '.') {
            etAmountTranfersFragment.setError("The amount field cannot contain just a dot!");
            isValid = false;
        }

        String amountString = etAmountTranfersFragment.getText().toString();
        if (amountString.matches(".+\\.0$") && Double.parseDouble(amountString) == 0) {
            etAmountTranfersFragment.setError("The amount field cannot have trailing '.0'!");
            isValid = false;
        }

        if (!amountString.isEmpty() && !amountString.equals(".")) {
            double amountValue = Double.parseDouble(amountString);
            boolean isDecimalZero = amountString.matches(".*\\.0$");
            boolean isIntegerZero = amountString.matches("0+");

            if (amountValue == 0.0 && (isDecimalZero || isIntegerZero)) {
                etAmountTranfersFragment.setError("The amount field cannot be zero!");
                isValid = false;
            }
        }

        return isValid;
    }

    private void enforceLettersOnly(EditText editText) {
        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                StringBuilder filteredStringBuilder = new StringBuilder();
                for (int i = start; i < end; i++) {
                    char currentChar = source.charAt(i);
                    if (Character.isLetter(currentChar)) {
                        filteredStringBuilder.append(currentChar);
                    }
                }
                return filteredStringBuilder.toString();
            }
        };

        editText.setFilters(new InputFilter[]{filter});
    }

    private void transferAmountValidation() {

        etAmountTranfersFragment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0 && !isValidDepositAmount(s.toString())) {
                    String string = validString(s.toString());
                    etAmountTranfersFragment.setText(string);
                    etAmountTranfersFragment.setSelection(string.length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private boolean isValidDepositAmount(String string) {
        return string.matches("[0-9.]*");
    }

    private String validString(String string) {
        return string.replaceAll("[^0-9.]", "");
    }

    private void transferIBANValidation() {
        etIBANTransfersFragment.addTextChangedListener(new TextWatcher() {
            final char space = ' ';

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String ibanRegex = "RO\\d{2}[A-Z]{4}\\d{16}";
                String string = s.toString();
                if (!(string.replace(" ", "")).matches(ibanRegex)) {
                    etIBANTransfersFragment.setError("The format of the IBAN is invalid!");
                }

                if (s.length() > 0 && (s.length() % 5) == 0) {
                    final char c = s.charAt(s.length() - 1);
                    if (space == c) {
                        s.delete(s.length() - 1, s.length());
                    }
                }

                if (s.length() > 0 && (s.length() % 5) == 0) {
                    char c = s.charAt(s.length() - 1);
                    if (TextUtils.split(s.toString(), String.valueOf(space)).length <= 6) {
                        s.insert(s.length() - 1, String.valueOf(space));
                    }
                }
            }
        });
    }

    private void getUser() {
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        user = sharedViewModel.getUser().getValue();
        if (user == null) {
            try {
                throw new Exception("The user in null in TransfersFragment!");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

}

