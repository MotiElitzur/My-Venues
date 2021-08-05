package moti.venues.model;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * This class handles the venues list and get the venues from the DB.
 */
public class VenuesRepository implements EventListener<QuerySnapshot>{

    // region constants

    private static final String VENUES_COLLECTION_NAME = "venues";

    private static final String VENUE_DOCUMENT_ID_START =  "venue_";

    // endregion

    // region Data Members

    // The venues list.
    private List<Venue> _venuesList = new ArrayList<>();

    // The venue live data list.
    private MutableLiveData<List<Venue>> _venuesLiveData =  new MutableLiveData<>();

    // The primary firestore collection.
    private CollectionReference _collectionRef =
            FirebaseFirestore.getInstance().collection(VENUES_COLLECTION_NAME);

    // endregion

    // region Properties

    public MutableLiveData<List<Venue>> getVenuesLiveData() {
        return _venuesLiveData;
    }

    public void setVenuesLiveData(MutableLiveData<List<Venue>> venuesLiveData) {
        this._venuesLiveData = venuesLiveData;
    }

    // endregion

    // region Public Methods

    /**
     * Start to listen the venues database (FireStore).
     */
    public void startListenVenuesDB() {

        // Add changes listener.
        _collectionRef.addSnapshotListener(this);
    }

    /**
     * Receive a venue and add it to venues DB.
     *
     * @param venue The venue to add.
     */
    public void addVenueToDB(Venue venue){

        // Check if the new venue has an id.
        if(venue.getId() == null) {

            // Create the venue id.
            venue.setId(VENUE_DOCUMENT_ID_START + venue.getName());
        }

        // Add the venue to DB.
        _collectionRef.document(venue.getId()).set(venue);
    }

    /**
     * Receive a venue and delete it from venues DB.
     *
     * @param venueToDelete The venue to delete.
     */
    public void deleteVenueFromDb(Venue venueToDelete) {

        // Delete the venue from DB.
        _collectionRef.document(venueToDelete.getId()).delete();
    }


    // endregion

    // region Private Methods

    /**
     * Receive a venue and add it to our venue list.
     *
     * @param venueToAdd The venue to add.
     */
    private void addVenue(Venue venueToAdd){

        // Add the new venue to our venue list.
        _venuesList.add(venueToAdd);

        // Update the venues live list.
        fetchLiveList();
    }

    /**
     * Receive a venue and remove it from our venue list.
     *
     * @param venueToRemove The venue to remove.
     */
    private void removeVenue(Venue venueToRemove) {

        // Remove the venue from our venue list.
        _venuesList.removeIf(venue -> venue.getId().equals(venueToRemove.getId()));

        // Update the venues live list.
        fetchLiveList();
    }

    /**
     * Update the venues live list with our venues list.
     */
    private void fetchLiveList(){

        // Update the venues live list with our venues list.
        _venuesLiveData.postValue(_venuesList);
    }

    // endregion

    // region Collection Event Listener

    @Override
    public void onEvent(@NotNull QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

        // Run for each document that change.
        for (DocumentChange document : value.getDocumentChanges()) {

            // Get changed venue.
            Venue venueToUpdate = document.getDocument().toObject(Venue.class);

            // Check the changes type.
            switch (document.getType()){

                case ADDED:
                    addVenue(venueToUpdate);
                    break;
                case REMOVED:
                    removeVenue(venueToUpdate);
                    break;
                default:
                    break;
            }
        }
    }

    // endregion
}