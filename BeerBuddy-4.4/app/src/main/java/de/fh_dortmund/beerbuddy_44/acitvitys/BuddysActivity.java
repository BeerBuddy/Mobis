package de.fh_dortmund.beerbuddy_44.acitvitys;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

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

    public void setValue()
    {

        try {
            friendList = DAOFactory.getFriendlistDAO(this).getFriendList(DAOFactory.getCurrentPersonDAO(this).getCurrentPersonId());
        ListView listViewBuddys = (ListView) this.findViewById(R.id.buddys_buddys);
        FriendListAdapter adapter = new FriendListAdapter(this,
                R.layout.buddy_list_row_layout, friendList.getFriends().toArray(new Person[]{}));
        listViewBuddys.setAdapter(adapter);

        //buddys_requests
        ListView listViewRequests = (ListView) this.findViewById(R.id.buddys_requests);
        friendInvitations = DAOFactory.getFriendInvitationDAO(this).getAllFor(DAOFactory.getCurrentPersonDAO(this).getCurrentPersonId());
        FriendInvitationAdapter freindAdapter =new FriendInvitationAdapter(this,  R.layout.buddy_list_row_layout, friendInvitations.toArray(new FriendInvitation[]{}));
        listViewRequests.setAdapter(freindAdapter);
        } catch (BeerBuddyException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }



  /*  private void performRequest() {
        BuddysActivity.this.setProgressBarIndeterminateVisibility(true);

        GetAllPersonsRequest request = new GetAllPersonsRequest();
        lastRequestCacheKey = request.createCacheKey();

        spiceManager.execute(request, lastRequestCacheKey, DurationInMillis.ONE_MINUTE, new ListPersonRequestListener() {
            @Override
            public void onRequestFailure(SpiceException e) {

            }

            @Override
            public void onRequestSuccess(PersonList listFollowers) {

            }
        });
    }*/


}
