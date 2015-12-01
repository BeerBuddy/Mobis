package de.fh_dortmund.beerbuddy_44.acitvitys;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.multidex.MultiDex;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.octo.android.robospice.JacksonSpringAndroidSpiceService;
import com.octo.android.robospice.SpiceManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;

import de.fh_dortmund.beerbuddy.Person;
import de.fh_dortmund.beerbuddy_44.R;
import de.fh_dortmund.beerbuddy_44.dao.DAOFactory;
import de.fh_dortmund.beerbuddy_44.exceptions.BeerBuddyException;
import de.fh_dortmund.beerbuddy_44.listener.android.NavigationListener;
import de.fh_dortmund.beerbuddy_44.listener.android.EditProfilListener;
import de.fh_dortmund.beerbuddy_44.picker.ImagePicker;
import lombok.Getter;

public class EditProfilActivity extends AppCompatActivity {

    public static final int PICK_IMAGE_ID = 1;
    protected SpiceManager spiceManager = new SpiceManager(JacksonSpringAndroidSpiceService.class);
    private String lastRequestCacheKey;
    private static final String TAG = "EditProfilActivity";
    @Getter
    private Person person;

    @Override
    protected void onStart() {
        super.onStart();
        spiceManager.start(this);
    }

    @Override
    protected void onStop() {
        spiceManager.shouldStop();
        super.onStop();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profil_activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //finish instance on Logout
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("de.fh_dortmund.beerbuddy_44.ACTION_LOGOUT");
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                finish();
            }
        }, intentFilter);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        //register Navigationb Listener
        NavigationListener listener = new NavigationListener(this);
        NavigationView navigationView = (NavigationView) this.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(listener);

        //getProfil
        try {
            person = DAOFactory.getPersonDAO(this).getById(DAOFactory.getCurrentPersonDAO(this).getCurrentPersonId());
            setValues();

           //register Listeners
            EditProfilListener profilSaveListener = new EditProfilListener(this);
            Button changePic = (Button) this.findViewById(R.id.action_profil_image);
            Button save = (Button) this.findViewById(R.id.profil_save);
            TextView dateofbirth = (TextView) this.findViewById(R.id.profil_dateofbirth);
            Button button = (Button) this.findViewById(R.id.profil_dateofbirth_picker);

            save.setOnClickListener(profilSaveListener);
            changePic.setOnClickListener(profilSaveListener);
            dateofbirth.setOnClickListener(profilSaveListener);
            button.setOnClickListener(profilSaveListener);
        } catch (BeerBuddyException e) {
            e.printStackTrace();
        }
    }



    public void setValues() {
        if(person.getImage() != null && person.getImage().length > 0)
        {
            Bitmap bitmap = BitmapFactory.decodeByteArray(person.getImage(), 0, person.getImage().length);
            ((ImageView) findViewById(R.id.profil_image)).setImageBitmap(bitmap);
        }


        ((EditText) findViewById(R.id.profil_username)).setText(person.getUsername());

        ((EditText) findViewById(R.id.profil_email)).setText(person.getEmail());
        switch (person.getGender()) {
            case Person.Gender.MALE:
                ((RadioButton) findViewById(R.id.radioButton_male)).setChecked(true);
                break;
            case Person.Gender.FEMALE:
                ((RadioButton) findViewById(R.id.radioButton_female)).setChecked(true);
                break;
            default:
                Log.e(TAG, "No Gender specidied with the int value " + person.getGender());
                break;
        }

        ((EditText) findViewById(R.id.profil_interessen)).setText(person.getInterests());
        ((EditText) findViewById(R.id.profil_vorlieben)).setText(person.getPrefers());
        ((TextView) findViewById(R.id.profil_dateofbirth)).setText(DateFormat.getDateInstance().format(person.getDateOfBirth()));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case PICK_IMAGE_ID:
                Bitmap bitmap = ImagePicker.getImageFromResult(this, resultCode, data);
                person.setImage( getByteArrayFromImage(bitmap));
                setValues();
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }


    public Person getValues() {
        //Date of Birth is set Through Listener
        //Need to set all other fields
        person.setInterests(((EditText) findViewById(R.id.profil_interessen)).getText().toString());
        person.setPrefers(((EditText) findViewById(R.id.profil_vorlieben)).getText().toString());

        if (((RadioButton) findViewById(R.id.radioButton_male)).isChecked()) {
            person.setGender(Person.Gender.MALE);
        } else if (((RadioButton) findViewById(R.id.radioButton_female)).isChecked()) {
            person.setGender(Person.Gender.FEMALE);
        }
        person.setEmail(((EditText) findViewById(R.id.profil_email)).getText().toString());
        person.setUsername(((EditText) findViewById(R.id.profil_username)).getText().toString());
        return person;
    }

    private byte[] getByteArrayFromImage(Bitmap map) {
        ByteArrayOutputStream stream = null;
        try {
            stream = new ByteArrayOutputStream();
            map.compress(Bitmap.CompressFormat.PNG, 90, stream);
            byte[] image = stream.toByteArray();
            return image;
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        MultiDex.install(this);
    }

}
