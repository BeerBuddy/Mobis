package de.fh_dortmund.beerbuddy_44.dao.sync;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Stack;
import java.util.concurrent.TimeUnit;

import de.fh_dortmund.beerbuddy_44.acitvitys.BeerBuddyActivity;
import de.fh_dortmund.beerbuddy_44.dao.sync.model.SyncModel;

public class SyncService extends Service {

    private static final String FILENAME = "Sync_requests";
    private BeerBuddyActivity activity;
    private SyncThread thread;

    public void shouldStop() {
        onDestroy();
    }

    public void start(BeerBuddyActivity activity) {
        if (thread != null && !thread.isStarted()) {
           Log.w("BeerBuddy", "SyncService Already started!");
        }
        else{
            this.activity = activity;
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
                fos = this.activity.openFileOutput(FILENAME, Context.MODE_PRIVATE);
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
            this.requests = new Stack<>();
        }

        private boolean stop = false;

        Stack<SyncModel> requests;

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
