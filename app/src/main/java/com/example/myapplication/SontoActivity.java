package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SontoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sonto);

        forceRTLIfSupported();
        setTitle("محاسبه ارتفاع بوسیله دستگاه سونتو");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#5eba7d")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button

        LinearLayout linearLayout;
        linearLayout = findViewById(R.id.sontoLayout);
        linearLayout.setBackgroundColor(Color.WHITE);

        final EditText horizontalDistanceEditText = (EditText)findViewById(R.id.horizontalDistance);
        final EditText slopeEditText = (EditText)findViewById(R.id.slope);
        final EditText nokEditText = (EditText)findViewById(R.id.nok);
        final EditText bonEditText = (EditText)findViewById(R.id.bon);
        Button Submit = (Button)findViewById(R.id.submit);

        //Set On Click Listener For Submit
        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(SontoActivity.this, MainActivity.class);
                if(horizontalDistanceEditText.getText().toString().isEmpty()) {
                    horizontalDistanceEditText.setError("لطفا فاصله افقی را وارد نمایید");
                }
                else if(slopeEditText.getText().toString().isEmpty()) {
                    slopeEditText.setError("لطفا شیب را وارد نمایید");
                }
                else if(nokEditText.getText().toString().isEmpty()) {
                    nokEditText.setError("لطفا نوک را وارد نمایید");
                }
                else if(bonEditText.getText().toString().isEmpty()) {
                    bonEditText.setError("لطفا بن را وارد نمایید");
                }
                else {
                    double horizontalDistance = Double.valueOf(toPersianNumber(horizontalDistanceEditText.getText().toString()));
                    double slope = Double.valueOf(toPersianNumber(slopeEditText.getText().toString()));
                    double nok = Double.valueOf(toPersianNumber(nokEditText.getText().toString()));
                    double bon = Double.valueOf(toPersianNumber(bonEditText.getText().toString()));
                    Log.d("dd", String.valueOf(Math.cos(slope * (Math.PI / 180))));
                    double height = (Math.abs(Math.abs(nok) - Math.abs(bon)) / 100) * horizontalDistance * Math.cos(slope * (Math.PI / 180));
                    myIntent.putExtra("height", String.valueOf(String.format("%.2f", height)));
                    SontoActivity.this.startActivity(myIntent);
                }
            }
        });

    }


    private String toPersianNumber(String input)
    {
        String[] persian = { "۰", "۱", "۲", "۳", "۴", "۵", "۶", "۷", "۸", "۹" };

        for (int j=0; j<persian.length; j++)
            input = input.replace(persian[j], Integer.toString(j));

        return input;
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void forceRTLIfSupported()
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
    }
}