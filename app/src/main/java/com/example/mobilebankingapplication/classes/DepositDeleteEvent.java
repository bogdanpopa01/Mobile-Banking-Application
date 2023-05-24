package com.example.mobilebankingapplication.classes;

public class DepositDeleteEvent {
    private boolean isDeleted;

    public DepositDeleteEvent(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public boolean isDeleted() {
        return isDeleted;
    }
}
