package com.example.mobilebankingapplication.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mobilebankingapplication.R;
import com.example.mobilebankingapplication.classes.User;
import com.example.mobilebankingapplication.utils.SharedViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class HomeFragment extends Fragment {
    FloatingActionButton fabSeeDeposits, fabAddDeposit;
    private SharedViewModel sharedViewModel;
    private User user;
    private View view;
    private TextView tvCardNumberHomeFragment, tvUserFirstAndLastName, tvValidThruDateHomeFragment, tvBalanceHomeFragment;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        initializeComponents();
        getUser();
        populateFields();

        fabSeeDeposits = view.findViewById(R.id.fabSeeDeposits);
        fabSeeDeposits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getChildFragmentManager();
                Fragment fragment = fragmentManager.findFragmentById(R.id.depositsFragment);
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                if (fragment != null && fragment.isVisible()) {
                    fragmentTransaction.remove(fragment);
                } else {
                    fragmentTransaction.add(R.id.depositsFragment, new DepositsFragment());
                }
                fragmentTransaction.commit();
            }
        });
        fabAddDeposit = view.findViewById(R.id.fabAddDeposit);
        fabAddDeposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getChildFragmentManager();

                // Remove fabSeeDeposits if it exists in the FrameLayout
                Fragment seeDepositsFragment = fragmentManager.findFragmentById(R.id.depositsFragment);
                if (seeDepositsFragment != null) {
                    FragmentTransaction removeTransaction = fragmentManager.beginTransaction();
                    removeTransaction.remove(seeDepositsFragment);
                    removeTransaction.commit();
                }

                // Show the AddDepositFragment
                AddDepositFragment addDepositFragment = new AddDepositFragment();
                addDepositFragment.show(fragmentManager, "AddDepositFragment");
            }
        });


        // to change the UI after a savings deposit has been opened
        sharedViewModel.getUser().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User updatedUser) {
                if(updatedUser!=null){
                    user = updatedUser;
                    populateBalance();
                }
            }
        });
        return view;
    }

    private void initializeComponents() {
        tvCardNumberHomeFragment = view.findViewById(R.id.tvCardNumberHomeFragment);
        tvUserFirstAndLastName = view.findViewById(R.id.tvUserNameHomeFragment);
        tvValidThruDateHomeFragment = view.findViewById(R.id.tvValidThruDateHomeFragment);
        tvBalanceHomeFragment = view.findViewById(R.id.tvBalanceValueHomeFragment);
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

    private void populateFields() {
        populateCardNumber();
        populateFirstAndLastName();
        populateCardExpirationDate();
        populateBalance();
    }

    private void populateCardNumber(){
        String cardNumber = user.getCardNumber();
        StringBuilder formattedCardNumber = new StringBuilder();
        for (int i = 0; i < cardNumber.length(); i++) {
            formattedCardNumber.append(cardNumber.charAt(i));
            if ((i + 1) % 4 == 0) {
                formattedCardNumber.append(" ");
            }
        }
        tvCardNumberHomeFragment.setText(formattedCardNumber);
    }

    private void populateFirstAndLastName(){
        String lastName = user.getLastName();
        String firstName = user.getFirstName();
        StringBuilder firstAndLastName = new StringBuilder();
        firstAndLastName.append(lastName);
        firstAndLastName.append(" ");
        firstAndLastName.append(firstName);
        tvUserFirstAndLastName.setText(firstAndLastName);
    }

    private void populateCardExpirationDate() {
        java.util.Date cardExpirationDate = user.getCardExpirationDate();
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/yy", Locale.US);
        String cardExpirationDateString = simpleDateFormat.format(cardExpirationDate);
        tvValidThruDateHomeFragment.setText(cardExpirationDateString);
    }

    private void populateBalance(){
        double balance = user.getBalance();
        DecimalFormat decimalFormat = new DecimalFormat("#,###.0");
        String balanceString = decimalFormat.format(balance);
        tvBalanceHomeFragment.setText(balanceString);
    }
}