package com.example.mobilebankingapplication.classes;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.mobilebankingapplication.utils.DateConverter;

import java.util.Date;

public class Transfer implements Parcelable {

    private long transferId;
    private double transferAmount;
    private String transferPayee;
    private String transferIBAN;
    private String transferDescription;
    private long userId;
    private Date transferDate;

    public Transfer(long transferId, double transferAmount, String transferPayee, String transferIBAN, String transferDescription, long userId, Date transferDate) {
        this.transferId = transferId;
        this.transferAmount = transferAmount;
        this.transferPayee = transferPayee;
        this.transferIBAN = transferIBAN;
        this.transferDescription = transferDescription;
        this.userId = userId;
        this.transferDate = transferDate;
    }

    protected Transfer(Parcel in) {
        transferId = in.readLong();
        transferAmount = in.readDouble();
        transferPayee = in.readString();
        transferIBAN = in.readString();
        transferDescription = in.readString();
        userId = in.readLong();
        transferDate = DateConverter.stringToDate(in.readString());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(transferId);
        dest.writeDouble(transferAmount);
        dest.writeString(transferPayee);
        dest.writeString(transferIBAN);
        dest.writeString(transferDescription);
        dest.writeLong(userId);
        dest.writeString(DateConverter.dateToString(transferDate));
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

    public Date getTransferDate() {
        return transferDate;
    }

    public void setTransferDate(Date transferDate) {
        this.transferDate = transferDate;
    }

    @Override
    public String toString() {
        return "Transfer{" +
                "transferId=" + transferId +
                ", transferAmount=" + transferAmount +
                ", transferPayee='" + transferPayee + '\'' +
                ", transferIBAN='" + transferIBAN + '\'' +
                ", transferDescription='" + transferDescription + '\'' +
                ", userId=" + userId +
                ", transferDate=" + transferDate +
                '}';
    }
}
