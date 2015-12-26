package de.fh_dortmund.beerbuddy_44.acitvitys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.EditText;

import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;

import de.fh_dortmund.beerbuddy.entities.Person;
import de.fh_dortmund.beerbuddy_44.R;
import de.fh_dortmund.beerbuddy_44.listener.android.LoginListener;
import lombok.Getter;
import lombok.Setter;

public class LoginActivity extends AppCompatActivity {
    /* Request code used to invoke sign in user interactions. */
    public static final int RC_SIGN_IN = 0;
    private static final String TAG = "LoginActivity";
    /* Is there a ConnectionResult resolution in progress? */
    @Getter
    @Setter
    private boolean mIsResolving = false;

    /* Should we automatically resolve ConnectionResults when possible? */
    @Getter
    @Setter
    private boolean mShouldResolve = false;

    /* Client used to interact with Google APIs. */
    @Getter
    @Setter
    private GoogleApiClient mGoogleApiClient;
    //protected SpiceManager spiceManager = new SpiceManager(JacksonSpringAndroidSpiceService.class);

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
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
       // spiceManager.shouldStop();
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult:" + requestCode + ":" + resultCode + ":" + data);

        if (requestCode == RC_SIGN_IN) {
            // If the error resolution was not successful we should not resolve further.
            if (resultCode != RESULT_OK) {
                mShouldResolve = false;
            }

            mIsResolving = false;
            mGoogleApiClient.connect();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        LoginListener loginListener = new LoginListener(this);

        //init GoogleApi
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(loginListener)
                .addOnConnectionFailedListener(loginListener)
                .addApi(Plus.API)
                .addScope(new Scope(Scopes.PROFILE))
                .addScope(new Scope(Scopes.EMAIL))
                .build();

        //register Google Login Listener
        findViewById(R.id.sign_in_button).setOnClickListener(loginListener);
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
