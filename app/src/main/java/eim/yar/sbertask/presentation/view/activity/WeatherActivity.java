package eim.yar.sbertask.presentation.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import eim.yar.sbertask.R;
import eim.yar.sbertask.data.entity.mapper.WeatherEntityDataMapper;
import eim.yar.sbertask.data.entity.mapper.WeatherEntityJsonMapper;
import eim.yar.sbertask.data.executor.JobExecutor;
import eim.yar.sbertask.data.location.FineCurrentLocationProvider;
import eim.yar.sbertask.data.net.OWMWeatherApi;
import eim.yar.sbertask.data.repository.WeatherDataRepository;
import eim.yar.sbertask.data.repository.dataloader.OWMWeatherCurrentDataLoader;
import eim.yar.sbertask.data.repository.locationhelper.FineLocationHelper;
import eim.yar.sbertask.domain.interactor.GetCurrentWeatherForCurrentLocationUseCase;
import eim.yar.sbertask.domain.interactor.GetCurrentWeatherUseCase;
import eim.yar.sbertask.domain.model.WeatherCurrent;
import eim.yar.sbertask.presentation.executor.UIThread;

/**
 * Main activity of the application.
 */
public class WeatherActivity extends AppCompatActivity {

    private GetCurrentWeatherForCurrentLocationUseCase currentWeatherForCurrentLocationUseCase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        initCurrentWeatherForCurrentLocationUseCase();
        initRefreshButton();
    }

    protected void updateView(int progressVisibility, String address, String latitude,
                            String longitude, String temperature) {
        findViewById(R.id.progress).setVisibility(progressVisibility);
        ((TextView) findViewById(R.id.address)).setText(address);
        ((TextView) findViewById(R.id.latitude)).setText(latitude);
        ((TextView) findViewById(R.id.longitude)).setText(longitude);
        ((TextView) findViewById(R.id.temperature)).setText(temperature);
    }

    private void initCurrentWeatherForCurrentLocationUseCase() {
        OWMWeatherCurrentDataLoader owmWeatherCurrentDataLoader =
                new OWMWeatherCurrentDataLoader(new OWMWeatherApi(this,
                        new WeatherEntityJsonMapper()));
        FineLocationHelper fineLocationHelper = new FineLocationHelper(
                new FineCurrentLocationProvider(this));
        WeatherDataRepository repository = new WeatherDataRepository(owmWeatherCurrentDataLoader,
                fineLocationHelper, new WeatherEntityDataMapper());
        currentWeatherForCurrentLocationUseCase = new GetCurrentWeatherForCurrentLocationUseCase(
                repository, JobExecutor.getInstance(), UIThread.getInstance());
    }

    private void initRefreshButton() {
        findViewById(R.id.refresh_btn).setOnClickListener(v -> {
            updateView(View.VISIBLE, "-", "-", "-", "-");
            currentWeatherForCurrentLocationUseCase.execute(
                    new GetCurrentWeatherUseCase.Callback() {
                        @Override
                        public void onCurrentWeatherLoaded(WeatherCurrent weatherCurrent) {
                            updateView(View.INVISIBLE, weatherCurrent.getAddress(),
                                    Double.toString(weatherCurrent.getLatitude()),
                                    Double.toString(weatherCurrent.getLongitude()),
                                    Double.toString(weatherCurrent.getTemperature()));
                        }

                        @Override
                        public void onError(Exception exception) {
                            updateView(View.INVISIBLE, exception.getMessage(), "-", "-", "-");
                        }
                    });
        });
    }
}
