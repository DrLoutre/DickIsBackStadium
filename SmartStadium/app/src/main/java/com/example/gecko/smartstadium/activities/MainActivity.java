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
import com.example.gecko.smartstadium.classes.Refreshment;
import com.example.gecko.smartstadium.classes.Seat;
import com.example.gecko.smartstadium.classes.custom.SeatsByTribune;
import com.example.gecko.smartstadium.events.AthleticEvent;
import com.example.gecko.smartstadium.events.GetRefreshmentsEvent;
import com.example.gecko.smartstadium.events.GetSeatsTribunesEvent;
import com.example.gecko.smartstadium.events.IdAthleticEvent;
import com.example.gecko.smartstadium.events.RefreshmentsEvent;
import com.example.gecko.smartstadium.events.SeatsTribunesEvent;
import com.example.gecko.smartstadium.utils.ConnectionUtils;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.text.NumberFormat;
import java.util.ArrayList;

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
                getRefreshments(false);
            }

        });

        QRButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, QRCodeActivity.class);
                startActivityForResult(i, QR_CODE);
            }
        });

        PlaceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSeatsTribunes();
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
            if(requestCode == QR_CODE) {
                final String result = data.getStringExtra("result");

                if (result.contains("zone")) {
                    getRefreshments(true);
                } else {
                    onScanSuccess(result);
                }
            }
        }
    }

    private void onScanSuccess(final String result) {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.putExtra("id", result);
        startActivity(intent);
    }

    private void getRefreshments(boolean bestOne) {
        mBus.post(new GetRefreshmentsEvent(bestOne));
    }

    private void getSeatsTribunes() {
        mBus.post(new GetSeatsTribunesEvent());
    }

    /*@Subscribe
    public void AthleticEvent(AthleticEvent athleticEvent) {
        if (athleticEvent.getAthletic() != null) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.putExtra("id", athleticEvent.getAthletic().getNFC());
            startActivity(intent);
        } else {
            Snackbar.make(findViewById(android.R.id.content), "Un problème de connexion.", Snackbar.LENGTH_LONG).show();
        }

    }*/

    @Subscribe
    public void RefreshmentsEvent(final RefreshmentsEvent refreshmentsEvent) {
        if (refreshmentsEvent.getRefreshment() != null) {
            StringBuilder message = new StringBuilder();
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Les buvettes sont en :");

            if (refreshmentsEvent.isBestOne()) {
                builder.setTitle("Buvette(s) la (les) plus libre(s) : ");
                refreshmentsEvent.setRefreshment(getFreeRefreshments(refreshmentsEvent.getRefreshment()));
            }

            int size = refreshmentsEvent.getRefreshment().size();
            for (Refreshment refreshment : refreshmentsEvent.getRefreshment()) {
                size--;
                NumberFormat defaultFormat = NumberFormat.getPercentInstance();
                defaultFormat.setMinimumFractionDigits(1);

                message.append(refreshment.getLocalisation()).append("\n  - ").
                        append(defaultFormat.format(refreshment.getAttendance())).append(" d\'occupation");

                if (size > 0) {
                    message.append("\n");
                }
            }
            builder.setMessage(message.toString());
            // Create the AlertDialog object and return it
            builder.create();
            builder.show();
        } else {
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Un problème est survenu.", Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("Réessayer", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getRefreshments(refreshmentsEvent.isBestOne());
                }
            });
            snackbar.show();
        }
    }

    @Subscribe
    public void TribunesEvent(SeatsTribunesEvent seatsTribunesEvent) {
        if (seatsTribunesEvent.getSeatsByTribunes() != null) {
            StringBuilder message = new StringBuilder();

            int length = seatsTribunesEvent.getSeatsByTribunes().size();
            for (SeatsByTribune seatsByTribune : seatsTribunesEvent.getSeatsByTribunes()) {
                length--;
                message.append(seatsByTribune.getTribune().getLocalisation()).append(": ");

                int size = seatsByTribune.getSeats().size();
                for (Seat seat : seatsByTribune.getSeats()) {
                    size--;
                    if (size == 0) {
                        message.append(seat.getId()).append(".");
                    } else {
                        message.append(seat.getId()).append(", ");
                    }
                }

                if (length > 0) {
                    message.append("\n");
                }
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Numéros des places libres :")
                    .setMessage(message.toString());
            // Create the AlertDialog object and return it
            builder.create();
            builder.show();
        } else {
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Un problème est survenu.", Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("Réessayer", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getSeatsTribunes();
                }
            });
            snackbar.show();
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

    private ArrayList<Refreshment> getFreeRefreshments(ArrayList<Refreshment> refreshments) {
        ArrayList<Refreshment> mins = new ArrayList<>();
        float minimum = refreshments.get(0).getAttendance();

        for (Refreshment refreshment : refreshments) {
            if (refreshment.getAttendance() < minimum) minimum = refreshment.getAttendance();
        }

        for (Refreshment refreshment : refreshments) {
            if (refreshment.getAttendance() == minimum) mins.add(refreshment);
        }
        return mins;
    }
}