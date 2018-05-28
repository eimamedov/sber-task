package eim.yar.sbertask.domain.interactor;

import eim.yar.sbertask.domain.executor.PostExecutionThread;
import eim.yar.sbertask.domain.executor.ThreadExecutor;
import eim.yar.sbertask.domain.model.WeatherCurrent;
import eim.yar.sbertask.domain.repository.WeatherRepository;

/**
 * This class represents a execution unit for a use case to get current weather data.
 * By convention this use case ({@link Interactor}) implementation will return the result using a
 * callback that should be executed in the UI thread.
 */
public abstract class GetCurrentWeatherUseCase {

    /**
     * Repository for retrieving weather data.
     */
    protected final WeatherRepository weatherRepository;

    /**
     * Use case execution thread.
     */
    protected final ThreadExecutor threadExecutor;

    /**
     * Use case post execution thread.
     */
    protected final PostExecutionThread postExecutionThread;

    /**
     * Callback used to be notified when either a current weather data has been loaded
     * or an error happened.
     */
    protected Callback callback;

    /**
     * Construct a {@link GetCurrentWeatherUseCase}.
     * @param weatherRepository repository for retrieving weather data
     * @param threadExecutor use case execution thread
     * @param postExecutionThread use case post execution thread
     */
    public GetCurrentWeatherUseCase(WeatherRepository weatherRepository,
                                    ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        if (weatherRepository == null || threadExecutor == null || postExecutionThread == null) {
            throw new IllegalArgumentException("Constructor parameters cannot be null!!!");
        }
        this.weatherRepository = weatherRepository;
        this.threadExecutor = threadExecutor;
        this.postExecutionThread = postExecutionThread;
        callback = null;
    }

    /**
     * Initialize get current weather use case callback.
     * @param callback get current weather use case callback
     */
    protected void initCallback(Callback callback) {
        if (callback == null) {
            throw new IllegalArgumentException("Interactor callback cannot be null!!!");
        }
        this.callback = callback;
    }

    /**
     * Callback to handle weather repository data retrieving results.
     */
    protected final WeatherRepository.WeatherCurrentCallback repositoryCallback =
            new WeatherRepository.WeatherCurrentCallback() {
                @Override
                public void onCurrentWeatherLoaded(WeatherCurrent weatherCurrent) {
                    notifyGetCurrentWeatherSuccessfully(weatherCurrent);
                }

                @Override
                public void onError(Exception exception) {
                    notifyError(exception);
                }
            };

    /**
     * Notify when current weather data was successfully retrieved.
     * @param weatherCurrent current weather data
     */
    protected void notifyGetCurrentWeatherSuccessfully(final WeatherCurrent weatherCurrent) {
        this.postExecutionThread.post(() -> callback.onCurrentWeatherLoaded(weatherCurrent));
    }

    /**
     * Notify when there is an error during current weather data retrieving.
     * @param exception occurred error
     */
    protected void notifyError(final Exception exception) {
        this.postExecutionThread.post(() -> callback.onError(exception));
    }

    /**
     * Callback used to be notified when either a current weather data has been loaded
     * or an error happened.
     */
    public interface Callback {
        void onCurrentWeatherLoaded(WeatherCurrent weatherCurrent);

        void onError(Exception exception);
    }
}
