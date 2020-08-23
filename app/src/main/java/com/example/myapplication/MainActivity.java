package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
        setTitle("محاسبه حجم تقریبی درخت");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#5eba7d")));

        //testing the model with sample data
//        double[] inputValues = {40,480,17.4,266};
//        double[] prediction=doInference(inputValues);
//        Log.d("prediction", "hi");
//        Log.d("prediction", Arrays.toString(prediction));

        //Declaring Input Element and Finding in Our Layout View by their id
        final EditText perimeterAtBreastHeight=(EditText)findViewById(R.id.p_b_h);
        final EditText treeHeight=(EditText)findViewById(R.id.tree_height);
        final TextView result=(TextView) findViewById(R.id.result);

        Button Submit=(Button)findViewById(R.id.submit);


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
                    double[] inputValues = {
                        Double.parseDouble(perimeterAtBreastHeight.getText().toString()),
                        Double.parseDouble(treeHeight.getText().toString())
                    };
                    Log.d("inputValues", Arrays.toString(inputValues));

                    //predict the tree volume bsed on the inut data and store the result in predictedVolume
                    double[] predictedVolume = predictVolume(inputValues);

                    //show the result in the text view which was hidden from the beginning
                    //prediction result is in array format so we convert it to string
                    result.setText("حجم تخمین زده شده درخت: " + Arrays.toString(predictedVolume));
                    Log.d("predictedVolume", Arrays.toString(predictedVolume));

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


    private double[] predictVolume (double[] input) {
        Interpreter tflite;
        double[] output = new double[1];
        try {
            tflite = new Interpreter(loadModelFile());
            tflite.run(input,output);
            return  output;
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return output;
    }
    //return back to main
    }
