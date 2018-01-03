package com.guidedmeditationtreks.vipassana;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.guidedmeditationtreks.vipassana.managers.VipassanaManager;
import com.guidedmeditationtreks.vipassana.models.TrackDelegate;

import org.w3c.dom.Text;

import java.text.ParseException;

public class MainActivity extends AppCompatActivity implements TrackDelegate {

    public static final String PREFS_NAME = "VipassanaPrefs";
    private VipassanaManager vipassanaManager;

    private Button introButton;
    private ToggleButton playPauseButton;
    private TextView timerTextView;
    private Button timerButton;
    private Button shamathaButton;
    private Button anapanaButton;
    private Button focusedAnapanaButton;
    private Button topToBottomVipassanaButton;
    private Button scanningVipassanaButton;
    private Button symmetricalVipassanaButton;
    private Button sweepingVipassanaButton;
    private Button inTheMomentVipassanaButton;
    private Button mettaButton;

    private boolean isInMeditation = false;

    public  void didTapMeditationButton(View v) {
        int trackLevel = Integer.parseInt((String)v.getTag());
        presentAlerts(trackLevel);
    }

    public void didTapPlayPause(View v) {
        vipassanaManager.pauseOrResume();
    }

    private void secureButtons() {
        int enabledLevel = vipassanaManager.getUserCompletedTrackLevel() + 1;
        introButton.setEnabled(true);
        timerButton.setEnabled(true);
        shamathaButton.setEnabled(enabledLevel >= 1);
        anapanaButton.setEnabled( enabledLevel >= 2);
        focusedAnapanaButton.setEnabled( enabledLevel >= 3);
        topToBottomVipassanaButton.setEnabled( enabledLevel >= 4);
        scanningVipassanaButton.setEnabled( enabledLevel >= 5);
        symmetricalVipassanaButton.setEnabled( enabledLevel >= 6);
        sweepingVipassanaButton.setEnabled( enabledLevel >= 7);
        inTheMomentVipassanaButton.setEnabled( enabledLevel >= 8);
        mettaButton.setEnabled(enabledLevel >= 9);
    }

    private void connectView() {
        playPauseButton = findViewById(R.id.playPauseButton);
        playPauseButton.setVisibility(View.INVISIBLE);
        timerTextView = findViewById(R.id.timerTextView);
        introButton = findViewById(R.id.introButton);
        shamathaButton = findViewById(R.id.shamathaButton);
        anapanaButton = findViewById(R.id.anapanaButton);
        focusedAnapanaButton = findViewById(R.id.focusedAnapanaButton);
        topToBottomVipassanaButton = findViewById(R.id.vipassanaButton);
        scanningVipassanaButton = findViewById(R.id.scanningVipassanaButton);
        sweepingVipassanaButton = findViewById(R.id.sweepingVipassanButton);
        symmetricalVipassanaButton = findViewById(R.id.symmetricalVipassanaButton);
        inTheMomentVipassanaButton = findViewById(R.id.inTheMomentVipassanaButton);
        mettaButton = findViewById(R.id.mettaButton);
        timerButton = findViewById(R.id.silentTimerButton);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        connectView();
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        vipassanaManager = new VipassanaManager(this, this, settings);
        this.secureButtons();
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
        secureButtons();
    }

    private void runMeditationWithGap(int gapAmount) {
        isInMeditation = true;
        vipassanaManager.playTrackFromBeginning(gapAmount);
        playPauseButton.setVisibility(View.VISIBLE);
    }

    private void runMeditationWithFullLength(int fullLengthSeconds) {
        int minDurationSeconds = vipassanaManager.getMinimumDuration();
        int gapLength = fullLengthSeconds - minDurationSeconds;
        runMeditationWithGap(gapLength);
    }

    private void presentAlerts(final int trackLevel) {
        if (isInMeditation) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Meditation Underway");
            alertDialogBuilder
                    .setMessage("Would you like to stop the current session?")
                    .setCancelable(false)
                    .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            presentCountdownLengthAlertOrRun(trackLevel);
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
            presentCountdownLengthAlertOrRun(trackLevel);
        }
    }

    private void presentCountdownLengthAlertOrRun(final int trackLevel) {

        vipassanaManager.initTrackAtLevel(trackLevel);
        int minDurationSeconds = vipassanaManager.getMinimumDuration();
        final int minDurationMinutes = minDurationSeconds / 60 + 2;

        if (!vipassanaManager.isMultiPart()) {
            this.runMeditationWithGap(0);
        } else {

            LayoutInflater layoutInflater = LayoutInflater.from(this);
            View promptView = layoutInflater.inflate(R.layout.prompt, null);
            final AlertDialog alertD = new AlertDialog.Builder(this).create();

            Button btnMinimum = promptView.findViewById(R.id.btnMinimum);
            btnMinimum.setText(String.format("%d minutes", minDurationMinutes));

            Button btnHour = promptView.findViewById(R.id.btnHour);

            btnMinimum.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    runMeditationWithFullLength(minDurationMinutes * 60);
                    alertD.dismiss();
                }
            });

            btnHour.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    runMeditationWithFullLength(60 * 60);
                    alertD.dismiss();
                }
            });

            final Button btnCustom = promptView.findViewById(R.id.btnCustom);
            final EditText userInput = promptView.findViewById(R.id.userInput);
            int customValue = vipassanaManager.getDefaultDurationMinutes();
            if (customValue < minDurationMinutes) {
                customValue = minDurationMinutes;
            }

            userInput.setText(String.format("%d", customValue));
            btnCustom.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Integer userValue;
                    try {
                        userValue = Integer.parseInt(userInput.getText().toString());
                    } catch (NumberFormatException ex) {
                        presentInvalidCustomCountdownAlert(trackLevel, minDurationMinutes);
                        return;
                    }
                    if (userValue.intValue() < minDurationMinutes) {
                        presentInvalidCustomCountdownAlert(trackLevel, minDurationMinutes);
                    } else {
                        vipassanaManager.setDefaultDurationMinutes(userValue);
                        runMeditationWithFullLength(userValue * 60);
                        alertD.dismiss();
                    }
                }
            });
            alertD.setView(promptView);
            alertD.show();
        }
    }

    private void presentInvalidCustomCountdownAlert(int trackLevel, int minDurationMinutes) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        String minAlertString = String.format("Length for this meditation must be at least %d minutes", minDurationMinutes);

        alertDialogBuilder.setTitle("Invalid Custom Time");
        alertDialogBuilder
                .setMessage(minAlertString)
                .setCancelable(false)
                .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}
