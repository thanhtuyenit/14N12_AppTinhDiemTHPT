package com.android.a14n12.tinhdiemthpt.Database;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by Nhi on 3/20/2018.
 */

public class MySharedPreferences {
    private String CONST_NAME_DATA = "Const_name_data";
    private String CONST_NAME = "Const_name";
    private String CONST_CLASS_NAME = "Const_class_name";
    private String CONST_SEMESTER = "Const_semester";
    private String CONST_TOTAL_SCORE = "Const_total_score";
    private String CONST_IS_FIRST_USE = "is_first_use";
    private String CONST_IS_SET_ALARM = "is_set_alarm";
    private String CONST_MINUTE_ALARM = "minute_alarm";
    private String CONST_HOURS_ALARM = "hours_alarm";
    private String CONST_COME_FROM_ALARM = "come_from_alarm";
    private static MySharedPreferences instance;
    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private MySharedPreferences(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(CONST_NAME_DATA, MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static MySharedPreferences getInstance(Context context) {
        if (instance == null) {
            instance = new MySharedPreferences(context);
        }
        return instance;
    }

    //Khi sử dụng, các hàm lưu nên đặt trong OnPause(), hàm lấy gtri nên đặt trong OnResume()

    public void setName(String name) {
        editor.putString(CONST_NAME, name);
        editor.commit();
    }

    public void setClassName(int className) {
        editor.putInt(CONST_CLASS_NAME, className);
        editor.commit();
    }

    public void setSemester(int semester) {
        editor.putInt(CONST_SEMESTER, semester);
        editor.commit();
    }
    public void setMinuteAlarm(int min) {
        editor.putInt(CONST_MINUTE_ALARM, min);
        editor.commit();
    }
    public void setHoursAlarm(int hours) {
        editor.putInt(CONST_HOURS_ALARM, hours);
        editor.commit();
    }

    public void setTotalScore(Float score) {
        editor.putFloat(CONST_TOTAL_SCORE, score);
        editor.commit();
        Log.d("TAG", "setTotalScore: "+score);
    }
     public void setIsFirstUse(Boolean isFirstUse){
         editor.putBoolean(CONST_IS_FIRST_USE, isFirstUse);
         editor.commit();
     }
    public void setIsSetAlarm(Boolean isSetAlarm){
        editor.putBoolean(CONST_IS_SET_ALARM, isSetAlarm);
        editor.commit();
    }
    public void setIsComeFromAlarm(Boolean bol){
        editor.putBoolean(CONST_COME_FROM_ALARM, bol);
        editor.commit();
    }

    public String getName() {
        return sharedPreferences.getString(CONST_NAME, "");
    }

    public int getClassName() {
        return sharedPreferences.getInt(CONST_CLASS_NAME, 10);
    }

    public int getSemester() {
        return sharedPreferences.getInt(CONST_SEMESTER, 1);
    }
    public int getMinuteAlarm() {
        return sharedPreferences.getInt(CONST_MINUTE_ALARM, 0);
    }
    public int getHoursAlarm() {
        return sharedPreferences.getInt(CONST_HOURS_ALARM, 0);
    }

    public Float getTotalScore() {
        return sharedPreferences.getFloat(CONST_TOTAL_SCORE, 0.0f);
    }

    public Boolean getIsFirstUse(){
        return sharedPreferences.getBoolean(CONST_IS_FIRST_USE,true);
    }

    public Boolean getIsSetAlarm(){
        return sharedPreferences.getBoolean(CONST_IS_SET_ALARM,false);
    }
    public Boolean getIsComeFromAlarm(){
        return sharedPreferences.getBoolean(CONST_COME_FROM_ALARM,false);
    }





}
