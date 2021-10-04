package com.hishd.qurantime.APIService;

public class APIOperation {
    //Singleton Instance
    private static final APIOperation instance = new APIOperation();
    private final String TAG = "API_Operation";

    //Singleton Constructor
    private APIOperation() {
    }

    //Singleton request instance
    public static APIOperation getInstance() {
        return instance;
    }
}
