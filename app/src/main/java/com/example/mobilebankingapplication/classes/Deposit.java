package com.example.mobilebankingapplication.classes;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.mobilebankingapplication.utils.ConverterUUID;
import com.example.mobilebankingapplication.utils.DateConverter;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

public class Deposit implements Parcelable {
    private UUID depositId;
    private String depositName;
    private double depositAmount;
    private int depositPeriod;
    private double depositInterestRate;
    private Timestamp depositTimeLeft;
    private UUID userId;

    public Deposit(UUID depositId, String depositName, double depositAmount, int depositPeriod, double depositInterestRate, Timestamp depositTimeLeft, UUID userId) {
        this.depositId = depositId;
        this.depositName = depositName;
        this.depositAmount = depositAmount;
        this.depositPeriod = depositPeriod;
        this.depositInterestRate = depositInterestRate;
        this.depositTimeLeft = depositTimeLeft;
        this.userId = userId;
    }

    protected Deposit(Parcel in) {
        depositId = ConverterUUID.stringToUUID(in.readString());
        depositName = in.readString();
        depositAmount = in.readDouble();
        depositPeriod = in.readInt();
        depositInterestRate = in.readDouble();
        depositTimeLeft = DateConverter.stringToTimestamp(in.readString());
        userId = ConverterUUID.stringToUUID(in.readString());
    }

    public static final Creator<Deposit> CREATOR = new Creator<Deposit>() {
        @Override
        public Deposit createFromParcel(Parcel in) {
            return new Deposit(in);
        }

        @Override
        public Deposit[] newArray(int size) {
            return new Deposit[size];
        }
    };

    public UUID getDepositId() {
        return depositId;
    }

    public void setDepositId(UUID depositId) {
        this.depositId = depositId;
    }

    public String getDepositName() {
        return depositName;
    }

    public void setDepositName(String depositName) {
        this.depositName = depositName;
    }

    public double getDepositAmount() {
        return depositAmount;
    }

    public void setDepositAmount(double depositAmount) {
        this.depositAmount = depositAmount;
    }

    public int getDepositPeriod() {
        return depositPeriod;
    }

    public void setDepositPeriod(int depositPeriod) {
        this.depositPeriod = depositPeriod;
    }

    public double getDepositInterestRate() {
        return depositInterestRate;
    }

    public void setDepositInterestRate(double depositInterestRate) {
        this.depositInterestRate = depositInterestRate;
    }

    public Timestamp getDepositTimeLeft() {
        return depositTimeLeft;
    }

    public void setDepositTimeLeft(Timestamp depositTimeLeft) {
        this.depositTimeLeft = depositTimeLeft;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Deposit{" +
                "depositId=" + depositId +
                ", depositName='" + depositName + '\'' +
                ", depositAmount=" + depositAmount +
                ", depositPeriod=" + depositPeriod +
                ", depositInterestRate=" + depositInterestRate +
                ", depositTimeLeft=" + depositTimeLeft +
                ", userId=" + userId +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(ConverterUUID.UUIDtoString(depositId));
        dest.writeString(depositName);
        dest.writeDouble(depositAmount);
        dest.writeInt(depositPeriod);
        dest.writeDouble(depositInterestRate);
        dest.writeString(DateConverter.timestampToString(depositTimeLeft));
        dest.writeString(ConverterUUID.UUIDtoString(userId));
    }
}
