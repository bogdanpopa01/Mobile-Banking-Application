package com.example.mobilebankingapplication.classes;

public class DepositUpdatedEvent {
    private boolean isUpdated;

    public DepositUpdatedEvent(boolean isUpdated) {
        this.isUpdated = isUpdated;
    }

    public boolean isUpdated() {
        return isUpdated;
    }
}
