package com.example.gecko.smartstadium.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.gecko.smartstadium.R;

public class StatActivity extends AppCompatActivity {
    private TextView nomText;
    private TextView prenomText;
    private TextView tempsText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stat);

        nomText = (TextView) findViewById(R.id.textNomStat);
        prenomText = (TextView) findViewById(R.id.textFirstnameStat);
        tempsText = (TextView) findViewById(R.id.textTpsStat);
        // todo JO récupérer les valeurs du joueur
        nomText.setText("");
        prenomText.setText("");
        tempsText.setText("");
    }
}
