package com.android.a14n12.tinhdiemthpt.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.a14n12.tinhdiemthpt.Database.DatabaseTinhDiemTHPT;
import com.android.a14n12.tinhdiemthpt.Database.MySharedPreferences;
import com.android.a14n12.tinhdiemthpt.Model.Score;
import com.android.a14n12.tinhdiemthpt.Model.Subject;
import com.android.a14n12.tinhdiemthpt.R;

import java.util.ArrayList;

/**
 * Created by Nhi on 5/8/2018.
 */

public class OverviewAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Subject> listSub;
    private DatabaseTinhDiemTHPT mDatabase;
    private MySharedPreferences mySharedPreferences;

    public OverviewAdapter(Context context, ArrayList<Subject> listSub, DatabaseTinhDiemTHPT mDatabase,MySharedPreferences mySharedPreferences) {
        this.context = context;
        this.listSub = listSub;
        this.mDatabase = mDatabase;
        this.mySharedPreferences = mySharedPreferences;
    }

    @Override
    public int getCount() {
        return listSub.size();
    }

    @Override
    public Object getItem(int i) {
        return listSub.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View row = inflater.inflate(R.layout.item_lv_overview_layout, null);

        TextView tvNameSub = row.findViewById(R.id.tv_name_sub);
        TextView tv1 = row.findViewById(R.id.tv_1);
        TextView tv2 = row.findViewById(R.id.tv_2);
        TextView tv3 = row.findViewById(R.id.tv_3);
        TextView tvTotal = row.findViewById(R.id.tv_total);

        tvNameSub.setText(listSub.get(i).getTenMonHoc());
        if(listSub.get(i).getAverage()!=0.0f){
            tvTotal.setText(String.format("%.2f",listSub.get(i).getAverage()));
        } else{
            tvTotal.setText("-:-");
        }

        for(int j =1; j<=3; j++){
            ArrayList<Score> listScore = mDatabase.getScoreByIdSubjectAndCoefficient(listSub.get(i).getMaMonHoc(),mySharedPreferences.getSemester(),j,mySharedPreferences.getClassName());
            String str = "";
            for(int k=0; k<listScore.size(); k++){
                if(k==0){
                    str+=listScore.get(k).getDiem();
                }else{
                    str+=", "+listScore.get(k).getDiem();
                }
            }
            switch (j){
                case 1:
                    tv1.setText(str);
                    break;
                case 2:
                    tv2.setText(str);
                    break;
                case 3:
                    tv3.setText(str);
                    break;
            }

        }

        return row;
    }
}
