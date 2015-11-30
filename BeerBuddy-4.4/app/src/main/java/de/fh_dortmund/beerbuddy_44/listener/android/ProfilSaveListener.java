package de.fh_dortmund.beerbuddy_44.listener.android;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import de.fh_dortmund.beerbuddy.Person;
import de.fh_dortmund.beerbuddy_44.R;
import de.fh_dortmund.beerbuddy_44.acitvitys.EditProfilActivity;
import de.fh_dortmund.beerbuddy_44.dao.DAOFactory;
import de.fh_dortmund.beerbuddy_44.exceptions.BeerBuddyException;

/**
 * Created by David on 30.11.2015.
 */
public class ProfilSaveListener implements View.OnClickListener {

    private static final String TAG = "ProfilSaveListener";
    private EditProfilActivity context;

    public ProfilSaveListener(EditProfilActivity context) {
        this.context=context;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.profil_save:
                saveProfil();
                break;

            default:
                Log.e(TAG, "No Action defined for " + v.getId());
                break;
        }
    }

    private void saveProfil() {

        Person p =  context.getValues();
        try {
            DAOFactory.getPersonDAO(context).insertOrUpdate(p);
            Toast.makeText(context, context.getString(R.string.profil_saved), Toast.LENGTH_SHORT).show();
        } catch (BeerBuddyException e) {
            Log.e(TAG, "Error accured during save: " ,e);
            e.printStackTrace();
        }
    }
}
