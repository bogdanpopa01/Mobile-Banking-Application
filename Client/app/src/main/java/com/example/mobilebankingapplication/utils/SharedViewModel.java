package com.example.mobilebankingapplication.utils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mobilebankingapplication.classes.User;

public class SharedViewModel extends ViewModel {
    private MutableLiveData<Long> longValue = new MutableLiveData<>();
    private MutableLiveData<User> userLiveData = new MutableLiveData<>();

    public void setLongValue(Long value) {
        longValue.setValue(value);
    }

    public void setUser(User user){
        userLiveData.setValue(user);
    }

    public LiveData<Long> getLongValue() {
        return longValue;
    }

    public LiveData<User> getUser(){
        return userLiveData;
    }

    public long getLongData() {
        if (longValue.getValue() != null) {
            return longValue.getValue();
        } else {
            return 0;
        }
    }
}
