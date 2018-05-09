package com.android.a14n12.tinhdiemthpt.Service;

import android.app.Activity;

import android.app.AlarmManager;

import android.app.PendingIntent;

import android.content.Context;
import android.content.Intent;

import android.os.Bundle;

import android.util.Log;
import android.view.View;

import android.view.View.OnClickListener;

import android.widget.TextView;

import android.widget.TimePicker;

import android.widget.Toast;

import android.widget.ToggleButton;


import com.android.a14n12.tinhdiemthpt.Database.DatabaseTinhDiemTHPT;
import com.android.a14n12.tinhdiemthpt.Model.ScheduleTable;

import java.util.ArrayList;
import java.util.Calendar;

import static android.content.ContentValues.TAG;
import static android.content.Context.ALARM_SERVICE;

/**
 * Created by Nhi on 5/7/2018.
 */

public class MyAlarmManage {
    private Activity context;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private DatabaseTinhDiemTHPT mDatabase;

    public MyAlarmManage(Activity context, DatabaseTinhDiemTHPT mDatabase) {
        this.context = context;
        this.mDatabase = mDatabase;
    }

    public void setAlarm(int day, int hours, int minute, int request) {
        Log.d("TAG", "setAlarm: " + day);
        alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, day);
        calendar.set(Calendar.HOUR_OF_DAY, hours);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        Intent myIntent = new Intent(context,
                AlarmReceiver.class);
        myIntent.putExtra("request", request);

        pendingIntent = PendingIntent.getBroadcast(context, request, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), 7 * AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    public void cancelAlarm(int id) {
        Intent myIntent = new Intent(context,
                AlarmReceiver.class);
        myIntent.putExtra("request", id);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);
//        pendingIntent.cancel();

//        removeAlarmId(context, notificationId);
    }

    public void cancelAllAlarm() {
        for (int i = 1; i <= 7; i++) {
            cancelAlarm(i);
        }
    }
    public void setAlarmForSchedule(){
        ArrayList<Integer> listAlarm = new ArrayList<>();
        ArrayList<ScheduleTable> listSchedule;
        listSchedule = mDatabase.getScheduleTable();

        for(int j=1; j<=7; j++){
            for(int i=0; i<listSchedule.size();i++){
                if(listSchedule.get(i).getDay() == j && !listSchedule.get(i).getNameSubject().equals("")){
                    listAlarm.add(j);
                    break;
                }
            }
        }

        for (int i = 0; i < listAlarm.size(); i++) {
            Log.d(TAG, "loadData: set alarm ==> " + (i + 1));
            setAlarm(listAlarm.get(i), 20, 0, listAlarm.get(i));
        }


    }
}

