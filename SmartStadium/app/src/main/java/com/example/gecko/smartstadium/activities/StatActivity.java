package com.example.gecko.smartstadium.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.gecko.smartstadium.R;
import com.example.gecko.smartstadium.adapter.MatchAdapter;
import com.example.gecko.smartstadium.bus.BusProvider;
import com.example.gecko.smartstadium.classes.Lap;
import com.example.gecko.smartstadium.classes.custom.MatchNotEnded;
import com.example.gecko.smartstadium.events.AthleticEvent;
import com.example.gecko.smartstadium.events.GetLastMatchsNotEndedEvent;
import com.example.gecko.smartstadium.events.GetLastRaceAthleticEvent;
import com.example.gecko.smartstadium.events.IdAthleticEvent;
import com.example.gecko.smartstadium.events.LastRaceAthleticEvent;
import com.example.gecko.smartstadium.events.MatchsEvent;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class StatActivity extends AppCompatActivity {

    private final Bus mBus = BusProvider.getInstance();

    private ProgressDialog dialog;

    private TextView nomText;
    private TextView prenomText;
    private TextView ageText;
    private TextView tempsMoyen;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter matchAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private String id;

    /**
     * Start the activity
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stat);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");

        nomText = (TextView) findViewById(R.id.textNomStat);
        prenomText = (TextView) findViewById(R.id.textFirstnameStat);
        ageText = (TextView) findViewById(R.id.textAgeStat);
        tempsMoyen = (TextView) findViewById(R.id.textTpsStat);


        mRecyclerView = (RecyclerView) findViewById(R.id.matchListStat);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setTitle("Chargement");
        dialog.setMessage("Chargement de vos données ...");

        getAthletic();

    }

    /**
     * Add the contexte to the bus
     */
    @Override
    public void onResume() {
        super.onResume();
        mBus.register(this);
    }

    /**
     * remove the contexte from the bus
     */
    @Override
    public void onPause() {
        super.onPause();
        mBus.unregister(this);
    }

    /**
     * return to the right activity
     */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(StatActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Send an event to the bus to get an athletic with its id
     */
    private void getAthletic() {
        dialog.show();
        mBus.post(new IdAthleticEvent(id));
    }

    /**
     * Send an event to the bus to get the last race of an athletic
     */
    private void getAthleticLap() {
        mBus.post(new GetLastRaceAthleticEvent(id));
    }

    /**
     * Send an event to the bus to get its matchs not ended
     */
    private void getMatchsNotEnded() {
        mBus.post(new GetLastMatchsNotEndedEvent(id));
    }

    /**
     * handle the result of the request on a athletic
     *
     * @param athleticEvent
     */
    @Subscribe
    public void onAthleticEvent(AthleticEvent athleticEvent) {
        if (athleticEvent.getAthletic() != null) {
            nomText.setText(athleticEvent.getAthletic().getNom());
            prenomText.setText(athleticEvent.getAthletic().getPrenom());
            ageText.setText(String.valueOf(athleticEvent.getAthletic().getAge()) + " ans");
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

    /**
     * handle the result of the request about the race of an athletic
     * @param lastRaceAthleticEvent
     */
    @Subscribe
    public void onAthleticLapEvent(LastRaceAthleticEvent lastRaceAthleticEvent) {
        if (lastRaceAthleticEvent.getLaps() != null) {
            Double time = 0.0;
            int nbr = 0;
            for (Lap elem : lastRaceAthleticEvent.getLaps()) {
                time = time + elem.getTemp_ms() + (elem.getTemp_sec() * 1000) + (elem.getTemp_min() * 60000) + (elem.getTemp_hour() * 3600000);
                nbr = nbr + 1;
            }

            String msg = "Indisponible";
            if (nbr != 0) {
                time = time / nbr;
                msg = String.format(Locale.FRANCE, "%d minute(s), %d seconde(s)",
                        TimeUnit.MILLISECONDS.toMinutes(Math.round(time)),
                        TimeUnit.MILLISECONDS.toSeconds(Math.round(time)) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(Math.round(time)))
                );
                tempsMoyen.setText(msg);
            } else {
                tempsMoyen.setText(msg);
            }
            getMatchsNotEnded();
        } else {
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Un problème est survenu.", Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("Réessayer", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getAthleticLap();
                }
            });
            snackbar.show();
            dialog.dismiss();
        }
        dialog.dismiss();
    }

    /**
     * handle the result of the request about matchs
     * @param matchsEvent
     */
    @Subscribe
    public void onMatchAthleticEvent(MatchsEvent matchsEvent) {
        if (matchsEvent.getMatchs() != null) {
            ArrayList<MatchNotEnded> listMatch = matchsEvent.getMatchs();
            matchAdapter = new MatchAdapter(listMatch);
            mRecyclerView.setAdapter(matchAdapter);
        } else {
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Un problème est survenu.", Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("Réessayer", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getMatchsNotEnded();
                }
            });
            snackbar.show();
        }
        dialog.dismiss();
    }
}
