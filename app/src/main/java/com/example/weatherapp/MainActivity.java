package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    EditText editTextCity;
    Button buttonWeather;
    TextView resultTextView;
    String weather;
    String weatherInfo,weatherInfo2;
    TextView resultTextView2,resultTextView3;

    public class fetchData extends AsyncTask<String,Void,String> {
        URL url;
        HttpURLConnection conn;
        String result="";

        @Override
        protected String doInBackground(String... strings) {
            try {
                url=new URL(strings[0]);
                conn= (HttpURLConnection) url.openConnection();
                InputStream iS= conn.getInputStream();
                InputStreamReader iSR=new InputStreamReader(iS);
                int data = iSR.read();
                while(data != -1)
                {
                    char c= (char) data;
                    result += c;
                    data= iSR.read();
                }
                return result;
            } catch (Exception e) {
                e.printStackTrace();
                return "Error fetching data";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {

                JSONObject jsonObject=new JSONObject(s);
                String weatherInformation= jsonObject.getString("weather");
                weather=jsonObject.getString("main");
                JSONObject jsonObject1= new JSONObject(weather);
                weatherInfo= jsonObject1.getString("temp_min");
                weatherInfo2= jsonObject1.getString("temp_max");
                double min = (Double.parseDouble(weatherInfo))-273.15;
                double max = (Double.parseDouble(weatherInfo2))-273.15;
                String min1= String.format("%.2f", min);
                String max1=String.format("%.2f", max);
                resultTextView2.setText("Min temp : "+ min1 +"C");
                resultTextView3.setText("Max temp : " + max1+ "C");



                Log.i("Temperature",weatherInfo);


                JSONArray jsonArray=new JSONArray(weatherInformation);

                for(int i=0 ;i<jsonArray.length();i++)
                {
                    JSONObject find= jsonArray.getJSONObject(i);
                    resultTextView.setText(find.getString("main")+":"+find.getString("description"));


                }





            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }
    public void buttonPressed(View view)
    {
        fetchData fD= new fetchData();
        String result="";
        String city= editTextCity.getText().toString();
        try {
            result= fD.execute("https://api.openweathermap.org/data/2.5/weather?q="+city+"&appid=a3a7e45e8d76de8afe4e458414b72b32").get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextCity=findViewById(R.id.editTextCity);
        resultTextView=findViewById(R.id.resultTextView);
        resultTextView2=findViewById(R.id.resultTextView2);
        resultTextView3=findViewById(R.id.resultTextView3);





    }
}