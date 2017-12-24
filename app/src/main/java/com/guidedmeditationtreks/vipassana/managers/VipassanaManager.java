package com.guidedmeditationtreks.vipassana.managers;

/**
 * Created by czar on 12/22/17.
 */

public class VipassanaManager {

    public static VipassanaManager  shared = new VipassanaManager();
    public TrackTemplateFactory trackTemplateFactory = TrackTemplateFactory.shared;

    public VipassanaManager() {

    }


}
