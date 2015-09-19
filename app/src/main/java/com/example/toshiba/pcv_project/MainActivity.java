package com.example.toshiba.pcv_project;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends LogoActivity {

    private static final int TAKE_PICTURE = 100;

    Button btnLine1;
    Button btnLine2;
    Button btnLine3;
    Button btnCal;
    LineTouchView lineTouchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnCapture = (Button) findViewById(R.id.button);
        btnCapture.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, TAKE_PICTURE);
            }
        });

        initInstances();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        ImageView image = (ImageView) findViewById(R.id.imageView);
        if (requestCode == TAKE_PICTURE && resultCode == RESULT_OK){
            Bitmap capturedImage = (Bitmap)data.getExtras().get("data");
            image.setImageBitmap(capturedImage);
        }
    }
    private void initInstances() {
        btnLine1 = (Button) findViewById(R.id.btnLine1);
        btnLine2 = (Button) findViewById(R.id.btnLine2);
        btnLine3 = (Button) findViewById(R.id.btnLine3);
        btnCal = (Button) findViewById(R.id.btnCal);

        lineTouchView = (LineTouchView) findViewById(R.id.vLineTouchView);
    }

    public void activeLine(View view) {
        switch (view.getId()) {
            case R.id.btnLine1:
                lineTouchView.line1.active();
                lineTouchView.line2.inactive();
                lineTouchView.line3.inactive();
                break;
            case R.id.btnLine2:
                lineTouchView.line1.inactive();
                lineTouchView.line2.active();
                lineTouchView.line3.inactive();
                break;
            case R.id.btnLine3:
                lineTouchView.line1.inactive();
                lineTouchView.line2.inactive();
                lineTouchView.line3.active();
                break;
        }
    }

    public void cal(View view) {
        float l1 = lineTouchView.line1.p;
        float l2 = lineTouchView.line2.p;
        float l3 = lineTouchView.line3.p;
        Log.d("l1", String.valueOf(l1));
        Log.d("l2", String.valueOf(l2));
        Log.d("l3", String.valueOf(l3));

        float t = (l3 - l1) * 100 / (l2 - l1);
        Log.d("t", String.valueOf(t));
        btnCal.setText(String.valueOf(t));

        lineTouchView.drawValue(t);
    }


}
