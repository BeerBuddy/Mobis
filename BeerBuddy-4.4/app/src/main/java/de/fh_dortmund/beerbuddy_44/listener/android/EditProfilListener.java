package de.fh_dortmund.beerbuddy_44.listener.android;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.Calendar;

import de.fh_dortmund.beerbuddy.entities.Person;
import de.fh_dortmund.beerbuddy_44.R;
import de.fh_dortmund.beerbuddy_44.acitvitys.EditProfilActivity;
import de.fh_dortmund.beerbuddy_44.acitvitys.MainViewActivity;
import de.fh_dortmund.beerbuddy_44.dao.DAOFactory;
import de.fh_dortmund.beerbuddy_44.picker.ImagePicker;
import de.fh_dortmund.beerbuddy_44.picker.PickerFragmentFactory;

/**
 * Created by David on 30.11.2015.
 */
public class EditProfilListener implements View.OnClickListener {

    private static final String TAG = "EditProfilListener";
    private EditProfilActivity context;

    public EditProfilListener(EditProfilActivity context) {
        this.context=context;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.profil_save:
                saveProfil();
                break;
            case R.id.action_profil_image:
                imageChange();
                break;
            case R.id.profil_dateofbirth_picker:
                showDatePicker();
                break;
            default:
                Log.e(TAG, "No Action defined for " + v.getId());
                break;
        }
    }

    private void showDatePicker() {
        DialogFragment newFragment = PickerFragmentFactory.DatePickerFragment.newInstance(context.getPerson().getDateOfBirth(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar instance = Calendar.getInstance();
                instance.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                instance.set(Calendar.MONTH, monthOfYear);
                instance.set(Calendar.YEAR, year);
                context.getPerson().setDateOfBirth(instance.getTime());
                context.setValues(context.getPerson());
            }
        });
        newFragment.show(context.getFragmentManager(),"datePicker");
    }

    private void imageChange() {
        Intent chooseImageIntent = ImagePicker.getPickImageIntent(context);
        context.startActivityForResult(chooseImageIntent, EditProfilActivity.PICK_IMAGE_ID);
    }

    private void saveProfil() {

        Person p =  context.getValues();
            DAOFactory.getPersonDAO(context).insertOrUpdate(p, new RequestListener<Person>() {
                @Override
                public void onRequestFailure(SpiceException e) {
                    // Error while saving the profile. We have to rollback the data because we are getting in a inconsistent state!
                    Toast.makeText(context, context.getString(R.string.profil_saved_error), Toast.LENGTH_SHORT).show();
                    DAOFactory.getPersonDAO(context).getById(context.getPerson().getId(), new RequestListener<Person>() {
                        @Override
                        public void onRequestFailure(SpiceException spiceException) {
                            Log.e(TAG, "Error occured during save and then retrieving the person from the server: ", spiceException);
                        }

                        @Override
                        public void onRequestSuccess(Person person) {
                            context.setValues(person);
                        }
                    });
                    Log.e(TAG, "Error occured during save: ", e);
                }

                @Override
                public void onRequestSuccess(Person person) {
                    Toast.makeText(context, context.getString(R.string.profil_saved), Toast.LENGTH_SHORT).show();
                    context.clearPasswordFields();
                    Intent i = new Intent(context, MainViewActivity.class);
                    context.startActivity(i);
                }
            });

    }
}
