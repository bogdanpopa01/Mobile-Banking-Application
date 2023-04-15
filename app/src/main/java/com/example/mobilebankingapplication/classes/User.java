package com.example.mobilebankingapplication.classes;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.mobilebankingapplication.utils.ConverterUUID;
import com.example.mobilebankingapplication.utils.DateConverter;

import java.util.Date;
import java.util.UUID;

public class User implements Parcelable {
    private UUID userId;
    private String userName;
    private String email;
    private String password;
    private String telephone;
    private String firstName;
    private String lastName;
    private String cardNumber;
    private int cardCvv;
    private Date cardExpirationDate;
    private double balance;

    public User(UUID userId, String userName, String email, String password, String telephone, String firstName, String lastName, String cardNumber, int cardCvv, Date cardExpirationDate, double balance) {
        this.userId = userId;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.telephone = telephone;
        this.firstName = firstName;
        this.lastName = lastName;
        this.cardNumber = cardNumber;
        this.cardCvv = cardCvv;
        this.cardExpirationDate = cardExpirationDate;
        this.balance = balance;
    }

    protected User(Parcel in) {
        userId = ConverterUUID.stringToUUID(in.readString());
        userName = in.readString();
        email = in.readString();
        password = in.readString();
        telephone = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        cardNumber = in.readString();
        cardCvv = in.readInt();
        cardExpirationDate = DateConverter.stringToDate(in.readString());
        balance = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ConverterUUID.UUIDtoString(userId));
        dest.writeString(userName);
        dest.writeString(email);
        dest.writeString(password);
        dest.writeString(telephone);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(cardNumber);
        dest.writeInt(cardCvv);
        dest.writeString(DateConverter.dateToString(cardExpirationDate));
        dest.writeDouble(balance);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public int getCardCvv() {
        return cardCvv;
    }

    public void setCardCvv(int cardCvv) {
        this.cardCvv = cardCvv;
    }

    public Date getCardExpirationDate() {
        return cardExpirationDate;
    }

    public void setCardExpirationDate(Date cardExpirationDate) {
        this.cardExpirationDate = cardExpirationDate;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", telephone='" + telephone + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", cardNumber='" + cardNumber + '\'' +
                ", cardCvv=" + cardCvv +
                ", cardExpirationDate=" + cardExpirationDate +
                ", balance=" + balance +
                '}';
    }
}
