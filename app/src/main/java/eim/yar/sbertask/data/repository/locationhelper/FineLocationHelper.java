package eim.yar.sbertask.data.repository.locationhelper;


import android.location.Location;

import eim.yar.sbertask.data.location.CurrentLocationProvider;
import eim.yar.sbertask.data.location.FineCurrentLocationProvider;

/**
 * {@link LocationHelper} implementation based on fine LocationProviders.
 */
public class FineLocationHelper implements LocationHelper {

    /**
     * Object to find current device location.
     */
    private final CurrentLocationProvider currentLocationProvider;

    /**
     * Construct a {@link FineLocationHelper}
     * @param currentLocationProvider object to find fine current device location
     */
    public FineLocationHelper(FineCurrentLocationProvider currentLocationProvider) {
        this.currentLocationProvider = currentLocationProvider;
    }

    @Override
    public void getCurrentLocation(final CurrentLocationCallback currentLocationCallback) {
        currentLocationProvider.findCurrentLocation(
                new CurrentLocationProvider.CurrentLocationCallback() {
            @Override
            public void onCurrentLocationFound(Location currentLocation) {
                currentLocationCallback.onCurrentLocationFound(currentLocation);
            }

            @Override
            public void onError(Exception exception) {
                currentLocationCallback.onError(exception);
            }
        });
    }
}
