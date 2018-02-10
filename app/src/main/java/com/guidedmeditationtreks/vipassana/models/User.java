package com.guidedmeditationtreks.vipassana.models;

/**
 * Created by aerozero on 12/22/17.
 */

public class User {

    private int completedTrackLevel;
    private int customMeditationDurationMinutes;
    private int totalSecondsInMeditation;

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


    public int getTotalSecondsInMeditation() {
        return totalSecondsInMeditation;
    }

    public void setTotalSecondsInMeditation(int totalSecondsInMeditation) {
        this.totalSecondsInMeditation = totalSecondsInMeditation;
    }
}
