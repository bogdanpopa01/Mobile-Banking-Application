package com.example.mobilebankingapplication.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.mobilebankingapplication.R;
import com.example.mobilebankingapplication.databinding.ActivityHomePageBinding;
import com.example.mobilebankingapplication.fragments.HomeFragment;
import com.example.mobilebankingapplication.fragments.ReportsFragment;
import com.example.mobilebankingapplication.fragments.TransactionsFragment;
import com.example.mobilebankingapplication.fragments.TransfersFragment;
import com.example.mobilebankingapplication.utils.SharedViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class HomePageActivity extends AppCompatActivity {
    private SharedViewModel sharedViewModel;

    ActivityHomePageBinding activityHomePageBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityHomePageBinding = ActivityHomePageBinding.inflate(getLayoutInflater());
        setContentView(activityHomePageBinding.getRoot());

        sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);


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

    public SharedViewModel getSharedViewModel(){
        return sharedViewModel;
    }
}