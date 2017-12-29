package com.guidedmeditationtreks.vipassana;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.guidedmeditationtreks.vipassana.managers.VipassanaManager;
import com.guidedmeditationtreks.vipassana.models.TrackDelegate;
import com.guidedmeditationtreks.vipassana.models.TrackTemplate;

public class MainActivity extends AppCompatActivity implements TrackDelegate {

    public static final String PREFS_NAME = "VipassanaPrefs";
    private VipassanaManager vipassanaManager;

    private TextView countdownLabel;
    private ToggleButton playPauseButton;
    private ToggleButton introButton;
    private ToggleButton timerButton;
    private ToggleButton shamathaButton;
    private ToggleButton anapanaButton;
    private ToggleButton focusedAnapanaButton;
    private ToggleButton topToBottomVipassanaButton;
    private ToggleButton sweepingVipassanaButton;
    private ToggleButton symmetricalVipassanaButton;
    private ToggleButton scanningVipassanaButton;
    private ToggleButton inTheMomentVipassanaButton;
    private ToggleButton mettaButton;

    public  void didTapMeditationButton(View v) {
//        int trackLevel = v.getId();
        presentCountdownLengthAlertOrRun(0);
    }

    private void secureButtons() {
        int enabledLevel = vipassanaManager.getUserCompletedTrackLevel() + 1;
        introButton.setEnabled(true);
        introButton.setEnabled(true);
        timerButton.setEnabled(true);
        shamathaButton.setEnabled(enabledLevel >= 1);
        anapanaButton.setEnabled( enabledLevel >= 2);
        focusedAnapanaButton.setEnabled( enabledLevel >= 3);
        topToBottomVipassanaButton.setEnabled( enabledLevel >= 4);
        sweepingVipassanaButton.setEnabled( enabledLevel >= 5);
        symmetricalVipassanaButton.setEnabled( enabledLevel >= 6);
        scanningVipassanaButton.setEnabled( enabledLevel >= 7);
        inTheMomentVipassanaButton.setEnabled( enabledLevel >= 8);
        mettaButton.setEnabled(enabledLevel >= 9);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        vipassanaManager = new VipassanaManager(this, this, settings);
        this.secureButtons();
    }

    @Override
    public void trackTimeRemainingUpdated(int timeRemaining) {

    }

    @Override
    public void trackEnded() {

    }

    private void runMeditationWithGap(int gapAmount) {
        vipassanaManager.playTrackFromBeginning(gapAmount);
        playPauseButton.setVisibility(View.VISIBLE);
    }

    private void presentCountdownLengthAlertOrRun(int trackLevel) {
        vipassanaManager.initTrackAtLevel(trackLevel);
        int minDurationSeconds = vipassanaManager.getMinimumDuration();
        int minDurationMinutes = minDurationSeconds / 60 + 2;

        if (!vipassanaManager.isMultiPart()) {
            this.runMeditationWithGap(0);
        } else {
            //present the popup


        }
    }
}
