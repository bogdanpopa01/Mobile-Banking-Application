package com.example.mobilebankingapplication.utils;

import java.util.UUID;

public class ConverterUUID {

    public static String UUIDtoString(UUID uuid){
        return uuid.toString();
    }

    public static UUID stringToUUID(String string){
        return UUID.fromString(string);
    }
}
