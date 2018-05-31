package eim.yar.sbertask;

import android.app.Application;
import android.location.Geocoder;

import java.util.Locale;

import eim.yar.sbertask.data.entity.mapper.WeatherEntityDataMapper;
import eim.yar.sbertask.data.entity.mapper.WeatherEntityJsonMapper;
import eim.yar.sbertask.data.executor.JobExecutor;
import eim.yar.sbertask.data.location.FineCurrentLocationProvider;
import eim.yar.sbertask.data.net.OWMWeatherApi;
import eim.yar.sbertask.data.repository.WeatherDataRepository;
import eim.yar.sbertask.data.repository.dataloader.OWMWeatherCurrentDataLoader;
import eim.yar.sbertask.data.repository.locationhelper.FineLocationHelper;
import eim.yar.sbertask.domain.interactor.GetCurrentWeatherByAddressUseCase;
import eim.yar.sbertask.domain.interactor.GetCurrentWeatherForCurrentLocationUseCase;
import eim.yar.sbertask.domain.repository.WeatherRepository;
import eim.yar.sbertask.presentation.executor.UIThread;
import eim.yar.sbertask.presentation.viewmodel.WeatherCurrentViewModel;

/**
 * The application class.
 */
public class TaskApplication extends Application {

    /**
     * Repository for retrieving weather data.
     */
    private WeatherRepository weatherRepository;

    /**
     * View model for UI showing current weather data.
     */
    private static WeatherCurrentViewModel weatherCurrentViewModel;

    @Override
    public void onCreate() {
        super.onCreate();
        initWeatherRepository();
        initWeatherCurrentViewModel();
    }

    /**
     * Initialize weather repository.
     */
    private void initWeatherRepository() {
        OWMWeatherCurrentDataLoader owmWeatherCurrentDataLoader =
                new OWMWeatherCurrentDataLoader(new OWMWeatherApi(this,
                        new WeatherEntityJsonMapper()));
        FineLocationHelper fineLocationHelper = new FineLocationHelper(
                new FineCurrentLocationProvider(this),
                new Geocoder(this, Locale.getDefault()));
        weatherRepository = new WeatherDataRepository(owmWeatherCurrentDataLoader,
                fineLocationHelper, new WeatherEntityDataMapper());
    }

    /**
     * Initialize view model for UI showing current weather data.
     */
    private void initWeatherCurrentViewModel() {
        GetCurrentWeatherForCurrentLocationUseCase getCurrentWeatherForCurrentLocationUseCase =
                new GetCurrentWeatherForCurrentLocationUseCase(weatherRepository,
                        JobExecutor.getInstance(), UIThread.getInstance());
        GetCurrentWeatherByAddressUseCase getCurrentWeatherByAddressUseCase =
                new GetCurrentWeatherByAddressUseCase(weatherRepository,
                        JobExecutor.getInstance(), UIThread.getInstance());
        weatherCurrentViewModel = new WeatherCurrentViewModel(
                getCurrentWeatherForCurrentLocationUseCase,
                getCurrentWeatherByAddressUseCase);
    }

    /**
     * Get view model for UI showing current weather data.
     * @return view model for UI showing current weather data
     */
    public static WeatherCurrentViewModel getWeatherCurrentViewModel() {
        return weatherCurrentViewModel;
    }
}