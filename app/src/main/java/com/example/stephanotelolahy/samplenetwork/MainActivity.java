package com.example.stephanotelolahy.samplenetwork;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchButtonClicked(null);
    }

    public void searchButtonClicked(View sender) {

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {

            EditText editText = (EditText)this.findViewById(R.id.editText);
            String keyWord = editText.getText().toString();

            new DownloadTask(keyWord, new DownloadTask.DownloadTaskListener() {
                @Override
                public void downloadTaskDidSucceed(String result) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Result");
                    builder.setMessage(result);
                    builder.setPositiveButton("OK", null);
                    builder.create().show();
                }

                @Override
                public void downloadTaskDidFail(Exception error) {
                    Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    error.printStackTrace();
                }
            }).execute();
        } else {
            Toast.makeText(this, "Not connected to internet", Toast.LENGTH_LONG).show();
        }
    }
}
