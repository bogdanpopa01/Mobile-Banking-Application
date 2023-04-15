package com.example.mobilebankingapplication.classes;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.mobilebankingapplication.utils.ConverterUUID;
import com.example.mobilebankingapplication.utils.DateConverter;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

public class Transfer implements Parcelable {

    private UUID transferId;
    private double transferAmount;
    private String transferPayee;
    private String transferIBAN;
    private String transferDescription;
    private UUID userId;
    private Timestamp transferDate;

    public Transfer(UUID transferId, double transferAmount, String transferPayee, String transferIBAN, String transferDescription, UUID userId, Timestamp transferDate) {
        this.transferId = transferId;
        this.transferAmount = transferAmount;
        this.transferPayee = transferPayee;
        this.transferIBAN = transferIBAN;
        this.transferDescription = transferDescription;
        this.userId = userId;
        this.transferDate = transferDate;
    }

    protected Transfer(Parcel in) {
        transferId = ConverterUUID.stringToUUID(in.readString());
        transferAmount = in.readDouble();
        transferPayee = in.readString();
        transferIBAN = in.readString();
        transferDescription = in.readString();
        userId = ConverterUUID.stringToUUID(in.readString());
        transferDate = DateConverter.stringToTimestamp(in.readString());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ConverterUUID.UUIDtoString(transferId));
        dest.writeDouble(transferAmount);
        dest.writeString(transferPayee);
        dest.writeString(transferIBAN);
        dest.writeString(transferDescription);
        dest.writeString(ConverterUUID.UUIDtoString(userId));
        dest.writeString(DateConverter.timestampToString(transferDate));
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

    public UUID getTransferId() {
        return transferId;
    }

    public void setTransferId(UUID transferId) {
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

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public Timestamp getTransferDate() {
        return transferDate;
    }

    public void setTransferDate(Timestamp transferDate) {
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
