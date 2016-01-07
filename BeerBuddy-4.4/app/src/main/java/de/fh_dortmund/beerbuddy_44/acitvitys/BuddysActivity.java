package de.fh_dortmund.beerbuddy_44.acitvitys;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.Arrays;
import java.util.List;

import de.fh_dortmund.beerbuddy.entities.FriendInvitation;
import de.fh_dortmund.beerbuddy.entities.FriendList;
import de.fh_dortmund.beerbuddy.entities.Person;
import de.fh_dortmund.beerbuddy.exceptions.BeerBuddyException;
import de.fh_dortmund.beerbuddy_44.R;
import de.fh_dortmund.beerbuddy_44.adapter.FriendInvitationAdapter;
import de.fh_dortmund.beerbuddy_44.adapter.FriendListAdapter;
import de.fh_dortmund.beerbuddy_44.dao.DAOFactory;

public class BuddysActivity extends BeerBuddyActivity {

    private static final String TAG = "BuddysActivity";
    private FriendList friendList;
    private List<FriendInvitation> friendInvitations;

    public BuddysActivity() {
        super(R.layout.buddys_activity_main, true);
    }

    @Override
    protected void onFurtherCreate(Bundle savedInstanceState) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(FriendInvitationAdapter.UPDATE_FRIENDLIST);
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                setValue();
            }
        }, intentFilter);
        setValue();
    }

    public void setValue() {
        try {
            DAOFactory.getFriendlistDAO(this).getFriendList(DAOFactory.getCurrentPersonDAO(this).getCurrentPersonId(), new RequestListener<FriendList>() {
                @Override
                public void onRequestFailure(SpiceException e) {
                    Log.e(TAG, e.getMessage(), e);
                }

                @Override
                public void onRequestSuccess(FriendList fl) {
                    friendList = fl;
                    setValueFL();
                }
            });

            DAOFactory.getFriendInvitationDAO(this).getAllFor(DAOFactory.getCurrentPersonDAO(this).getCurrentPersonId(), new RequestListener<FriendInvitation[]>() {

                @Override
                public void onRequestFailure(SpiceException e) {
                    Log.e(TAG, e.getMessage(), e);
                }

                @Override
                public void onRequestSuccess(FriendInvitation[] fi) {
                    friendInvitations = Arrays.asList(fi);
                    setValueFI();
                }
            });
        } catch (BeerBuddyException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    public void setValueFL() {
        ListView listViewBuddys = (ListView) this.findViewById(R.id.buddys_buddys);
        FriendListAdapter adapter = new FriendListAdapter(this,
                R.layout.buddy_list_row_layout, friendList.getFriends().toArray(new Person[]{}));
        listViewBuddys.setAdapter(adapter);
    }

    public void setValueFI() {
        ListView listViewRequests = (ListView) this.findViewById(R.id.buddys_requests);
        FriendInvitationAdapter freindAdapter = new FriendInvitationAdapter(this, R.layout.buddy_list_row_layout, friendInvitations.toArray(new FriendInvitation[]{}));
        listViewRequests.setAdapter(freindAdapter);

    }


}
