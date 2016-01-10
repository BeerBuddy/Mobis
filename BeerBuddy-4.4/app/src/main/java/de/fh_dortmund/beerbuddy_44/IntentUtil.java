package de.fh_dortmund.beerbuddy_44;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;

import de.fh_dortmund.beerbuddy.entities.DrinkingSpot;
import de.fh_dortmund.beerbuddy_44.acitvitys.MainViewActivity;
import de.fh_dortmund.beerbuddy_44.acitvitys.ViewDrinkingActivity;
import de.fh_dortmund.beerbuddy_44.acitvitys.ViewProfilActivity;

/**
 * Created by David on 21.12.2015.
 */
public class IntentUtil {

    private IntentUtil(){}

    public static class ShowProfilListener implements View.OnClickListener {
        private final long id;
        private final Context context;

        public ShowProfilListener(Context context,long profilid)
        {
            this.context = context;
            this.id = profilid;
        }

        @Override
        public void onClick(View v) {
            Intent i = new Intent(context, ViewProfilActivity.class);
            i.putExtra("id", id);
            context.startActivity(i);
        }
    }

    public static class ShowDrinkingSpotListener implements View.OnClickListener {
        private final long id;
        private final Context context;
        private boolean joinPossible;

        public ShowDrinkingSpotListener(Context context, long dsid, boolean isJoinPossible)
        {
            this.context = context;
            this.id = dsid;
            this.joinPossible = isJoinPossible;
        }

        @Override
        public void onClick(View v) {
            Intent i = new Intent(context, ViewDrinkingActivity.class);
            i.putExtra("id", id);
            i.putExtra("joinPossible", joinPossible);
            context.startActivity(i);
        }
    }

    public static class ShowDrinkingSpotOnMapListener implements View.OnClickListener {
        private final long id;
        private final Context context;

        public ShowDrinkingSpotOnMapListener(Context context,long dsid)
        {
            this.context = context;
            this.id = dsid;
        }

        @Override
        public void onClick(View v) {
            Intent i = new Intent(context, MainViewActivity.class);
            i.putExtra("id", id);
            context.startActivity(i);
        }
    }
    public static class ShowDrinkingSpotOnGoogleMapListener implements View.OnClickListener {
        private final DrinkingSpot id;
        private final Context context;

        public ShowDrinkingSpotOnGoogleMapListener(Context context,DrinkingSpot dsid)
        {
            this.context = context;
            this.id = dsid;
        }

        @Override
        public void onClick(View v) {
            String geo = id.getGps().replace(";", ",");
            Uri gmmIntentUri = Uri.parse("geo:" + geo);
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(mapIntent);
            } else {
                //TODO show info GoogleMaps not installed
            }
        }
    }

}
