package eim.yar.sbertask.domain.interactor;

import eim.yar.sbertask.domain.executor.PostExecutionThread;
import eim.yar.sbertask.domain.executor.ThreadExecutor;
import eim.yar.sbertask.domain.repository.WeatherRepository;

/**
 * This class is an implementation of {@link GetCurrentWeatherUseCase} that represents a use case
 * for retrieving current weather data for current location.
 */
public class GetCurrentWeatherForCurrentLocationUseCase extends GetCurrentWeatherUseCase
        implements Interactor {

    /**
     * Construct a {@link GetCurrentWeatherForCurrentLocationUseCase}.
     * @param weatherRepository repository for retrieving weather data
     * @param threadExecutor interactor execution thread
     * @param postExecutionThread interactor post execution thread
     */
    public GetCurrentWeatherForCurrentLocationUseCase(WeatherRepository weatherRepository,
            ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        super(weatherRepository, threadExecutor, postExecutionThread);
    }

    /**
     * Executes this use case.
     * @param callback A {@link GetCurrentWeatherUseCase.Callback} used for notify the client.
     */
    public void execute(Callback callback) {
        initCallback(callback);
        threadExecutor.execute(this);
    }

    @Override
    public void run() {
        weatherRepository.getCurrentWeatherForCurrentLocation(repositoryCallback);
    }
}
