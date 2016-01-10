package de.fh_dortmund.beerbuddy_44.dao.sync;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.UriMatcher;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.Stack;
import java.util.Vector;

import de.fh_dortmund.beerbuddy_44.acitvitys.BeerBuddyActivity;
import de.fh_dortmund.beerbuddy_44.dao.local.PersonDAOLocal;
import de.fh_dortmund.beerbuddy_44.dao.remote.PersonDAORemote;
import lombok.Setter;

public class SyncService extends Service {

    private static final String FILENAME = "Sync_requests";
    private final BeerBuddyActivity activity;
    public SyncService(BeerBuddyActivity activity)
    {
        this.activity = activity;
    }
    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private static final int PERSON_SAVE = 0;
    private static final int FRIENDINVITATION_SAVE = 1;
    private static final int FRIENDINVITATION_ACCEPT = 2;
    private static final int FRIENDINVITATION_DECLINE = 3;
    private static final int FRIENDLIST_SAVE = 4;
    private static final int DRINKINGINVITATION_SAVE = 5;
    private static final int DRINKINGINVITATION_ACCEPT = 6;
    private static final int DRINKINGINVITATION_DECLINE = 7;
    private static final int DRINKINGSPOT_SAVE = 8;
    private static final int DRINKINGSPOT_JOIN = 9;
    private static final int DRINKINGSPOT_DEACTIVATE = 10;

    static {
        sURIMatcher.addURI("person", "save/#", PERSON_SAVE);
        sURIMatcher.addURI("friendinvitation", "save/#", FRIENDINVITATION_SAVE);
        sURIMatcher.addURI("friendinvitation", "accept/#", FRIENDINVITATION_ACCEPT);
        sURIMatcher.addURI("friendinvitation", "decline/#", FRIENDINVITATION_DECLINE);
        sURIMatcher.addURI("friendlist", "save/#", FRIENDLIST_SAVE);
        sURIMatcher.addURI("drinkinginvitation", "save/#", DRINKINGINVITATION_SAVE);
        sURIMatcher.addURI("drinkinginvitation", "accept/#", DRINKINGINVITATION_ACCEPT);
        sURIMatcher.addURI("drinkinginvitation", "decline/#", DRINKINGINVITATION_DECLINE);
        sURIMatcher.addURI("drinkingspot", "save/#", DRINKINGSPOT_SAVE);
        sURIMatcher.addURI("drinkingspot", "join/#/#", DRINKINGSPOT_JOIN);
        sURIMatcher.addURI("drinkingspot", "deactivate/#", DRINKINGSPOT_DEACTIVATE);
    }

    private class SyncThread extends Thread {


        private final BeerBuddyActivity activity;
        public SyncThread(BeerBuddyActivity activity)
        {
            this.activity = activity;
        }
        private boolean stop = false;
        Stack<String> requests = new Stack<String>();

        public void shouldStop() {
            stop = true;
        }

        @Override
        public void run() {
            while (!stop) {
                String res = requests.pop();
                Uri uri = Uri.parse("http://www.google.com" + res);
                switch (sURIMatcher.match(uri)) {
                    case PERSON_SAVE:
                       // new PersonDAOLocal(activity).getById();
                       // new PersonDAORemote(activity).insertOrUpdate();
                        break;
                    case FRIENDINVITATION_SAVE:
                        break;
                    case FRIENDINVITATION_ACCEPT:
                        break;
                    case FRIENDINVITATION_DECLINE:
                        break;
                    case FRIENDLIST_SAVE:
                        break;
                    case DRINKINGINVITATION_SAVE:
                        break;
                    case DRINKINGINVITATION_ACCEPT:
                        break;
                    case DRINKINGINVITATION_DECLINE:
                        break;
                    case DRINKINGSPOT_SAVE:
                        break;
                    case DRINKINGSPOT_JOIN:
                        break;
                    case DRINKINGSPOT_DEACTIVATE:
                        break;
                    default:
                        Log.e("BeerBuddy", "Rerquest uri has no match");
                }
            }
        }
    }

    private SyncThread thread ;

    @Override
    public void onCreate() {
        thread = new SyncThread(activity);
        try {
            FileInputStream fos = openFileInput(FILENAME);
            ObjectInputStream oi = new ObjectInputStream(fos);
            thread.requests = (Stack<String>) oi.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("BeerBuddy", "Could not load Requests to sync", e);
        }
    }




    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        thread.start();
        return super.onStartCommand(intent, flags, startId);
    }

    public void addRequest(String s) {
        thread.requests.push(s);
    }

    @Override
    public void onLowMemory() {
        onDestroy();
        super.onLowMemory();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        thread.shouldStop();
        try {
            FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
            ObjectOutputStream out = new ObjectOutputStream(fos);
            out.writeObject(thread.requests);
            out.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("BeerBuddy", "Could not save Requests to sync", e);
        }

        super.onDestroy();
    }
}
