package eim.yar.sbertask.data.repository.locationhelper;

import android.location.Address;
import android.location.Location;

import java.util.List;

/**
 * Interface that represents a location data helper.
 */
public interface LocationHelper {

    /**
     * Callback to be notified when found a current device location.
     */
    interface CurrentLocationCallback {

        void onCurrentLocationFound(Location currentLocation);

        void onError(Exception exception);
    }

    /**
     * Get current device location.
     * @param currentLocationCallback {@link CurrentLocationCallback} to handle results
     */
    void getCurrentLocation(CurrentLocationCallback currentLocationCallback);


    /**
     * Callback to be notified when reverse geocode results found for a location.
     */
    interface ReverseGeocodeCallback {

        void onReverseGeocodeFound(List<Address> addresses);

        void onError(Exception exception);
    }

    /**
     * Reverse geocode for a location.
     * @param location a location to find addresses for
     * @param callback {@link ReverseGeocodeCallback} to handle results
     */
    void reverseGeocode(Location location, ReverseGeocodeCallback callback);
}
