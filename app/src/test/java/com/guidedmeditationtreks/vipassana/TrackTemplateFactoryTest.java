package com.guidedmeditationtreks.vipassana;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.guidedmeditationtreks.vipassana.managers.TrackTemplateFactory;
import com.guidedmeditationtreks.vipassana.models.TrackTemplate;

import org.junit.Test;

public class TrackTemplateFactoryTest {

    private final TrackTemplateFactory factory = TrackTemplateFactory.singleton;

    @Test
    public void getTrackTemplateCount_returnsEleven() {
        assertEquals(11, factory.getTrackTemplateCount());
    }

    @Test
    public void silentMeditation_isMultiPart() {
        TrackTemplate track = factory.getTrackTemplate(0);
        assertEquals("Silent Meditation", track.getName());
        assertTrue(track.isMultiPart());
    }

    @Test
    public void introduction_isSinglePart() {
        TrackTemplate track = factory.getTrackTemplate(1);
        assertEquals("Introduction", track.getName());
        assertFalse(track.isMultiPart());
    }

    @Test
    public void requireMeditationsBeDoneInOrder_defaultsToTrue() {
        assertTrue(factory.getRequireMeditationsBeDoneInOrder());
    }
}
