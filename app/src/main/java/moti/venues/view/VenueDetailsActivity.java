package moti.venues.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;


import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.api.geocoding.v5.models.GeocodingResponse;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.maps.Style.OnStyleLoaded;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import moti.venues.R;
import moti.venues.databinding.VenueDetailsBinding;
import moti.venues.model.Venue;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * The venue details activity that contains the venue details layout.
 */
public class VenueDetailsActivity extends AppCompatActivity implements
        OnMapReadyCallback, OnStyleLoaded, Callback<GeocodingResponse>   {

    // region Data Members

    // The venue to present.
    private Venue _venue;

    // The map view.
    private MapView _mapView;

    // The map box
    private MapboxMap _mapboxMap;

    // endregion

    // region Life Cycle Methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get map box instance.
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));

        // Get the venue details binding.
        VenueDetailsBinding activityBinding =
                DataBindingUtil.setContentView(this, R.layout.venue_details);

        // Get the venue.
        _venue = (Venue) getIntent().getSerializableExtra(Venue.class.getSimpleName());

        // Set the venue to venue details binding.
        activityBinding.setVenue(_venue);

        // Initialize the map view.
        _mapView = activityBinding.venueMap;
        _mapView.onCreate(savedInstanceState);

        // Start to listen to map ready callback.
        _mapView.getMapAsync(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        _mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        _mapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        _mapView.onDestroy();
    }

    // endregion

    // region MapBox Methods

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {

        _mapboxMap = mapboxMap;

        // Set the map style.
        _mapboxMap.setStyle(Style.MAPBOX_STREETS, this);
    }

    @Override
    public void onStyleLoaded(@NonNull Style style) {

        // Create geocoding with venue address..
        MapboxGeocoding geocoding =
                MapboxGeocoding.builder().accessToken(getString(R.string.mapbox_access_token))
                        .query(_venue.getAddress()).build();

        // Start to listen to geocoding response callback.
        geocoding.enqueueCall(this);
    }

    @Override
    public void onResponse(@NotNull Call<GeocodingResponse> call,
                           @NotNull Response<GeocodingResponse> response) {

        // Get the geocoding response results.
        List<CarmenFeature> results = response.body().features();

        // Check that the address found.
        if (results.size() > 0) {

            // Get the first results Point.
            Point point = results.get(0).center();

            // Get the coordinate of the address location.
            LatLng position = new LatLng(point.latitude(), point.longitude(), point.altitude());

            // Create the camera position of the specific location.
            CameraPosition cameraPosition =
                    new CameraPosition.Builder().target(position).zoom(11).build();

            // Move the map camera to the new camera position.
            _mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            // Draw a beautiful location marker on the address location.
            _mapboxMap.addMarker(new MarkerOptions().position(position));
        }
    }

    @Override
    public void onFailure(Call<GeocodingResponse> call, Throwable throwable) {

        // Print failure stack trace.
        throwable.printStackTrace();
    }

    // endregion
}