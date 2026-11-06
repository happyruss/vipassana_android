package com.guidedmeditationtreks.vipassana;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.guidedmeditationtreks.vipassana.managers.VipassanaManager;
import com.guidedmeditationtreks.vipassana.models.TrackDelegate;

import java.util.Locale;

public class MeditationActivity extends AppCompatActivity implements TrackDelegate {

    private ToggleButton playPauseButton;
    private TextView timerTextView;
    private TextView meditationNameTextView;
    private boolean isInMeditation = false;
    private VipassanaManager vipassanaManager = VipassanaManager.singleton;
    private View clearClickBackgroundView;
    private View blackClickBackgroundView;
    private static final String POST_NOTIFICATIONS_REQUESTED = "postNotificationsRequested";

    private final ActivityResultLauncher<String> notificationPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), granted -> {
                recordNotificationRequestFinished();
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(getWindow());
        setContentView(R.layout.activity_meditation);
        EdgeToEdge.applySystemBarPadding(findViewById(R.id.meditationRoot));
        vipassanaManager.setDelegate(this);
        connectView();

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                handleBackNavigation();
            }
        });

        boolean notificationsGranted = hasPostNotificationsPermission();
        if (!Boolean.TRUE.equals(vipassanaManager.getInMeditation())) {
            Intent intent = getIntent();
            int gapAmount = intent.getIntExtra("gapAmount", 0);
            if (SessionNotificationPolicy.shouldStartPlayback(notificationsGranted)) {
                vipassanaManager.playActiveTrackFromBeginning(gapAmount);
                vipassanaManager.setInMeditation(true);
            }
        }
        requestPostNotificationsOnce(notificationsGranted);
        if (!Boolean.TRUE.equals(vipassanaManager.getTrackCompleted())) {
            isInMeditation = true;
            vipassanaManager.userStartedTrack();
        }
    }

    private boolean hasPostNotificationsPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPostNotificationsOnce(boolean granted) {
        SharedPreferences preferences = getSharedPreferences(MainActivity.PREFS_NAME, MODE_PRIVATE);
        boolean sheetReturned = preferences.getBoolean(POST_NOTIFICATIONS_REQUESTED, false);
        if (SessionNotificationPolicy.shouldRequestPostNotifications(Build.VERSION.SDK_INT, granted, sheetReturned)) {
            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
        }
    }

    private void recordNotificationRequestFinished() {
        if (!SessionNotificationPolicy.shouldMarkNotificationRequestComplete(true)) {
            return;
        }
        getSharedPreferences(MainActivity.PREFS_NAME, MODE_PRIVATE)
                .edit()
                .putBoolean(POST_NOTIFICATIONS_REQUESTED, true)
                .apply();
    }

    private void closeActivity() {
        vipassanaManager.clearCurrentTrack();
        finish();
    }

    private void handleBackNavigation() {
        if (isInMeditation) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Meditation Underway");
            alertDialogBuilder
                    .setMessage("Would you like to stop the current session?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            closeActivity();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
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
        handleBackNavigation();
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
