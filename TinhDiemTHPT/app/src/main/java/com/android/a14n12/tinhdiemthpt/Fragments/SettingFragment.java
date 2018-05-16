package com.android.a14n12.tinhdiemthpt.Fragments;


import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.a14n12.tinhdiemthpt.Activity.MainActivity;
import com.android.a14n12.tinhdiemthpt.Database.DatabaseTinhDiemTHPT;
import com.android.a14n12.tinhdiemthpt.Database.MySharedPreferences;
import com.android.a14n12.tinhdiemthpt.R;
import com.android.a14n12.tinhdiemthpt.Service.MyAlarmManage;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Nhi on 3/19/2018.
 */
public class SettingFragment extends Fragment {
    private static final String TAG ="SettingFragment" ;
    private CheckBox cbxAlarm;
    private TextView tvTime,tvTurnOnAlarm;
    private MySharedPreferences mySharedPreferences;
    private MyAlarmManage alarmManage;
    private DatabaseTinhDiemTHPT mDatabase;
    private SQLiteDatabase db;
    private Date calendar;

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
        cbxAlarm.setClickable(false);
        tvTime = view.findViewById(R.id.tv_time);
        tvTurnOnAlarm = view.findViewById(R.id.tv_turn_on_alarm);
        calendar = new Date();
        connectDB();
        mySharedPreferences = MySharedPreferences.getInstance(getActivity());
        alarmManage = new MyAlarmManage(getActivity(), mDatabase,mySharedPreferences);
        if (mySharedPreferences.getIsSetAlarm()) {
            cbxAlarm.setChecked(true);
        }
        tvTurnOnAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!cbxAlarm.isChecked()) {
                    showTimePickerDialog();

                } else {
                    cbxAlarm.setChecked(false);
                    mySharedPreferences.setIsSetAlarm(false);
                    alarmManage.cancelAllAlarm();
                    tvTime.setTextColor(getResources().getColor(R.color.color_button_number));
                }
            }
        });

        Date date = new Date();
        date.setDate(0);
        if (mySharedPreferences.getHoursAlarm() == 0) {
            mySharedPreferences.setHoursAlarm(20);
            date.setHours(20);
        } else {
            date.setHours(mySharedPreferences.getHoursAlarm());
        }
        date.setMinutes(mySharedPreferences.getMinuteAlarm());
        SimpleDateFormat dt = new SimpleDateFormat("HH:mm");
        tvTime.setText(dt.format(date).toString());
        tvTime.setTag(date.getTime());
    }

    private void connectDB() {
        mDatabase = new DatabaseTinhDiemTHPT(getActivity());
        mDatabase.createDatabase();
        db = mDatabase.openDatabase();
    }


    /**
     * Hàm hiển thị TimePickerDialog
     */
    public void showTimePickerDialog() {
        TimePickerDialog.OnTimeSetListener callback = new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker view,
                                  int hourOfDay, int minute) {

//                tvTime.setText
//                        (hourTam + ":" + minute + (hourOfDay > 12 ? " PM" : " AM"));
                //lưu giờ thực vào tag
//                tvTime.setTag(s);
                //lưu vết lại giờ vào hourFinish
                calendar.setHours( hourOfDay);
                calendar.setMinutes( minute);
                SimpleDateFormat dt = new SimpleDateFormat("HH:mm");
                tvTime.setText(dt.format(calendar).toString());
                mySharedPreferences.setHoursAlarm(hourOfDay);
                mySharedPreferences.setMinuteAlarm(minute);
                mySharedPreferences.setIsSetAlarm(true);
                alarmManage.cancelAllAlarm();
                alarmManage.setAlarmForSchedule();
                Toast.makeText(getActivity(), "Đã bật nhắc nhở", Toast.LENGTH_SHORT).show();
                tvTime.setTextColor(getResources().getColor(R.color.color_text));
                cbxAlarm.setChecked(true);

                Log.d(TAG, "onTimeSet: "+mySharedPreferences.getHoursAlarm()+":"+mySharedPreferences.getMinuteAlarm());
            }
        };
        //các lệnh dưới này xử lý ngày giờ trong TimePickerDialog
        //sẽ giống với trên TextView khi mở nó lên
//        String s = tvTime.getTag() + "";
//        String strArr[] = s.split(":");
//        int gio = Integer.parseInt(strArr[0]);
//        int phut = Integer.parseInt(strArr[1]);
        TimePickerDialog time = new TimePickerDialog(
                getActivity(),
                callback, 20, 0, true);
        time.setTitle("Chọn giờ nhắc nhở");
        time.show();
    }
}
