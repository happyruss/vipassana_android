package com.guidedmeditationtreks.vipassana.models;

import android.media.MediaPlayer;

import java.io.File;

/**
 * Created by aerozero on 12/21/17.
 */

public class TrackTemplate {

    public String name;
    public String part1FileName;
    public MediaPlayer part1MediaPlayer;
    public int part1Duration;

    public String part2FileName;
    public MediaPlayer part2MediaPlayer;
    public int part2Duration;

    public int minimumDuration;
    public boolean isMultiPart;

    public TrackTemplate(String name, String part1FileName, String part2FileName) {

        this.name = name;
        this.part1FileName = part1FileName;
        this.part1MediaPlayer = new MediaPlayer();

        try {
            this.part1MediaPlayer.setDataSource("/app/src/main/res/audio" + File.separator + part1FileName);
            this.part1MediaPlayer.prepare();
            this.part1Duration = part1MediaPlayer.getDuration();

            if (part2FileName != null) {
                this.part2FileName = part2FileName;
                this.part2MediaPlayer = new MediaPlayer();
                this.part2MediaPlayer.setDataSource("/app/src/main/res/audio" + File.separator + part2FileName);
                this.part2MediaPlayer.prepare();
                this.part2Duration = part2MediaPlayer.getDuration();
                this.minimumDuration = this.part1Duration + this.part2Duration;
                this.isMultiPart = true;
            } else {
                this.minimumDuration = this.part1Duration;
                this.isMultiPart = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
