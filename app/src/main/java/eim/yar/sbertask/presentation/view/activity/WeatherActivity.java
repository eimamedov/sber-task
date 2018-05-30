package eim.yar.sbertask.presentation.view.activity;

import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import eim.yar.sbertask.R;
import eim.yar.sbertask.TaskApplication;
import eim.yar.sbertask.databinding.ActivityWeatherBinding;
import eim.yar.sbertask.presentation.viewmodel.WeatherCurrentViewModel;

/**
 * Main activity of the application.
 */
public class WeatherActivity extends AppCompatActivity {

    /**
     * View model for work with current weather data.
     */
    WeatherCurrentViewModel weatherCurrentViewModel;

    /**
     * Callback for current weather data view model error field change.
     */
    private final Observable.OnPropertyChangedCallback errorChangedCallback =
            new Observable.OnPropertyChangedCallback() {
                @Override
                public void onPropertyChanged(Observable sender, int propertyId) {
                    Toast.makeText(getApplicationContext(), weatherCurrentViewModel
                            .getError().get(), Toast.LENGTH_SHORT).show();
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        initViewModel();
    }

    @Override
    protected void onDestroy() {
        weatherCurrentViewModel.getError().removeOnPropertyChangedCallback(errorChangedCallback);
        super.onDestroy();
    }

    /**
     * Initialize view model.
     */
    private void initViewModel() {
        weatherCurrentViewModel = TaskApplication.getWeatherCurrentViewModel();
        weatherCurrentViewModel.getError().addOnPropertyChangedCallback(errorChangedCallback);
        ActivityWeatherBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_weather);
        binding.setWeatherCurrent(weatherCurrentViewModel);
    }
}
