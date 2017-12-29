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

    public TrackTemplate(String name, int part1Resource, int part2Resource) {

        this.name = name;
        this.part1Resource = part1Resource;

        try {
            if (part2FileName != null) {
                this.part2Resource = part2Resource;
                this.isMultiPart = true;
            } else {
                this.isMultiPart = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isMultiPart() {
        return isMultiPart;
    }

    public int getPart1Resource() {
        return part1Resource;
    }

    public int getPart2Resource() {
        return part2Resource;
    }

}
