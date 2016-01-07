package de.fh_dortmund.beerbuddy_44.dao.remote;

    import com.octo.android.robospice.request.listener.RequestListener;

    import de.fh_dortmund.beerbuddy.entities.FriendInvitation;
    import de.fh_dortmund.beerbuddy_44.acitvitys.BeerBuddyActivity;
    import de.fh_dortmund.beerbuddy_44.dao.interfaces.FriendInvitationDAO;
    import de.fh_dortmund.beerbuddy_44.requests.AcceptFriendInvitationRequest;
    import de.fh_dortmund.beerbuddy_44.requests.DeclineFriendInvitationRequest;
    import de.fh_dortmund.beerbuddy_44.requests.GetAllForFriendInvitationRequest;
    import de.fh_dortmund.beerbuddy_44.requests.GetAllFromFriendInvitationRequest;
    import de.fh_dortmund.beerbuddy_44.requests.SaveFriendInvitationRequest;

    /**
     * Created by David on 30.11.2015.
     */
    public  class FriendInvitationDAORemote  extends FriendInvitationDAO {


        public FriendInvitationDAORemote(BeerBuddyActivity context) {
            super(context);
        }

        @Override
    public void insertOrUpdate(FriendInvitation i,  RequestListener<FriendInvitation> listener)  {
            SaveFriendInvitationRequest req = new SaveFriendInvitationRequest(i);
            context.getSpiceManager().execute(req, listener);
    }
        @Override
    public  void getAllFor(long personid,RequestListener<FriendInvitation[]> listener)  {
            GetAllForFriendInvitationRequest req = new GetAllForFriendInvitationRequest(personid);
            context.getSpiceManager().execute(req, listener);
    }
        @Override
    public void getAllFrom(long personid,RequestListener<FriendInvitation[]> listener)  {
            GetAllFromFriendInvitationRequest req = new GetAllFromFriendInvitationRequest(personid);
            context.getSpiceManager().execute(req, listener);
    }
        @Override
    public void accept(final FriendInvitation friendInvitation,RequestListener<Void> listener)  {
        AcceptFriendInvitationRequest req = new AcceptFriendInvitationRequest(friendInvitation);
        context.getSpiceManager().execute(req, listener);
    }
        @Override
    public void decline(FriendInvitation invitation,RequestListener<Void> listener)  {
        DeclineFriendInvitationRequest req = new DeclineFriendInvitationRequest(invitation);
        context.getSpiceManager().execute(req, listener);
    }

}
