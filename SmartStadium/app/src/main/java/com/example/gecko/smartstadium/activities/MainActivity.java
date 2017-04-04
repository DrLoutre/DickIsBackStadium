package com.example.gecko.smartstadium.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.gecko.smartstadium.R;

public class MainActivity extends AppCompatActivity {

    private static final int NFC_SCANNER = 1;
    private static final int QR_CODE = 2;
    CredentialSingletion credential;
    private Button NFCButton;
    private Button QRButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        credential = CredentialSingletion.getInstance();
        NFCButton = (Button) findViewById(R.id.loginButtonNFC);
        QRButton = (Button) findViewById(R.id.loginButtonQRcode);

        //todo ajouter la demande de permission

        NFCButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo changer pour aller vers le lecteur NFC
                Intent i = new Intent(MainActivity.this, QRCodeActivity.class);
                startActivityForResult(i, NFC_SCANNER);
            }
        });

        QRButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, QRCodeActivity.class);
                startActivityForResult(i, QR_CODE);
            }
        });
    }

    /**
     * Result of the QRCode scanner
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            final String result = data.getStringExtra("result");
            //todo requète api ici pour capter les info

            Intent intent = new Intent(MainActivity.this, PublicActivity.class);
            startActivity(intent);
        }
    }
}