package eim.yar.sbertask.presentation.viewmodel;

import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.view.View;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import eim.yar.sbertask.domain.interactor.GetCurrentWeatherForCurrentLocationUseCase;
import eim.yar.sbertask.domain.interactor.GetCurrentWeatherUseCase;
import eim.yar.sbertask.domain.model.WeatherCurrent;

/**
 * View model to work with UI showing current weather data.
 */
@SuppressWarnings("PMD.DataClass")
public class WeatherCurrentViewModel {

    final ObservableField<String> address = new ObservableField<>();

    final ObservableField<String> latitude = new ObservableField<>();

    final ObservableField<String> longitude = new ObservableField<>();

    final ObservableField<String> datetime = new ObservableField<>();

    final ObservableField<String> temperature = new ObservableField<>();

    final ObservableInt progressVisibility = new ObservableInt();

    final ObservableField<String> error = new ObservableField<>();

    /**
     * Get current weather data for current location use case.
     */
    final GetCurrentWeatherForCurrentLocationUseCase currentWeatherForCurrentLocationUseCase;

    /**
     * Construct a {@link WeatherCurrentViewModel}
     * @param currentWeatherForCurrentLocationUseCase use case to get current weather data for
     * current location.
     */
    public WeatherCurrentViewModel(
            GetCurrentWeatherForCurrentLocationUseCase currentWeatherForCurrentLocationUseCase) {
        this.currentWeatherForCurrentLocationUseCase = currentWeatherForCurrentLocationUseCase;
        progressVisibility.set(View.INVISIBLE);
    }

    public ObservableField<String> getAddress() {
        return address;
    }

    public ObservableField<String> getLatitude() {
        return latitude;
    }

    public ObservableField<String> getLongitude() {
        return longitude;
    }

    public ObservableField<String> getDatetime() {
        return datetime;
    }

    public ObservableField<String> getTemperature() {
        return temperature;
    }

    public ObservableInt getProgressVisibility() {
        return progressVisibility;
    }

    public ObservableField<String> getError() {
        return error;
    }

    public void onForCurrentLocationClick() {
        setProgressBarVisible(true);
        currentWeatherForCurrentLocationUseCase.execute(
            new GetCurrentWeatherUseCase.Callback() {
                @Override
                public void onCurrentWeatherLoaded(WeatherCurrent weatherCurrent) {
                    setProgressBarVisible(currentWeatherForCurrentLocationUseCase
                            .getThreadExecutor().isRunning());
                    updateWeatherCurrentDataFields(weatherCurrent);
                }

                @Override
                public void onError(Exception exception) {
                    setProgressBarVisible(currentWeatherForCurrentLocationUseCase
                            .getThreadExecutor().isRunning());
                    updateErrorMessageField(exception);
                }
            });
    }

    void setProgressBarVisible(boolean visible) {
        progressVisibility.set(visible ? View.VISIBLE : View.INVISIBLE);
    }

    void updateWeatherCurrentDataFields(WeatherCurrent weatherCurrent) {
        address.set(weatherCurrent.getAddress());
        latitude.set(Double.toString(weatherCurrent.getLatitude()));
        longitude.set(Double.toString(weatherCurrent.getLongitude()));
        DateFormat dateFormat = SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.MEDIUM,
                SimpleDateFormat.MEDIUM, Locale.getDefault());
        datetime.set(dateFormat.format(new Date(weatherCurrent.getTimestamp())));
        temperature.set(Double.toString(weatherCurrent.getTemperature()));
    }

    void updateErrorMessageField(Exception exception) {
        if (exception.getMessage().equals(error.get())) {
            error.notifyChange();
        } else {
            error.set(exception.getMessage());
        }
    }
}
