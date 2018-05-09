package com.android.a14n12.tinhdiemthpt.Activity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.a14n12.tinhdiemthpt.Adapter.DetailOutcomesExpLvAdapter;
import com.android.a14n12.tinhdiemthpt.Database.DatabaseTinhDiemTHPT;
import com.android.a14n12.tinhdiemthpt.Database.MySharedPreferences;
import com.android.a14n12.tinhdiemthpt.Dialog.CustomDialog;
import com.android.a14n12.tinhdiemthpt.Model.Score;
import com.android.a14n12.tinhdiemthpt.R;

import java.util.ArrayList;
import java.util.HashMap;

public class DetailActivity extends AppCompatActivity {
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
    //    private CustomDialog customDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
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
//                new CustomDialog(DetailActivity.this).showDialogAddScoreForSubject(idSubject,mySharedPreferences,mDatabase);
            }
        });

        lnBtnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                customDialog.showDialogEditNameSubject(idSubject,mDatabase);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        nameSubject = mDatabase.getNameSubject(idSubject);
        toolbar.setTitle(nameSubject);
        tvScore.setText(String.format("%.2f",averageScore));
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

        mySharedPreferences = MySharedPreferences.getInstance(DetailActivity.this);
        mListheader = new ArrayList<>();
//        customDialog = new CustomDialog(DetailedOutcomesActivity.this);
        connectDB();
    }
    private void connectDB() {
        mDatabase = new DatabaseTinhDiemTHPT(DetailActivity.this);
        mDatabase.createDatabase();
        db = mDatabase.openDatabase();
    }

}
