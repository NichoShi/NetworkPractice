package com.example.nichoshi.networkpractice;

import android.app.Activity;
import android.content.Entity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class HttpClientActivity extends Activity {
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
        setContentView(R.layout.activity_http_client);
        responseText = (TextView) findViewById(R.id.HttpClientResponse);
        requestBtn = (Button) findViewById(R.id.HttpClientRequestBtn);

        requestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            HttpClient client = new DefaultHttpClient();
                            HttpGet get = new HttpGet("http://www.baidu.com");
                            HttpResponse response = client.execute(get);
                            if(response.getStatusLine().getStatusCode() == 200){
                                HttpEntity entity = response.getEntity();
                                String res = EntityUtils.toString(entity,"utf-8");
                                Message msg = new Message();
                                msg.obj = res.toString();
                                handler.sendMessage(msg);
                            }


                        }catch (Exception e){
                            e.printStackTrace();
                        }


                    }
                }).start();
            }
        });
    }
}
