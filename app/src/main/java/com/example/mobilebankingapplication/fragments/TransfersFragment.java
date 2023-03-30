package com.example.mobilebankingapplication.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mobilebankingapplication.R;
import com.example.mobilebankingapplication.classes.Transfer;
import com.example.mobilebankingapplication.database.Constants;
import com.example.mobilebankingapplication.utils.RandomLongGenerator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TransfersFragment extends Fragment {
    private EditText etAmountTranfersFragment, etPayeeTransfersFragment, etIBANTransfersFragment, etDescriptionTransfersActivity;
    private SwitchCompat switchIsInvoice;
    private Spinner spinnerCompanies;
    private Button btnSend;
    private View view;

    public TransfersFragment() {
        // Required empty public constructor
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
        transferAmountValidation();
        transferIBANValidation();

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validation()) {
                    Transfer transfer = createTransfer();
                    if (transfer != null) {
                        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                                Constants.URL_REGISTER_TRANSFER,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            Toast.makeText(getContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
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
                                params.put("transferId", String.valueOf(transfer.getTransferId()));
                                params.put("transferAmount", String.valueOf(transfer.getTransferAmount()));
                                params.put("transferIsInvoice", String.valueOf(transfer.isTransferIsInvoice()));
                                params.put("transferPayee", transfer.getTransferPayee());
                                params.put("transferIBAN", transfer.getTransferIBAN());
                                params.put("transferDescription", transfer.getTransferDescription());
                                params.put("userId", String.valueOf(transfer.getUserId()));
                                return params;
                            }
                        };

                        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                        requestQueue.add(stringRequest);

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
        switchIsInvoice = view.findViewById(R.id.switchIsInvoice);
        spinnerCompanies = view.findViewById(R.id.spinnerCompanies);
        ArrayAdapter<CharSequence> arrayAdapterSpinner = ArrayAdapter.createFromResource(getContext(), R.array.array_companies, android.R.layout.simple_spinner_dropdown_item);
        spinnerCompanies.setAdapter(arrayAdapterSpinner);
        btnSend = view.findViewById(R.id.btnSendTransfersFragment);
    }

    private Transfer createTransfer() {
        long transferId = RandomLongGenerator.generateLong();
        double transferAmount = Double.parseDouble(etAmountTranfersFragment.getText().toString());
        boolean isInvoice = switchIsInvoice.isChecked();
        String transferPayee = etPayeeTransfersFragment.getText().toString();
        String transferIBAN = etIBANTransfersFragment.getText().toString().replace(" ", "");
        String transferDescription = etDescriptionTransfersActivity.getText().toString();
        long userId = RandomLongGenerator.generateLong();

        Transfer transfer = new Transfer(transferId, transferAmount, isInvoice, transferPayee, transferIBAN, transferDescription, userId);

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
        }

        if (etIBANTransfersFragment.getText() == null || etIBANTransfersFragment.getText().toString().trim().isEmpty()) {
            etIBANTransfersFragment.setError("The IBAN field cannot be empty!");
            isValid = false;
        }

        return isValid;
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
}