package com.example.nichoshi.networkpractice;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpURLConnectionActivity extends Activity {
    TextView responseText;
    Button requestBtn;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            String response = (String) msg.obj;
            responseText.setText(response);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http_urlconnection);
        responseText = (TextView) findViewById(R.id.HttpURLConnectionResponse);
        requestBtn = (Button) findViewById(R.id.HttpURLConnectionRequestBtn);

        requestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        HttpURLConnection connection = null;
                        try{
                            URL url = new URL("http://www.baidu.com");
                            connection = (HttpURLConnection) url.openConnection();

                            connection.setRequestMethod("GET");
                            connection.setReadTimeout(8000);
                            connection.setConnectTimeout(8000);


                            if(connection.getResponseCode() == 302){
                                String location = connection.getHeaderField("Location");
                                URL newUrl = new URL(location);
                                connection = (HttpURLConnection) newUrl.openConnection();
                            }


                            //Log.d("hahaha",connection.getResponseCode()+"");

                            if(connection.getResponseCode() == 200){
                                InputStream in = connection.getInputStream();
                                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                                StringBuilder response = new StringBuilder();
                                String line;
                                while((line = reader.readLine()) != null){
                                    //Log.d("hahaha",line);
                                    response.append(line);
                                }
                                Message message = new Message();
                                message.obj = response.toString();
                                //Log.d("hahaha",response.toString().length()+"");
                                handler.sendMessage(message);
                            }





                        }catch (Exception e){
                            e.printStackTrace();
                        }finally {
                            if(connection != null){
                                connection.disconnect();
                            }
                        }
                    }
                }).start();

            }
        });
    }
}
