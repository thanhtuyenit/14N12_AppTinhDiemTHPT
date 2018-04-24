package com.android.a14n12.tinhdiemthpt.Adapter;

import android.content.Context;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.a14n12.tinhdiemthpt.Database.DatabaseTinhDiemTHPT;
import com.android.a14n12.tinhdiemthpt.Database.MySharedPreferences;
import com.android.a14n12.tinhdiemthpt.Model.MonHoc;
import com.android.a14n12.tinhdiemthpt.R;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

/**
 * Created by Nhi on 4/21/2018.
 */

public class TrangChuAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<MonHoc> listMonHoc;
    private DatabaseTinhDiemTHPT mDatabase;
    private MySharedPreferences mySharedPreferences;


    public TrangChuAdapter(Context context, ArrayList<MonHoc> listMonHoc, DatabaseTinhDiemTHPT mDatabase, MySharedPreferences mySharedPreferences) {
        Log.d("TAG","adapter construct");
        this.context = context;
        this.mySharedPreferences = mySharedPreferences;
        this.listMonHoc = listMonHoc;
        this.mDatabase = mDatabase;
    }

    @Override
    public int getCount() {
        return listMonHoc.size();
    }

    @Override
    public Object getItem(int i) {
        return listMonHoc.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View row = inflater.inflate(R.layout.item_gridview_trangchu, null);
        TextView tvMonHoc = row.findViewById(R.id.tv_monhoc);
        TextView tvDiemTB = row.findViewById(R.id.tv_diem_tb);
        LinearLayout main = row.findViewById(R.id.ln_main);
        int[] arr_bg_color = {R.drawable.bg_item_gridview,R.drawable.bg_item_gridview_1,
                R.drawable.bg_item_gridview_2, R.drawable.bg_item_gridview_3, R.drawable.bg_item_gridview_4, R.drawable.bg_item_gridview_5};
        int randomAndroidColor = arr_bg_color[new Random().nextInt(arr_bg_color.length)];
        main.setBackgroundResource(randomAndroidColor);

        tvMonHoc.setText(listMonHoc.get(i).getTenMonHoc());
        Float diemtb = mDatabase.getDiemTBfromIdMonhoc(listMonHoc.get(i).getMaMonHoc(),1);
        if(diemtb == null){
            tvDiemTB.setText("--");
        }else{
            tvDiemTB.setText(diemtb.toString());
        }

        return row;
    }
}
