package com.guidedmeditationtreks.vipassana.managers;

import android.content.Context;
import android.content.SharedPreferences;

import com.guidedmeditationtreks.vipassana.models.Track;
import com.guidedmeditationtreks.vipassana.models.TrackDelegate;
import com.guidedmeditationtreks.vipassana.models.TrackTemplate;
import com.guidedmeditationtreks.vipassana.models.User;

/**
 * Created by czar on 12/22/17.
 * Manages the vipassana tasks
 */

public class VipassanaManager {

    public static VipassanaManager singleton = new VipassanaManager();
    private SharedPreferences settings;
    private TrackTemplateFactory trackTemplateFactory = TrackTemplateFactory.shared;
    private User user;
    private Track activeTrack;
    private int activeTrackLevel;

    private VipassanaManager() {
        this.user = new User();
    }

    public void initTrackAtLevel(int trackLevel, Context context) {
        this.clearCurrentTrack();
        this.activeTrackLevel = trackLevel;
        TrackTemplate trackTemplate = trackTemplateFactory.getTrackTemplate(trackLevel);
        this.activeTrack = new Track(trackTemplate, context);
    }

    public boolean isMultiPart() {
        return activeTrack.isMultiPart();
    }

    public void playActiveTrackFromBeginning(int gapDuration) {
        this.activeTrack.setGapDuration(gapDuration);
        this.activeTrack.playFromBeginning();
    }

    public void pauseOrResume() {
        if (activeTrack != null) {
            activeTrack.pauseOrResume();
        }
    }

    public void clearCurrentTrack() {
        if (activeTrack != null) {
            activeTrack.stop();
            activeTrack = null;
            activeTrackLevel = 0;
        }
    }

    public void userCompletedTrack() {
        if (this.activeTrackLevel > this.user.getCompletedTrackLevel()) {
            this.user.setCompletedTrackLevel(this.activeTrackLevel);
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("savedCompletedLevel", this.activeTrackLevel);
            editor.apply();
        }
    }

    public void setDefaultDurationMinutes(int durationMinutes) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("savedCustomMeditationDurationMinutes", durationMinutes);
        editor.apply();
        this.user.setCustomMeditationDurationMinutes(durationMinutes);
    }

    public int getDefaultDurationMinutes() {
        return user.getCustomMeditationDurationMinutes();
    }

    public int getMinimumDuration() {
        return activeTrack.getMinimumDuration();
    }

    public int getUserCompletedTrackLevel(){
        return user.getCompletedTrackLevel();
    }

    public int getUserTotalSecondsInMeditation(){
        return user.getTotalSecondsInMeditation();
    }

    public void setSettings(SharedPreferences settings) {
        this.settings = settings;
        int savedCompletedLevel = settings.getInt("savedCompletedLevel", 0);
        this.user.setCompletedTrackLevel(savedCompletedLevel);

        int savedCustomMeditationDurationMinutes = settings.getInt("savedCustomMeditationDurationMinutes", 0);
        this.user.setCustomMeditationDurationMinutes(savedCustomMeditationDurationMinutes);
        int totalSecondsInMeditation = settings.getInt("totalSecondsInMeditation", 0);
        this.user.setTotalSecondsInMeditation(totalSecondsInMeditation);
    }

    public void incrementTotalSecondsInMeditation() {
        this.user.setTotalSecondsInMeditation(this.user.getTotalSecondsInMeditation() + 1);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("totalSecondsInMeditation", this.user.getTotalSecondsInMeditation());
        editor.apply();
    }

    public void setDelegate(TrackDelegate delegate) {
        if (this.activeTrack != null) {
            this.activeTrack.setDelegate(delegate);
        }
    }

    public String getActiveTrackName() {
        if (activeTrack == null) {
            return null;
        }
        return activeTrack.getName();
    }

    public String getActiveTrackLongName() {
        if (activeTrack == null) {
            return null;
        }
        return activeTrack.getLongName();
    }

}
