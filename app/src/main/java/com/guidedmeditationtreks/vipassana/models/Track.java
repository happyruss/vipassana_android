package com.guidedmeditationtreks.vipassana.models;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.CountDownTimer;

import com.guidedmeditationtreks.vipassana.R;

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
    private int totalDuration;

    private MediaPlayer playerPart2;

    public TrackDelegate delegate;

    public Track(TrackDelegate delegate, TrackTemplate trackTemplate, int gapDuration, Context context) {

        this.trackTemplate = trackTemplate;
        this.delegate = delegate;

        //initialize the audio files
        this.playerPart1 = MediaPlayer.create(context, trackTemplate.getPart1Resource());
        playerPart1.start(); // no need to call prepare(); create() does that for you

        if (this.trackTemplate.isMultiPart()) {
            this.playerPart2 = MediaPlayer.create(context, trackTemplate.getPart2Resource());
            this.gapDuration = gapDuration;
            this.totalDuration = this.gapDuration + this.playerPart1.getDuration() + this.playerPart2.getDuration();
        } else {
            this.totalDuration = this.playerPart1.getDuration();
        }
        this.remainingTime = this.totalDuration;

    }

//    @objc func update() {
//        self.remainingTime = self.remainingTime - 1
//        delegate?.trackTimeRemainingUpdated(timeRemaining: self.remainingTime)
//
//        guard self.remainingTime > 0 else {
//            delegate?.trackEnded()
//            return
//        }
//
//    }

    private void createTimer(int seconds) {




        timer = new CountDownTimer(seconds, 1000) {
            public void onTick(long millisUntilFinished) {
                remainingTime --1;
                delegate.trackTimeRemainingUpdated(remainingTime);

                if (this.totalDuration)


            if ( this.totalDuration - this.remainingTime < self.trackTemplate.part1Duration) {
                if (self.playerPart1.rate != 0 && self.playerPart1.error == nil) {
                } else {
                    self.playerPart1.play()
                }
            }

        guard self.remainingTime > 0 else {
            return
        }
        guard self.trackTemplate.isMultiPart else {
            return
        }
        guard self.remainingTime < (self.trackTemplate.part2Duration! + 1) else {
            return
        }

        if (self.playerPart2!.rate != 0 && self.playerPart2!.error == nil) {
        } else {
            self.playerPart2?.play()
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
