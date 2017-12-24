//package com.guidedmeditationtreks.vipassana.managers;
//
//import com.guidedmeditationtreks.vipassana.R;
//import com.guidedmeditationtreks.vipassana.models.TrackTemplate;
//
//import java.util.jar.Attributes;
//
///**
// * Created by aerozero on 12/22/17.
// */
//
//public class TrackTemplateFactory {
//
//    public static TrackTemplateFactory shared = new TrackTemplateFactory();
//
//    public TrackTemplate[] trackTemplates;
//
//
//    public TrackTemplateFactory() {
//        trackTemplates = new TrackTemplate[10];
//
//        trackTemplates[0] = new TrackTemplate("Introduction", R.raw.introduction, 0, "introduction.ogg", null, context);
//
//        trackTemplates[1] = new TrackTemplate("Shamatha","shamatha.ogg", "shamatha2.ogg");
//
//        trackTemplates[2] = new TrackTemplate( "Anapana", "anapana.ogg", "anapana2.ogg");
//
//        trackTemplates[3] = new TrackTemplate(  "Focused Anapana", "focusedanapana.ogg", "focusedanapana2.ogg");
//
//        trackTemplates[4] = new TrackTemplate( "Top to Bottom Vispassana", "toptobottom.ogg", "toptobottom2.ogg");
//
//        trackTemplates[5] = new TrackTemplate( "Top to Bottom Bottom to Top", "toptobottombottomtotop.ogg",  "toptobottombottomtotop2.ogg");
//
//        trackTemplates[6] = new TrackTemplate( "Symmetrical Vispassana", "symmetricalvispassana.ogg",  "symmetricalvispassana2.ogg");
//
//        trackTemplates[7] = new TrackTemplate( "Sweeping Vispassana", "sweepingvispassana.ogg", "sweepingvispassana2.ogg");
//
//        trackTemplates[8] = new TrackTemplate(  "In The Moment Vispassana", "inthemoment.ogg", "inthemoment2.ogg");
//
//        trackTemplates[9] = new TrackTemplate( "Metta", "metapana.ogg",  null);
//
//
//    }
//
//    public TrackTemplate[] getTrackTemplates() {
//        return trackTemplates;
//    }
//}
