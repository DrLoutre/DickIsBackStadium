package com.example.gecko.smartstadium.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.gecko.smartstadium.R;
import com.example.gecko.smartstadium.bus.BusProvider;
import com.example.gecko.smartstadium.events.AthleticEvent;
import com.example.gecko.smartstadium.events.IdAthleticEvent;
import com.example.gecko.smartstadium.utils.ConnectionUtils;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

public class MainActivity extends AppCompatActivity {

    private static final int NFC_SCANNER = 1;
    private static final int QR_CODE = 2;
    CredentialSingletion credential;
    private Bus mBus = BusProvider.getInstance();
    private Button NFCButton;
    private Button QRButton;
    private Button PublicButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int MyVersion = Build.VERSION.SDK_INT;
        if (MyVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (!checkIfAlreadyHavePermission()) {
                requestForSpecificPermission();
            }
        }
        credential = CredentialSingletion.getInstance();
        NFCButton = (Button) findViewById(R.id.loginButtonNFC);
        QRButton = (Button) findViewById(R.id.loginButtonQRcode);
        PublicButton = (Button) findViewById(R.id.loginButtonPublic);


        //todo ajouter la demande de permission

        NFCButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo changer pour aller vers le lecteur NFC
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivityForResult(i, NFC_SCANNER);
            }
        });

        QRButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo changer PublicActivity par QRCodeActivity
                Intent i = new Intent(MainActivity.this, PublicActivity.class);
                startActivityForResult(i, QR_CODE);
            }
        });

        PublicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PublicActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mBus.register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        mBus.unregister(this);
    }

    /**
     * Result of the QRCode scanner
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            final String result = data.getStringExtra("result");
            //todo requète api ici pour capter les info : DONE

            onLoginSuccess(result);

            /*
            Intent intent = new Intent(MainActivity.this, PublicActivity.class);
            startActivity(intent);
            */
        }
    }

    private void onLoginSuccess(final String result) {
        if (!ConnectionUtils.isOnline(this)) {
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Pas de connexion internet", Snackbar.LENGTH_SHORT);
            snackbar.setAction("Réssayer", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onLoginSuccess(result);
                }
            });
        } else {
            mBus.post(new IdAthleticEvent(result));
        }
    }

    @Subscribe
    public void AthleticEvent(AthleticEvent athleticEvent) {
        Intent intent;
        if (athleticEvent.getAthletic() != null) {
            intent = new Intent(MainActivity.this, LoginActivity.class);
        } else {
            intent = new Intent(MainActivity.this, PublicActivity.class);
        }
        startActivity(intent);
    }

    /**
     * Verify is the application has the require permissions
     *
     * @return true if the permissions are granted
     */
    private boolean checkIfAlreadyHavePermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Make the request for the permissions
     */
    private void requestForSpecificPermission() {
        ActivityCompat
                .requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.INTERNET}, 101);
    }

}