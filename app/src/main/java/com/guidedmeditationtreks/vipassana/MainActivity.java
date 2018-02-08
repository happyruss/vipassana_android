package com.guidedmeditationtreks.vipassana;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.guidedmeditationtreks.vipassana.managers.VipassanaManager;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "VipassanaPrefs";
    public VipassanaManager vipassanaManager = VipassanaManager.singleton;

    private Button introButton;
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
    private TextView meditationTotalTimeTextView;

    public  void didTapMeditationButton(View v) {
        int trackLevel = Integer.parseInt((String)v.getTag());
        presentAlerts(trackLevel);
    }

    private void secureButtons() {
        int enabledLevel = vipassanaManager.getUserCompletedTrackLevel() + 1;
        introButton.setEnabled(true);
        timerButton.setEnabled(true);
        shamathaButton.setEnabled(enabledLevel > 1);
        anapanaButton.setEnabled( enabledLevel > 2);
        focusedAnapanaButton.setEnabled( enabledLevel > 3);
        topToBottomVipassanaButton.setEnabled( enabledLevel > 4);
        scanningVipassanaButton.setEnabled( enabledLevel > 5);
        symmetricalVipassanaButton.setEnabled( enabledLevel > 6);
        sweepingVipassanaButton.setEnabled( enabledLevel > 7);
        inTheMomentVipassanaButton.setEnabled( enabledLevel > 8);
        mettaButton.setEnabled(enabledLevel > 9);
    }

    private void connectView() {
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
        meditationTotalTimeTextView = findViewById(R.id.meditationTotalTimeTextView);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        connectView();
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        vipassanaManager.setSettings(settings);
        this.secureButtons();
    }

    private void presentAlerts(final int trackLevel) {
        presentCountdownLengthAlertOrRun(trackLevel);
    }

    private void presentCountdownLengthAlertOrRun(final int trackLevel) {

        vipassanaManager.initTrackAtLevel(trackLevel, this);
        int minDurationSeconds = vipassanaManager.getMinimumDuration();
        final int minDurationMinutes = minDurationSeconds / 60 + 2;

        if (!vipassanaManager.isMultiPart()) {
            this.runMeditationWithGap(0);
        } else {
            LayoutInflater layoutInflater = LayoutInflater.from(this);
            View promptView = layoutInflater.inflate(R.layout.prompt, null);
            final AlertDialog alertD = new AlertDialog.Builder(this).create();

            Button btnMinimum = promptView.findViewById(R.id.btnMinimum);
            btnMinimum.setText(String.format(Locale.getDefault(),"%d minutes", minDurationMinutes));

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

            userInput.setText(String.format(Locale.getDefault(),"%d", customValue));
            btnCustom.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Integer userValue;
                    try {
                        userValue = Integer.parseInt(userInput.getText().toString());
                    } catch (NumberFormatException ex) {
                        presentInvalidCustomCountdownAlert(trackLevel, minDurationMinutes);
                        return;
                    }
                    if (userValue < minDurationMinutes) {
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
        String minAlertString = String.format(Locale.getDefault(), "Length for this meditation must be at least %d minutes", minDurationMinutes);

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

    private static final int MEDITATION_ACTIVITY_REQUEST_CODE = 0xe110;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MEDITATION_ACTIVITY_REQUEST_CODE)
            secureButtons();
    }

    private void runMeditationWithGap(int gapAmount) {
        Intent myIntent = new Intent(MainActivity.this, MeditationActivity.class);
        myIntent.putExtra("gapAmount", gapAmount);
//        MainActivity.this.startActivity(myIntent);
        startActivityForResult(myIntent, MEDITATION_ACTIVITY_REQUEST_CODE);
    }

    private void runMeditationWithFullLength(int fullLengthSeconds) {
        int minDurationSeconds = vipassanaManager.getMinimumDuration();
        int gapLength = fullLengthSeconds - minDurationSeconds;
        runMeditationWithGap(gapLength);
    }


}
