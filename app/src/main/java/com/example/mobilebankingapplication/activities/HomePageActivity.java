package com.example.mobilebankingapplication.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.mobilebankingapplication.R;
import com.example.mobilebankingapplication.databinding.ActivityHomePageBinding;
import com.example.mobilebankingapplication.fragments.HomeFragment;
import com.example.mobilebankingapplication.fragments.ReportsFragment;
import com.example.mobilebankingapplication.fragments.TransactionsFragment;
import com.example.mobilebankingapplication.fragments.TransfersFragment;

public class HomePageActivity extends AppCompatActivity {
    ActivityHomePageBinding activityHomePageBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityHomePageBinding = ActivityHomePageBinding.inflate(getLayoutInflater());
        setContentView(activityHomePageBinding.getRoot());
        replaceFragment(new HomeFragment());

        activityHomePageBinding.bottomNavigationHomePage.setOnItemSelectedListener(item -> {

            switch (item.getItemId()){
                case R.id.bottom_navigation_home:
                    replaceFragment(new HomeFragment());
                    break;
                case R.id.bottom_navigation_reports:
                    replaceFragment(new ReportsFragment());
                    break;
                case R.id.bottom_navigation_transactions:
                    replaceFragment(new TransactionsFragment());
                    break;
                case R.id.bottom_navigation_transfers:
                    replaceFragment(new TransfersFragment());
                    break;
            }

            return true;
        });
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainerHomePage,fragment);
        fragmentTransaction.commit();
    }
}