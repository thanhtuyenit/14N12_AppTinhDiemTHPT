package com.android.a14n12.tinhdiemthpt.Fragments;

import android.app.Dialog;
import android.app.Fragment;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.a14n12.tinhdiemthpt.Activity.MainActivity;
import com.android.a14n12.tinhdiemthpt.Adapter.TrangChuAdapter;
import com.android.a14n12.tinhdiemthpt.Database.DatabaseTinhDiemTHPT;
import com.android.a14n12.tinhdiemthpt.Database.MySharedPreferences;
import com.android.a14n12.tinhdiemthpt.Model.Diem;
import com.android.a14n12.tinhdiemthpt.Model.DiemTBMon;
import com.android.a14n12.tinhdiemthpt.Model.MonHoc;
import com.android.a14n12.tinhdiemthpt.R;

import java.util.ArrayList;

/**
 * Created by Nhi on 3/19/2018.
 */
public class TrangChuFragment extends Fragment {
    private static final String TAG = "TrangChuFragment";
    private MySharedPreferences mySharedPreferences;
    private TextView tvClassName, tvSemester, tvDiemTB;
    private GridView lvTongQuan;
    private DatabaseTinhDiemTHPT mDatabase;
    private SQLiteDatabase db;
    private ArrayList<MonHoc> listMonHoc;
    private FloatingActionButton fab;
    private TrangChuAdapter mAdapter;

    MainActivity mainActivity;


    public TrangChuFragment() {
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
        listMonHoc = mDatabase.getMonHoc();
        mAdapter = new TrangChuAdapter(getActivity(), listMonHoc, mDatabase, mySharedPreferences,tvDiemTB);
        lvTongQuan.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        tvClassName.setText(mySharedPreferences.getClassName()+"");
        tvSemester.setText(mySharedPreferences.getSemester()+"");
//        tinhDiemTBCong(mySharedPreferences.getSemester());
        tvDiemTB.setText(String.format("%.2f",mySharedPreferences.getTotalScore()));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogNhapDiem();
            }
        });

    }

    public void init() {
        tvClassName = getView().findViewById(R.id.tv_class);
        tvSemester = getView().findViewById(R.id.tv_semester);
        tvDiemTB = getView().findViewById(R.id.tv_diem_tb);
        lvTongQuan = getView().findViewById(R.id.lv_tong_quan);
        fab = getView().findViewById(R.id.fab);
        listMonHoc = new ArrayList<>();

    }

    private void connectDB() {
        mDatabase = new DatabaseTinhDiemTHPT(getActivity());
        mDatabase.createDatabase();
        db = mDatabase.openDatabase();
    }

    private void showDialogNhapDiem() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.layout_dialog_nhapdiem);

        final RadioGroup radioGroupHeSoDiem = dialog.findViewById(R.id.radio_group_he_so_diem);
        RadioButton radioButtonHS1 = dialog.findViewById(R.id.radio_button_hs1);
        RadioButton radioButtonHS2 = dialog.findViewById(R.id.radio_button_hs2);
        RadioButton radioButtonHS3 = dialog.findViewById(R.id.radio_button_hs3);

        final Spinner spinnerMonHoc = dialog.findViewById(R.id.spn_mon_hoc);
        final EditText edtNhapDiem = dialog.findViewById(R.id.edt_nhap_diem);

        Button btnDong = dialog.findViewById(R.id.btn_dong);
        Button btnCapNhat = dialog.findViewById(R.id.btn_cap_nhat);


        ArrayAdapter<MonHoc> adapter =
                new ArrayAdapter<MonHoc>(getActivity(), android.R.layout.simple_spinner_item, listMonHoc);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerMonHoc.setAdapter(adapter);

        btnDong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnCapNhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    int heSoDiem = 1;
                    if (radioGroupHeSoDiem.getCheckedRadioButtonId() == R.id.radio_button_hs2) {
                        heSoDiem = 2;
                    } else if (radioGroupHeSoDiem.getCheckedRadioButtonId() == R.id.radio_button_hs3) {
                        heSoDiem = 3;
                    }
                    Log.d(TAG, "onClick: " + spinnerMonHoc.getSelectedItemPosition());
                    int maMonHoc = listMonHoc.get(spinnerMonHoc.getSelectedItemPosition()).getMaMonHoc();
                    Float diem = Float.parseFloat(edtNhapDiem.getText().toString());
                    int hocKi = mySharedPreferences.getSemester();

                    if (diem >= 0.0 && diem <= 10.0) {
                        //Lưu vào db
                        if(mDatabase.insertDiem(new Diem(maMonHoc,hocKi,heSoDiem,diem))){
                            Toast.makeText(getActivity(), "Cập nhật điểm thành công!", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        } else{
                            Toast.makeText(getActivity(), "Đã xảy ra lỗi khi cập nhật điểm!", Toast.LENGTH_SHORT).show();

                        }

                    } else {
                        Toast.makeText(getActivity(), "Dữ liệu bạn nhập chưa đúng, mời bạn nhập lại!", Toast.LENGTH_SHORT).show();
                    }


                } catch (NumberFormatException e) {
                    Log.d(TAG, "onClick: "+e);
                    if (edtNhapDiem.getText().toString().equals("")) {
                        Toast.makeText(getActivity(), "Bạn chưa nhập điểm", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Dữ liệu bạn nhập chưa đúng, mời bạn nhập lại!", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });


        dialog.show();
    }

//    public void tinhDiemTBCong(int hocKi){
//        float diemTBC = 0.0f;
//
//        ArrayList<DiemTBMon> listDiemTBM = mDatabase.getDiemTB(hocKi);
//        if(listDiemTBM.isEmpty()){
//            tvDiemTB.setText("-:-");
//        }else{
//            for(int i = 0; i < listDiemTBM.size(); i++){
//                diemTBC+=listDiemTBM.get(i).getDiemTB();
//                Log.d(TAG, "tinhDiemTBCong: i=> "+i +"DTB => "+diemTBC);
//            }
//            diemTBC/= listDiemTBM.size();
//            tvDiemTB.setText(String.format("%.2f", diemTBC));
//        }
//
//
//    }

}
