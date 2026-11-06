package com.guidedmeditationtreks.vipassana;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SystemBarPaddingTest {

    @Test
    public void paddingUsesTheLargerInsetOnEachEdge() {
        SystemBarPadding.Edges systemBars = new SystemBarPadding.Edges(0, 24, 0, 48);
        SystemBarPadding.Edges displayCutout = new SystemBarPadding.Edges(16, 47, 0, 0);

        SystemBarPadding.Edges padding = SystemBarPadding.padding(systemBars, displayCutout);

        assertEquals(16, padding.left);
        assertEquals(47, padding.top);
        assertEquals(0, padding.right);
        assertEquals(48, padding.bottom);
    }

    @Test
    public void paddingClampsNegativeInsetsToZero() {
        SystemBarPadding.Edges systemBars = new SystemBarPadding.Edges(-4, -1, 0, 12);
        SystemBarPadding.Edges displayCutout = new SystemBarPadding.Edges(0, -8, -2, 0);

        SystemBarPadding.Edges padding = SystemBarPadding.padding(systemBars, displayCutout);

        assertEquals(0, padding.left);
        assertEquals(0, padding.top);
        assertEquals(0, padding.right);
        assertEquals(12, padding.bottom);
    }
}
