package com.guidedmeditationtreks.vipassana.models;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.util.Log;

import com.google.android.vending.expansion.zipfile.APKExpansionSupport;
import com.google.android.vending.expansion.zipfile.ZipResourceFile;
import com.guidedmeditationtreks.vipassana.licensing.MeditationDownloaderActivity;

import java.io.IOException;

/**
 * Created by aerozero on 12/22/17.
 */

public class Track {

    private int remainingTime;
    private boolean isPaused;
    private CountDownTimer timer;

    private TrackTemplate trackTemplate;

    private MediaPlayer playerPart1;
    private int gapDuration;

    private int part1Duration;
    private int part2Duration;
    private int minimumDuration;
    private int totalDuration;

    private MediaPlayer playerPart2;

    private TrackDelegate delegate;
    private ZipResourceFile expansionFile;


    public Track(TrackTemplate trackTemplate, Context context) {

        try {
            expansionFile =  APKExpansionSupport.getAPKExpansionZipFile(context, MeditationDownloaderActivity.xAPKS[0].mFileVersion, 0);
        } catch (IOException ex) {
            Log.e("Vipassana", "Error Getting expansion Zip File");
        }

        this.trackTemplate = trackTemplate;

        //initialize the audio files
        AssetFileDescriptor part1Asset = expansionFile.getAssetFileDescriptor(trackTemplate.getPart1ResourceName());
        this.playerPart1 = new MediaPlayer();
        try {
            this.playerPart1.setDataSource(part1Asset.getFileDescriptor(), part1Asset.getStartOffset(), part1Asset.getLength());
        } catch(IOException ex) {
            Log.e("Vipassana", "Error Getting Meditation track from Zip File");
        }

        this.part1Duration = this.playerPart1.getDuration() / 1000;

        if (this.trackTemplate.isMultiPart()) {
            AssetFileDescriptor part2Asset = expansionFile.getAssetFileDescriptor(trackTemplate.getPart2ResourceName());
            this.playerPart2 = new MediaPlayer();
            try {
                this.playerPart2.setDataSource(part2Asset.getFileDescriptor(), part2Asset.getStartOffset(), part2Asset.getLength());
            } catch(IOException ex) {
                Log.e("Vipassana", "Error Getting Meditation track from Zip File");
            }
            this.part2Duration = this.playerPart2.getDuration() / 1000;
            this.minimumDuration = this.part1Duration + this.part2Duration;
        } else {
            this.minimumDuration = this.part1Duration;
        }
        this.totalDuration = this.minimumDuration;
        this.remainingTime = this.totalDuration;
    }

    public boolean isMultiPart() {
        return this.trackTemplate.isMultiPart();
    }

    public void setGapDuration(int gapDuration) {
        this.gapDuration = gapDuration;
        if (this.trackTemplate.isMultiPart()) {
            this.totalDuration = this.gapDuration + this.part1Duration + this.part2Duration;
        } else {
            this.totalDuration = this.part1Duration;
        }
        this.remainingTime = this.totalDuration;
    }

    public int getMinimumDuration() {
        return minimumDuration;
    }

    private void createTimer(int seconds) {

        int milliseconds = seconds * 1000;
        timer = new CountDownTimer(milliseconds, 1000) {

            public void onTick(long millisUntilFinished) {
                remainingTime--;
                delegate.trackTimeRemainingUpdated(remainingTime);

                if (totalDuration - remainingTime < part1Duration) {
                    if (!playerPart1.isPlaying()) {
                        playerPart1.start();
                    }
                }

                if(remainingTime > 0 && trackTemplate.isMultiPart()) {
                    if (remainingTime < part2Duration) {
                        if(!playerPart2.isPlaying()) {
                            playerPart2.start();
                        }
                    }
                }
            }

            public void onFinish() {
                delegate.trackTimeRemainingUpdated(0);
                delegate.trackEnded();
            }

        }.start();
    }

    public void playFromBeginning() {
        this.createTimer(this.totalDuration);
        this.isPaused = false;
        this.playerPart1.start();
    }

    private void pause() {
        timer.cancel();
        this.isPaused = true;
        if (this.playerPart1.isPlaying()) {
            this.playerPart1.pause();
        }
        if (this.trackTemplate.isMultiPart()) {
            if (this.playerPart2.isPlaying()) {
                this.playerPart2.pause();
            }
        }
    }

    private void resume() {
        this.createTimer(this.remainingTime);
        this.isPaused = false;
    }

    public void stop() {
//        this.remainingTime = this.totalDuration;
//        this.isPaused = false;
        if (playerPart1.isPlaying()) {
            this.playerPart1.stop();
        }
        if (this.trackTemplate.isMultiPart()) {
            if (playerPart2.isPlaying()) {
                this.playerPart2.stop();
            }
        }
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public void pauseOrResume() {
        if (this.isPaused) {
            this.resume();
        } else {
            this.pause();
        }
    }

    public String getName() {
        return trackTemplate.getName();
    }

    public String getLongName() {
        return trackTemplate.getLongName();
    }

    public void setDelegate(TrackDelegate delegate) {
        this.delegate = delegate;
    }

}
