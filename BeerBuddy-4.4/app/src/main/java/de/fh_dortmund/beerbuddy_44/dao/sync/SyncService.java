package de.fh_dortmund.beerbuddy_44.dao.sync;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.UriMatcher;
import android.net.ConnectivityManager;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import de.fh_dortmund.beerbuddy_44.acitvitys.BeerBuddyActivity;
import de.fh_dortmund.beerbuddy_44.dao.local.PersonDAOLocal;
import de.fh_dortmund.beerbuddy_44.dao.remote.PersonDAORemote;
import de.fh_dortmund.beerbuddy_44.dao.sync.model.SyncModel;
import lombok.Setter;

public class SyncService extends Service {

    private static final String FILENAME = "Sync_requests";
    private BeerBuddyActivity activiy;
    private SyncThread thread;

    public void shouldStop() {
        onDestroy();
    }

    public void start(BeerBuddyActivity activity) {
        if (thread != null && !thread.isStarted()) {
           Log.w("BeerBuddy", "SyncService Already started!");
        }
        else{
            this.activiy = activity;
            thread = new SyncThread(activity);
            FileInputStream fos = null;
            ObjectInputStream oi = null;
            try {
                fos = activity.openFileInput(FILENAME);
                oi = new ObjectInputStream(fos);
                thread.requests = (Stack<SyncModel>) oi.readObject();
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("BeerBuddy", "Could not load Requests to sync", e);
            } finally {
                if (fos != null)
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                if (oi != null)
                    try {
                        oi.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
            thread.start();
        }
    }

    public void addSyncModel(SyncModel s) {
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
        if (thread != null) {
            thread.shouldStop();
            FileOutputStream fos = null;
            ObjectOutputStream out = null;
            try {
                fos = this.activiy.openFileOutput(FILENAME, Context.MODE_PRIVATE);
                out = new ObjectOutputStream(fos);
                out.writeObject(thread.requests);
                out.close();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("BeerBuddy", "Could not save Requests to sync", e);
            } finally {
                if (fos != null)
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                if (out != null)
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
        }

        super.onDestroy();
    }

    private class SyncThread extends Thread {

        public synchronized boolean isStarted() {
            return !stop;
        }

        private final BeerBuddyActivity activity;

        public SyncThread(BeerBuddyActivity activity) {
            this.activity = activity;
        }

        private boolean stop = false;
        Stack<SyncModel> requests = new Stack<SyncModel>();

        public synchronized void shouldStop() {
            stop = true;
        }

        @Override
        public void run() {
            while (!stop) {
                if (isOnline() && !requests.isEmpty()) {
                    SyncModel pop = requests.pop();
                    pop.performSync(activity);
                } else {
                    try {
                        Thread.sleep(TimeUnit.SECONDS.toMillis(10));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }

        private boolean isOnline() {
            try {
                ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
                return cm.getActiveNetworkInfo().isConnectedOrConnecting();
            } catch (Exception e) {
                return false;
            }
        }
    }
}
