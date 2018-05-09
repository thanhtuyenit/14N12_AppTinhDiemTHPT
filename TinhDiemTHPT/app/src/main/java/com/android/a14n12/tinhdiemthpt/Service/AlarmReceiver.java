package com.android.a14n12.tinhdiemthpt.Service;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.android.a14n12.tinhdiemthpt.Activity.MainActivity;
import com.android.a14n12.tinhdiemthpt.R;

import java.util.Calendar;
import java.util.Date;


public class AlarmReceiver extends BroadcastReceiver {
    Context context;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override

    public void onReceive(final Context context, Intent intent) {
        this.context = context;
        int request = intent.getIntExtra("request", 0);
        Log.d("AlarmReceiver", "onReceive: " + request);
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        if(day == request){
            sendNotification( context, request);

        }




    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void sendNotification( Context context, int request) {
        Log.d("AlarmReceiver", "sendNotification: ");


        // NotificationManager class to notify the user of events
        NotificationManagerCompat alarmNotificationManager = NotificationManagerCompat.from(context);

        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                new Intent(context, MainActivity.class), 0);

        // set icon, title, sound and message for notification
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Notification n = new Notification.Builder(context)
                .setContentTitle("Bạn có lịch học ngày mai")
                .setContentText("Bấm để xem lại thời khóa biểu")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentIntent(contentIntent)
                .setSound(uri)
                .setAutoCancel(true).build();

        alarmNotificationManager.notify(request, n);


    }



}