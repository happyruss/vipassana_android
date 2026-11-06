package com.guidedmeditationtreks.vipassana.media;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.ServiceInfo;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.guidedmeditationtreks.vipassana.MainActivity;
import com.guidedmeditationtreks.vipassana.R;

/**
 * Keeps guided meditation playback alive when the screen locks (API 26+ foreground requirement).
 */
public class MeditationPlaybackService extends Service {

    public static final String ACTION_START = "com.guidedmeditationtreks.vipassana.action.PLAYBACK_START";
    public static final String ACTION_STOP = "com.guidedmeditationtreks.vipassana.action.PLAYBACK_STOP";
    public static final String EXTRA_TITLE = "sessionTitle";

    private static final int NOTIFICATION_ID = 9001;
    private static final String CHANNEL_ID = "vipassana_meditation_playback";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && ACTION_STOP.equals(intent.getAction())) {
            stopForeground(true);
            stopSelf();
            return START_NOT_STICKY;
        }

        String title = intent != null ? intent.getStringExtra(EXTRA_TITLE) : null;
        if (title == null || title.isEmpty()) {
            title = getString(R.string.app_name);
        }

        Notification notification = buildNotification(title);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(NOTIFICATION_ID, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK);
        } else {
            startForeground(NOTIFICATION_ID, notification);
        }
        return START_STICKY;
    }

    private Notification buildNotification(String title) {
        createChannelIfNeeded();

        Intent launch = new Intent(this, MainActivity.class);
        launch.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0,
                launch,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(getString(R.string.notification_meditation_in_progress))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .setOnlyAlertOnce(true)
                .setSilent(true)
                .build();
    }

    private void createChannelIfNeeded() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return;
        }
        NotificationManager manager = getSystemService(NotificationManager.class);
        if (manager == null) {
            return;
        }
        NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                getString(R.string.notification_channel_meditation),
                NotificationManager.IMPORTANCE_LOW
        );
        channel.setDescription(getString(R.string.notification_channel_meditation_desc));
        manager.createNotificationChannel(channel);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
