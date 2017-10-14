package com.example.comquimes.myapplicationd;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

/**
 * Created by comquimes on 14/10/17.
 */

public class Notification {
    int mNotificationID;
    NotificationCompat.Builder mBuilder;
    public Notification(MainActivity mainActivity) {
        mNotificationID = 999;
        mBuilder = new NotificationCompat.Builder(mainActivity)
                //.setSmallIcon(android.R.drawable.ic_notification_clear_all)
                .setSmallIcon(R.drawable.upc)
                .setContentTitle("Watch Out")
                .setContentText("Estas muy cerca de un punto de interes!")
                .setOngoing(true);


        Intent resultIntent = new Intent(mainActivity,mainActivity.getClass());

        PendingIntent resultPendingIntent = PendingIntent.getActivity(mainActivity,0,resultIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(resultPendingIntent);
    }
    public int getID(){return mNotificationID;}

    public NotificationCompat.Builder getmBuilder() {return mBuilder;}

    public void changeOnGoing(){
            this.mBuilder.setOngoing(false);

    }

}
