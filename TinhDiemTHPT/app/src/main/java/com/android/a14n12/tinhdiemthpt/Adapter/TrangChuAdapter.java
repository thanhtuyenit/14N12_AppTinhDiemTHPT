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
import com.android.a14n12.tinhdiemthpt.Model.Diem;
import com.android.a14n12.tinhdiemthpt.Model.DiemTBMon;
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
    private TextView tvDiemTB;



    public TrangChuAdapter(Context context, ArrayList<MonHoc> listMonHoc, DatabaseTinhDiemTHPT mDatabase, MySharedPreferences mySharedPreferences,TextView tvDiemTB) {
        Log.d("TAG", "adapter construct");
        this.context = context;
        this.mySharedPreferences = mySharedPreferences;
        this.listMonHoc = listMonHoc;
        this.mDatabase = mDatabase;
        this.tvDiemTB = tvDiemTB;
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
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View row = inflater.inflate(R.layout.item_gridview_trangchu, null);
        TextView tvMonHoc = row.findViewById(R.id.tv_monhoc);
        TextView tvDiemTB = row.findViewById(R.id.tv_diem_tb);
        LinearLayout main = row.findViewById(R.id.ln_main);
        int maMonHoc = listMonHoc.get(i).getMaMonHoc();
        int hocKi =  mySharedPreferences.getSemester();
        int[] arr_bg_color = {R.drawable.bg_item_gridview, R.drawable.bg_item_gridview_1,
                R.drawable.bg_item_gridview_2, R.drawable.bg_item_gridview_3, R.drawable.bg_item_gridview_4, R.drawable.bg_item_gridview_5};
        int randomAndroidColor = arr_bg_color[new Random().nextInt(arr_bg_color.length)];
        main.setBackgroundResource(randomAndroidColor);

        tvMonHoc.setText(listMonHoc.get(i).getTenMonHoc());

        ArrayList<Diem> listDiem = mDatabase.getDiemTheoMonHoc(maMonHoc, hocKi);
        if (listDiem.isEmpty()) {
            tvDiemTB.setText("-:-");
        } else {
            Float diemtb = 0.0f;
            int tongHeSo = 0;
            for (int j = 0; j < listDiem.size(); j++) {
//                Log.d("TAG", "getView: diem "+listDiem.get(j).getDiem()+"----- he so "+listDiem.get(j).getHeSO());
                diemtb += listDiem.get(j).getDiem() * listDiem.get(j).getHeSO();
                tongHeSo += listDiem.get(j).getHeSO();


            }
            diemtb /= tongHeSo;
//            Log.d("TAG", "getView: diemTB  "+diemtb+"-----Tong he so "+tongHeSo);
            tvDiemTB.setText(String.format("%.2f", diemtb));
            if(mDatabase.getDiemTBfromIdMonhoc(maMonHoc,hocKi) == null){
                mDatabase.insertDiemTB(new DiemTBMon(maMonHoc,hocKi,diemtb));
            } else{
                mDatabase.updateDiemTB(new DiemTBMon(maMonHoc,hocKi,diemtb));
            }

        }
        tinhDiemTBCong(hocKi);
        return row;
    }

    public void tinhDiemTBCong(int hocKi){
        float diemTBC = 0.0f;

        ArrayList<DiemTBMon> listDiemTBM = mDatabase.getDiemTB(hocKi);
        if(!listDiemTBM.isEmpty()){
            for(int i = 0; i < listDiemTBM.size(); i++){
                diemTBC+=listDiemTBM.get(i).getDiemTB();
                Log.d("TAG", "tinhDiemTBCong: i=> "+i +"DTB => "+diemTBC);
            }
            diemTBC/= listDiemTBM.size();
        }
//        mySharedPreferences.setTotalScore(diemTBC);
        tvDiemTB.setText(String.format("%.2f",diemTBC));



    }
}
