package com.example.stephanotelolahy.samplenetwork;

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by stephanotelolahy on 26/10/2016.
 */

public class DownloadTask extends AsyncTask<String, Void, Boolean> {

    private final String keyWord;
    private DownloadTaskListener listener;

    private String result;
    private Exception error;

    public DownloadTask(String keyWord, DownloadTaskListener listener) {
        super();
        this.keyWord = keyWord;
        this.listener = listener;
    }

    @Override
    protected Boolean doInBackground(String... params) {

        try {
            this.result = this.downloadModel();
            return Boolean.TRUE;
        } catch (IOException e) {
            e.printStackTrace();
            this.error = e;
            return Boolean.FALSE;
        }
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);

        if (null != this.listener) {
            if (aBoolean.booleanValue()) {
                this.listener.downloadTaskDidSucceed(this.result);
            } else {
                this.listener.downloadTaskDidFail(this.error);
            }
        }
    }

    private String downloadModel() throws IOException {

        String contentAsString = null;

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();

        Headers customHeaders = Headers.of(
                "Authorization",
                "Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXUyJ9.eyJleHAiOjE0ODUxNjUzNjEsInVzZXJuYW1lIjoic3RlcGhhbm8udGVsb2xhaHlAYnVzaW5lc3NkZWNpc2lvbi5jb20iLCJpYXQiOiIxNDc3Mzg5MzYxIn0.C4ae3kvCikmYaddDHHejTYNIqrWLBRxGMnBg_UR0pIv_-xHlh-UUUQ16tOzCO54Zh-Lt_iH1Rcv6bM28wyU70L5VQ9F-CBB6Vk1lyvU7mGozojCisAxB_RPs8rAJGAi49f3XpRQXxuBTtQCz3mBoAYwSKDaqiRM566NJLU8hKTKcwaJAs8DxuPdwQHz9n6SQp6WEjq61Fclp9hmr1A5ExNAVAqj1llv5CZOEboichY5NCgQIVC5ZXKL0lh8Y8zZw0-V_3xXbUbfB_c6aDT2kw1i1RJ6kCKlTtS0OI-GVv4dhHUxAD6pI52VAb0gmeNSsXMAVG30NJbuRytqxIraiIhLIfTcbmnjsbG1RARg3zHdum91qwq7boujQZDtSvQsZH0Cu0V7eq6eQi29M0llkrSQLvSVk8vvorrwfG5SjsEUwHgQzYky40FI83LGxq5ug1AOdfQYGrax8_I38w3H2N1flC8AZMBp68EMb-BId7jNWluTX8goV7SxOgjMmTGgZvXU2IliuWEABQtxj7x8QJVRuo0nFZOQkfSbhbyKiYSWCSxxn-TuXMWTrjbMv6mhjJZ44pcDA1ki56wPj34GIOeUOAErjNE_jYNv48Nda0XkgG0rhHzLvMLOxPcUJyUPpuN1sGUb7lZ3FaFAFK69G93uK-gVzbx_5eM5lcjjkkV8",
                "content",
                this.keyWord,
                "X-Requested-With",
                "XMLHttpRequest",
                "Content-Type",
                "text/plain; charset=UTF-8",
                "Accept-Charset",
                "ISO-8859-1");

        Log.i("", "Headers: " + customHeaders);

        Request request = new Request.Builder()
                .url("https://snapics-preprod-ws.bnpparibas.com/api/user/search")
                .headers(customHeaders)
                .build();

        Response response = client.newCall(request).execute();
        contentAsString = response.body().string();
        Log.i("DownloadTask", contentAsString);

        return contentAsString;
    }

    // Reads an InputStream and converts it to a String.
    public String readIt(InputStream stream) throws IOException {

        StringWriter writer = new StringWriter();
        IOUtils.copy(stream, writer, "UTF-8");
        return writer.toString();
    }

    public interface DownloadTaskListener {
        void downloadTaskDidSucceed(String result);
        void downloadTaskDidFail(Exception error);
    }
}
