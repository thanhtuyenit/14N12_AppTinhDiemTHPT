package com.android.a14n12.tinhdiemthpt.Fragments;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.Fragment;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.a14n12.tinhdiemthpt.Database.DatabaseTinhDiemTHPT;
import com.android.a14n12.tinhdiemthpt.Database.MySharedPreferences;
import com.android.a14n12.tinhdiemthpt.Model.Score;
import com.android.a14n12.tinhdiemthpt.Model.Subject;
import com.android.a14n12.tinhdiemthpt.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Nhi on 3/19/2018.
 */
public class ExpectedScoreFragment extends Fragment {

    private Button bt_Input;
    private EditText editText_point, editText_coeff, editText_amount;
    private TextView textView_MonLuaChon, textView_HocKi, textView_DiemTBMon, textView_DiemTBHocKi;
    private TableLayout tableLayoutDiemDuKien;
    private DatabaseTinhDiemTHPT mDatabase;
    private SQLiteDatabase db;
    private ArrayList<Subject> listMonHoc;
    private ArrayList<Score> listDiemTBMon/*, listDiemTBHocKi*/;
    private MySharedPreferences mySharedPreferences;
    private DecimalFormat precision;
    private Spinner spinnerMonHoc;
    private float diemTBHocKi = 0;
    private int sum_he_so = 0;

    public ExpectedScoreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_diem_du_kien, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mySharedPreferences = MySharedPreferences.getInstance(getActivity());
        connectDB();
        listMonHoc = mDatabase.getMonHoc();
//        listDiemTBHocKi = mDatabase.getDiemTBHocKi();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.init();
    }

    private void init() {
        precision = new DecimalFormat("0.00");

        textView_MonLuaChon = getActivity().findViewById(R.id.textView_monTB);
        textView_HocKi = getActivity().findViewById(R.id.textView_hocki);
        textView_HocKi.setText("HỌC KÌ " + String.valueOf(mySharedPreferences.getSemester()));
        textView_DiemTBMon = getActivity().findViewById(R.id.textView_DiemTbm);
        textView_DiemTBHocKi = getActivity().findViewById(R.id.textView_DiemTbHKi);

        editText_point = getActivity().findViewById(R.id.editText_Diem);
        editText_coeff = getActivity().findViewById(R.id.editText_HeSo);
        editText_coeff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog_ChonHeSo();
            }
        });
        editText_amount = getActivity().findViewById(R.id.editText_SoLuong);
        editText_amount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog_ChonSoLuong();
            }
        });

        tableLayoutDiemDuKien = getActivity().findViewById(R.id.tb_DiemDuKien);

        spinnerMonHoc = getActivity().findViewById(R.id.spinnerChonMon);
        final ArrayAdapter<Subject> adapter =
                new ArrayAdapter<Subject>(getActivity(), android.R.layout.simple_spinner_item, listMonHoc);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerMonHoc.setAdapter(adapter);
        spinnerMonHoc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                textView_MonLuaChon.setText(adapterView.getSelectedItem().toString());
                for (int j = 1; j < tableLayoutDiemDuKien.getChildCount(); j++) {
                    tableLayoutDiemDuKien.removeView(tableLayoutDiemDuKien.getChildAt(j));
                }
                listDiemTBMon = mDatabase.getDiemTBMon(adapterView.getSelectedItemPosition() + 1, mySharedPreferences.getSemester(),mySharedPreferences.getClassName());
                if (listDiemTBMon.size() != 0) {
                    int tong_he_so = 0;
                    float diemTBMon = 0;
                    for (Score diem : listDiemTBMon) {
                        diemTBMon += diem.getDiem() * diem.getHeSO();
                        tong_he_so += diem.getHeSO();
                    }
                    diemTBMon /= tong_he_so;
                    textView_DiemTBMon.setText(precision.format(diemTBMon));
                } else
                    textView_DiemTBMon.setText("-:-");
                if (sum_he_so != 0) {
                    textView_DiemTBHocKi.setText(precision.format(diemTBHocKi / sum_he_so));
                }else{
                    textView_DiemTBHocKi.setText("-:-");

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        if (listMonHoc.size() != 0) {
            for (Subject monHoc : listMonHoc) {
                listDiemTBMon = mDatabase.getDiemTBMon(monHoc.getMaMonHoc(), mySharedPreferences.getSemester(),mySharedPreferences.getClassName());
                if (listDiemTBMon.size() != 0) {
                    int tong_he_so = 0;
                    float diemTBMon = 0;
                    for (Score diem : listDiemTBMon) {
                        diemTBMon += diem.getDiem() * diem.getHeSO();
                        tong_he_so += diem.getHeSO();
                    }
                    diemTBHocKi += (diemTBMon / tong_he_so);
                    sum_he_so++;
                }
            }
            textView_DiemTBHocKi.setText(precision.format(diemTBHocKi / sum_he_so));
        }

        bt_Input = getActivity().findViewById(R.id.button_NhapDiem);
        bt_Input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText_point.getText().toString().equals("") || editText_coeff.getText().toString().equals("") || editText_amount.getText().toString().equals(""))
                    Toast.makeText(getActivity(), "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_LONG).show();
                else {
                    if (Float.parseFloat(editText_point.getText().toString()) > 10 || Float.parseFloat(editText_point.getText().toString()) < 0) {
                        Toast.makeText(getActivity(), "Bạn phải nhập điểm từ 0 đến 10!", Toast.LENGTH_LONG).show();

                    } else {
                        setInput(editText_point.getText().toString(), editText_coeff.getText().toString(), editText_amount.getText().toString());
                        calculateExpectedPoint();
                    }

                }
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    public void setInput(String point, String coeff, String amount) {
        final TableRow tb_row = new TableRow(getActivity());

        TextView tv0 = new TextView(getActivity());
        tv0.setText(point);
        tv0.setTextColor(Color.BLACK);
        tv0.setHeight(getActivity().findViewById(R.id.textView_Diem).getHeight());
        tv0.setWidth(getActivity().findViewById(R.id.textView_Diem).getWidth());
        tv0.setGravity(Gravity.CENTER);
        tv0.setPadding(0, getActivity().findViewById(R.id.textView_Diem).getPaddingTop(), 0, getActivity().findViewById(R.id.textView_Diem).getPaddingBottom());
        tv0.setBackgroundResource(R.drawable.bg_table_row_white);
        tb_row.addView(tv0);

        TextView tv1 = new TextView(getActivity());
        tv1.setText(coeff);
        tv1.setTextColor(Color.BLACK);
        tv1.setHeight(getActivity().findViewById(R.id.textView_HeSo).getHeight());
        tv1.setWidth(getActivity().findViewById(R.id.textView_HeSo).getWidth());
        tv1.setGravity(Gravity.CENTER);
        tv1.setPadding(0, getActivity().findViewById(R.id.textView_HeSo).getPaddingTop(), 0, getActivity().findViewById(R.id.textView_HeSo).getPaddingBottom());
        tv1.setBackgroundResource(R.drawable.bg_table_row_white);
        tb_row.addView(tv1);

        TextView tv2 = new TextView(getActivity());
        tv2.setText(amount);
        tv2.setTextColor(Color.BLACK);
        tv2.setHeight(getActivity().findViewById(R.id.textView_SoLuong).getHeight());
        tv2.setWidth(getActivity().findViewById(R.id.textView_SoLuong).getWidth());
        tv2.setGravity(Gravity.CENTER);
        tv2.setPadding(0, getActivity().findViewById(R.id.textView_SoLuong).getPaddingTop(), 0, getActivity().findViewById(R.id.textView_SoLuong).getPaddingBottom());
        tv2.setBackgroundResource(R.drawable.bg_table_row_white);
        tb_row.addView(tv2);

        ImageView imageView = new ImageView(getActivity());
        imageView.setImageResource(R.drawable.ic_delete_black_24dp);
        imageView.setDrawingCacheEnabled(true);
        imageView.setMinimumHeight(getActivity().findViewById(R.id.textView_Delete).getHeight());
        imageView.setMaxWidth(getActivity().findViewById(R.id.textView_Delete).getWidth());
        imageView.setPadding(0, (int) getResources().getDimension(R.dimen.image_margin), 0, (int) getResources().getDimension(R.dimen.image_margin) + 2);
        imageView.setBackgroundResource(R.drawable.bg_table_row_white);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tableLayoutDiemDuKien.removeView(tb_row);
                calculateExpectedPoint();
            }
        });
        tb_row.addView(imageView);

        tableLayoutDiemDuKien.addView(tb_row);

        editText_point.setText("");
        editText_point.onEditorAction(EditorInfo.IME_ACTION_DONE);
        editText_coeff.setText("");
        editText_coeff.onEditorAction(EditorInfo.IME_ACTION_DONE);
        editText_amount.setText("");
        editText_amount.onEditorAction(EditorInfo.IME_ACTION_DONE);
    }

    private void connectDB() {
        mDatabase = new DatabaseTinhDiemTHPT(getActivity());
        mDatabase.createDatabase();
        db = mDatabase.openDatabase();
    }

    private void showDialog_ChonHeSo() {
        final Dialog dialogHeSo = new Dialog(getActivity());
        dialogHeSo.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialogHeSo.setContentView(R.layout.dialog_chon_heso_diem);

        final RadioGroup radioGroupHeSo = dialogHeSo.findViewById(R.id.radioGroup_HeSo);

        TextView textView_DongHeSo = dialogHeSo.findViewById(R.id.tv_huy_heso);
        textView_DongHeSo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogHeSo.dismiss();
            }
        });
        TextView textView_CapNhatHeSo = dialogHeSo.findViewById(R.id.tv_dong_y_heso);
        textView_CapNhatHeSo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (radioGroupHeSo.getCheckedRadioButtonId()) {
                    case R.id.radioButton_HeSo1:
                        editText_coeff.setText(String.valueOf(1));
                        break;
                    case R.id.radioButton_HeSo2:
                        editText_coeff.setText(String.valueOf(2));
                        break;
                    case R.id.radioButton_HeSo3:
                        editText_coeff.setText(String.valueOf(3));
                        break;
                }
                dialogHeSo.dismiss();
            }
        });

        dialogHeSo.show();
    }

    private void showDialog_ChonSoLuong() {
        final Dialog dialogSoLuong = new Dialog(getActivity());
        dialogSoLuong.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialogSoLuong.setContentView(R.layout.dialog_chon_soluong_diem);

        final RadioGroup radioGroupSoLuong = dialogSoLuong.findViewById(R.id.radioGroup_SoLuong);

        TextView textView_DongSoLuong = dialogSoLuong.findViewById(R.id.tv_huy_SoLuong);
        textView_DongSoLuong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogSoLuong.dismiss();
            }
        });
        TextView textView_CapNhatSoLuong = dialogSoLuong.findViewById(R.id.tv_dong_y_SoLuong);
        textView_CapNhatSoLuong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (radioGroupSoLuong.getCheckedRadioButtonId()) {
                    case R.id.radioButton_SoLuong1:
                        editText_amount.setText(String.valueOf(1));
                        break;
                    case R.id.radioButton_SoLuong2:
                        editText_amount.setText(String.valueOf(2));
                        break;
                    case R.id.radioButton_SoLuong3:
                        editText_amount.setText(String.valueOf(3));
                        break;
                    case R.id.radioButton_SoLuong4:
                        editText_amount.setText(String.valueOf(4));
                        break;
                    case R.id.radioButton_SoLuong5:
                        editText_amount.setText(String.valueOf(5));
                        break;
                    case R.id.radioButton_SoLuong6:
                        editText_amount.setText(String.valueOf(6));
                        break;
                    case R.id.radioButton_SoLuong7:
                        editText_amount.setText(String.valueOf(7));
                        break;
                    case R.id.radioButton_SoLuong8:
                        editText_amount.setText(String.valueOf(8));
                        break;
                    case R.id.radioButton_SoLuong9:
                        editText_amount.setText(String.valueOf(9));
                        break;
                    case R.id.radioButton_SoLuong10:
                        editText_amount.setText(String.valueOf(10));
                        break;
                }
                dialogSoLuong.dismiss();
            }
        });

        dialogSoLuong.show();
    }

    private void calculateExpectedPoint() {
        listDiemTBMon = mDatabase.getDiemTBMon(spinnerMonHoc.getSelectedItemPosition() + 1, mySharedPreferences.getSemester(),mySharedPreferences.getClassName());
        int tongHeSoMonDuKien = 0, tongHeSoHocKiDuKien = 1;
        float diemTBMonDuKien = 0, diemTBHocKiDuKien = 0;

        if (listDiemTBMon.size() != 0) {
            for (Score diem : listDiemTBMon) {
                diemTBMonDuKien += diem.getDiem() * diem.getHeSO();
                tongHeSoMonDuKien += diem.getHeSO();
            }
        }
        for (int i = 1; i < tableLayoutDiemDuKien.getChildCount(); i++) {
            View child = tableLayoutDiemDuKien.getChildAt(i);
            float tempDiem = 1;
            int tempHeSo = 1;

            if (child instanceof TableRow) {
                TableRow row1 = (TableRow) child;
                for (int x = 0; x < row1.getChildCount() - 1; x++) {
                    View rowPart = row1.getChildAt(x);
                    if (rowPart instanceof TextView) {
                        TextView textViewTemp = (TextView) rowPart;
                        tempDiem *= Float.parseFloat(textViewTemp.getText().toString());
                        if (x != 0)
                            tempHeSo *= Integer.parseInt(textViewTemp.getText().toString());
                    }
                }
            }
            diemTBMonDuKien += tempDiem;
            tongHeSoMonDuKien += tempHeSo;
        }
        diemTBHocKiDuKien += (diemTBMonDuKien / tongHeSoMonDuKien);

        if (listMonHoc.size() != 0) {
            for (Subject hoc : listMonHoc) {
                if (hoc.getMaMonHoc() == (spinnerMonHoc.getSelectedItemPosition() + 1))
                    continue;

                listDiemTBMon = mDatabase.getDiemTBMon(hoc.getMaMonHoc(), mySharedPreferences.getSemester(),mySharedPreferences.getClassName());
                if (listDiemTBMon.size() != 0) {
                    int tong_he_so = 0;
                    float diemTBMon = 0;
                    for (Score diem : listDiemTBMon) {
                        diemTBMon += diem.getDiem() * diem.getHeSO();
                        tong_he_so += diem.getHeSO();
                    }
                    diemTBHocKiDuKien += diemTBMon / tong_he_so;
                    tongHeSoHocKiDuKien++;
                }
            }
        }

        if (String.valueOf(diemTBMonDuKien / tongHeSoMonDuKien).equals("NaN"))
            textView_DiemTBMon.setText("-:-");
        else textView_DiemTBMon.setText(precision.format(diemTBMonDuKien / tongHeSoMonDuKien));
        textView_DiemTBHocKi.setText(precision.format(diemTBHocKiDuKien / tongHeSoHocKiDuKien));
    }
}
