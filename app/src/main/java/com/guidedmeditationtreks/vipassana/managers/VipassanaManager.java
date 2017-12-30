package com.guidedmeditationtreks.vipassana.managers;

import android.content.Context;
import android.content.SharedPreferences;

import com.guidedmeditationtreks.vipassana.models.Track;
import com.guidedmeditationtreks.vipassana.models.TrackDelegate;
import com.guidedmeditationtreks.vipassana.models.TrackTemplate;
import com.guidedmeditationtreks.vipassana.models.User;

/**
 * Created by czar on 12/22/17.
 */

public class VipassanaManager {

    private TrackTemplateFactory trackTemplateFactory = TrackTemplateFactory.shared;
    private TrackDelegate delegate;
    private Context context;

    private User user;
    private Track activeTrack;
    private int activeTrackLevel;

    private SharedPreferences settings;

    public VipassanaManager(TrackDelegate delegate, Context context, SharedPreferences settings) {
        this.delegate = delegate;
        this.context = context;
        this.settings = settings;
        this.user = new User();

        int savedCompletedLevel = settings.getInt("savedCompletedLevel", 0);
        this.user.setCompletedTrackLevel(savedCompletedLevel);

        int savedCustomMeditationDurationMinutes = settings.getInt("savedCustomMeditationDurationMinutes", 0);
        this.user.setCustomMeditationDurationMinutes(savedCustomMeditationDurationMinutes);
    }

    public void initTrackAtLevel(int trackLevel) {

        if (this.activeTrack != null) {
            this.activeTrack.stop();
            this.activeTrack = null;
            this.activeTrackLevel = 0;
        }
        this.activeTrackLevel = trackLevel;
        TrackTemplate trackTemplate = trackTemplateFactory.trackTemplates[trackLevel];
        this.activeTrack = new Track(this.delegate, trackTemplate, this.context);
    }

    public boolean isMultiPart() {
        return activeTrack.isMultiPart();
    }

    public void playTrackFromBeginning(int gapDuration) {
        stop();
        activeTrack.setGapDuration(gapDuration);
        this.activeTrack.playFromBeginning();
    }

    public void pauseOrResume() {
        if (activeTrack != null) {
            activeTrack.pauseOrResume();
        }
    }

    public void stop() {
        if (activeTrack != null) {
            activeTrack.stop();
            activeTrack = null;
        }
    }

    public void userCompletedTrack() {
        if (this.activeTrackLevel > this.user.getCompletedTrackLevel()) {
            this.user.setCompletedTrackLevel(this.activeTrackLevel);
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("savedCompletedLevel", this.activeTrackLevel);
            editor.commit();
        }
    }

    public void setDefaultDurationMinutes(int durationMinutes) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("savedCustomMeditationDurationMinutes", durationMinutes);
        editor.commit();
        this.user.setCustomMeditationDurationMinutes(durationMinutes);
    }

    public int getMinimumDuration() {
        return activeTrack.getMinimumDuration();
    }

    public int getUserCompletedTrackLevel(){
        return user.getCompletedTrackLevel();
    }
}
