package com.guidedmeditationtreks.vipassana;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import com.guidedmeditationtreks.vipassana.managers.VipassanaManager;

import java.util.Locale;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "VipassanaPrefs";
    public VipassanaManager vipassanaManager = VipassanaManager.singleton;

    private Button introButton;
    private ImageButton timerButton;
    private ImageButton infoButton;
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

    public void didTapInfoButton(View v) {
        Uri uriUrl = Uri.parse(getResources().getString(R.string.url));
        Intent webView = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(webView);
    }

    private void secureButtons() {

        float disabledAlpha = 0.35f;

        int enabledLevel = vipassanaManager.getUserCompletedTrackLevel() + 1;
        introButton.setEnabled(true);
        timerButton.setEnabled(true);

        findViewById(R.id.dots2).setBackgroundResource(enabledLevel > 1 ? R.mipmap.dots : R.mipmap.dots_copy);
        shamathaButton.setEnabled(enabledLevel > 1);
        shamathaButton.setAlpha(enabledLevel > 1 ? 1.0f : disabledAlpha);

        findViewById(R.id.dots3).setBackgroundResource(enabledLevel > 2 ? R.mipmap.dots : R.mipmap.dots_copy);
        anapanaButton.setEnabled(enabledLevel > 2);
        anapanaButton.setAlpha(enabledLevel > 2 ? 1.0f : disabledAlpha);

        findViewById(R.id.dots4).setBackgroundResource(enabledLevel > 3 ? R.mipmap.dots : R.mipmap.dots_copy);
        focusedAnapanaButton.setEnabled( enabledLevel > 3);
        focusedAnapanaButton.setAlpha(enabledLevel > 3 ? 1.0f : disabledAlpha);

        findViewById(R.id.dots5).setBackgroundResource(enabledLevel > 4 ? R.mipmap.dots : R.mipmap.dots_copy);
        topToBottomVipassanaButton.setEnabled( enabledLevel > 4);
        topToBottomVipassanaButton.setAlpha(enabledLevel > 4 ? 1.0f : disabledAlpha);

        findViewById(R.id.dots6).setBackgroundResource(enabledLevel > 5 ? R.mipmap.dots : R.mipmap.dots_copy);
        scanningVipassanaButton.setEnabled( enabledLevel > 5);
        scanningVipassanaButton.setAlpha(enabledLevel > 5 ? 1.0f : disabledAlpha);

        findViewById(R.id.dots7).setBackgroundResource(enabledLevel > 6 ? R.mipmap.dots : R.mipmap.dots_copy);
        symmetricalVipassanaButton.setEnabled( enabledLevel > 6);
        symmetricalVipassanaButton.setAlpha(enabledLevel > 6 ? 1.0f : disabledAlpha);

        findViewById(R.id.dots8).setBackgroundResource(enabledLevel > 7 ? R.mipmap.dots : R.mipmap.dots_copy);
        sweepingVipassanaButton.setEnabled( enabledLevel > 7);
        sweepingVipassanaButton.setAlpha(enabledLevel > 7 ? 1.0f : disabledAlpha);

        findViewById(R.id.dots9).setBackgroundResource(enabledLevel > 8 ? R.mipmap.dots : R.mipmap.dots_copy);
        inTheMomentVipassanaButton.setEnabled( enabledLevel > 8);
        inTheMomentVipassanaButton.setAlpha(enabledLevel > 8 ? 1.0f : disabledAlpha);

        findViewById(R.id.dots10).setBackgroundResource(enabledLevel > 9 ? R.mipmap.dots : R.mipmap.dots_copy);
        mettaButton.setEnabled(enabledLevel > 9);
        mettaButton.setAlpha(enabledLevel > 9 ? 1.0f : disabledAlpha);


        int medHours = vipassanaManager.getUserTotalSecondsInMeditation() / 3600;
        String meditationTimeLabelText = medHours == 1 ? String.format(Locale.getDefault(), "%d hour spent meditating", medHours) : String.format(Locale.getDefault(),"%d hours spent meditating", medHours);
        meditationTotalTimeTextView.setText(meditationTimeLabelText);
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
        infoButton = findViewById(R.id.infoButton);
        meditationTotalTimeTextView = findViewById(R.id.meditationTotalTimeTextView);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        connectView();
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        vipassanaManager.setSettings(settings);
        this.secureButtons();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/sf-pro-text-semibold.otf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
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
            btnMinimum.setText(String.format(Locale.getDefault(),"%d minutes (minimum)", minDurationMinutes));

            Button btnHour = promptView.findViewById(R.id.btnHour);
            Button btnFortyFive = promptView.findViewById(R.id.btnFortyFive);

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

            btnFortyFive.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    runMeditationWithFullLength(45 * 60);
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
            userInput.setOnEditorActionListener(new EditText.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        btnCustom.callOnClick();
                        return true;
                    }
                    return false;
                }
            });
            userInput.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    userInput.setText("");
                }
            });

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
