package com.guidedmeditationtreks.vipassana;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.guidedmeditationtreks.vipassana.managers.VipassanaManager;
import com.guidedmeditationtreks.vipassana.models.TrackDelegate;

import java.util.Locale;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MeditationActivity extends AppCompatActivity implements TrackDelegate {

    private ToggleButton playPauseButton;
    private TextView timerTextView;
    private TextView meditationNameTextView;
    private boolean isInMeditation = false;
    private VipassanaManager vipassanaManager = VipassanaManager.singleton;
    private View clearClickBackgroundView;
    private View blackClickBackgroundView;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meditation);
        vipassanaManager.setDelegate(this);
        connectView();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/sf-pro-text-semibold.otf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        if (!vipassanaManager.getInMeditation()) { //workaround for screen rotation
            Intent intent = getIntent();
            int gapAmount =  intent.getIntExtra("gapAmount", 0);
            vipassanaManager.playActiveTrackFromBeginning(gapAmount);
            vipassanaManager.setInMeditation(true);
        }
        if (!vipassanaManager.getTrackCompleted()) {
            isInMeditation = true;
            vipassanaManager.userStartedTrack();
        }
    }

    private void closeActivity() {
        vipassanaManager.clearCurrentTrack();
        finish();
    }

    @Override
    public void onBackPressed()
    {
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

    public void didTapBackButton(View v) {
        this.onBackPressed();
    }

    private void connectView() {
        playPauseButton = findViewById(R.id.playPauseButton);
        playPauseButton.setVisibility(View.VISIBLE);
        timerTextView = findViewById(R.id.timerTextView);
        meditationNameTextView = findViewById(R.id.meditationNameTextView);
        meditationNameTextView.setText(vipassanaManager.getActiveTrackLongName());
        meditationNameTextView.setShadowLayer(2.0f, 2.0f, 2.0f, R.color.colorShadow);
        clearClickBackgroundView = findViewById(R.id.clearClickBackground);
        blackClickBackgroundView = findViewById(R.id.blackClickBackground);

        clearClickBackgroundView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                blackClickBackgroundView.setVisibility(View.VISIBLE);
            }
        });

        blackClickBackgroundView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                blackClickBackgroundView.setVisibility(View.INVISIBLE);
            }
        });

        blackClickBackgroundView.setZ(50);
        playPauseButton.setZ(20);

    }

    public void didTapPlayPause(View v) {
        vipassanaManager.pauseOrResume();
    }

    @Override
    public void trackTimeRemainingUpdated(int timeRemaining) {
        vipassanaManager.incrementTotalSecondsInMeditation();
        String timeRemainingString = String.format(Locale.getDefault(), "%01d:%02d", timeRemaining / 60, ((timeRemaining % 3600) % 60));
        timerTextView.setText(timeRemainingString);
    }

    @Override
    public void trackEnded() {
        vipassanaManager.userCompletedTrack();
        playPauseButton.setVisibility(View.INVISIBLE);
        isInMeditation = false;
        vipassanaManager.setTrackCompleted(true);
        closeActivity();
    }


}
