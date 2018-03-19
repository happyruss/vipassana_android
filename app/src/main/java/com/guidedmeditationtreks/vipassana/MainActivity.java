package com.guidedmeditationtreks.vipassana;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.vending.expansion.downloader.Helpers;
import com.google.android.vending.expansion.zipfile.APKExpansionSupport;
import com.google.android.vending.licensing.AESObfuscator;
import com.google.android.vending.licensing.LicenseChecker;
import com.google.android.vending.licensing.LicenseCheckerCallback;
import com.google.android.vending.licensing.Policy;
import com.google.android.vending.licensing.ServerManagedPolicy;
import com.guidedmeditationtreks.vipassana.licensing.MeditationDownloaderActivity;
import com.guidedmeditationtreks.vipassana.managers.TrackTemplateFactory;
import com.guidedmeditationtreks.vipassana.managers.VipassanaManager;
import com.guidedmeditationtreks.vipassana.models.TrackTemplate;

import java.io.IOException;
import java.util.Locale;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "VipassanaPrefs";
    public VipassanaManager vipassanaManager = VipassanaManager.singleton;
    public TrackTemplateFactory trackTemplateFactory = TrackTemplateFactory.singleton;

    private ImageButton timerButton;
    private ImageButton infoButton;
    private TextView meditationTotalTimeTextView;

    //Licensing and Expansion Stuff
    private static final String BASE64_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAsLnfE45WJDnwhP+Yc1+mhgK1lC0g5DxuoLK4l9kpz0CWVgBIrDeLne0u1K25W8/emi/sTWCVcJlb6M1wRKf2/ukW2Koz4UApUWDJYsLz34rooRJRoyk6tESQSE+Jjg816wZpjNpsG6+iMrBSjcanb2wbK9+sr9wMuDHqPooAdcrOcntrtjnroZ+L08TOqe/827S0ffpDKYziwobaDiJZxwrtoX92KdkrMA0FuNkBch4nPv9kXOMcBDZCaMmCFPZqmWi2lpfREG4MUts6bxpXDqET/2D1MMLvkSH0AEjn+UENu3avKkvVnCJW/CJXaQ+G/7OdFDulXVpRr8b1Qh06gQIDAQAB";
    private static final byte[] SALT = new byte[] {
            -34, -35, 47, -17, 22, -119, -21, 54, -94, -11, 18, 52, -18, 15, -31, -115, -2, -43, -22, 43
    };
    private LicenseCheckerCallback mLicenseCheckerCallback;
    private LicenseChecker mChecker;
    private AlertDialog.Builder alertDialogLicense;
    private AlertDialog alertLicense;
    private Handler mHandler;
    private int policyErrorCode;

    public  void didTapMeditationButton(View v) {
        int trackLevel = (int)v.getTag();
        presentAlerts(trackLevel);
    }

    public void didTapInfoButton(View v) {
        Uri uriUrl = Uri.parse(getResources().getString(R.string.url));
        Intent webView = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(webView);
    }

    private void secureButtons() {

        float disabledAlpha = 0.5f;
        int enabledLevel = vipassanaManager.getUserCompletedTrackLevel() + 1;
        boolean alwaysEnable = !trackTemplateFactory.getRequireMeditationsBeDoneInOrder();
        int totalTrackCount = trackTemplateFactory.getTrackTemplateCount();
        timerButton.setEnabled(true);

        for (int i = 1; i < totalTrackCount; i++) {
            TrackTemplate trackTemplate = trackTemplateFactory.getTrackTemplate(i);

            boolean isNotLastTrack = i < totalTrackCount - 1;
            LinearLayout linearLayout = this.findViewById(R.id.buttonLinearLayout);

            Button button = linearLayout.findViewById(trackTemplate.getButtonId());
            button.setEnabled(alwaysEnable || enabledLevel >= i);
            button.setAlpha(enabledLevel >= i ? 1.0f : disabledAlpha);

            if (isNotLastTrack) {
                ImageView dots = this.findViewById(trackTemplate.getSpacerId());
                dots.setBackgroundResource(alwaysEnable || enabledLevel >= i ? R.mipmap.dots : R.mipmap.dots_copy);
            }
        }

        int medHours = vipassanaManager.getUserTotalSecondsInMeditation() / 3600;
        String meditationTimeLabelText = medHours == 1 ? String.format(Locale.getDefault(), "%d hour spent meditating", medHours) : String.format(Locale.getDefault(),"%d hours spent meditating", medHours);
        meditationTotalTimeTextView.setText(meditationTimeLabelText);
    }

    private boolean expansionFilesDelivered() {
        for (MeditationDownloaderActivity.XAPKFile xf :  MeditationDownloaderActivity.xAPKS) {
            String fileName = Helpers.getExpansionAPKFileName(this, xf.mIsMain, xf.mFileVersion);
            if (!Helpers.doesFileExist(this, fileName, xf.mFileSize, false)) {
                Log.e("vipassana", "ExpansionAPKFile doesn't exist or has a wrong size (" + fileName + ").");
                return false;
            }
        }
        return true;
    }

//    private void setExpansionFile() {
//        try {
//            meditationPlayer.setExpansionFile(APKExpansionSupport.getAPKExpansionZipFile(this, MeditationDownloaderActivity.xAPKS[0].mFileVersion, 0));
//        } catch (IOException ex) {
//            Log.e("Vipassana", "Error Getting expansion Zip File");
//        }
//    }

    private void handleExpansionFile() {
        //Handle Expansion File stuff
        if (!expansionFilesDelivered()){
            Intent intent= new Intent(this, MeditationDownloaderActivity.class);
            if (this.getIntent().getCategories() != null) {
                for (String category : this.getIntent().getCategories()) {
                    intent.addCategory(category);
                }
            }
            startService(intent);
            startActivityForResult(intent, 1);
        } else {
//            setExpansionFile();
        }
    }

    private void connectView() {
        timerButton = findViewById(R.id.silentTimerButton);
        infoButton = findViewById(R.id.infoButton);
        meditationTotalTimeTextView = findViewById(R.id.meditationTotalTimeTextView);

        int trackCount = trackTemplateFactory.getTrackTemplateCount();

        for (int i = 1; i < trackCount; i++) {
            TrackTemplate trackTemplate = trackTemplateFactory.getTrackTemplate(i);
            trackTemplate.setButtonId(View.generateViewId());
            trackTemplate.setSpacerId(View.generateViewId());

            View v = LayoutInflater.from(this).inflate(R.layout.button_template, null);
            Button button = (Button) v.findViewById(R.id.templateButton);
            button.setTag(i);
            button.setId(trackTemplate.getButtonId());
            button.setText(trackTemplate.getName());
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    didTapMeditationButton(v);
                }
            });
            LinearLayout linearLayout = (LinearLayout)this.findViewById(R.id.buttonLinearLayout);
            LinearLayout parent = (LinearLayout) button.getParent();
            parent.removeView(button);
            linearLayout.addView(button);
            if (i < trackCount - 1) {
                ImageView dots = (ImageView) v.findViewById(R.id.templateDots);
                dots.setId(trackTemplate.getSpacerId());
                parent.removeView(dots);
                linearLayout.addView(dots);
            }
        }

        handleExpansionFile();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        handleLicensing();

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

    private void handleLicensing() {
        //Begin Licensing Stuff
        alertDialogLicense = new AlertDialog.Builder(MainActivity.this);
        prepareLicenseAlertDialog();
        mHandler = new Handler();

        // Try to use more data here. ANDROID_ID is a single point of attack.
        String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        // Construct the LicenseCheckerCallback. The library calls this when done.
        mLicenseCheckerCallback = new MyLicenseCheckerCallback();

        // Construct the LicenseChecker with a Policy.
        mChecker = new LicenseChecker(
                this, new ServerManagedPolicy(this,
                new AESObfuscator(SALT, getPackageName(), deviceId)),
                BASE64_PUBLIC_KEY  // Your public licensing key.
        );
        mChecker.checkAccess(mLicenseCheckerCallback);
        //End Licensing Stuff
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

        if (requestCode == MEDITATION_ACTIVITY_REQUEST_CODE) {
            secureButtons();
        } else if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                String result=data.getStringExtra("result");
//                setExpansionFile();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
                alertDialogLicense.setTitle(String.format("Error Getting Expansion File! Error Code:%d", 0));
                alertLicense = alertDialogLicense.create();
                alertLicense.show();
            }
        }
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


    //Licensing

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mChecker.onDestroy();
    }

    private class MyLicenseCheckerCallback implements LicenseCheckerCallback {
        public void allow(int policyReason) {
            if (isFinishing()) {
                // Don't update UI if Activity is finishing.
                return;
            }
            // Should allow user access.
            //displayResult(getString(R.string.allow));
        }

        public void dontAllow(int policyReason) {
            if (isFinishing()) {
                // Don't update UI if Activity is finishing.
                return;
            }
            if (Policy.RETRY == policyReason)
            {
                mHandler.post(new Runnable() {
                    public void run() {
                        alertDialogLicense.setMessage("Make sure that your phone is configured to your Google Account. Retry License Check?");
                        alertDialogLicense.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                mChecker.checkAccess(mLicenseCheckerCallback);
                                //dialog.cancel();
                            }
                        });
                        alertDialogLicense.setTitle("License Check Failed!");
                        alertLicense = alertDialogLicense.create();
                        alertLicense.show();
                    }
                });
            } else {
                mHandler.post(new Runnable() {
                    public void run() {
                        alertDialogLicense.setMessage("Unlicensed App!");
                        alertDialogLicense.setPositiveButton("Play", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent marketIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(
                                        "http://market.android.com/details?id=" + getPackageName()));
                                startActivity(marketIntent);
                                //dialog.cancel();
                            }
                        });
                        alertDialogLicense.setTitle("License Check Failed!");
                        alertLicense = alertDialogLicense.create();
                        alertLicense.show();
                    }
                });
            }
        }

        public void applicationError(int errorCode) {
            policyErrorCode = errorCode;
            if (isFinishing()) {
                // Don't update UI if Activity is finishing.
                return;
            }
            mHandler.post(new Runnable() {
                public void run() {
                    // This is a polite way of saying the developer made a mistake
                    // while setting up or calling the license checker library.
                    // Please examine the error code and fix the error.
                    //String result = String.format(getString(R.string.application_error), errorCode);
                    alertDialogLicense.setTitle(String.format("Error Checking License! Error Code:%d", policyErrorCode));
                    alertLicense = alertDialogLicense.create();
                    alertLicense.show();
                }
            });
        }
    }

    private void prepareLicenseAlertDialog() {

        // set dialog message
        alertDialogLicense
                .setCancelable(false)
                .setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, close
                        // current activity
                        System.exit(0);
                        android.os.Process.killProcess(android.os.Process.myPid());
                        MainActivity.this.finish();

                        //manual override
                        //dialog.cancel();
                    }
                });
    }

}
