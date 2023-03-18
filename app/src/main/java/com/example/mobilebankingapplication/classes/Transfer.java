package com.example.mobilebankingapplication.classes;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Transfer implements Parcelable {

    private long transferId;
    private double transferAmount;
    private boolean transferIsInvoice;
    private String transferPayee;
    private String transferIBAN;
    private String transferDescription;
    private long userId;

    public Transfer(long transferId, double transferAmount, boolean transferIsInvoice, String transferPayee, String transferIBAN, String transferDescription, long userId) {
        this.transferId = transferId;
        this.transferAmount = transferAmount;
        this.transferIsInvoice = transferIsInvoice;
        this.transferPayee = transferPayee;
        this.transferIBAN = transferIBAN;
        this.transferDescription = transferDescription;
        this.userId = userId;
    }

    protected Transfer(Parcel in) {
        transferId = in.readLong();
        transferAmount = in.readDouble();
        transferIsInvoice = in.readByte() != 0;
        transferPayee = in.readString();
        transferIBAN = in.readString();
        transferDescription = in.readString();
        userId = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(transferId);
        dest.writeDouble(transferAmount);
        dest.writeByte((byte) (transferIsInvoice ? 1 : 0));
        dest.writeString(transferPayee);
        dest.writeString(transferIBAN);
        dest.writeString(transferDescription);
        dest.writeLong(userId);
    }

    @Override
    public int describeContents() {
        return 0;
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

    public long getTransferId() {
        return transferId;
    }

    public void setTransferId(long transferId) {
        this.transferId = transferId;
    }

    public double getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(double transferAmount) {
        this.transferAmount = transferAmount;
    }

    public boolean isTransferIsInvoice() {
        return transferIsInvoice;
    }

    public void setTransferIsInvoice(boolean transferIsInvoice) {
        this.transferIsInvoice = transferIsInvoice;
    }

    public String getTransferPayee() {
        return transferPayee;
    }

    public void setTransferPayee(String transferPayee) {
        this.transferPayee = transferPayee;
    }

    public String getTransferIBAN() {
        return transferIBAN;
    }

    public void setTransferIBAN(String transferIBAN) {
        this.transferIBAN = transferIBAN;
    }

    public String getTransferDescription() {
        return transferDescription;
    }

    public void setTransferDescription(String transferDescription) {
        this.transferDescription = transferDescription;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Transfer{" +
                "transferId=" + transferId +
                ", transferAmount=" + transferAmount +
                ", transferIsInvoice=" + transferIsInvoice +
                ", transferPayee='" + transferPayee + '\'' +
                ", transferIBAN='" + transferIBAN + '\'' +
                ", transferDescription='" + transferDescription + '\'' +
                ", userId=" + userId +
                '}';
    }
}
