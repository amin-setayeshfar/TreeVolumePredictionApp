package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        forceRTLIfSupported();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("تخمین حجم درخت");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#5eba7d")));
        LinearLayout linearLayout;
        linearLayout = findViewById(R.id.mainLayout);
        linearLayout.setBackgroundColor(Color.WHITE);
        //testing the moden", "hi");
//        Log.d("predictiol with sample data
//        float[][] inputValues = {{17.4f,0.266f}};
//        String prediction = predictVolume(inputValues);
//        Log.d("prediction", prediction);

        //Declaring Input Element and Finding in Our Layout View by their id
        final EditText perimeterAtBreastHeight = (EditText)findViewById(R.id.p_b_h);
        final EditText treeHeight = (EditText)findViewById(R.id.tree_height);
        final TextView result = (TextView) findViewById(R.id.result);
        final ImageView sonto = (ImageView) findViewById(R.id.sonto);
        final ImageView meter = (ImageView) findViewById(R.id.meter);

        Button Submit = (Button)findViewById(R.id.submit);

        //to check if height is passed to activity
        Intent intent = getIntent();
        String height = intent.getStringExtra("height");
        if (height!=null & height!="" && height.length()!=0) {
            treeHeight.setText(height);
        }

        //to check if perimeter is passed to activity
        String perimeter = intent.getStringExtra("perimeter");
        if (perimeter!=null & perimeter!="" && perimeter.length()!=0) {
            perimeterAtBreastHeight.setText(perimeter);
        }

        //Set On Click Listener For meter
        meter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, MetersActivity.class);
                MainActivity.this.startActivity(myIntent);
            }
        });

        //Set On Click Listener For sonto
        sonto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, SontoActivity.class);
                MainActivity.this.startActivity(myIntent);
            }
        });

        //Set On Click Listener For Button
        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Now Doing Some Validation to check if all required parameters are entered
                //We Can Get Value of Edit Text By using method gettext

                if(perimeterAtBreastHeight.getText().toString().isEmpty()){
                    perimeterAtBreastHeight.setError("محیط در ارتفاع سینه را وارد کنید");
                }
                else if(treeHeight.getText().toString().isEmpty()){
                    treeHeight.setError("ارتفاع درخت را وارد کنید");
                }
                else{
                    //if all parameters are entered then we create the input array from the entered parameters in the text inputs
                    //we read each input text and convert the string value to double(floating numbers)
                    float[][] inputValues ={ {
                        Float.valueOf(perimeterAtBreastHeight.getText().toString()),
                        Float.valueOf(treeHeight.getText().toString())
                    }};
                    Log.d("me inputValues", Arrays.toString(inputValues));

                    //predict the tree volume based on the input data and store the result in predictedVolume
                    String predictedVolume = predictVolume(scaleInputData(inputValues));
                    Log.d("me predictedVolume", predictedVolume);

                    //show the result in the text view which was hidden from the beginning
                    //prediction result is in array format so we convert it to string
                    result.setText("حجم تخمین زده شده درخت: " + predictedVolume);
                    Log.d("me reverse", predictedVolume);

                    //show hidden text view containing the result
                    result.setVisibility(View.VISIBLE);
                }

            }
        });

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void forceRTLIfSupported()
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
    }

    private float[][] scaleInputData(float[][] inputValues) {
        inputValues[0][0] = (inputValues[0][0] - 436) / (3860 - 436);
        inputValues[0][1] = (inputValues[0][1] - 13) / (47.8f - 13);
        return inputValues;
    }

    private float reverseScalePrediction(float scaledPrediction) {
        scaledPrediction = scaledPrediction * (31.914f - 0.087f) + 0.087f;
        Log.d("prediction scaled",Float.toString(scaledPrediction));
        return scaledPrediction;
    }

    //importing the model created and trained in python from the exported file
    private MappedByteBuffer loadModelFile(){
        AssetFileDescriptor fileDescriptor= null;
        try {
            fileDescriptor = this.getAssets().openFd("degree.tflite");

            FileInputStream inputStream=new FileInputStream(fileDescriptor.getFileDescriptor());
            FileChannel fileChannel=inputStream.getChannel();
            long startOffset=fileDescriptor.getStartOffset();
            long declareLength=fileDescriptor.getDeclaredLength();
            return fileChannel.map(FileChannel.MapMode.READ_ONLY,startOffset,declareLength);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    private String predictVolume (float[][] input) {
        Interpreter tflite;
        float[][] output = new float[1][1];
        try {
            tflite = new Interpreter(loadModelFile());
            tflite.run(input,output);
            Log.d("prediction scaled",Float.toString(output[0][0]));
            return  Float.toString(reverseScalePrediction(output[0][0]));
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return  Float.toString(reverseScalePrediction(output[0][0]));

    }
}