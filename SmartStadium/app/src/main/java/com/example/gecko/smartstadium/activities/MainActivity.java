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
import android.support.v7.app.AlertDialog;
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

    private static final int QR_CODE = 2;
    CredentialSingletion credential;
    private Bus mBus = BusProvider.getInstance();
    private Button BuvetteButton;
    private Button QRButton;
    private Button PlaceButton;

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
        BuvetteButton = (Button) findViewById(R.id.loginButtonBuvettes);
        QRButton = (Button) findViewById(R.id.loginButtonQRcode);
        PlaceButton = (Button) findViewById(R.id.loginButtonPlaces);


        BuvetteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Les buvettes sont en:")
                        // todo JO avoir la liste des buvettes

                        .setMessage("* zone 1:\n   - 10% d\'occupation\n" +
                                "* Zone 7:\n   - 70% d\'occupation");// message test
                // Create the AlertDialog object and return it
                builder.create();
                builder.show();
            }
        });

        QRButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, StatActivity.class);
                startActivityForResult(i, QR_CODE);
            }
        });

        PlaceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Il y a des places libre en:")
                        // todo JO avoir la liste des places libres.

                        .setMessage("* Zone 1: 4d,5d,8h" +
                                "\n* Zone 2: 2b" +
                                "\n* Zone 5: 1a,1b,1c,3c");// message test
                // Create the AlertDialog object and return it
                builder.create();
                builder.show();
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

            if (result.contains("zone")) {
                CalculeBuvette();
            } else {
                onLoginSuccess(result);
            }

        }
    }

    // Va calculer la buvette la plus intéressante
    private void CalculeBuvette() {
        // faire le calcule
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Les buvettes sont en:")
                // todo JO avoir la liste des buvettes

                .setMessage("* zone 1:\n   - 10% d\'occupation\n" +
                        "* Zone 7:\n   - 70% d\'occupation");// message test
        // Create the AlertDialog object and return it
        builder.create();
        builder.show();
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
            startActivity(intent);
        }

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