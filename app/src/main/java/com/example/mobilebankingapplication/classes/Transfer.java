package com.example.mobilebankingapplication.classes;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Transfer implements Parcelable {
    private double amount;
    private boolean isInvoice;
    private String payee;
    private String IBAN;
    private String description;

    public Transfer(double amount, boolean isInvoice, String payee, String IBAN, String description) {
        this.amount = amount;
        this.isInvoice = isInvoice;
        this.payee = payee;
        this.IBAN = IBAN;
        this.description = description;
    }

    protected Transfer(Parcel in) {
        amount = in.readDouble();
        isInvoice = in.readByte() != 0;
        payee = in.readString();
        IBAN = in.readString();
        description = in.readString();
    }

    public static final Creator<Transfer> CREATOR = new Creator<Transfer>() {
        @Override
        public Transfer createFromParcel(Parcel in) {
            return new Transfer(in);
        }

        @Override
        public Transfer[] newArray(int size) {
            return new Transfer[size];
        }
    };

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public boolean isInvoice() {
        return isInvoice;
    }

    public void setInvoice(boolean invoice) {
        isInvoice = invoice;
    }

    public String getPayee() {
        return payee;
    }

    public void setPayee(String payee) {
        this.payee = payee;
    }

    public String getIBAN() {
        return IBAN;
    }

    public void setIBAN(String IBAN) {
        this.IBAN = IBAN;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Transfer{" +
                "amount=" + amount +
                ", isInvoice=" + isInvoice +
                ", payee='" + payee + '\'' +
                ", IBAN='" + IBAN + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeDouble(amount);
        dest.writeByte((byte) (isInvoice ? 1 : 0));
        dest.writeString(payee);
        dest.writeString(IBAN);
        dest.writeString(description);
    }
}
