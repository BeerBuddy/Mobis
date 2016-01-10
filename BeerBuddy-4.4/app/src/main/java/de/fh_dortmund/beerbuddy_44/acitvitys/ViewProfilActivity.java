package de.fh_dortmund.beerbuddy_44.acitvitys;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import de.fh_dortmund.beerbuddy.entities.Person;
import de.fh_dortmund.beerbuddy.exceptions.BeerBuddyException;
import de.fh_dortmund.beerbuddy_44.ObjectMapperUtil;
import de.fh_dortmund.beerbuddy_44.R;
import de.fh_dortmund.beerbuddy_44.dao.DAOFactory;
import de.fh_dortmund.beerbuddy_44.listener.android.ViewProfilListener;

public class ViewProfilActivity extends BeerBuddyActivity {
    private static final String TAG = "ViewProfilActivity";

    public ViewProfilActivity() {
        super(R.layout.view_profil_activity_main, true);
    }

    @Override
    protected void onFurtherCreate(Bundle savedInstanceState) {
        Bundle b = getIntent().getExtras();
        long id = b.getLong("id");

        final ViewProfilActivity context = this;
        if (id != 0) {
            DAOFactory.getPersonDAO(this).getById(id, new RequestListener<Person>() {
                @Override
                public void onRequestFailure(SpiceException spiceException) {

                }

                @Override
                public void onRequestSuccess(final Person person) {
                    try {
                        fillValues(person);
                        final long currentPerson = DAOFactory.getCurrentPersonDAO(context).getCurrentPersonId();


                        //register ViewProfilListener
                        DAOFactory.getFriendlistDAO(context).isFriendFromId(currentPerson, person.getId(), new RequestListener<Boolean>() {
                            @Override
                            public void onRequestFailure(SpiceException spiceException) {

                            }

                            @Override
                            public void onRequestSuccess(Boolean aBoolean) {
                                if (currentPerson == person.getId() || aBoolean) {
                                    //hide the Button
                                    findViewById(R.id.action_profil_send_request).setVisibility(View.INVISIBLE);
                                } else {
                                    ViewProfilListener viewListener = new ViewProfilListener(context, person);
                                    findViewById(R.id.action_profil_send_request).setOnClickListener(viewListener);
                                }
                            }
                        });

                    } catch (BeerBuddyException e) {
                        e.printStackTrace();
                    }
                }
            });


        } else {
            throw new RuntimeException("Expected a Parameter: long id when calling ViewProfil");
        }


    }

    private void fillValues(Person p) {
        if (p.getImage() != null && p.getImage().length > 0) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(p.getImage(), 0, p.getImage().length);
            ((ImageView) findViewById(R.id.profil_image)).setImageBitmap(bitmap);
        }
        ((TextView) findViewById(R.id.profil_username)).setText(p.getUsername());
        ((TextView) findViewById(R.id.profil_vorlieben)).setText(p.getPrefers());
        ((TextView) findViewById(R.id.profil_interessen)).setText(p.getInterests());
        switch (p.getGender()) {
            case Person.Gender.MALE:
                ((TextView) findViewById(R.id.profil_geschlecht)).setText(getString(R.string.profil_gender_male));
                break;
            case Person.Gender.FEMALE:
                ((TextView) findViewById(R.id.profil_geschlecht)).setText(getString(R.string.profil_gender_female));
                break;
            case Person.Gender.OTHER:
                ((TextView) findViewById(R.id.profil_geschlecht)).setText(getString(R.string.profil_gender_other));
                break;
            default:
                Log.e(TAG, "undefinde Gender for Person:" + p.getId());
                break;
        }
        if (p.getDateOfBirth() != null) {
            int ageFromBirthday = ObjectMapperUtil.getAgeFromBirthday(p.getDateOfBirth());
            ((TextView) findViewById(R.id.profil_alter)).setText(ageFromBirthday + "");
        }
    }

}
