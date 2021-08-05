package moti.venues.viewModel;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import moti.venues.R;
import moti.venues.model.Venue;
import moti.venues.model.VenuesRepository;
import moti.venues.view.AddVenueDialog;
import moti.venues.view.VenueDetailsActivity;

/**
 *  The venues view model that hold the venues list and operations when it's change .
 */
public class VenuesViewModel extends ViewModel implements IOnVenueAddedListener {

    // region Data Members

    // The main context.
    private Context _context;

    // The venues list.
    private MutableLiveData<List<Venue>> _venuesList;

    // The venue repository.
    private VenuesRepository _venuesRepository;

    // endregion

    // region Constructor

    public VenuesViewModel(Context context){

        _context = context;
        _venuesRepository = new VenuesRepository();

        // Get the venues live data list.
        _venuesList = _venuesRepository.getVenuesLiveData();
    }

    // endregion

    // region Properties

    public MutableLiveData<List<Venue>> getVenuesList() {
        return _venuesList;
    }

    public void setVenuesList(MutableLiveData<List<Venue>> venuesList) {
        this._venuesList = venuesList;
    }

    // endregion

    // region Public Methods

    /**
     * Start to read the venues from the DB.
     */
    public void startReadDBVenues(){

         // Call the venues repository to start to the venues from the DB.
        _venuesRepository.startListenVenuesDB();
    }

    /**
     * This function called when a venue pressed and open an activity with venue details.
     *
     * @param venue The venue that pressed.
     */
    public void onVenuePressed(Venue venue){

        // Pass the venue to venue details activity.
        Intent intent = new Intent(_context, VenueDetailsActivity.class);
        intent.putExtra(Venue.class.getSimpleName(), venue);

        // start venue details activity.
        _context.startActivity(intent);
    }

    /**
     * This function called when a venue pressed long click,
     * and show the delete venue dialog.
     *
     * @param venueToDelete The venue to delete.
     *
     * @return Long click value.
     */
    public boolean onVenueLongClicked(Venue venueToDelete) {

        // Create a dialog click listener.
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Check if the user pressed on positive button.
                if(which == DialogInterface.BUTTON_POSITIVE){

                    // Delete the venue from venues DB.
                    _venuesRepository.deleteVenueFromDb(venueToDelete);
                }
            }
        };

        // Create the delete venue alert dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(_context);

        builder.setTitle(_context.getString(R.string.delete_venue_title));
        builder.setMessage(R.string.delete_venue_message);
        builder.setPositiveButton(android.R.string.yes, dialogClickListener);
        builder.setNegativeButton(android.R.string.no, dialogClickListener).show();

        return false;
    }

    /**
     * This function called when a add venue button pressed and open the add venue dialog.
     */
    public void onAddVenuePressed(){

        // Open the add venue dialog.
        new AddVenueDialog(_context, this).show();
    }

    // endregion

    // region On Venue Added Listener

    @Override
    public void onVenueAdded(Venue venue) {

        // Add the venue to DB.
        _venuesRepository.addVenueToDB(venue);
    }

    // endregion
}