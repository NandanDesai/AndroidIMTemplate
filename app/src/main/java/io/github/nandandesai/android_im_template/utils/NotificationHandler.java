package io.github.nandandesai.android_im_template.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import io.github.nandandesai.android_im_template.R;


public class NotificationHandler {
    private static int notifId=0;   //this is a temporary setup


    public static void displayNotification(Context context, String title, String content) {


        //properly construct this method to be compatible with all the versions of Android and clean up this method
        //and place it where it should be used.

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        final String NOTIF_CHANNEL_ID="peerlink_msgs";
        final String NOTIF_CHANNEL_NAME="AndroidIMTemplate Messages";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(NOTIF_CHANNEL_ID, NOTIF_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder notification = new NotificationCompat.Builder(context, NOTIF_CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(Notification.PRIORITY_HIGH)
                .setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(R.drawable.ic_launcher_foreground);

        notifId++;

        notificationManager.notify(notifId, notification.build());
    }
}
