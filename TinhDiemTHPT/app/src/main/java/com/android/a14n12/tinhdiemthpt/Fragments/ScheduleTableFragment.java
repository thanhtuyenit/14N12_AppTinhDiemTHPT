package com.android.a14n12.tinhdiemthpt.Fragments;


import android.app.Dialog;
import android.app.Fragment;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.a14n12.tinhdiemthpt.Database.DatabaseTinhDiemTHPT;
import com.android.a14n12.tinhdiemthpt.Database.MySharedPreferences;
import com.android.a14n12.tinhdiemthpt.Model.ScheduleTable;
import com.android.a14n12.tinhdiemthpt.Model.Subject;
import com.android.a14n12.tinhdiemthpt.R;
import com.android.a14n12.tinhdiemthpt.Service.MyAlarmManage;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by Nhi on 3/19/2018.
 */
public class ScheduleTableFragment extends Fragment {
    private MySharedPreferences mySharedPreferences;
    private TableLayout schedule_table;
    final int row_count = 18;
    private DatabaseTinhDiemTHPT mDatabase;
    private SQLiteDatabase db;
    private ArrayList<Subject> listSubject;
    private EditText edt_sub;
    private ArrayList<ScheduleTable> scheduleTableArrayList;
    private TableRow[] row;
    private TextView[] row_text;
    public MyAlarmManage myAlarmManage;

    public ScheduleTableFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_thoi_khoa_bieu, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        connectDB();
        listSubject = mDatabase.getMonHoc();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    public void init() {
        mySharedPreferences = MySharedPreferences.getInstance(getActivity());
        myAlarmManage = new MyAlarmManage(getActivity(),mDatabase,mySharedPreferences);
        scheduleTableArrayList = new ArrayList<>();
        schedule_table = getView().findViewById(R.id.sche_table);
        row = new TableRow[row_count];
        row_text = new TextView[row_count];

        for (int i = 0; i < row_count; i++) {
            final int time;
            final int period;
            if ((i == 0) || (i == 7) || (i == 14)) {
                continue;
            } else if (i > 0 && i < 7) {
                time = 1;
                period = i;
            } else if (i > 7 && i < 14) {
                time = 2;
                period = i - 7;
            } else {
                time = 3;
                period = i - 14;
            }
            row[i] = (TableRow) schedule_table.getChildAt(i);
            for (int j = 0; j < 8; j++) {
                final int day = j;
                row_text[j] = (TextView) row[i].getChildAt(j);
                row_text[j].setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        if ((getNamebyId(v).equals("no-id")) || (getNamebyId(v).charAt(getNamebyId(v).length() - 1) == '1'))
                            return;
                        else {
                            showDialogEdit(v.getId(), period, day, time);

                            Log.d(TAG, "onClick: time ===> " + time + " period ===> " + period + " day ==> " + day);
                        }
                    }
                });
            }

        }

    }

    private static String getNamebyId(View view) {
        if (view.getId() == -0x1) return "no-id";
        else {
            Log.d("dskfjdskjlksjk====>>> ", "getNamebyId: " + view.getResources().getResourceName(view.getId()).charAt(1));
            return view.getResources().getResourceName(view.getId());
        }
    }


    private void showDialogEdit(int textViewId, final int numOfPeriod, final int day, final int time) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.dialog_sua_tkbieu);

        Button btnCl = dialog.findViewById(R.id.btn_Close);
        Button btnUp = dialog.findViewById(R.id.btn_Update);
        Button btnAdd = dialog.findViewById(R.id.btn_AddMore);

        final TextView textViewSub = getActivity().findViewById(textViewId);

        String str = getNamebyId(textViewSub).split("/")[1];

        edt_sub = dialog.findViewById(R.id.editTextSubject);
        edt_sub.setText(textViewSub.getText().toString());

        btnCl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                textViewSub.setText(edt_sub.getText().toString());

                ScheduleTable scheduleTable = mDatabase.getScheduleBytime(time, day, numOfPeriod);
                if (scheduleTable == null) {
                    Log.d(TAG, "onClick: " + "scheduleTable null");
                    //insert new record
                    scheduleTable = new ScheduleTable(0, time, numOfPeriod, edt_sub.getText().toString(), day);

                    if (mDatabase.insertSchedule(scheduleTable)) {
                        Toast.makeText(getActivity(), "Cập nhập Thời Khóa Biểu thành công", Toast.LENGTH_LONG).show();
                        if(mySharedPreferences.getIsSetAlarm()){
                            Log.d(TAG, "onClick: "+" set alarm true");
                            myAlarmManage.cancelAllAlarm();
                            myAlarmManage.setAlarmForSchedule();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Cập nhập Thời Khóa Biểu không thành công", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Log.d(TAG, "onClick: " + "scheduleTable NOT null");
                    //update record
                    scheduleTable.setNameSubject(edt_sub.getText().toString());
                    if (mDatabase.updateSchedule(scheduleTable)) {
                        if(mySharedPreferences.getIsSetAlarm()){
                            myAlarmManage.cancelAllAlarm();
                            myAlarmManage.setAlarmForSchedule();
                        }
                        Toast.makeText(getActivity(), "Cập nhập Thời Khóa Biểu thành công", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getActivity(), "Cập nhập Thời Khóa Biểu không thành công", Toast.LENGTH_LONG).show();
                    }
                }
                dialog.dismiss();
                loadData();

            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSubjectInfo();
            }
        });

        dialog.show();
    }

    private void showSubjectInfo() {
        final Dialog dialogChoose = new Dialog(getActivity());
        dialogChoose.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialogChoose.setContentView(R.layout.layout_dialog_chonmonhoc);

        final Spinner spinnerSubject = dialogChoose.findViewById(R.id.spn_mon_hoc2);

        Button btn_Dong = dialogChoose.findViewById(R.id.btn_Close1);
        Button btn_CapNhat = dialogChoose.findViewById(R.id.btn_Update1);

        ArrayAdapter<Subject> adapter =
                new ArrayAdapter<Subject>(getActivity(), android.R.layout.simple_spinner_item, listSubject);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerSubject.setAdapter(adapter);

        btn_Dong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogChoose.dismiss();
            }
        });

        btn_CapNhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edt_sub.setText(spinnerSubject.getSelectedItem().toString());
                dialogChoose.dismiss();
            }
        });


        dialogChoose.show();

    }

    private void connectDB() {
        mDatabase = new DatabaseTinhDiemTHPT(getActivity());
        mDatabase.createDatabase();
        db = mDatabase.openDatabase();
    }

    public void loadData() {

        scheduleTableArrayList = mDatabase.getScheduleTable();
        int time, period, day;
        for (int i = 0; i < row_count; i++) {
            row[i] = (TableRow) schedule_table.getChildAt(i);
            if ((i == 0) || (i == 7) || (i == 14)) {
                continue;
            } else if (i > 0 && i < 7) {
                time = 1;
                period = i;
            } else if (i > 7 && i < 14) {
                time = 2;
                period = i - 7;
            } else {
                time = 3;
                period = i - 14;
            }

            for (int j = 0; j < 8; j++) {
                row_text[j] = (TextView) row[i].getChildAt(j);
                day = j;
                ScheduleTable scheduleTable = mDatabase.getScheduleBytime(time, day, period);
                if (scheduleTable != null) {

                    row_text[j].setText(scheduleTable.getNameSubject());
                }
            }

        }


    }
}
