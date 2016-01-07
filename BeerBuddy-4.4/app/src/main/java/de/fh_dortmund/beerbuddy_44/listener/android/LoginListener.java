package de.fh_dortmund.beerbuddy_44.listener.android;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import de.fh_dortmund.beerbuddy_44.R;
import de.fh_dortmund.beerbuddy_44.acitvitys.EditProfilActivity;
import de.fh_dortmund.beerbuddy_44.acitvitys.LoginActivity;
import de.fh_dortmund.beerbuddy_44.acitvitys.MainViewActivity;
import de.fh_dortmund.beerbuddy_44.dao.DAOFactory;
import de.fh_dortmund.beerbuddy.exceptions.BeerBuddyException;
import de.fh_dortmund.beerbuddy_44.dao.interfaces.CurrentPersonDAO;
import de.fh_dortmund.beerbuddy_44.dao.interfaces.PersonDAO;

/**
 * Created by David on 19.11.2015.
 */
public class LoginListener implements
        View.OnClickListener  {

    private LoginActivity activity;
    private CurrentPersonDAO currentPersonDAO;
    private PersonDAO personDAO;
    private static final String TAG = "LoginListener";

   public LoginListener(LoginActivity activity){
       this.activity = activity;
       currentPersonDAO= DAOFactory.getCurrentPersonDAO(activity);
       personDAO = DAOFactory.getPersonDAO(activity);
   }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.action_login: loginRegisterClicked();
                break;
            default:
                Log.e(TAG, "No Action specified for Button");
                break;
        }
    }


    private void loginRegisterClicked() {
        try {
            de.fh_dortmund.beerbuddy.entities.Person p = activity.getPerson();
            de.fh_dortmund.beerbuddy.entities.Person pFromDb = personDAO.getByEmail(p.getEmail());
            if(pFromDb == null){
                Log.w(TAG , "HELP, A NEWBIE!");
                //insert Person first Login
                personDAO.insertOrUpdate(p);
                p = personDAO.getByEmail(p.getEmail());
                //Register successfull close this activity and open EditProfil
                currentPersonDAO.insertCurrentPersonId(p.getId());
                activity.setResult(Activity.RESULT_OK, new Intent(activity, EditProfilActivity.class));
                activity.finish();//dead code ahead
                Intent i = new Intent(activity, MainViewActivity.class);
                activity.startActivity(i);
                return;
            }
            else
            if(pFromDb.getPassword().equals(p.getPassword()))
            {
                //Login successfull close this activity
                Log.w(TAG, "I KNOW YOU!");
                currentPersonDAO.insertCurrentPersonId(pFromDb.getId());
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







}
