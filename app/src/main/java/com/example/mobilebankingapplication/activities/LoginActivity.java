package com.example.mobilebankingapplication.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mobilebankingapplication.R;
import com.example.mobilebankingapplication.classes.User;
import com.example.mobilebankingapplication.database.Constants;
import com.example.mobilebankingapplication.database.RequestHandler;
import com.example.mobilebankingapplication.utils.ConverterUUID;
import com.example.mobilebankingapplication.utils.DateConverter;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.UUID;

public class LoginActivity extends AppCompatActivity {
    public static final String USER_ACCOUNT_KEY = "USER_ACCOUNT_KEY";
    private EditText etUserNameLogin, etPasswordLogin;
    private Button btnSignInLogin;

    private void initializeComponents() {
        etPasswordLogin = findViewById(R.id.etPasswordLogin);
        etUserNameLogin = findViewById(R.id.etUserNameLogin);
        btnSignInLogin = findViewById(R.id.btnSignInLogin);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initializeComponents();

        btnSignInLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = etUserNameLogin.getText().toString();
                String password = etPasswordLogin.getText().toString();


                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                String url = Constants.URL_GET_USER + "?userName=" + userName + "&password=" + password;
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(!userName.isEmpty() && !password.isEmpty())
                        {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                boolean error = jsonObject.getBoolean("error");
                                if (!error) {
                                    Intent intent = new Intent(getApplicationContext(), HomePageActivity.class);

                                    UUID userId = ConverterUUID.stringToUUID(jsonObject.getString("userId").trim());
                                    String userName = jsonObject.getString("userName");
                                    String email = jsonObject.getString("email");
                                    String password = jsonObject.getString("password");
                                    String telephone = jsonObject.getString("telephone");
                                    String firstName = jsonObject.getString("firstName");
                                    String lastName = jsonObject.getString("lastName");
                                    String cardNumber = jsonObject.getString("cardNumber");
                                    String cardCvv = jsonObject.getString("cardCvv");
                                    Timestamp cardExpirationDate = DateConverter.stringToTimestamp(jsonObject.getString("cardExpirationDate"));
                                    double balance = jsonObject.getDouble("balance");

                                    User user = new User(userId, userName, email, password, telephone, firstName, lastName, cardNumber, cardCvv, cardExpirationDate, balance);
                                    intent.putExtra(USER_ACCOUNT_KEY,user);

                                    startActivity(intent);
                                } else {
                                    // User is invalid, display error message
                                    String message = jsonObject.getString("message");
                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(),R.string.error_username_or_password_empty,Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle network errors
                        Toast.makeText(getApplicationContext(), "Network error", Toast.LENGTH_SHORT).show();
                    }
                });
                RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
            }
        });

    }
}