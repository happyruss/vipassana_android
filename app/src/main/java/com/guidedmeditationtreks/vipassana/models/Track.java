package com.guidedmeditationtreks.vipassana.models;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.CountDownTimer;

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

    public Track(TrackDelegate delegate, TrackTemplate trackTemplate, Context context) {

        this.trackTemplate = trackTemplate;
        this.delegate = delegate;

        //initialize the audio files
        this.playerPart1 = MediaPlayer.create(context, trackTemplate.getPart1Resource());
        //playerPart1.start(); // no need to call prepare(); create() does that for you

        this.part1Duration = this.playerPart1.getDuration();

        if (this.trackTemplate.isMultiPart()) {
            this.playerPart2 = MediaPlayer.create(context, trackTemplate.getPart2Resource());
            this.part2Duration = this.playerPart2.getDuration();
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

        timer = new CountDownTimer(seconds, 1000) {

            public void onTick(long millisUntilFinished) {
                remainingTime--;
                delegate.trackTimeRemainingUpdated(remainingTime);

                if (totalDuration - remainingTime < part1Duration) {
                    if (!playerPart1.isPlaying()) {
                        playerPart1.start();
                    }
                }

                if(remainingTime > 0 && trackTemplate.isMultiPart()) {
                    if (remainingTime < (part2Duration + 1)) {
                        if(!playerPart2.isPlaying()) {
                            playerPart2.start();
                        }
                    }
                }
            }

            public void onFinish() {
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
        this.remainingTime = this.totalDuration;
        this.isPaused = false;
        this.playerPart1.stop();
        if (this.trackTemplate.isMultiPart()) {
            this.playerPart2.stop();
        }
        timer.cancel();
    }

    public void pauseOrResume() {
        if (this.isPaused) {
            this.resume();
        } else {
            this.pause();
        }
    }

}
