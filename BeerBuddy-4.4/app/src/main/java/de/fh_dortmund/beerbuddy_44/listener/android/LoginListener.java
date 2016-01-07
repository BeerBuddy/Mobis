package de.fh_dortmund.beerbuddy_44.listener.android;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import de.fh_dortmund.beerbuddy.entities.Person;
import de.fh_dortmund.beerbuddy.exceptions.BeerBuddyException;
import de.fh_dortmund.beerbuddy_44.R;
import de.fh_dortmund.beerbuddy_44.acitvitys.LoginActivity;
import de.fh_dortmund.beerbuddy_44.acitvitys.MainViewActivity;
import de.fh_dortmund.beerbuddy_44.dao.DAOFactory;
import de.fh_dortmund.beerbuddy_44.dao.interfaces.CurrentPersonDAO;
import de.fh_dortmund.beerbuddy_44.dao.interfaces.PersonDAO;

/**
 * Created by David on 19.11.2015.
 */
public class LoginListener implements
        View.OnClickListener {

    private LoginActivity activity;
    private CurrentPersonDAO currentPersonDAO;
    private PersonDAO personDAO;
    private static final String TAG = "LoginListener";


    public LoginListener(LoginActivity activity) {
        this.activity = activity;
        currentPersonDAO = DAOFactory.getCurrentPersonDAO(activity);
        personDAO = DAOFactory.getPersonDAO(activity);
    }



    @Override

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.action_login:
                loginRegisterClicked();
                break;
            default:
                Log.e(TAG, "No Action specified for Button");
                break;
        }
    }


    private void loginRegisterClicked() {
        final de.fh_dortmund.beerbuddy.entities.Person p = activity.getPerson();
        DAOFactory.getPersonDAO(activity).getByEmail(p.getEmail(), new RequestListener<Person>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                spiceException.printStackTrace();
            }

            @Override
            public void onRequestSuccess(Person person) {
                Log.d("BuddysActivity", "recieved Person: "+person);
                if (person == null) {
                    //insert Person first Login
                    Log.d("BuddysActivity", "trying to save Person: "+p);
                    personDAO.insertOrUpdate(p, new RequestListener<Person>() {
                        @Override
                        public void onRequestFailure(SpiceException spiceException) {
                            spiceException.printStackTrace();
                        }

                        @Override
                        public void onRequestSuccess(Person person) {
                            try {
                                currentPersonDAO.insertCurrentPersonId(p.getId());
                                Intent i = new Intent(activity, MainViewActivity.class);
                                activity.startActivity(i);
                                return;
                            } catch (BeerBuddyException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } else if (person.getPassword().equals(p.getPassword())) {
                    try {
                        //Login successfull close this activity
                        currentPersonDAO.insertCurrentPersonId(person.getId());
                        activity.setResult(Activity.RESULT_OK, new Intent(activity, MainViewActivity.class));
                        activity.finish();//dead code ahead
                        return;
                    } catch (BeerBuddyException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(activity, activity.getString(R.string.wrong_pw), Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Wrong Password or Email");
                }
            }
        });

    }


}
