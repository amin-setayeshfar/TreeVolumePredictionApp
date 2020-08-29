package com.example.myapplication;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class MetersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meter);

        forceRTLIfSupported();
        setTitle("محاسبه محیط بوسیله دستگاه");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#5eba7d")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button

        final EditText navarEditText = (EditText)findViewById(R.id.navar);
        final EditText dobazoEditText = (EditText)findViewById(R.id.dobazo);
        Button Submit = (Button)findViewById(R.id.submit);

        LinearLayout linearLayout;
        linearLayout = findViewById(R.id.meterLayout);
        linearLayout.setBackgroundColor(Color.WHITE);

        //Set On Click Listener For Submit
        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MetersActivity.this, MainActivity.class);
                if(!dobazoEditText.getText().toString().isEmpty()) {
                    double dobazo = Double.valueOf(dobazoEditText.getText().toString().isEmpty() ? "0" : toPersianNumber(dobazoEditText.getText().toString()));
                    double perimeter =  dobazo * Math.PI;
                    myIntent.putExtra("perimeter", String.valueOf(String.format("%.2f", perimeter)));
                    MetersActivity.this.startActivity(myIntent);
                }
                else if(!navarEditText.getText().toString().isEmpty()) {
                    double navar = Double.valueOf(navarEditText.getText().toString().isEmpty() ? "0" : toPersianNumber(navarEditText.getText().toString()));
                    double perimeter =  navar * Math.PI;
                    myIntent.putExtra("perimeter", String.valueOf(String.format("%.2f", perimeter)));
                    MetersActivity.this.startActivity(myIntent);
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