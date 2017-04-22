package com.example.gecko.smartstadium.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.gecko.smartstadium.R;
import com.example.gecko.smartstadium.bus.BusProvider;
import com.example.gecko.smartstadium.events.AthleticEvent;
import com.example.gecko.smartstadium.events.GetLastRaceAthleticEvent;
import com.example.gecko.smartstadium.events.IdAthleticEvent;
import com.example.gecko.smartstadium.events.LastRaceAthleticEvent;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

public class StatActivity extends AppCompatActivity {

    private final Bus mBus = BusProvider.getInstance();

    private ProgressDialog dialog;

    private TextView nomText;
    private TextView prenomText;
    private TextView tempsText;
    private TextView equipeText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stat);

        nomText = (TextView) findViewById(R.id.textNomStat);
        prenomText = (TextView) findViewById(R.id.textFirstnameStat);
        tempsText = (TextView) findViewById(R.id.textTpsStat);
        equipeText = (TextView) findViewById(R.id.textTeamStat);

        dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setTitle("Chargement");
        dialog.setMessage("Chargement de vos données ...");

        tempsText.setText("");

        getAthletic();
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

    private void getAthletic() {
        dialog.show();
        mBus.post(new IdAthleticEvent("1"));
    }

    private void getAthleticLap() {
        mBus.post(new GetLastRaceAthleticEvent("1"));
    }

    @Subscribe
    public void onAthleticEvent(AthleticEvent athleticEvent) {
        if (athleticEvent.getAthletic() != null) {
            nomText.setText(athleticEvent.getAthletic().getNom());
            prenomText.setText(athleticEvent.getAthletic().getPrenom());
            equipeText.setText(athleticEvent.getAthletic().getTeam());
            getAthleticLap();
        } else {
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Un problème est survenu.", Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("Réessayer", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getAthletic();
                }
            });
            snackbar.show();
            dialog.dismiss();
        }
    }

    @Subscribe
    public void onAthleticLapEvent(LastRaceAthleticEvent lastRaceAthleticEvent) {
        if (lastRaceAthleticEvent.getLaps() != null) {
            //TODO : Calculer le temps moyen avec la liste "lastRaceAthleticEvent.getLaps()"
        } else {
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Un problème est survenu.", Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("Réessayer", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getAthleticLap();
                }
            });
            snackbar.show();
        }
        dialog.dismiss();
    }
}
