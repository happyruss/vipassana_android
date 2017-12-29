package com.guidedmeditationtreks.vipassana.models;

/**
 * Created by aerozero on 12/22/17.
 */

public class User {

    private int completedTrackLevel;
    private int customMeditationDurationMinutes;

    public boolean isAllowedToAccessLevel(int requestedLevel) {
        return requestedLevel <= this.completedTrackLevel + 1;
    }

    public int getCompletedTrackLevel() {
        return completedTrackLevel;
    }

    public void setCompletedTrackLevel(int completedTrackLevel) {
        this.completedTrackLevel = completedTrackLevel;
    }

    public int getCustomMeditationDurationMinutes() {
        return customMeditationDurationMinutes;
    }

    public void setCustomMeditationDurationMinutes(int customMeditationDurationMinutes) {
        this.customMeditationDurationMinutes = customMeditationDurationMinutes;
    }


}
