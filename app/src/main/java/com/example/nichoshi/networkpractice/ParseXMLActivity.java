package com.example.nichoshi.networkpractice;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import javax.xml.parsers.SAXParserFactory;

public class ParseXMLActivity extends AppCompatActivity {
    private Button parseByPULLBtn,parseBySAXBtn;
    private TextView parseResultTextView;
    private StringBuilder xmlContent;
    private String Data;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parse_xml);
        parseByPULLBtn = (Button) findViewById(R.id.parseByPULLBtn);
        parseBySAXBtn = (Button) findViewById(R.id.parseBySAXBtn);
        parseResultTextView = (TextView) findViewById(R.id.parseResultTextView);
        requestData();

        Data = null;

        parseByPULLBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parseXMLbyPULL(Data);
            }
        });
        parseBySAXBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setParseBySAX(Data);
            }
        });
    }

    public void parseXMLbyPULL(String xmlString){
        xmlContent = new StringBuilder();
        try{
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(xmlString));
            int eventType = parser.getEventType();
            String id = "";
            String name = "";
            String version ="";
            while(eventType != XmlPullParser.END_DOCUMENT){
                String nodeName = parser.getName();
                switch (eventType){
                    case XmlPullParser.START_TAG:
                        switch (nodeName){
                            case "id":
                                id = parser.nextText();
                                break;
                            case "name":
                                name = parser.nextText();
                                break;
                            case "version":
                                version = parser.nextText();
                                break;
                            default:
                                break;
                        }
                        break;

                    case  XmlPullParser.END_TAG:
                        if("app".equals(nodeName)){
                            xmlContent.append(id+" ").append(name+" ").append(version + "\n");
                        }
                        break;

                    default:
                        break;

                }
                eventType = parser.next();
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            parseResultTextView.setText(xmlContent.toString());
        }

    }

    public void setParseBySAX(String xmlString){
        try{
            SAXParserFactory factory = SAXParserFactory.newInstance();
            XMLReader reader = factory.newSAXParser().getXMLReader();
            ContentHandler handler = new ContentHandler();
            reader.setContentHandler(handler);
            reader.parse(new InputSource(new StringReader(xmlString)));
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    class ContentHandler extends DefaultHandler{
        private StringBuilder id;
        private StringBuilder name;
        private StringBuilder version;
        private String nodeName;

        @Override
        public void startDocument() throws SAXException {
            id = new StringBuilder();
            name = new StringBuilder();
            version = new StringBuilder();
            xmlContent = new StringBuilder();
            super.startDocument();
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

            nodeName = localName;
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            switch (nodeName){
                case "id":
                    id.append(ch,start,length).append(" ");
                    break;

                case "name":
                    name.append(ch,start,length).append(" ");
                    break;

                case "version":
                    version.append(ch,start,length).append("\n");
                    break;

                default:
                    break;
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            if("app".equals(localName)){

                xmlContent.append(id).append(name).append(version);
                id.setLength(0);
                name.setLength(0);
                version.setLength(0);
            }

        }

        @Override
        public void endDocument() throws SAXException {
            parseResultTextView.setText(xmlContent.toString());
        }
    }



    public void requestData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try{
                    URL url = new URL("http://10.0.2.2:8080/data.xml");
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setReadTimeout(8000);
                    connection.setConnectTimeout(8000);

                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder data = new StringBuilder();
                    String line = "";
                    while ((line = reader.readLine())!= null){
                        data.append(line);
                    }
                    Data = data.toString();

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
