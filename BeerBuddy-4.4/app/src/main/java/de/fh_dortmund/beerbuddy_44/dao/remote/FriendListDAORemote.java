package de.fh_dortmund.beerbuddy_44.dao.remote;

import android.content.Context;

import com.octo.android.robospice.request.listener.RequestListener;

import de.fh_dortmund.beerbuddy.entities.FriendList;
import de.fh_dortmund.beerbuddy.entities.Person;
import de.fh_dortmund.beerbuddy_44.acitvitys.BeerBuddyActivity;
import de.fh_dortmund.beerbuddy_44.dao.interfaces.FriendListDAO;
import de.fh_dortmund.beerbuddy_44.exceptions.DataAccessException;
import de.fh_dortmund.beerbuddy_44.requests.GetFriendListRequest;
import de.fh_dortmund.beerbuddy_44.requests.GetIsFriendRequest;
import de.fh_dortmund.beerbuddy_44.requests.SaveFriendListRequest;

/**
 * Created by David on 19.11.2015.
 */
public  class FriendListDAORemote extends FriendListDAO {


    public FriendListDAORemote(BeerBuddyActivity context) {
        super(context);
    }

    @Override
    public void isFriendFromId(long personid, long friendid, RequestListener<Boolean> listener)  {

            GetIsFriendRequest req = new GetIsFriendRequest(personid, friendid);
            context.getSpiceManager().execute(req, listener);
    }
    @Override
    public void getFriendList(long personid, RequestListener<FriendList> listener)  {
            GetFriendListRequest req = new GetFriendListRequest(personid);
            context.getSpiceManager().execute(req, listener);
    }
    @Override
    public void insertOrUpdate(FriendList friendList, RequestListener<FriendList> listener)  {
            SaveFriendListRequest req = new SaveFriendListRequest(friendList);
            context.getSpiceManager().execute(req, listener);
    }
}
