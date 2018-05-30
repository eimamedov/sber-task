package eim.yar.sbertask.presentation.view.activity;

import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import eim.yar.sbertask.R;
import eim.yar.sbertask.TaskApplication;
import eim.yar.sbertask.databinding.ActivityWeatherBinding;
import eim.yar.sbertask.presentation.viewmodel.WeatherCurrentViewModel;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

/**
 * Main activity of the application.
 */
public class WeatherActivity extends AppCompatActivity {

    /**
     * Code for location permission request.
     */
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

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

    /**
     * On click listener for get current weather for current location button click.
     * @param view the view that was clicked
     */
    void onForCurrentLocationClick(View view) {
        if (ActivityCompat.checkSelfPermission(this,
                ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            weatherCurrentViewModel.onForCurrentLocationClick();
        } else {
            ActivityCompat.requestPermissions(this, new String[] {ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE && grantResults.length == 1
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            weatherCurrentViewModel.onForCurrentLocationClick();
        }
    }
}
