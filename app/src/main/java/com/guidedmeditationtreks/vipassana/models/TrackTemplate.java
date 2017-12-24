package com.guidedmeditationtreks.vipassana.models;

import android.content.Context;
import android.media.MediaPlayer;

import com.guidedmeditationtreks.vipassana.R;

import java.io.File;

/**
 * Created by aerozero on 12/21/17.
 */

public class TrackTemplate {

    private String name;
    private String part1FileName;
    private MediaPlayer part1MediaPlayer;
    private int part1Duration;

    private String part2FileName;
    private MediaPlayer part2MediaPlayer;
    private int part2Duration;

    private int minimumDuration;
    private boolean isMultiPart;

    private int part1Resource;
    private int part2Resource;

    public TrackTemplate(String name, int part1Resource, int part2Resource, String part1FileName, String part2FileName, Context context) {

        this.name = name;
        this.part1FileName = part1FileName;
        this.part1Resource = part1Resource;
        this.part2Resource = part2Resource;
        // this.part1MediaPlayer = new MediaPlayer();

        try {
//            this.part1MediaPlayer.setDataSource("/app/src/main/res/raw" + File.separator + part1FileName);
//            this.part1MediaPlayer.prepare();
//            this.part1Duration = part1MediaPlayer.getDuration();

            if (part2FileName != null) {
                this.part2FileName = part2FileName;
//                this.part2MediaPlayer = new MediaPlayer();
//                this.part2MediaPlayer.setDataSource("/app/src/main/res/raw" + File.separator + part2FileName);
//                this.part2MediaPlayer.prepare();
//                this.part2Duration = part2MediaPlayer.getDuration();
//                this.minimumDuration = this.part1Duration + this.part2Duration;
                this.isMultiPart = true;
            } else {
//                this.minimumDuration = this.part1Duration;
                this.isMultiPart = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


//    public int getMinimumDuration() {
//        return minimumDuration;
//    }

    public boolean isMultiPart() {
        return isMultiPart;
    }

    public int getPart1Resource() {
        return part1Resource;
    }

    public int getPart2Resource() {
        return part2Resource;
    }

//    public int getPart1Duration() {
//        return part1Duration;
//    }
//
//    public int getPart2Duration() {
//        return part2Duration;
//    }
}
