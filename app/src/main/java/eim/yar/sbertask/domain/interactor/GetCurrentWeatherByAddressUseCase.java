package eim.yar.sbertask.domain.interactor;

import eim.yar.sbertask.domain.executor.PostExecutionThread;
import eim.yar.sbertask.domain.executor.ThreadExecutor;
import eim.yar.sbertask.domain.repository.WeatherRepository;

/**
 * This class is an implementation of {@link GetCurrentWeatherUseCase} that represents a use case
 * for retrieving current weather data by address.
 */
public class GetCurrentWeatherByAddressUseCase extends GetCurrentWeatherUseCase
        implements Interactor {

    /**
     * String with address for looking for.
     */
    private String address;

    /**
     * Construct a {@link GetCurrentWeatherByAddressUseCase}.
     * @param weatherRepository repository for retrieving weather data
     * @param threadExecutor interactor execution thread
     * @param postExecutionThread interactor post execution thread
     */
    public GetCurrentWeatherByAddressUseCase(WeatherRepository weatherRepository,
                                             ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        super(weatherRepository, threadExecutor, postExecutionThread);
    }

    /**
     * Executes this use case.
     * @param address string with address for looking for
     * @param callback A {@link Callback} used for notify the client
     */
    public void execute(String address, Callback callback) {
        initCallback(callback);
        this.address = address;
        threadExecutor.execute(this);
    }

    @Override
    public void run() {
        weatherRepository.getCurrentWeatherByAddress(address, repositoryCallback);
    }
}
