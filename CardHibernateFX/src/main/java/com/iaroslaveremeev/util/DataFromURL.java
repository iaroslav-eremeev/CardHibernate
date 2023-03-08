package com.iaroslaveremeev.util;

import com.iaroslaveremeev.dto.ResponseResult;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

public class DataFromURL {

    public static InputStream getData(String link, String method) {
        try {
            java.net.URL url = new java.net.URL(link);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod(method);
            if (httpURLConnection.getResponseCode() == 400){
                try (BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(httpURLConnection.getErrorStream()))){
                    throw new IOException(new ResponseResult<>(bufferedReader.readLine()).getMessage());
                }
            }
            return httpURLConnection.getInputStream();
        } catch (IOException ignored) {}
        return null;
    }
}
