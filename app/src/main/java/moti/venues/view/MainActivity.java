package moti.venues.view;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.Observer;

import java.util.List;

import moti.venues.R;
import moti.venues.databinding.ActivityMainBinding;
import moti.venues.model.Venue;
import moti.venues.viewModel.VenuesViewModel;

/**
 * The main app activity with the venues list.
 */
public class MainActivity extends AppCompatActivity {

    // region Data Members

    // The main activity binding.
    private ActivityMainBinding _mainBinding;

    // The venues adapter.
    private VenuesAdapter _venuesAdapter;

    // The loading visibility status.
    private ObservableInt _loadingVisibility = new ObservableInt(View.VISIBLE);

    // endregion

    // region Life Cycle Methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the main binding from main layout.
        _mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        // Initialize the activity.
        initVenues();
    }

    // endregion

    // region Private Methods

    /**
     * Initialize the activity with the view model.
     */
    private void initVenues(){

        VenuesViewModel venuesViewModel = new VenuesViewModel(this);

        // Set the view model to the adapter.
        _venuesAdapter = new VenuesAdapter(venuesViewModel);

        _mainBinding.setVenuesViewModel(venuesViewModel);

        // Add the venues adapter to the main activity.
        _mainBinding.setVenuesAdapter(_venuesAdapter);

        // Add the loading visibility status to the main activity.
        _mainBinding.setLoadingVisibility(_loadingVisibility);

        // Start listening when the venue live list updated.
        venuesViewModel.getVenuesList().observe(this, new Observer<List<Venue>>() {
            @Override
            public void onChanged(List<Venue> venues) {

                // Update the loading visibility.
                _loadingVisibility.set(venues.size() > 0 ? View.GONE : View.VISIBLE);

                // Notify the venues adapter that the venues list updated.
                _venuesAdapter.notifyDataSetChanged();
            }
        });

        // Start to read venues from the DB.
        venuesViewModel.startReadDBVenues();
    }

    // endregion
}