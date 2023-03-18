package com.example.mobilebankingapplication.classes;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.mobilebankingapplication.utils.DateConverter;

import java.util.Date;

public class Deposit implements Parcelable {
    private long depositId;
    private String depositName;
    private double depositAmount;
    private int depositPeriod;
    private double depositInterestRate;
    private Date depositTimeLeft;
    private long userId;

    public Deposit(long depositId, String depositName, double depositAmount, int depositPeriod, double depositInterestRate, Date depositTimeLeft, long userId) {
        this.depositId = depositId;
        this.depositName = depositName;
        this.depositAmount = depositAmount;
        this.depositPeriod = depositPeriod;
        this.depositInterestRate = depositInterestRate;
        this.depositTimeLeft = depositTimeLeft;
        this.userId = userId;
    }

    protected Deposit(Parcel in) {
        depositId = in.readLong();
        depositName = in.readString();
        depositAmount = in.readDouble();
        depositPeriod = in.readInt();
        depositInterestRate = in.readDouble();
        depositTimeLeft = DateConverter.stringToDate(in.readString());
        userId = in.readLong();
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

    public long getDepositId() {
        return depositId;
    }

    public void setDepositId(long depositId) {
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

    public Date getDepositTimeLeft() {
        return depositTimeLeft;
    }

    public void setDepositTimeLeft(Date depositTimeLeft) {
        this.depositTimeLeft = depositTimeLeft;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
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
        dest.writeLong(depositId);
        dest.writeString(depositName);
        dest.writeDouble(depositAmount);
        dest.writeInt(depositPeriod);
        dest.writeDouble(depositInterestRate);
        dest.writeString(DateConverter.dateToString(depositTimeLeft));
        dest.writeLong(userId);
    }
}
