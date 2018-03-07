package com.guidedmeditationtreks.vipassana.managers;

import com.guidedmeditationtreks.vipassana.models.TrackTemplate;

import java.util.jar.Attributes;

/**
 * Created by aerozero on 12/22/17.
 */

public class TrackTemplateFactory {

    public static TrackTemplateFactory singleton = new TrackTemplateFactory();

    private TrackTemplate[] trackTemplates;

    private boolean requireMeditationsBeDoneInOrder = true;

    public TrackTemplateFactory() {

        trackTemplates = new TrackTemplate[11];

        trackTemplates[0] = new TrackTemplate( "Silent Meditation", "Silent Meditation", "bellstarting.ogg", "bellclosing.ogg");

        trackTemplates[1] = new TrackTemplate("Introduction", "Introduction", "introduction.ogg", null);

        trackTemplates[2] = new TrackTemplate("Shamatha", "Shamatha", "shamatha.ogg", "shamatha2.ogg");

        trackTemplates[3] = new TrackTemplate( "Anapana", "Anapana","anapana.ogg", "anapana2.ogg");

        trackTemplates[4] = new TrackTemplate(  "Focused Anapana", "Focused Anapana", "focusedanapana.ogg", "focusedanapana2.ogg");

        trackTemplates[5] = new TrackTemplate( "Vispassana", "Top To Bottom Vispassana", "toptobottom.ogg", "toptobottom2.ogg");

        trackTemplates[6] = new TrackTemplate( "Scanning Vipassana", "Part By Part Vipassana", "toptobottombottomtotop.ogg", "toptobottombottomtotop2.ogg");

        trackTemplates[7] = new TrackTemplate( "Symmetrical Vispassana", "Symmetrical Vispassana", "symmetrical.ogg", "symmetrical2.ogg");

        trackTemplates[8] = new TrackTemplate( "Sweeping Vispassana", "Sweeping Vispassana", "sweeping.ogg", "sweeping2.ogg");

        trackTemplates[9] = new TrackTemplate(  "Piercing Vispassana", "In The Moment Vispassana", "inthemoment.ogg", "inthemoment2.ogg");

        trackTemplates[10] = new TrackTemplate( "Metta", "Metta", "metapana.ogg", null);

    }

    public TrackTemplate getTrackTemplate(int index) {
        return trackTemplates[index];
    }
    public boolean getRequireMeditationsBeDoneInOrder() { return requireMeditationsBeDoneInOrder; }
    public int getTrackTemplateCount() { return trackTemplates.length; }
}
