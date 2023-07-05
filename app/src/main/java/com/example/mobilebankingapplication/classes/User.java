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
    private String cardCvv;
    private Date cardExpirationDate;
    private double balance;
    private String IBAN;

    public User(UUID userId, String userName, String email, String password, String telephone, String firstName, String lastName, String cardNumber, String cardCvv, Date cardExpirationDate, double balance, String IBAN) {
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
        this.IBAN = IBAN;
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
        cardCvv = in.readString();
        cardExpirationDate = DateConverter.stringToDate(in.readString());
        balance = in.readDouble();
        IBAN = in.readString();
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
        dest.writeString(cardCvv);
        dest.writeString(DateConverter.dateToString(cardExpirationDate));
        dest.writeDouble(balance);
        dest.writeString(IBAN);
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

    public String getCardCvv() {
        return cardCvv;
    }

    public void setCardCvv(String cardCvv) {
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

    public String getIBAN() {
        return IBAN;
    }

    public void setIBAN(String IBAN) {
        this.IBAN = IBAN;
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
                ", cardCvv='" + cardCvv + '\'' +
                ", cardExpirationDate=" + cardExpirationDate +
                ", balance=" + balance +
                ", IBAN='" + IBAN + '\'' +
                '}';
    }
}
