package eim.yar.sbertask.data.repository.locationhelper;

import android.location.Location;
import android.os.Looper;

import eim.yar.sbertask.data.executor.LooperTask;
import eim.yar.sbertask.data.location.CurrentLocationProvider;

/**
 * Looper task to get current location data.
 */
class CurrentLocationLooperTask extends LooperTask implements Runnable {

    /**
     * Object to find current device location.
     */
    final CurrentLocationProvider currentLocationProvider;

    /**
     * Object to notify current location provider callback data.
     */
    final CurrentLocationProviderData currentLocationProviderData;

    /**
     * Construct a {@link CurrentLocationLooperTask}.
     * @param currentLocationProvider object to find current device location
     * @param currentLocationProviderData object to notify current location provider
     * callback data
     */
    CurrentLocationLooperTask(CurrentLocationProvider currentLocationProvider,
                                     CurrentLocationProviderData currentLocationProviderData) {
        this.currentLocationProvider = currentLocationProvider;
        this.currentLocationProviderData = currentLocationProviderData;
    }

    @Override
    public void run() {
        synchronized (currentLocationProviderData) {
            prepareLooper();
            currentLocationProvider.findCurrentLocation(
                    new CurrentLocationProvider.CurrentLocationCallback() {
                        @Override
                        public void onCurrentLocationFound(Location currentLocation) {
                            currentLocationProviderData.setCurrentLocation(currentLocation);
                            quitLooper();
                            currentLocationProviderData.notifyAll();
                        }

                        @Override
                        public void onError(Exception exception) {
                            currentLocationProviderData.setException(exception);
                            quitLooper();
                            currentLocationProviderData.notifyAll();
                        }
                    });
            Looper.loop();
        }
    }
}
