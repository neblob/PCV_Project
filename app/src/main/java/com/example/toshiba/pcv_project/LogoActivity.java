package com.example.toshiba.pcv_project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class LogoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);

        Button btnDisplayMainActivity = (Button) findViewById(R.id.button_start);
        btnDisplayMainActivity.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LogoActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        Button btnDisplayManualActivity = (Button) findViewById(R.id.button_manual);
        btnDisplayManualActivity.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LogoActivity.this, ManualActivity.class);
                startActivity(intent);
            }
        });
    }

}
