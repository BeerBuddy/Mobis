package de.fh_dortmund.beerbuddy_44.listener.android;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import de.fh_dortmund.beerbuddy.entities.DrinkingInvitation;
import de.fh_dortmund.beerbuddy.entities.DrinkingSpot;
import de.fh_dortmund.beerbuddy_44.R;
import de.fh_dortmund.beerbuddy_44.acitvitys.DrinkingActivity;
import de.fh_dortmund.beerbuddy_44.acitvitys.MainViewActivity;
import de.fh_dortmund.beerbuddy_44.dao.DAOFactory;
import de.fh_dortmund.beerbuddy_44.picker.BuddyPicker;

public class DrinkingListener implements View.OnClickListener {

    private static final String TAG = "DrinkingListener";
    private DrinkingActivity context;

    public DrinkingListener(DrinkingActivity context) {
        this.context = context;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.drinking_group:
                showGroupLayout();
                break;
            case R.id.drinking_alone:
                hideGroupLayout();
                break;
            case R.id.drinking_invite:
                showInviteDialog();
                break;
            case R.id.drinking_save:
                saveDrinkingSpot();
                break;
            case R.id.drinking_deactivate:
                deactivateDrinkingSpot();
                break;
            default:
                Log.e(TAG, "No Action defined for " + v.getId());
                break;
        }
    }

    private void saveDrinkingSpot() {
            DrinkingSpot drinkingSpot = context.getValue();
            DAOFactory.getDrinkingSpotDAO(context).insertOrUpdate(drinkingSpot, new RequestListener<DrinkingSpot>() {
                @Override
                public void onRequestFailure(SpiceException spiceException) {
                    Log.e(TAG, "Error during save of DrinkingSpot " ,spiceException );
                }

                @Override
                public void onRequestSuccess(DrinkingSpot drinkingSpot) {
                    //sending the Invitations
                    final DrinkingInvitation invitation = new DrinkingInvitation();
                    invitation.setDrinkingSpotId(drinkingSpot.getId());
                    invitation.setEinladerId(drinkingSpot.getCreator().getId());
                    invitation.setFreitext("Hey Buddy, join my DrinkingSpot!");

                    if(context.getInvitedPersons() != null) {
                        //for all invited persons
                        int zaehler = 0;
                        while (zaehler < context.getInvitedPersons().length && context.getInvitedPersons()[zaehler] != 0) {
                            invitation.setEingeladenerId(context.getInvitedPersons()[zaehler]);
                            //are there open invitations for the person?
                            DAOFactory.getDrinkingInvitationDAO(context).getAllFor(invitation.getEingeladenerId(), new RequestListener<DrinkingInvitation[]>() {
                                @Override
                                public void onRequestFailure(SpiceException spiceException) {
                                    Log.d(TAG, "Error GettingActive " + invitation.getEingeladenerId());
                                }

                                @Override
                                public void onRequestSuccess(DrinkingInvitation[] oldInvitations) {
                                    //is one of these invitations for the current spot?
                                    boolean notInvited = true;
                                    for (int i = 0; i < oldInvitations.length; i++) {
                                        long oldInvit = oldInvitations[i].getDrinkingSpotId();
                                        long newInvit = invitation.getDrinkingSpotId();
                                        if (oldInvit == newInvit) {
                                            notInvited = false;
                                            break;
                                        }
                                    }
                                    //if there is NO invitation for the current spot, send the invitation
                                    if (notInvited) {
                                        DAOFactory.getDrinkingInvitationDAO(context).insertOrUpdate(invitation, new RequestListener<DrinkingInvitation>() {
                                            @Override
                                            public void onRequestFailure(SpiceException e) {
                                                Log.d(TAG, "Error during sending Invitation", e);
                                            }

                                            @Override
                                            public void onRequestSuccess(DrinkingInvitation drinkingInvitation) {
                                                //do nothing
                                            }
                                        });
                                    }
                                }
                            });
                            zaehler++;
                        }
                    }
                    //Spot saved, change activity
                    context.setInvitedPersons(null);
                    Toast.makeText(context, context.getString(R.string.drinking_saved), Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(context, MainViewActivity.class);
                    context.startActivity(i);
                }
            });
    }

    private void deactivateDrinkingSpot(){
        DrinkingSpot drinkingSpot = context.getValue();
        DAOFactory.getDrinkingSpotDAO(context).deactivate(drinkingSpot.getId(), new RequestListener<Void>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                Log.e(TAG, "Error during save of DrinkingSpot ", spiceException);
            }

            @Override
            public void onRequestSuccess(Void aVoid) {
                Toast.makeText(context, context.getString(R.string.drinking_deactivated), Toast.LENGTH_SHORT).show();
                Intent i = new Intent(context, MainViewActivity.class);
                context.startActivity(i);
            }
        });
    }

    private void showInviteDialog() {
        BuddyPicker.show(context);
    }

    private void showGroupLayout() {
        LinearLayout layout = (LinearLayout) context.findViewById(R.id.drinking_group_layout);
        layout.setVisibility(View.VISIBLE);
    }

    private void hideGroupLayout() {
        LinearLayout layout = (LinearLayout) context.findViewById(R.id.drinking_group_layout);
        layout.setVisibility(View.GONE);
        if(context.getDrinkingSpot() == null){
            context.createDrinkingSpot();
        }
        context.getDrinkingSpot().setAmountMaleWithoutBeerBuddy(0);
        context.getDrinkingSpot().setAmountFemaleWithoutBeerBuddy(0);
        context.getDrinkingSpot().setAgeFrom(0);
        context.getDrinkingSpot().setAgeTo(0);
        ((NumberPicker)context.findViewById(R.id.drinking_group_amount_female)).setValue(context.getDrinkingSpot().getAmountFemaleWithoutBeerBuddy());
        ((NumberPicker)context.findViewById(R.id.drinking_group_amount_male)).setValue(context.getDrinkingSpot().getAmountMaleWithoutBeerBuddy());
        ((NumberPicker) context.findViewById(R.id.drinking_group_age_from)).setValue(context.getDrinkingSpot().getAgeFrom());
        ((NumberPicker) context.findViewById(R.id.drinking_group_age_to)).setValue(context.getDrinkingSpot().getAgeTo());
    }
}
