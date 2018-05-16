package com.android.a14n12.tinhdiemthpt.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.android.a14n12.tinhdiemthpt.Adapter.OverviewAdapter;
import com.android.a14n12.tinhdiemthpt.Database.DatabaseTinhDiemTHPT;
import com.android.a14n12.tinhdiemthpt.Database.MySharedPreferences;
import com.android.a14n12.tinhdiemthpt.Model.Score;
import com.android.a14n12.tinhdiemthpt.Model.Subject;
import com.android.a14n12.tinhdiemthpt.R;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by Nhi on 3/19/2018.
 */
public class OverviewFragment extends Fragment {
    private ArrayList<Subject> listSubject;
    private DatabaseTinhDiemTHPT mDatabase;
    private MySharedPreferences mySharedPreferences;
    private SQLiteDatabase db;
    private OverviewAdapter overviewAdapter;
    private ListView listView;
    private TextView tvScoreTotal;


    public OverviewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tong_quan, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    private void init(View view) {
        listView = view.findViewById(R.id.lv_overview);
        tvScoreTotal = view.findViewById(R.id.tv_core_total);
        listSubject = new ArrayList<>();
        mySharedPreferences = MySharedPreferences.getInstance(getActivity());
        connectDB();

    }

    private void loadData() {
        listSubject = mDatabase.getMonHoc();
        for (int i = 0; i < listSubject.size(); i++) {
            ArrayList<Score> listScore = mDatabase.getDiemTheoMonHoc(listSubject.get(i).getMaMonHoc(), mySharedPreferences.getSemester(),mySharedPreferences.getClassName());
            if (listScore.isEmpty()) {
                listSubject.get(i).setAverage(0.0f);

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
            overviewAdapter = new OverviewAdapter(getActivity(),listSubject,mDatabase,mySharedPreferences);
            listView.setAdapter(overviewAdapter);

            Float score = mySharedPreferences.getTotalScore();

            if (score >=0.0f) {
                Log.d(TAG, "loadData: "+score);
                tvScoreTotal.setText(String.format("%.2f", score));
            } else {
                tvScoreTotal.setText("-:-");
            }



        }
    }

    private void connectDB() {
        mDatabase = new DatabaseTinhDiemTHPT(getActivity());
        mDatabase.createDatabase();
        db = mDatabase.openDatabase();
    }
}
