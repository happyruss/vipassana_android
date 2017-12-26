package com.guidedmeditationtreks.vipassana;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.guidedmeditationtreks.vipassana.managers.VipassanaManager;
import com.guidedmeditationtreks.vipassana.models.TrackDelegate;

public class MainActivity extends AppCompatActivity implements TrackDelegate {

    public static final String PREFS_NAME = "VipassanaPrefs";
    private VipassanaManager vipassanaManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        vipassanaManager = new VipassanaManager(this, this, settings);

    }

    @Override
    public void trackTimeRemainingUpdated(int timeRemaining) {

    }

    @Override
    public void trackEnded() {

    }
}
