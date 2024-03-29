package com.example.mobilebankingapplication.classes;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.mobilebankingapplication.enums.TransactionType;
import com.example.mobilebankingapplication.utils.ConverterUUID;
import com.example.mobilebankingapplication.utils.DateConverter;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Comparator;
import java.util.Date;
import java.util.UUID;

public class Transaction implements Parcelable {
    private UUID transactionId;
    private String transactionName;
    private double transactionAmount;
    private Timestamp transactionDate;
    private TransactionType transactionType;
    private UUID userId;

    public Transaction(UUID transactionId, String transactionName, double transactionAmount, Timestamp transactionDate, TransactionType transactionType, UUID userId) {
        this.transactionId = transactionId;
        this.transactionName = transactionName;
        this.transactionAmount = transactionAmount;
        this.transactionDate = transactionDate;
        this.transactionType = transactionType;
        this.userId = userId;
    }
    protected Transaction(Parcel in) {
        transactionId = ConverterUUID.stringToUUID(in.readString());
        transactionName = in.readString();
        transactionAmount = in.readDouble();
        transactionDate = DateConverter.stringToTimestamp(in.readString());
        transactionType = TransactionType.valueOf(in.readString());
        userId = ConverterUUID.stringToUUID(in.readString());
    }

    public static Comparator<Transaction> amountAscending = new Comparator<Transaction>() {
        @Override
        public int compare(Transaction t1, Transaction t2) {
            BigDecimal a1 = BigDecimal.valueOf(t1.getTransactionAmount());
            BigDecimal a2 = BigDecimal.valueOf(t2.getTransactionAmount());

            return a1.compareTo(a2);
        }
    };


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ConverterUUID.UUIDtoString(transactionId));
        dest.writeString(transactionName);
        dest.writeDouble(transactionAmount);
        dest.writeString(DateConverter.timestampToString(transactionDate));
        dest.writeString(transactionType.toString());
        dest.writeString(ConverterUUID.UUIDtoString(userId));
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

    public UUID getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(UUID transactionId) {
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

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
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
