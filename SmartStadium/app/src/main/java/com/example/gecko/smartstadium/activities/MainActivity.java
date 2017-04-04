package com.example.gecko.smartstadium.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.gecko.smartstadium.R;

public class MainActivity extends AppCompatActivity {

    private Button NFCButton;
    private Button QRButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NFCButton = (Button) findViewById(R.id.loginButtonNFC);
        QRButton = (Button) findViewById(R.id.loginButtonQRcode);

        //todo ajouter la demande de permission

        NFCButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo changer pour aller vers le lecteur NFC
                Intent intent = new Intent(MainActivity.this, QRCodeActivity.class);
                startActivity(intent);
            }
        });

        QRButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, QRCodeActivity.class);
                startActivity(intent);
            }
        });
    }
}