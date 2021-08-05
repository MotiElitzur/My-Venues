package moti.venues.view;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDialog;
import androidx.databinding.DataBindingUtil;

import moti.venues.R;
import moti.venues.databinding.AddVenueDialogBinding;
import moti.venues.model.Venue;
import moti.venues.viewModel.IOnVenueAddedListener;

/**
 * This class represent the add venue dialog.
 */
public class AddVenueDialog extends AppCompatDialog {

    // region Data Members

    // The venue added listener.
    private IOnVenueAddedListener _IOnVenueAddedListener;

    // endregion

    // region Constructor

    public AddVenueDialog(Context context, IOnVenueAddedListener listener) {
        super(context);

        // Initialize the added venue listener.
        _IOnVenueAddedListener = listener;
    }

    // endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the dialog layout binding
        AddVenueDialogBinding binding =
                DataBindingUtil.inflate(LayoutInflater.from(getContext()),
                        R.layout.add_venue_dialog, null, false);

        // Set the dialog binding.
        setContentView(binding.getRoot());

        // Add a listener when save button clicked.
        binding.saveVenueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get the new venue details.
                String venueName = binding.addVenueName.getText().toString();
                String venueAddress = binding.addVenueAddress.getText().toString();

                // Check that the user fill the all fields.
                if(venueName.isEmpty() || venueAddress.isEmpty()){

                    // Make a error toast to the user.
                    Toast.makeText(getContext(), getContext().getString(R.string.save_venue_error), Toast.LENGTH_SHORT).show();

                    return;
                } else {

                    // Fire that new venue added.
                    _IOnVenueAddedListener.onVenueAdded(new Venue(venueName, venueAddress));

                    // Close the dialog.
                    dismiss();
                }
            }
        });
    }
}
