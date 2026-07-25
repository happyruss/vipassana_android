package com.guidedmeditationtreks.vipassana;

import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        Context appContext = ApplicationProvider.getApplicationContext();
        assertEquals("com.guidedmeditationtreks.vipassana", appContext.getPackageName());
    }
}
