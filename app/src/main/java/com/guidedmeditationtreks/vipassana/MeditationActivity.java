package com.guidedmeditationtreks.vipassana;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.guidedmeditationtreks.vipassana.managers.VipassanaManager;
import com.guidedmeditationtreks.vipassana.models.TrackDelegate;

public class MeditationActivity extends AppCompatActivity implements TrackDelegate {

    private ToggleButton playPauseButton;
    private TextView timerTextView;
    private TextView meditationNameTextView;
    public static final String PREFS_NAME = "VipassanaPrefs";
    private boolean isInMeditation = false;
    private VipassanaManager vipassanaManager = VipassanaManager.singleton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meditation);
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        vipassanaManager.setDelegate(this);
        vipassanaManager.setSettings(settings);
        vipassanaManager.setContext(this);
        connectView();
        Intent intent = getIntent();
        int gapAmount =  intent.getIntExtra("gapAmount", 0);
        vipassanaManager.playActiveTrackFromBeginning(gapAmount);
        isInMeditation = true;
    }

    private void closeActivity() {
        vipassanaManager.setDelegate(null);
        vipassanaManager.setSettings(null);
        vipassanaManager.setContext(null);
        finish();
    }

    public  void didTapBackButton(View v) {
        if (isInMeditation) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Meditation Underway");
            alertDialogBuilder
                    .setMessage("Would you like to stop the current session?")
                    .setCancelable(false)
                    .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            closeActivity();
                        }
                    })
                    .setNegativeButton("No",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        } else {
            closeActivity();
        }
    }

    private void connectView() {
        playPauseButton = findViewById(R.id.playPauseButton);
        playPauseButton.setVisibility(View.VISIBLE);
        timerTextView = findViewById(R.id.timerTextView);
        meditationNameTextView = findViewById(R.id.meditationNameTextView);
        meditationNameTextView.setText(vipassanaManager.getActiveTrackName());
    }

    public void didTapPlayPause(View v) {
        vipassanaManager.pauseOrResume();
    }

    @Override
    public void trackTimeRemainingUpdated(int timeRemaining) {
        String timeRemainingString = String.format("%02d:%02d", timeRemaining / 60, ((timeRemaining % 3600) % 60));
        timerTextView.setText(timeRemainingString);
    }

    @Override
    public void trackEnded() {
        vipassanaManager.userCompletedTrack();
        playPauseButton.setVisibility(View.INVISIBLE);
        isInMeditation = false;
    }


}
