package com.example.lawsh.personalbest;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationBuilderWithBuilderAccessor;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

public class GoalToPushAdapter {

    String title;
    String message;
    MainActivity.Congratulations goalAlert;
    Activity mainActivity;
    NotificationCompat.Builder builder;
    NotificationManagerCompat notificationManager;
    Intent intent;
    PendingIntent pendingIntent;

    public GoalToPushAdapter(String title, String message, MainActivity.Congratulations goalAlert) {
        this.title = title;
        this.message = message;
        this.goalAlert = goalAlert;
        this.mainActivity = goalAlert.activity;
        intent = new Intent(mainActivity, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        pendingIntent = PendingIntent.getActivity(mainActivity, 0, intent, 0);

        builder = new NotificationCompat.Builder(mainActivity, "0")
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(message))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent);
    }

    public void show() {
        notificationManager = NotificationManagerCompat.from(mainActivity);
        notificationManager.notify(0, builder.build());
    }
}
