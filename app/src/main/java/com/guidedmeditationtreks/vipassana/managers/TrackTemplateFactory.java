package com.guidedmeditationtreks.vipassana.managers;

import com.guidedmeditationtreks.vipassana.R;
import com.guidedmeditationtreks.vipassana.models.TrackTemplate;

import java.util.jar.Attributes;

/**
 * Created by aerozero on 12/22/17.
 */

public class TrackTemplateFactory {

    public static TrackTemplateFactory shared = new TrackTemplateFactory();

    private TrackTemplate[] trackTemplates;

    public TrackTemplateFactory() {

        trackTemplates = new TrackTemplate[11];

        trackTemplates[0] = new TrackTemplate( "Silent Meditation", "Silent Meditation", R.raw.bellstarting, R.raw.bellclosing);

        trackTemplates[1] = new TrackTemplate("Introduction", "Introduction", R.raw.introduction, 0);

        trackTemplates[2] = new TrackTemplate("Shamatha", "Shamatha",R.raw.shamatha, R.raw.shamatha2);

        trackTemplates[3] = new TrackTemplate( "Anapana", "Anapana",R.raw.anapana, R.raw.anapana2);

        trackTemplates[4] = new TrackTemplate(  "Focused Anapana", "Focused Anapana",R.raw.focusedanapana, R.raw.focusedanapana2);

        trackTemplates[5] = new TrackTemplate( "Vispassana", "Vispassana", R.raw.toptobottom, R.raw.toptobottom2);

        trackTemplates[6] = new TrackTemplate( "Scanning Vipassana", "Scanning Vipassana", R.raw.toptobottombottomtotop, R.raw.toptobottombottomtotop2);

        trackTemplates[7] = new TrackTemplate( "Symmetrical Vispassana", "Symmetrical Vispassana",R.raw.symmetrical, R.raw.symmetrical2);

        trackTemplates[8] = new TrackTemplate( "Sweeping Vispassana", "Sweeping Vispassana",R.raw.symmetrical, R.raw.symmetrical2);

        trackTemplates[9] = new TrackTemplate(  "Piercing Vispassana", "In The Moment Vispassana", R.raw.inthemoment, R.raw.inthemoment2);

        trackTemplates[10] = new TrackTemplate( "Metta", "Metta", R.raw.metapana, 0);

    }

    public TrackTemplate getTrackTemplate(int index) {
        return trackTemplates[index];
    }
}
