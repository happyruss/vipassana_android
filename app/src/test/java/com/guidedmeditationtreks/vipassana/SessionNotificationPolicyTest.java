package com.guidedmeditationtreks.vipassana;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SessionNotificationPolicyTest {

    @Test
    public void requestsPostNotificationsOnceOnApi33WhenNotYetGranted() {
        assertTrue(SessionNotificationPolicy.shouldRequestPostNotifications(33, false, false));
        assertTrue(SessionNotificationPolicy.shouldRequestPostNotifications(36, false, false));
    }

    @Test
    public void doesNotRequestBelowApi33WhenAlreadyGrantedOrAlreadyRequested() {
        assertFalse(SessionNotificationPolicy.shouldRequestPostNotifications(32, false, false));
        assertFalse(SessionNotificationPolicy.shouldRequestPostNotifications(33, true, false));
        assertFalse(SessionNotificationPolicy.shouldRequestPostNotifications(33, false, true));
    }

    @Test
    public void inFlightPermissionSheetIsNotRecordedAsComplete() {
        assertFalse(SessionNotificationPolicy.shouldMarkNotificationRequestComplete(false));
        assertTrue(SessionNotificationPolicy.shouldRequestPostNotifications(33, false, false));
    }

    @Test
    public void completedPermissionSheetIsNotAskedAgain() {
        assertTrue(SessionNotificationPolicy.shouldMarkNotificationRequestComplete(true));
        assertFalse(SessionNotificationPolicy.shouldRequestPostNotifications(33, false, true));
    }

    @Test
    public void deniedNotificationPermissionStillStartsPlayback() {
        assertTrue(SessionNotificationPolicy.shouldStartPlayback(false));
        assertTrue(SessionNotificationPolicy.shouldStartPlayback(true));
    }
}
