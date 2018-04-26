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

    public void setClassName(String className) {
        editor.putString(CONST_CLASS_NAME, className);
        editor.commit();
    }

    public void setSemester(int semester) {
        editor.putInt(CONST_SEMESTER, semester);
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

    public String getName() {
        return sharedPreferences.getString(CONST_NAME, "");
    }

    public String getClassName() {
        return sharedPreferences.getString(CONST_CLASS_NAME, "");
    }

    public int getSemester() {
        return sharedPreferences.getInt(CONST_SEMESTER, 0);
    }

    public Float getTotalScore() {
        return sharedPreferences.getFloat(CONST_TOTAL_SCORE, 0);
    }

    public Boolean getIsFirstUse(){
        return sharedPreferences.getBoolean(CONST_IS_FIRST_USE,true);
    }


}
