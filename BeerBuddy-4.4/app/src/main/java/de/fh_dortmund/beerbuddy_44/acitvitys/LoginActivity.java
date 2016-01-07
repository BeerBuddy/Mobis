package de.fh_dortmund.beerbuddy_44.acitvitys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;


import de.fh_dortmund.beerbuddy.entities.Person;
import de.fh_dortmund.beerbuddy_44.R;
import de.fh_dortmund.beerbuddy_44.listener.android.LoginListener;
import lombok.Getter;
import lombok.Setter;

public class LoginActivity extends AppCompatActivity {
    /* Request code used to invoke sign in user interactions. */
    private static final String TAG = "LoginActivity";
    /* Is there a ConnectionResult resolution in progress? */
    @Getter
    @Setter
    private boolean mIsResolving = false;

    /* Should we automatically resolve ConnectionResults when possible? */
    @Getter
    @Setter
    private boolean mShouldResolve = false;

    public Person getPerson() {
        String email = ((EditText) findViewById(R.id.login_email)).getText().toString();
        String password = ((EditText) findViewById(R.id.login_password)).getText().toString();
        Person p = new Person();
        p.setPassword(password);
        p.setEmail(email);
        return p;

    }

    @Override
    protected void onStart() {
        super.onStart();
        //spiceManager.start(this);
    }

    @Override
    protected void onStop() {
       // spiceManager.shouldStop();
        super.onStop();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        LoginListener loginListener = new LoginListener(this);

        //register Google Login Listener
        //findViewById(R.id.sign_in_button).setOnClickListener(loginListener);
        //register login/register Button
        findViewById(R.id.action_login).setOnClickListener(loginListener);
    }

    @Override
    public void onBackPressed() {
            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction("de.fh_dortmund.beerbuddy_44.ACTION_LOGOUT");
            this.sendBroadcast(broadcastIntent);
            super.onBackPressed();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        MultiDex.install(this);
    }
}
