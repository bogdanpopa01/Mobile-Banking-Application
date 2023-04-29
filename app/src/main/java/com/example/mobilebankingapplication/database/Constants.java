package com.example.mobilebankingapplication.database;

public class Constants {
    private static final String IP = "192.168.171.11";
    private static final String ROOT_URL = "http://" + IP + "/MobileBankingApplication/v1/";
    public static final String URL_REGISTER_TRANSFER = ROOT_URL + "registerTransfer.php";
    public static final String URL_GET_ALL_TRANSFERS = ROOT_URL + "getAllTransfers.php";
    public static final String URL_GET_TRANSFER = ROOT_URL + "getTransfer.php";
    public static final String URL_GET_USER = ROOT_URL + "getUserByCredentials.php";

    public static final String URL_REGISTER_TRANSACTION = ROOT_URL + "registerTransaction.php";
    public static final String URL_GET_ALL_TRANSACTIONS = ROOT_URL + "getAllTransactions.php";
    public static final String URL_REGISTER_DEPOSIT = ROOT_URL + "registerDeposit.php";
    public static final String URL_GET_ALL_DEPOSITS = ROOT_URL + "getAllDeposits.php";
    public static final String URL_DELETE_DEPOSIT = ROOT_URL + "deleteDeposit.php";
    public static final String URL_UPDATE_DEPOSIT = ROOT_URL + "updateDeposit.php";
}
