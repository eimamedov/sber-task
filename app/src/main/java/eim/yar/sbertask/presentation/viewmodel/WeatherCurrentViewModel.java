package eim.yar.sbertask.presentation.viewmodel;

import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.view.View;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import eim.yar.sbertask.domain.interactor.GetCurrentWeatherByAddressUseCase;
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

    final ObservableField<String> addressSearch = new ObservableField<>();

    final ObservableField<String> error = new ObservableField<>();

    /**
     * Get current weather data for current location use case.
     */
    final GetCurrentWeatherForCurrentLocationUseCase currentWeatherForCurrentLocationUseCase;

    /**
     * Get current weather data by address use case.
     */
    final GetCurrentWeatherByAddressUseCase currentWeatherByAddressUseCase;

    /**
     * Construct a {@link WeatherCurrentViewModel}
     * @param currentWeatherForCurrentLocationUseCase use case to get current weather data for
     * current location
     * @param currentWeatherByAddressUseCase use case to get current weather data for by address
     */
    public WeatherCurrentViewModel(
            GetCurrentWeatherForCurrentLocationUseCase currentWeatherForCurrentLocationUseCase,
            GetCurrentWeatherByAddressUseCase currentWeatherByAddressUseCase) {
        this.currentWeatherForCurrentLocationUseCase = currentWeatherForCurrentLocationUseCase;
        this.currentWeatherByAddressUseCase = currentWeatherByAddressUseCase;
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

    public ObservableField<String> getAddressSearch() {
        return addressSearch;
    }

    public ObservableField<String> getError() {
        return error;
    }

    public void onForCurrentLocationClick() {
        addressSearch.set("");
        setProgressBarVisible(true);
        currentWeatherForCurrentLocationUseCase.execute(
                createUseCaseCallback(currentWeatherForCurrentLocationUseCase));
    }

    public void onByAddressSearchClick(View view) {
        setProgressBarVisible(true);
        currentWeatherByAddressUseCase.execute(addressSearch.get(),
                createUseCaseCallback(currentWeatherByAddressUseCase));
    }

    private GetCurrentWeatherUseCase.Callback createUseCaseCallback(
            final GetCurrentWeatherUseCase useCase) {
        return new GetCurrentWeatherUseCase.Callback() {
            @Override
            public void onCurrentWeatherLoaded(WeatherCurrent weatherCurrent) {
                setProgressBarVisible(useCase.getThreadExecutor().isRunning());
                updateWeatherCurrentDataFields(weatherCurrent);
            }

            @Override
            public void onError(Exception exception) {
                setProgressBarVisible(useCase.getThreadExecutor().isRunning());
                updateErrorMessageField(exception);
            }
        };
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
