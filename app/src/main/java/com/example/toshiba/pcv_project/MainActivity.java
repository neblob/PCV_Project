package com.example.toshiba.pcv_project;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends LogoActivity {

    private static final int TAKE_PICTURE = 100;
    private static final int REQUEST_TAKE_PHOTO = 101;

    Button btnLine1;
    Button btnLine2;
    Button btnLine3;
    Button btnCal;
    Button btnZoom;
    LineTouchView lineTouchView;
    ImageView image;
    Button btnCapture;

    File photoFile;
    Uri savedOriginalImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initInstances();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == TAKE_PICTURE && resultCode == RESULT_OK){
            Bitmap capturedImage = (Bitmap)data.getExtras().get("data");
            image.setImageBitmap(capturedImage);
            lineTouchView.setMPaint(capturedImage);
        }

        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            Log.d("app", "result: image uri: " + String.valueOf(Uri.fromFile(photoFile)));
            savedOriginalImageUri = Uri.fromFile(photoFile);

            galleryAddPic(savedOriginalImageUri);
            setPic();
        }
    }
    private void initInstances() {
        btnCapture = (Button) findViewById(R.id.button);
        btnCapture.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                takePictureIntent();
            }
        });

        btnZoom = (Button) findViewById(R.id.button_zoom);
        btnZoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleView();
            }
        });

        btnLine1 = (Button) findViewById(R.id.btnLine1);
        btnLine2 = (Button) findViewById(R.id.btnLine2);
        btnLine3 = (Button) findViewById(R.id.btnLine3);
        btnCal = (Button) findViewById(R.id.btnCal);

        lineTouchView = (LineTouchView) findViewById(R.id.vLineTouchView);

        image = (ImageView) findViewById(R.id.imageView);
    }

    private void takePictureIntent() {
        Intent takePictureIntent = new Intent("android.media.action.IMAGE_CAPTURE");
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);

        return File.createTempFile(imageFileName, ".jpg", storageDir);
    }

    private void galleryAddPic(Uri contentUri) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private void setPic() {
        // check photo rotation *problem from camera app
        try {
            Bitmap mBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), savedOriginalImageUri);
            if (mBitmap.getWidth() > mBitmap.getHeight()) {
                // make new photo if wrong rotation
                Log.d("app", "wrong rotation");
                File rotatedPhotoFile = rotatePhoto(mBitmap);
                galleryAddPic(Uri.fromFile(rotatedPhotoFile));
                mBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.fromFile(rotatedPhotoFile));

            }

            image.setImageBitmap(mBitmap);
            lineTouchView.setMPaint(mBitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private File rotatePhoto(Bitmap mBitmap) throws IOException {
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        Bitmap mBitmapNew = Bitmap.createBitmap(mBitmap , 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), matrix, true);
        File file = createImageFile();
        OutputStream fOut = new FileOutputStream(file);
        mBitmapNew.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
        fOut.flush();
        fOut.close();

        return file;
    }

    private void toggleView() {
        ViewGroup myViewGroup = ((ViewGroup) findViewById(R.id.frameLayout2));
        myViewGroup.bringChildToFront(myViewGroup.getChildAt(1));
        for (int i=0; i<myViewGroup.getChildCount(); i++) {
            myViewGroup.getChildAt(i).invalidate();
        }
        myViewGroup.invalidate();
        Log.d("toggle","toggle");
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
