package com.guidedmeditationtreks.vipassana;

/**
 * POST_NOTIFICATIONS is requested once on API 33+. Foreground-service playback
 * still starts when the permission is missing or denied.
 */
public final class SessionNotificationPolicy {

    public static final int POST_NOTIFICATIONS_MIN_SDK = 33;

    private SessionNotificationPolicy() {
    }

    public static boolean shouldRequestPostNotifications(int sdkInt, boolean permissionGranted, boolean sheetReturned) {
        return sdkInt >= POST_NOTIFICATIONS_MIN_SDK && !permissionGranted && !sheetReturned;
    }

    /** An in-flight permission sheet is not complete until the system returns a grant or denial. */
    public static boolean shouldMarkNotificationRequestComplete(boolean systemSheetReturned) {
        return systemSheetReturned;
    }

    @SuppressWarnings("unused")
    public static boolean shouldStartPlayback(boolean notificationPermissionGranted) {
        return true;
    }
}
