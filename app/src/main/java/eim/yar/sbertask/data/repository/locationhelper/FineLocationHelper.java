package eim.yar.sbertask.data.repository.locationhelper;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;

import eim.yar.sbertask.data.exception.LocationDeterminationException;
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
     * Object for geocode/reverse geocode.
     */
    private final Geocoder geocoder;

    /**
     * Construct a {@link FineLocationHelper}
     * @param currentLocationProvider object to find fine current device location
     */
    public FineLocationHelper(FineCurrentLocationProvider currentLocationProvider,
                              Geocoder geocoder) {
        this.currentLocationProvider = currentLocationProvider;
        this.geocoder = geocoder;
    }

    @Override
    public void getCurrentLocation(final CurrentLocationCallback currentLocationCallback) {
        CurrentLocationProviderData currentLocationProviderData = new CurrentLocationProviderData();
        synchronized (currentLocationProviderData) {
            try {
                CurrentLocationLooperTask looperTask = new CurrentLocationLooperTask(
                        currentLocationProvider, currentLocationProviderData);
                Executors.defaultThreadFactory().newThread(looperTask).start();
                while (currentLocationProviderData.getCurrentLocation() == null &&
                        currentLocationProviderData.getException() == null) {
                    currentLocationProviderData.wait();
                }
                if (currentLocationProviderData.getCurrentLocation() == null) {
                    currentLocationCallback.onError(currentLocationProviderData.getException());
                } else {
                    currentLocationCallback.onCurrentLocationFound(
                            currentLocationProviderData.getCurrentLocation());
                }
            } catch (InterruptedException exception) {
                currentLocationCallback.onError(exception);
            }
        }
    }

    @Override
    public void reverseGeocode(Location location, ReverseGeocodeCallback callback) {
        try  {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),
                    location.getLongitude(), 10);
            if (addresses == null || addresses.isEmpty()) {
                callback.onError(new LocationDeterminationException(
                        "Reverse geocode not found"));
            } else {
                callback.onReverseGeocodeFound(addresses);
            }
        } catch (IOException exception)  {
            callback.onError(exception);
        }
    }
}
