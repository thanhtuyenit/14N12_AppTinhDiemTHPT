package com.android.a14n12.tinhdiemthpt.Fragments;

import android.app.Fragment;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.android.a14n12.tinhdiemthpt.Activity.DetailedOutcomesActivity;
import com.android.a14n12.tinhdiemthpt.Activity.MainActivity;
import com.android.a14n12.tinhdiemthpt.Adapter.HomeAdapter;
import com.android.a14n12.tinhdiemthpt.Database.DatabaseTinhDiemTHPT;
import com.android.a14n12.tinhdiemthpt.Database.MySharedPreferences;
import com.android.a14n12.tinhdiemthpt.Dialog.CustomDialog;
import com.android.a14n12.tinhdiemthpt.Model.Score;
import com.android.a14n12.tinhdiemthpt.Model.Subject;
import com.android.a14n12.tinhdiemthpt.R;

import java.util.ArrayList;

/**
 * Created by Nhi on 3/19/2018.
 */
public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    private MySharedPreferences mySharedPreferences;
    private TextView tvClassName, tvSemester, tvDiemTB;
    private GridView gridViewHome;
    private DatabaseTinhDiemTHPT mDatabase;
    private SQLiteDatabase db;
    private ArrayList<Subject> listSubject;
    private FloatingActionButton fab;
    private HomeAdapter mAdapter;

    MainActivity mainActivity;


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trang_chu, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mySharedPreferences = MySharedPreferences.getInstance(getActivity());
        connectDB();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");


        loadData();
        tvClassName.setText(mySharedPreferences.getClassName() + "");
        tvSemester.setText(mySharedPreferences.getSemester() + "");
        if (mySharedPreferences.getTotalScore() >=0.0f) {
            tvDiemTB.setText(String.format("%.2f", mySharedPreferences.getTotalScore()));
        } else {
            tvDiemTB.setText("-:-");
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CustomDialog(getActivity()).showDialogAddScore(listSubject, mySharedPreferences, mDatabase, HomeFragment.this);
            }
        });

        gridViewHome.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), DetailedOutcomesActivity.class);
                intent.putExtra("idSubject", listSubject.get(i).getMaMonHoc());
//                intent.putExtra("nameSubject",listSubject.get(i).getTenMonHoc());
                intent.putExtra("averageScore", listSubject.get(i).getAverage());
                startActivity(intent);
            }
        });

    }

    public void init() {
        tvClassName = getView().findViewById(R.id.tv_class);
        tvSemester = getView().findViewById(R.id.tv_semester);
        tvDiemTB = getView().findViewById(R.id.tv_diem_tb);
        gridViewHome = getView().findViewById(R.id.lv_tong_quan);
        fab = getView().findViewById(R.id.fab);
        listSubject = new ArrayList<>();

    }

    private void connectDB() {
        mDatabase = new DatabaseTinhDiemTHPT(getActivity());
        mDatabase.createDatabase();
        db = mDatabase.openDatabase();
    }

    public void loadData() {
        listSubject = mDatabase.getMonHoc();
        for (int i = 0; i < listSubject.size(); i++) {
            //Get score from DB and calculate average score
            ArrayList<Score> listScore = mDatabase.getDiemTheoMonHoc(listSubject.get(i).getMaMonHoc(), mySharedPreferences.getSemester(),mySharedPreferences.getClassName());
            if (listScore.isEmpty()) {
                listSubject.get(i).setAverage(0.0f);
                Log.d("TAG", "getView: getAverage " + listSubject.get(i).getAverage());

            } else {
                Float average = 0.0f;
                int coefficientTotal = 0;
                for (int j = 0; j < listScore.size(); j++) {
                    average += listScore.get(j).getDiem() * listScore.get(j).getHeSO();
                    coefficientTotal += listScore.get(j).getHeSO();
                }
                average /= coefficientTotal;
                listSubject.get(i).setAverage(average);
            }
        }
        tinhDiemTBCong();
        mAdapter = new HomeAdapter(getActivity(), listSubject);
        gridViewHome.setAdapter(mAdapter);
    }

    public void tinhDiemTBCong() {
        float result = 0.0f;
        int count = 0;
        for (int i = 0; i < listSubject.size(); i++) {

            if (listSubject.get(i).getAverage() != null && listSubject.get(i).getAverage() != 0.0f) {
                result += listSubject.get(i).getAverage();
                count++;
            }
        }
        result /= count;
        tvDiemTB.setText(String.format("%.2f", result));
        mySharedPreferences.setTotalScore(result);


    }


}
