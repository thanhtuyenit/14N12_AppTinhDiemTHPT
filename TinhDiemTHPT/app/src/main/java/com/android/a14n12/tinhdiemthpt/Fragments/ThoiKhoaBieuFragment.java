package com.android.a14n12.tinhdiemthpt.Fragments;


import android.app.Dialog;
import android.app.Fragment;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.a14n12.tinhdiemthpt.Database.DatabaseTinhDiemTHPT;
import com.android.a14n12.tinhdiemthpt.Database.MySharedPreferences;
import com.android.a14n12.tinhdiemthpt.Model.MonHoc;
import com.android.a14n12.tinhdiemthpt.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Nhi on 3/19/2018.
 */
public class ThoiKhoaBieuFragment extends Fragment {

    private TableLayout schedule_table;
    final int row_count = 18;
    private DatabaseTinhDiemTHPT mDatabase;
    private SQLiteDatabase db;
    private ArrayList<MonHoc> listMonHoc;
    private EditText edt_sub;

    public ThoiKhoaBieuFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_thoi_khoa_bieu, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        connectDB();
        listMonHoc = mDatabase.getMonHoc();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    public void init() {
        schedule_table = getView().findViewById(R.id.sche_table);
        final TableRow[] row = new TableRow[row_count];
        final TextView[] row_text = new TextView[row_count];

        for (int i = 0; i < row_count; i++) {
            if ((i == 0) || (i == 7) || (i == 14)) {
                continue;
            }
            row[i] = (TableRow) schedule_table.getChildAt(i);
            for (int j = 0; j < 8; j++) {
                row_text[j] = (TextView) row[i].getChildAt(j);
                row_text[j].setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        if ((getNamebyId(v).equals("no-id")) || (getNamebyId(v).charAt(getNamebyId(v).length() - 1) == '1'))
                            return;
                        else {
                            showDialogEdit(v.getId());
                        }
                    }
                });
            }

        }

    }

    private static String getNamebyId(View view) {
        if (view.getId() == -0x1) return "no-id";
        else return view.getResources().getResourceName(view.getId());
    }


    private void showDialogEdit(int textViewId) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.dialog_sua_tkbieu);

        Button btnCl = dialog.findViewById(R.id.btn_Close);
        Button btnUp = dialog.findViewById(R.id.btn_Update);
        Button btnAdd = dialog.findViewById(R.id.btn_AddMore);

        final TextView textViewSub = getActivity().findViewById(textViewId);

        String str = getNamebyId(textViewSub).split("/")[1];

        edt_sub = dialog.findViewById(R.id.editTextSubject);
        edt_sub.setText(textViewSub.getText().toString());

        btnCl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textViewSub.setText(edt_sub.getText().toString());
                Toast.makeText(getActivity(), "Cập nhập Thời Khóa Biểu thành công",
                        Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSubjectInfo();
            }
        });

        dialog.show();
    }

    private void showSubjectInfo() {
        final Dialog dialogChoose = new Dialog(getActivity());
        dialogChoose.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialogChoose.setContentView(R.layout.layout_dialog_chonmonhoc);

        final Spinner spinnerSubject = dialogChoose.findViewById(R.id.spn_mon_hoc2);

        Button btn_Dong = dialogChoose.findViewById(R.id.btn_Close1);
        Button btn_CapNhat = dialogChoose.findViewById(R.id.btn_Update1);

        ArrayAdapter<MonHoc> adapter =
                new ArrayAdapter<MonHoc>(getActivity(), android.R.layout.simple_spinner_item, listMonHoc);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerSubject.setAdapter(adapter);

        btn_Dong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogChoose.dismiss();
            }
        });

        btn_CapNhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edt_sub.setText(spinnerSubject.getSelectedItem().toString());
                dialogChoose.dismiss();
            }
        });


        dialogChoose.show();

    }

    private void connectDB() {
        mDatabase = new DatabaseTinhDiemTHPT(getActivity());
        mDatabase.createDatabase();
        db = mDatabase.openDatabase();
    }
}
