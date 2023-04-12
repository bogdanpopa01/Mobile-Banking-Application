package com.example.mobilebankingapplication.database;

public class Constants {
    private static final String ROOT_URL = "http://192.168.0.104/MobileBankingApplication/v1/";
    public static final String URL_REGISTER_TRANSFER = ROOT_URL + "registerTransfer.php";
    public static final String URL_GET_ALL_TRANSFERS = ROOT_URL + "getAllTransfers.php";
    public static final String URL_GET_TRANSFER = ROOT_URL + "getTransfer.php";

    public static final String URL_REGISTER_TRANSACTION = ROOT_URL + "registerTransaction.php";
    public static final String URL_GET_ALL_TRANSACTIONS = ROOT_URL + "getAllTransactions.php";

    public static final String URL_REGISTER_DEPOSIT = ROOT_URL + "registerDeposit.php";
    public static final String URL_GET_ALL_DEPOSITS = ROOT_URL + "getAllDeposits.php";
}
