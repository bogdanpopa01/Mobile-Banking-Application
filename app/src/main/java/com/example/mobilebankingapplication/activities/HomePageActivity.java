package com.example.mobilebankingapplication.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.mobilebankingapplication.R;
import com.example.mobilebankingapplication.classes.User;
import com.example.mobilebankingapplication.databinding.ActivityHomePageBinding;
import com.example.mobilebankingapplication.fragments.HomeFragment;
import com.example.mobilebankingapplication.fragments.ReportsFragment;
import com.example.mobilebankingapplication.fragments.TransactionsFragment;
import com.example.mobilebankingapplication.fragments.TransfersFragment;
import com.example.mobilebankingapplication.utils.SharedViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class HomePageActivity extends AppCompatActivity {
    User user = null;
    private SharedViewModel sharedViewModel;
    ActivityHomePageBinding activityHomePageBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityHomePageBinding = ActivityHomePageBinding.inflate(getLayoutInflater());
        setContentView(activityHomePageBinding.getRoot());
        getUser();

        sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);
        if(user!=null) {
            sharedViewModel.setUser(user);
        } else {
            Toast.makeText(this, "User-ul e null!", Toast.LENGTH_SHORT).show();
        }

        BottomNavigationView bottomNavigationHomePage = findViewById(R.id.bottomNavigationHomePage);
        replaceFragment(new HomeFragment());



        bottomNavigationHomePage.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.bottom_navigation_home:
                        replaceFragment(new HomeFragment());
                        return true;
                    case R.id.bottom_navigation_reports:
                        replaceFragment(new ReportsFragment());
                        return true;
                    case R.id.bottom_navigation_transfers:
                        replaceFragment(new TransfersFragment());
                        return true;
                    case R.id.bottom_navigation_transactions:
                        replaceFragment(new TransactionsFragment());
                        return true;
                }
                return false;
            }
        });
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainerHomePage,fragment);
        fragmentTransaction.commit();
    }

    private void getUser(){
        if(getIntent()!=null){
            user  = getIntent().getParcelableExtra(LoginActivity.USER_ACCOUNT_KEY);
            if(user!=null){
                Toast.makeText(this, "S-a primit userul cu usernameul: " + user.getUserName(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Userul e gol!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}