package de.fh_dortmund.beerbuddy_44.listener.android;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import de.fh_dortmund.beerbuddy_44.ObjectMapperUtil;
import de.fh_dortmund.beerbuddy_44.R;
import de.fh_dortmund.beerbuddy_44.acitvitys.EditProfilActivity;
import de.fh_dortmund.beerbuddy_44.acitvitys.LoginActivity;
import de.fh_dortmund.beerbuddy_44.acitvitys.MainViewActivity;
import de.fh_dortmund.beerbuddy_44.dao.DAOFactory;
import de.fh_dortmund.beerbuddy_44.exceptions.BeerBuddyException;

/**
 * Created by David on 19.11.2015.
 */
public class LoginListener implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener  {

    private LoginActivity activity;
    private static final String TAG = "LoginListener";

   public LoginListener(LoginActivity activity){
        this.activity = activity;
   }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button: gPlusSignInClicked();
                break;
            case R.id.action_login: loginRegisterClicked();
                break;
            default:
                Log.e(TAG, "No Action specified for Button");
                break;
        }
    }


    private void loginRegisterClicked() {
        try {
            de.fh_dortmund.beerbuddy.Person p = activity.getPerson();
            de.fh_dortmund.beerbuddy.Person pFromDb = DAOFactory.getPersonDAO(activity).getByEmail(p.getEmail());
            if(pFromDb == null){
                //insert Person first Login
                DAOFactory.getPersonDAO(activity).insertOrUpdate(p);
                //Register successfull close this activity and open EditProfil
                DAOFactory.getCurrentPersonDAO(activity).insertCurrentPersonId(p.getId());
                activity.setResult(Activity.RESULT_OK, new Intent(activity, EditProfilActivity.class));
                activity.finish();//dead code ahead
                return;
            }
            else
            if(pFromDb.getPassword().equals(p.getPassword()))
            {
                //Login successfull close this activity
                DAOFactory.getCurrentPersonDAO(activity).insertCurrentPersonId(pFromDb.getId());
                activity.setResult(Activity.RESULT_OK, new Intent(activity, MainViewActivity.class));
                activity.finish();//dead code ahead
                return;
            }
            else{
                Toast.makeText(activity, activity.getString(R.string.wrong_pw), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Wrong Password or Email");
            }
        } catch (BeerBuddyException e) {
            Toast.makeText(activity, "Error accured during Login", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Error accured during Login", e);
        }
    }



    private void gPlusSignInClicked() {
        try {
            // User clicked the sign-in button, so begin the sign-in process and automatically
            // attempt to resolve any errors that occur.
            activity.setMShouldResolve(true);
            activity.getMGoogleApiClient().connect();

            // Show a message to the user that we are signing in.
            Log.i(TAG, "Logged in");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Error during g+ login", e);
        }
    }



    @Override
    public void onConnected(Bundle bundle) {
        // onConnected indicates that an account was selected on the device, that the selected
        // account has granted any requested permissions to our app and that we were able to
        // establish a service connection to Google Play services.
        Log.d(TAG, "onConnected:" + bundle);
        activity.setMShouldResolve(false);
        Person currentPerson = Plus.PeopleApi.getCurrentPerson(activity.getMGoogleApiClient());
        if (currentPerson != null) {
            try {
                de.fh_dortmund.beerbuddy.Person person = ObjectMapperUtil.toPerson(currentPerson);
                DAOFactory.getPersonDAO(activity).insertOrUpdate(person);
                DAOFactory.getCurrentPersonDAO(activity).insertCurrentPersonId(person.getId());
                //end this activity
                activity.setResult(Activity.RESULT_OK, new Intent(activity, MainViewActivity.class));
                activity.finish();//dead code ahead
                return;
            } catch (BeerBuddyException e) {
                Log.e(TAG,"Error accured during g+ login",  e);
            }
        }
        else
        {
            //Could not resolve cuurent Person
            Log.i(TAG, "show login failed");
        }


    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // Could not connect to Google Play Services.  The user needs to select an account,
        // grant permissions or resolve an error in order to sign in. Refer to the javadoc for
        // ConnectionResult to see possible error codes.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);

        if (! activity.isMShouldResolve() && activity.isMIsResolving()) {
            if (connectionResult.hasResolution()) {
                try {
                    connectionResult.startResolutionForResult(activity,LoginActivity.RC_SIGN_IN);
                    activity.setMIsResolving(true);
                } catch (IntentSender.SendIntentException e) {
                    Log.e(TAG, "Could not resolve ConnectionResult.", e);
                    activity.setMIsResolving(false);
                    activity.getMGoogleApiClient().connect();
                }
            } else {
                // Could not resolve the connection result, show the user an
                // error dialog.
                //showErrorDialog(connectionResult);
                Log.e(TAG, connectionResult.toString());
            }
        } else {
            // Show the signed-out UI
            //showSignedOutUI();
            Log.i(TAG, "show signed out");
        }
    }

}
