package com.example.nichoshi.networkpractice;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class ParseJSONActivity extends Activity {
    private Button parseByJSONObjectBtn;
    private Button parseByGSONBtn;
    private TextView responseTextView;
    private String ResponseData;
    private StringBuilder JSONContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parse_json);
        parseByGSONBtn = (Button) findViewById(R.id.parseByGSONBtn);
        parseByJSONObjectBtn = (Button) findViewById(R.id.parseByJSONObjectBtn);
        responseTextView = (TextView) findViewById(R.id.JSONResponseTextView);
        requestData();

        parseByJSONObjectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parseJSONByJSONobject(ResponseData);
            }
        });

        parseByGSONBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parseJSONByGSON(ResponseData);
            }
        });
    }

    public void parseJSONByJSONobject(String JSONString){
        JSONContent = new StringBuilder();
        try{
            JSONArray jsonArray = new JSONArray(JSONString);
            for(int i = 0;i < jsonArray.length();i++){
                JSONObject object = jsonArray.getJSONObject(i);
                String id = object.getString("id");
                String name = object.getString("name");
                String version = object.getString("version");
                JSONContent.append(id).append(" ").append(name).append(" ").append(version).append('\n');

            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(JSONContent.length() != 0){
                responseTextView.setText(JSONContent.toString());
            }
        }

    }

    public void parseJSONByGSON(String JSONString){
        JSONContent = new StringBuilder();
        Gson gson = new Gson();
        List<App> appList = gson.fromJson(JSONString,new TypeToken<List<App>>(){}.getType());
        for(App app : appList){
            JSONContent.append(app.getId()).append(" ").append(app.getName()).append(" ").append(app.getVersion()).append('\n');

        }
        responseTextView.setText(JSONContent.toString());

    }

    public void requestData(){
        ResponseData = "";
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try{
                    URL url = new URL("http://10.0.2.2:8080/data.json");
                    connection =(HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    String line;
                    StringBuilder data = new StringBuilder();
                    while ((line = reader.readLine()) != null){
                        data.append(line);
                    }
                    ResponseData = data.toString();
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
}
