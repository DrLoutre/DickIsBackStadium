package com.example.gecko.smartstadium.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.gecko.smartstadium.R;
import com.example.gecko.smartstadium.bus.BusProvider;
import com.example.gecko.smartstadium.classes.Credentials;
import com.example.gecko.smartstadium.events.LoginEvent;
import com.example.gecko.smartstadium.events.PostLoginEvent;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

/**
 * Created by Quentin Jacquemotte
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity { //implements LoaderCallbacks<Cursor> {

    private Bus mBus = BusProvider.getInstance();

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    //private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    //private static final String[] DUMMY_CREDENTIALS = new String[]{
    //"foo@example.com:hello", "bar@example.com:world"
    //};
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    //private UserLoginTask mAuthTask = null;

    // UI references.
    private Button validationButton;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private CredentialSingletion credentialSingletion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        validationButton = (Button) findViewById(R.id.loginValidationButton);
        credentialSingletion = CredentialSingletion.getInstance();
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_NULL) {
                    return true;
                }
                return false;
            }
        });

        validationButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
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
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        // Reset errors.
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String password = mPasswordView.getText().toString();

        // Check for a valid password, if the user entered one.
        if (!isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            Snackbar.make(findViewById(android.R.id.content), "Erreur", Snackbar.LENGTH_LONG).show();
        } else {
            connection(password, credentialSingletion.getId());
        }
    }

    private void connection(String password, String id) {
        mBus.post(new PostLoginEvent(new Credentials(id, password)));
    }

    // Check the lenght of the password
    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    @Subscribe
    public void onLoginEvent(LoginEvent loginEvent) {
        if (loginEvent.getAthletic() != null) {
            Intent intent = new Intent(LoginActivity.this, StatActivity.class);
            startActivity(intent);
        } else {
            Snackbar.make(findViewById(android.R.id.content), "Le mot de passe est incorrect", Snackbar.LENGTH_LONG).show();
        }
    }
}

