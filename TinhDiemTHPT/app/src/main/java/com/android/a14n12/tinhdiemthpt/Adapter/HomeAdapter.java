package com.android.a14n12.tinhdiemthpt.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.a14n12.tinhdiemthpt.Database.DatabaseTinhDiemTHPT;
import com.android.a14n12.tinhdiemthpt.Database.MySharedPreferences;
import com.android.a14n12.tinhdiemthpt.Model.Score;
import com.android.a14n12.tinhdiemthpt.Model.Subject;
import com.android.a14n12.tinhdiemthpt.R;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Nhi on 4/21/2018.
 */

public class HomeAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Subject> listSubject;


    public HomeAdapter(Context context, ArrayList<Subject> listSubject) {
        Log.d("TAG", "adapter construct");
        this.context = context;
        this.listSubject = listSubject;
    }

    @Override
    public int getCount() {
        return listSubject.size();
    }

    @Override
    public Object getItem(int i) {
        return listSubject.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View row = inflater.inflate(R.layout.item_gridview_trangchu, null);

        TextView tvMonHoc = row.findViewById(R.id.tv_monhoc);
        TextView tvDiemTB = row.findViewById(R.id.tv_diem_tb);
        LinearLayout main = row.findViewById(R.id.ln_main);

        //random background color
//        int[] arr_bg_color = {R.drawable.bg_item_gridview, R.drawable.bg_item_gridview_1,
//                R.drawable.bg_item_gridview_2, R.drawable.bg_item_gridview_3, R.drawable.bg_item_gridview_4, R.drawable.bg_item_gridview_5};
//        int randomAndroidColor = arr_bg_color[new Random().nextInt(arr_bg_color.length)];
//        main.setBackgroundResource(R.drawable.bg_item_gridview);

        tvMonHoc.setText(listSubject.get(i).getTenMonHoc());
        Float average = listSubject.get(i).getAverage();
        if(average == 0.0f){
            tvDiemTB.setText("-:-");
        }else{
            tvDiemTB.setText(String.format("%.2f",listSubject.get(i).getAverage()));
        }


        return row;
    }


}
