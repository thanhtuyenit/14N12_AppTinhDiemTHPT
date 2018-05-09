package com.android.a14n12.tinhdiemthpt.Activity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.a14n12.tinhdiemthpt.Adapter.DetailOutcomesExpLvAdapter;
import com.android.a14n12.tinhdiemthpt.Database.DatabaseTinhDiemTHPT;
import com.android.a14n12.tinhdiemthpt.Database.MySharedPreferences;
import com.android.a14n12.tinhdiemthpt.Dialog.CustomDialog;
import com.android.a14n12.tinhdiemthpt.Model.Score;
import com.android.a14n12.tinhdiemthpt.R;

import java.util.ArrayList;
import java.util.HashMap;

public class DetailedOutcomesActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private LinearLayout lnBtnAdd, lnBtnEdit;
    private int idSubject;
    private String nameSubject;
    private DatabaseTinhDiemTHPT mDatabase;
    private SQLiteDatabase db;
    private MySharedPreferences mySharedPreferences;
    private TextView tvNameSubject, tvScore;
    private Float averageScore = 0.0f;
    private ArrayList<String> mListheader;
    private HashMap<String, ArrayList<Score>> mData;
    private DetailOutcomesExpLvAdapter adapterExpLV;
    private ExpandableListView explvScore;
    private CustomDialog customDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_outcomes);
        init();

        Intent intent = getIntent();
        idSubject = intent.getIntExtra("idSubject",0);
        averageScore = intent.getFloatExtra("averageScore", 0);



        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        lnBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customDialog.showDialogAddScoreForSubject(idSubject,mySharedPreferences,mDatabase,DetailedOutcomesActivity.this);
            }
        });

        lnBtnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customDialog.showDialogEditNameSubject(idSubject,mDatabase,DetailedOutcomesActivity.this);
            }
        });



    }

    @Override
    protected void onResume() {
        super.onResume();

        tvScore.setText(String.format("%.2f",averageScore));
        loadData();
    }

    private void init(){
        toolbar = findViewById(R.id.toolbar);
        lnBtnAdd = findViewById(R.id.ln_btn_add);
        lnBtnEdit = findViewById(R.id.ln_btn_edit);
        tvScore = findViewById(R.id.tv_score);
        explvScore = findViewById(R.id.explv_score);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);

        actionbar.setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));

        mySharedPreferences = MySharedPreferences.getInstance(DetailedOutcomesActivity.this);
        mListheader = new ArrayList<>();
        customDialog = new CustomDialog(DetailedOutcomesActivity.this);
        connectDB();
    }

    private void connectDB() {
        mDatabase = new DatabaseTinhDiemTHPT(DetailedOutcomesActivity.this);
        mDatabase.createDatabase();
        db = mDatabase.openDatabase();
    }

    public void loadData(){
        Log.d("DETAIL", "loadData: ");
        nameSubject = mDatabase.getNameSubject(idSubject);
        toolbar.setTitle(nameSubject);
        if(mListheader.size() ==0){
            mListheader.add(getString(R.string.str_text_diemheso1));
            mListheader.add(getString(R.string.str_text_diemheso2));
            mListheader.add(getString(R.string.str_text_diemheso3));
        }
        mData = new HashMap<>();
        for (int i = 0; i < mListheader.size(); i++) {
            mData.put(mListheader.get(i), getScoreByIdAndCoefficient(i+1));
        }
        adapterExpLV = new DetailOutcomesExpLvAdapter(DetailedOutcomesActivity.this, mListheader, mData,mDatabase);
        explvScore.setAdapter(adapterExpLV);
        for (int i = 0; i < mListheader.size(); i++) {
            explvScore.expandGroup(i);
        }

        ArrayList<Score> listScore = mDatabase.getDiemTheoMonHoc(idSubject, mySharedPreferences.getSemester());
        if (listScore.isEmpty()) {
            tvScore.setText("-:-");

        } else {
            Float average = 0.0f;
            int coefficientTotal = 0;
            for (int j = 0; j < listScore.size(); j++) {
                average += listScore.get(j).getDiem() * listScore.get(j).getHeSO();
                coefficientTotal += listScore.get(j).getHeSO();
            }
            average /= coefficientTotal;
            tvScore.setText(String.format("%.2f",average));
        }

    }

    private ArrayList<Score> getScoreByIdAndCoefficient(int coefficient){
        ArrayList<Score> list = mDatabase.getScoreByIdSubjectAndCoefficient(idSubject,mySharedPreferences.getSemester(),coefficient);
        Log.d("LOG", "getScoreByIdAndCoefficient: "+list.size());
        return list;
    }
}
