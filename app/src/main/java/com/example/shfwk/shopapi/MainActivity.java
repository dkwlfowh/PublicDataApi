package com.example.shfwk.shopapi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button Lbtn;
    Button Mbtn;
    Button Sbtn;
    Button Enter;
    EditText edit;
    TextView textView;
    String data;
    String a[]=new String[21];
    String b[]=new String[21];
    List<String>MCode=new ArrayList<>();
    List<String>Mname=new ArrayList<>();
    List<String>Scode=new ArrayList<>();
    List<String>Sname=new ArrayList<>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Lbtn = (Button) findViewById(R.id.Lbtn);
        Mbtn = (Button) findViewById(R.id.Mbtn);
        Sbtn=(Button)findViewById(R.id.Sbtn);
        Enter=(Button)findViewById(R.id.enter);
        edit = (EditText) findViewById(R.id.edit1);
        textView = (TextView) findViewById(R.id.textview);
        final String[] Lname = new String[1];
        final String[] Lresult = new String[1];
        final String[] LMname = new String[1];
        final String[] LMresult = new String[1];
        final String[] Shortname = new String[1];
        final String[] Shortresult = new String[1];

        Lbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        LgetData();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                StringBuffer buffer = new StringBuffer();
                                for (int i = 0; i < 21; i++) {
                                    buffer.append(a[i] + "\n");
                                    buffer.append(b[i] + "\n");
                                }
                                textView.setText(buffer);
                            }
                        });
                    }
                }).start();

            }
        });
        Mbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MCode.clear();
                Mname.clear();
                Lname[0] = edit.getText().toString();
                for (int i = 0; i < 21; i++) {
                    if (Lname[0].equals(a[i]))
                        Lresult[0] = b[i];//대분류 알파벳
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        MgetData(Lresult[0]);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                StringBuffer buffer = new StringBuffer();
                                for (int i = 0; i <MCode.size(); i++) {
                                    buffer.append(MCode.get(i) + "\n");
                                    buffer.append(Mname.get(i) + "\n");
                                }
                                textView.setText(buffer);
                            }
                        });
                    }
                }).start();
            }

        });
        Sbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Scode.clear();
                Sname.clear();
                LMname[0]=edit.getText().toString();
                for(int i=0;i<Mname.size();i++){ //중분류의 코드 번호 찾기
                    if(LMname[0].equals(Mname.get(i)))
                        LMresult[0]=MCode.get(i);
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SgetData(Lresult[0],LMresult[0]); //대분류코드 중분류 코드 넘겨주기
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                StringBuffer buffer = new StringBuffer();
                                for (int i = 0; i <Sname.size(); i++) {
                                    buffer.append(Scode.get(i) + "\n");
                                    buffer.append(Sname.get(i) + "\n");
                                }
                                textView.setText(buffer);
                            }
                        });
                    }
                }).start();
            }
        });
        Enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Shortname[0]=edit.getText().toString();
                for(int i=0;i<Sname.size();i++){
                    if(Shortname[0].equals(Sname.get(i)))
                        Shortresult[0]=Scode.get(i);
                }
                textView.setText(Lname[0]+LMname[0]+Shortname[0]+"\n"+Lresult[0]+LMresult[0]+Shortresult[0]);
            }
        });
    }




    public void LgetData(){
        StringBuffer buffer=new StringBuffer();
        String queryUrl="http://apis.data.go.kr/B553077/api/open/sdsc/largeUpjongList?" +
                "ServiceKey=MxfED6C3Sd6Ja7QuU2BNU8xqBX5Yiy26t4sWS0PWUm%2B6WFjChgI3KoNQRMdO9LM5xvKfXOtMIh40XqadzCbTfw%3D%3D";
        int su=0;
        try {
            URL url= new URL(queryUrl);//문자열로 된 요청 url을 URL 객체로 생성.
            InputStream is= url.openStream(); //url위치로 입력스트림 연결

            XmlPullParserFactory factory= XmlPullParserFactory.newInstance();
            XmlPullParser xpp= factory.newPullParser();
            xpp.setInput( new InputStreamReader(is, "UTF-8") ); //inputstream 으로부터 xml 입력받기
            String tag;
            xpp.next();
            int eventType= xpp.getEventType();
            while( eventType != XmlPullParser.END_DOCUMENT ){
                switch( eventType ){
                    case XmlPullParser.START_DOCUMENT:
                        buffer.append("파싱 시작...\n\n");
                        break;

                    case XmlPullParser.START_TAG:
                        tag= xpp.getName();//태그 이름 얻어오기

                        if(tag.equals("item")) ;// 첫번째 검색결과
                        else if(tag.equals("indsLclsCd")){
                            xpp.next();
                            b[su]=xpp.getText();
                        }
                        else if(tag.equals("indsLclsNm")){
                            xpp.next();
                            a[su]=xpp.getText();
                            su++;
                        }
                        break;
                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag= xpp.getName(); //태그 이름 얻어오기
                        if(tag.equals("item")) buffer.append("\n");// 첫번째 검색결과종료..줄바꿈
                        break;
                }
                eventType= xpp.next();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch blocke.printStackTrace();
        }
    }

    public void MgetData(String Lcls){
        StringBuffer buffer=new StringBuffer();
        String queryUrl="http://apis.data.go.kr/B553077/api/open/sdsc/middleUpjongList?"+
                "indsLclsCd="+Lcls+"&ServiceKey=MxfED6C3Sd6Ja7QuU2BNU8xqBX5Yiy26t4sWS0PWUm%2B6WFjChgI3KoNQRMdO9LM5xvKfXOtMIh40XqadzCbTfw%3D%3D";
        int su=0;
        try {
            URL url= new URL(queryUrl);//문자열로 된 요청 url을 URL 객체로 생성.
            InputStream is= url.openStream(); //url위치로 입력스트림 연결

            XmlPullParserFactory factory= XmlPullParserFactory.newInstance();
            XmlPullParser xpp= factory.newPullParser();
            xpp.setInput( new InputStreamReader(is, "UTF-8") ); //inputstream 으로부터 xml 입력받기
            String tag;
            xpp.next();
            int eventType= xpp.getEventType();
            while( eventType != XmlPullParser.END_DOCUMENT ){
                switch( eventType ){
                    case XmlPullParser.START_DOCUMENT:
                        buffer.append("파싱 시작...\n\n");
                        break;

                    case XmlPullParser.START_TAG:
                        tag= xpp.getName();//태그 이름 얻어오기

                        if(tag.equals("item")) ;// 첫번째 검색결과
                        else if(tag.equals("indsMclsCd")){
                            xpp.next();
                            MCode.add(xpp.getText());
                        }
                        else if(tag.equals("indsMclsNm")){
                            xpp.next();
                            Mname.add(xpp.getText());
                            su++;
                        }
                        break;
                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag= xpp.getName(); //태그 이름 얻어오기
                        if(tag.equals("item")) buffer.append("\n");// 첫번째 검색결과종료..줄바꿈
                        break;
                }
                eventType= xpp.next();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch blocke.printStackTrace();
        }
    }

    public void SgetData(String Lcls,String Mcls){
        StringBuffer buffer=new StringBuffer();
        String queryUrl="http://apis.data.go.kr/B553077/api/open/sdsc/smallUpjongList?"+
                "indsLclsCd="+Lcls+"&indsMclsCd="+Mcls+"&ServiceKey=MxfED6C3Sd6Ja7QuU2BNU8xqBX5Yiy26t4sWS0PWUm%2B6WFjChgI3KoNQRMdO9LM5xvKfXOtMIh40XqadzCbTfw%3D%3D";
        int su=0;
        try {
            URL url= new URL(queryUrl);//문자열로 된 요청 url을 URL 객체로 생성.
            InputStream is= url.openStream(); //url위치로 입력스트림 연결

            XmlPullParserFactory factory= XmlPullParserFactory.newInstance();
            XmlPullParser xpp= factory.newPullParser();
            xpp.setInput( new InputStreamReader(is, "UTF-8") ); //inputstream 으로부터 xml 입력받기
            String tag;
            xpp.next();
            int eventType= xpp.getEventType();
            while( eventType != XmlPullParser.END_DOCUMENT ){
                switch( eventType ){
                    case XmlPullParser.START_DOCUMENT:
                        buffer.append("파싱 시작...\n\n");
                        break;

                    case XmlPullParser.START_TAG:
                        tag= xpp.getName();//태그 이름 얻어오기

                        if(tag.equals("item")) ;// 첫번째 검색결과
                        else if(tag.equals("indsSclsCd")){
                            xpp.next();
                            Scode.add(xpp.getText());
                        }
                        else if(tag.equals("indsSclsNm")){
                            xpp.next();
                            Sname.add(xpp.getText());
                            su++;
                        }
                        break;
                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag= xpp.getName(); //태그 이름 얻어오기
                        if(tag.equals("item")) buffer.append("\n");// 첫번째 검색결과종료..줄바꿈
                        buffer.append("파싱끝");
                        break;
                }
                eventType= xpp.next();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch blocke.printStackTrace();
        }
    }
}
