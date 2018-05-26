package eim.yar.sbertask.data.repository.locationhelper;

import android.location.Location;

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
}
