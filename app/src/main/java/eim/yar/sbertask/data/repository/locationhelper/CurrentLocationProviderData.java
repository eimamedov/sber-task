package eim.yar.sbertask.data.repository.locationhelper;

import android.location.Location;

import eim.yar.sbertask.data.location.CurrentLocationProvider;

/**
 * Class to store {@link CurrentLocationProvider} callback data.
 */
@SuppressWarnings("PMD.DataClass")
class CurrentLocationProviderData {

    private Location currentLocation;

    private Exception exception;

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }
}
