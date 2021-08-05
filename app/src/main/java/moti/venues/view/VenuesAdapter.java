package moti.venues.view;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import moti.venues.BR;
import moti.venues.R;
import moti.venues.model.Venue;
import moti.venues.viewModel.VenuesViewModel;

/**
 * This class contains the venues recycler view.
 */
public class VenuesAdapter extends RecyclerView.Adapter<VenuesAdapter.VenueViewHolder> {

    // region Data Members

    // The venues view model.
    private VenuesViewModel _venuesViewModel;

    // The venues list.
    private MutableLiveData<List<Venue>> _venueList;

    // endregion

    // region Constructor

    public VenuesAdapter(VenuesViewModel venuesViewModel) {

        _venuesViewModel = venuesViewModel;
        _venueList = venuesViewModel.getVenuesList();
    }

    // endregion

    // region RecyclerView Adapter

    @Override
    public int getItemCount() {
        return _venueList == null || _venueList.getValue() == null ? 0 : _venueList.getValue().size();
    }

    @Override
    public void onBindViewHolder(@NonNull VenueViewHolder holder, int position) {
        holder.bind(_venueList.getValue().get(position), _venuesViewModel);
    }

    public VenueViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // Get the venue view binding.
        ViewDataBinding binding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.venue_view, parent, false);

        return new VenueViewHolder(binding);
    }

    // endregion

    // region Venue View Holder

    /**
     * The venue view holder to bind the venue in venue view.
     */
    public class VenueViewHolder extends RecyclerView.ViewHolder {

        // region Data Members

        // The venue binding.
        private final ViewDataBinding binding;

        // endregion

        // region Constructor

        public VenueViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        // endregion

        // region Public Methods

        /**
         * Receive a venue and bind it to venue view binding.
         *
         * @param venue The venue to bind.
         *
         * @param venuesViewModel VenuesViewModel, to do more action from venues view binding.
         */
        public void bind(Venue venue, VenuesViewModel venuesViewModel) {

            // bind the venue.
            binding.setVariable(BR.venue, venue);
            binding.setVariable(BR.venuesViewModel, venuesViewModel);

            binding.executePendingBindings();
        }

        // endregion
    }

    // endregion
}