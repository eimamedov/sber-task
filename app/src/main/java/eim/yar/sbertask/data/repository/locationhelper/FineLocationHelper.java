package eim.yar.sbertask.data.repository.locationhelper;

import java.util.concurrent.Executors;

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
}
