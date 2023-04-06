package com.example.mobilebankingapplication.utils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    private MutableLiveData<Long> longValue = new MutableLiveData<>();

    public void setLongValue(Long value) {
        longValue.setValue(value);
    }

    public LiveData<Long> getLongValue() {
        return longValue;
    }

    public long getData() {
        if (longValue.getValue() != null) {
            return longValue.getValue();
        } else {
            return 0;
        }
    }
}
