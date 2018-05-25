package eim.yar.sbertask.data.location;

import android.location.Location;

/**
 * Interface that represents a current location data provider.
 */
public interface CurrentLocationProvider {

    /**
     * Callback to be notified when found a current device location.
     */
    interface CurrentLocationCallback {

        void onCurrentLocationFound(Location currentLocation);

        void onError(Exception exception);
    }

    /**
     * Determine current device location.
     * @param currentLocationCallback {@link CurrentLocationCallback} to handle results
     */
    void findCurrentLocation(CurrentLocationCallback currentLocationCallback);
}
