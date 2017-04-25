package com.example.nichoshi.networkpractice;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import java.io.BufferedReader;

public class MainActivity extends AppCompatActivity {
    private Button WebViewBtn;
    private Button HttpURLConnectionBtn;
    private Button HttpClientBtn;
    private Button parseXMLBtn;
    private Button parseJSONBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WebViewBtn = (Button) findViewById(R.id.WebViewBtn);
        HttpURLConnectionBtn = (Button) findViewById(R.id.HttpURLConnectionBtn);
        HttpClientBtn = (Button) findViewById(R.id.HttpClientBtn);
        parseXMLBtn = (Button) findViewById(R.id.parseXMLBtn);
        parseJSONBtn = (Button) findViewById(R.id.parseJSONBtn);


        WebViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,WebViewActivity.class);
                startActivity(intent);
            }
        });

        HttpURLConnectionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,HttpURLConnectionActivity.class);
                startActivity(intent);
            }
        });

        HttpClientBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,HttpClientActivity.class);
                startActivity(intent);
            }
        });

        parseXMLBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ParseXMLActivity.class);
                startActivity(intent);
            }
        });

        parseJSONBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ParseJSONActivity.class);
                startActivity(intent);
            }
        });
    }
}
