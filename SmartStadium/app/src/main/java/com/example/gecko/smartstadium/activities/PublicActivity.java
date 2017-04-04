package com.example.gecko.smartstadium.activities;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.gecko.smartstadium.R;

public class PublicActivity extends AppCompatActivity {

    CredentialSingletion credential;
    private Button placeButton;
    private Button buvetteButon;
    private TextView nom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public);
        credential = CredentialSingletion.getInstance();

        // set the right name on the screen
        nom = (TextView) findViewById(R.id.publicName);
        nom.setText(credential.getName());

        placeButton = (Button) findViewById(R.id.publicPlaceButton);
        buvetteButon = (Button) findViewById(R.id.publicBuvetteButton);

        placeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(findViewById(android.R.id.content), "Pas encore d'activité pour les places ...", Snackbar.LENGTH_LONG).show();
            }
        });

        buvetteButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(findViewById(android.R.id.content), "Pas encore d'activité pour trouver la buvette", Snackbar.LENGTH_LONG).show();
            }
        });
    }


}