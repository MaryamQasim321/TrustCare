package com.example.trustcare.logging;
public class LogUtils {
    public static String warn(String message){
        return "{\"status\": \"warn\", \"message\": \"" + message + "\"}";
    }
public static String error(String message){
        return "{\"status\": \"error\", \"message\": \"" + message + "\"}";
}
public static String info(String message){
        return "{\"status\": \"info\", \"message\": \"" + message + "\"}";
}
public static String debug(String message){
        return "{\"status\": \"debug\", \"message\": \"" + message + "\"}";
}
public static String success(String message){
        return "{\"status\": \"success\", \"message\": \"" + message + "\"}";
}
}