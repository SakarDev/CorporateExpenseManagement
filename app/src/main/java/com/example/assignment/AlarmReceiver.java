package com.example.assignment;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        // Get id & message from intent.
        int notificationId = intent.getIntExtra("notificationId", 0);
        String message = intent.getStringExtra("todo");

        // When notification is tapped, call MainActivity.
        Intent mainIntent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, mainIntent, 0);

//        NotificationManager myNotificationManager =
//                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        // Prepare notification.
//        Notification.Builder builder = new Notification.Builder(context);
//        builder.setSmallIcon(android.R.drawable.ic_dialog_info)
//                .setContentTitle("It's Time!")
//                .setContentText(message)
//                .setWhen(System.currentTimeMillis())
//                .setAutoCancel(true)
//                .setContentIntent(contentIntent)
//                .setPriority(Notification.PRIORITY_MAX)
//                .setDefaults(Notification.DEFAULT_ALL);
//        // Notify
//        myNotificationManager.notify(notificationId, builder.build());


        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= 26)
        {
            //When sdk version is larger than26
            String id = "channel_1";
            String description = "143";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(id, description, importance);
//                     channel.enableLights(true);
//                     channel.enableVibration(true);//
            manager.createNotificationChannel(channel);
            Notification notification = new Notification.Builder(context, id)
                    .setSmallIcon(android.R.drawable.ic_dialog_info)
                    .setContentTitle("It's Time!")
                    .setContentText(message)
                    .setWhen(System.currentTimeMillis())
                    .setAutoCancel(true)
                    .setContentIntent(contentIntent)
                    .setPriority(Notification.PRIORITY_MAX)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .build();
            manager.notify(notificationId, notification);
        }
        else
        {
            //When sdk version is less than26
            Notification notification = new NotificationCompat.Builder(context)
                    .setSmallIcon(android.R.drawable.ic_dialog_info)
                    .setContentTitle("It's Time!")
                    .setContentText(message)
                    .setContentIntent(contentIntent)
                    .build();
            manager.notify(notificationId, notification);
        }
    }
}
