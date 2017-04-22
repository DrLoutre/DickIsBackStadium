package com.example.gecko.smartstadium.activities;

import android.app.ProgressDialog;
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
import com.example.gecko.smartstadium.events.AthleticEvent;
import com.example.gecko.smartstadium.events.GetLastRaceAthleticEvent;
import com.example.gecko.smartstadium.events.IdAthleticEvent;
import com.example.gecko.smartstadium.events.LastRaceAthleticEvent;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

public class StatActivity extends AppCompatActivity {

    private final Bus mBus = BusProvider.getInstance();

    private ProgressDialog dialog;

    private TextView nomText;
    private TextView prenomText;
    private TextView tempsText;
    private TextView equipeText;
    private TextView tempsMoyen;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter matchAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stat);

        nomText = (TextView) findViewById(R.id.textNomStat);
        prenomText = (TextView) findViewById(R.id.textFirstnameStat);
        tempsText = (TextView) findViewById(R.id.textTpsStat);
        equipeText = (TextView) findViewById(R.id.textTeamStat);
        tempsMoyen = (TextView) findViewById(R.id.textTpsStat);


        mRecyclerView = (RecyclerView) findViewById(R.id.matchListStat);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        /*todo   il faudra mettre ca où on aura l'info
         ca serait cool d'avoir une liste des matchs.
         Chaque élément de la liste est une sous liste avec [date,team à domicile, team visiteur]
        */
        ArrayList listMatch = new ArrayList(new ArrayList());

        //variable temporaire pour des tests
        String[] list = new String[5];
        list[1] = "coucou1";
        list[2] = "coucou2";
        list[3] = "coucou3";
        list[4] = "coucou4";
        list[0] = "coucou0";
        // specify an adapter (see also next example)
        matchAdapter = new MatchAdapter(list);
        mRecyclerView.setAdapter(matchAdapter);





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
            Double time = 0.0;
            int nbr = 0;
            for (Lap elem : lastRaceAthleticEvent.getLaps()) {
                time = time + elem.getTemp_ms();
                nbr = nbr + 1;
            }
            if (nbr != 0) {
                time = time / nbr;
                tempsMoyen.setText("" + time);
            } else {
                tempsMoyen.setText("indisponible");
            }

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
