package com.example.mobilebankingapplication.classes;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.mobilebankingapplication.enums.TransactionType;
import com.example.mobilebankingapplication.utils.DateConverter;

import java.sql.Timestamp;
import java.util.Date;

public class Transaction implements Parcelable {
    private long transactionId;
    private String transactionName;
    private double transactionAmount;
    private Timestamp transactionDate;
    private TransactionType transactionType;
    private long userId;

    public Transaction(long transactionId, String transactionName, double transactionAmount, Timestamp transactionDate, TransactionType transactionType, long userId) {
        this.transactionId = transactionId;
        this.transactionName = transactionName;
        this.transactionAmount = transactionAmount;
        this.transactionDate = transactionDate;
        this.transactionType = transactionType;
        this.userId = userId;
    }

    protected Transaction(Parcel in) {
        transactionId = in.readLong();
        transactionName = in.readString();
        transactionAmount = in.readDouble();
        transactionDate = DateConverter.stringToTimestamp(in.readString());
        transactionType = TransactionType.valueOf(in.readString());
        userId = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(transactionId);
        dest.writeString(transactionName);
        dest.writeDouble(transactionAmount);
        dest.writeString(DateConverter.timestampToString(transactionDate));
        dest.writeString(transactionType.toString());
        dest.writeLong(userId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Transaction> CREATOR = new Creator<Transaction>() {
        @Override
        public Transaction createFromParcel(Parcel in) {
            return new Transaction(in);
        }

        @Override
        public Transaction[] newArray(int size) {
            return new Transaction[size];
        }
    };

    public long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(long transactionId) {
        this.transactionId = transactionId;
    }

    public String getTransactionName() {
        return transactionName;
    }

    public void setTransactionName(String transactionName) {
        this.transactionName = transactionName;
    }

    public double getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(double transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public Timestamp getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Timestamp transactionDate) {
        this.transactionDate = transactionDate;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId=" + transactionId +
                ", transactionName='" + transactionName + '\'' +
                ", transactionAmount=" + transactionAmount +
                ", transactionDate=" + transactionDate +
                ", transactionType=" + transactionType +
                ", userId=" + userId +
                '}';
    }
}
