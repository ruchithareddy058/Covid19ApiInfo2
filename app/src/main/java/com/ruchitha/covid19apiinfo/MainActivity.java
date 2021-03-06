package com.ruchitha.covid19apiinfo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
TextView dateObj,countryObj,confirmedObj,deathObj,recoveredObj,activeObj;
ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dateObj=findViewById(R.id.tv_date);
        countryObj=findViewById(R.id.tv_country);
        confirmedObj=findViewById(R.id.tv_confirmed);
        deathObj=findViewById(R.id.tv_deaths);
        recoveredObj=findViewById(R.id.tv_recovered);
        activeObj=findViewById(R.id.tv_active);
        dialog=new ProgressDialog(this);
        dialog.setTitle("Data Fetching from Internet");
        dialog.setMessage("Please wait Data Loading..");
        dialog.show();
        EndPointService service=Covid19ApiInstance.getInstance().create(EndPointService.class);
        Call<String> c=service.getData();
        c.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                dialog.dismiss();
                try {
                    JSONArray rootAry=new JSONArray(response.body());
                    for (int i=0;i<rootAry.length();i++) {
                    JSONObject rootObj=rootAry.getJSONObject(i);
                    String resultCountry = rootObj.getString("Country");
                    String resultConfirmed = rootObj.getString("Confirmed");
                    String resultDeaths = rootObj.getString("Deaths");
                    String resultRecovered = rootObj.getString("Recovered");
                    String resultActive = rootObj.getString("Active");
                    String resultDate = rootObj.getString("Date");
                    dateObj.setText("Date : "+properformat(resultDate));
                    activeObj.setText("Active : "+resultActive);
                    countryObj.setText("Country : "+resultCountry);
                    confirmedObj.setText("Confirmed :"+resultConfirmed);
                    deathObj.setText("Deaths: "+resultDeaths);
                    recoveredObj.setText("Recovered: "+resultRecovered);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }
    private String properformat(String resultDate) {
        String inputFormat="yy-mm-dd";
        String outFormat="dd-mm-yy";
        SimpleDateFormat inputform=new SimpleDateFormat(inputFormat);
        SimpleDateFormat outputform=new SimpleDateFormat(outFormat);
        Date d=null;
        String str=null;
        try {
            d=inputform.parse(resultDate);
            str=outputform.format(d);
        }catch (ParseException e){
            e.printStackTrace();
        }
        return str;
    }
}