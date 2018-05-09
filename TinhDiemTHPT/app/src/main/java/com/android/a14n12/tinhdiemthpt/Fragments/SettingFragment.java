package com.android.a14n12.tinhdiemthpt.Fragments;


import android.app.Fragment;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.android.a14n12.tinhdiemthpt.Database.DatabaseTinhDiemTHPT;
import com.android.a14n12.tinhdiemthpt.Database.MySharedPreferences;
import com.android.a14n12.tinhdiemthpt.R;
import com.android.a14n12.tinhdiemthpt.Service.MyAlarmManage;

/**
 * Created by Nhi on 3/19/2018.
 */
public class SettingFragment extends Fragment {
    private CheckBox cbxAlarm;
    private MySharedPreferences mySharedPreferences;
    private MyAlarmManage alarmManage;
    private DatabaseTinhDiemTHPT mDatabase;
    private SQLiteDatabase db;

    public SettingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cbxAlarm = view.findViewById(R.id.cbx_alarm);
        connectDB();
        mySharedPreferences = MySharedPreferences.getInstance(getActivity());
        alarmManage = new MyAlarmManage(getActivity(),mDatabase);
        if(cbxAlarm.isChecked()){
            cbxAlarm.setChecked(true);
        }
        cbxAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cbxAlarm.isChecked()){
                    mySharedPreferences.setIsSetAlarm(true);
                    alarmManage.cancelAllAlarm();
                    alarmManage.setAlarmForSchedule();
                }else{
                    mySharedPreferences.setIsSetAlarm(false);
                }
            }
        });
    }
    private void connectDB() {
        mDatabase = new DatabaseTinhDiemTHPT(getActivity());
        mDatabase.createDatabase();
        db = mDatabase.openDatabase();
    }
}
