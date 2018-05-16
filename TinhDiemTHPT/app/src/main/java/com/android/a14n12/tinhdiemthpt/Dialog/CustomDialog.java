package com.android.a14n12.tinhdiemthpt.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.a14n12.tinhdiemthpt.Activity.DetailedOutcomesActivity;
import com.android.a14n12.tinhdiemthpt.Activity.MainActivity;
import com.android.a14n12.tinhdiemthpt.Database.DatabaseTinhDiemTHPT;
import com.android.a14n12.tinhdiemthpt.Database.MySharedPreferences;
import com.android.a14n12.tinhdiemthpt.Fragments.HomeFragment;
import com.android.a14n12.tinhdiemthpt.Model.ScheduleTable;
import com.android.a14n12.tinhdiemthpt.Model.Score;
import com.android.a14n12.tinhdiemthpt.Model.Subject;
import com.android.a14n12.tinhdiemthpt.R;

import java.util.ArrayList;

/**
 * Created by Admin on 5/3/2018.
 */

public class CustomDialog {

    private Activity activity;
    private HomeFragment homeFragment;

    public CustomDialog(Activity activity) {
        this.activity = activity;
    }

    public void showDialogAddScore(final ArrayList<Subject> listSubject, final MySharedPreferences mySharedPreferences, final DatabaseTinhDiemTHPT mDatabase, final HomeFragment homeFragment) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.layout_dialog_addscore);

        final RadioGroup radioGroupHeSoDiem = dialog.findViewById(R.id.radio_group_he_so_diem);

        final Spinner spinnerMonHoc = dialog.findViewById(R.id.spn_mon_hoc);
        final EditText edtNhapDiem = dialog.findViewById(R.id.edt_nhap_diem);

        Button btnDong = dialog.findViewById(R.id.btn_dong);
        Button btnCapNhat = dialog.findViewById(R.id.btn_cap_nhat);


        ArrayAdapter<Subject> adapter =
                new ArrayAdapter<Subject>(activity, android.R.layout.simple_spinner_item, listSubject);
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
                    Log.d("TAG", "onClick: " + spinnerMonHoc.getSelectedItemPosition());
                    int maMonHoc = listSubject.get(spinnerMonHoc.getSelectedItemPosition()).getMaMonHoc();
                    Float diem = Float.parseFloat(edtNhapDiem.getText().toString());
                    int hocKi = mySharedPreferences.getSemester();

                    if (diem >= 0.0 && diem <= 10.0) {
                        //Lưu vào db
                        if (mDatabase.insertDiem(new Score(maMonHoc, hocKi, heSoDiem, diem),mySharedPreferences.getClassName())) {
                            Toast.makeText(activity, "Cập nhật điểm thành công!", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            homeFragment.loadData();
                        } else {
                            Toast.makeText(activity, "Đã xảy ra lỗi khi cập nhật điểm!", Toast.LENGTH_SHORT).show();

                        }

                    } else {
                        Toast.makeText(activity, "Dữ liệu bạn nhập chưa đúng, mời bạn nhập lại!", Toast.LENGTH_SHORT).show();
                    }


                } catch (NumberFormatException e) {
                    Log.d("TAG", "onClick: " + e);
                    if (edtNhapDiem.getText().toString().equals("")) {
                        Toast.makeText(activity, "Bạn chưa nhập điểm", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(activity, "Dữ liệu bạn nhập chưa đúng, mời bạn nhập lại!", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });


        dialog.show();
    }

    public void showDialogAddScoreForSubject(final int idSubject, final MySharedPreferences mySharedPreferences, final DatabaseTinhDiemTHPT mDatabase, final DetailedOutcomesActivity detailedOutcomesActivity) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.layout_dialog_addscore_for_subject);

        final RadioGroup radioGroupHeSoDiem = dialog.findViewById(R.id.radio_group_he_so_diem);


        final EditText edtNhapDiem = dialog.findViewById(R.id.edt_nhap_diem);

        Button btnDong = dialog.findViewById(R.id.btn_dong);
        Button btnCapNhat = dialog.findViewById(R.id.btn_cap_nhat);


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
//                    int maMonHoc = listSubject.get(spinnerMonHoc.getSelectedItemPosition()).getMaMonHoc();
                    Float diem = Float.parseFloat(edtNhapDiem.getText().toString());
                    int hocKi = mySharedPreferences.getSemester();

                    if (diem >= 0.0 && diem <= 10.0) {
                        //Lưu vào db
                        if (mDatabase.insertDiem(new Score(idSubject, hocKi, heSoDiem, diem),mySharedPreferences.getClassName())) {
                            Toast.makeText(activity, "Cập nhật điểm thành công! " + idSubject, Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            detailedOutcomesActivity.loadData();
                        } else {
                            Toast.makeText(activity, "Đã xảy ra lỗi khi cập nhật điểm!", Toast.LENGTH_SHORT).show();

                        }

                    } else {
                        Toast.makeText(activity, "Dữ liệu bạn nhập chưa đúng, mời bạn nhập lại!", Toast.LENGTH_SHORT).show();
                    }


                } catch (NumberFormatException e) {
                    Log.d("TAG", "onClick: " + e);
                    if (edtNhapDiem.getText().toString().equals("")) {
                        Toast.makeText(activity, "Bạn chưa nhập điểm", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(activity, "Dữ liệu bạn nhập chưa đúng, mời bạn nhập lại!", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });


        dialog.show();
    }

    public void showDialogEditNameSubject(final int idSubject, final DatabaseTinhDiemTHPT mDatabase, final DetailedOutcomesActivity detailedOutcomesActivity) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_edit_name_subject_layout);

        final EditText edtNameSubject = dialog.findViewById(R.id.edt_name_subject);

        Button btnClose = dialog.findViewById(R.id.btn_close);
        Button btnUpdate = dialog.findViewById(R.id.btn_update);
        edtNameSubject.setText(mDatabase.getNameSubject(idSubject));


        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = edtNameSubject.getText().toString();
                if (name.equals("")) {
                    Toast.makeText(activity, R.string.str_update_null, Toast.LENGTH_SHORT).show();
                } else {
                    if (mDatabase.updateNameSubject(new Subject(idSubject, name))) {
                        Toast.makeText(activity, R.string.str_update_success, Toast.LENGTH_SHORT).show();
                        detailedOutcomesActivity.loadData();
                    } else {
                        Toast.makeText(activity, R.string.str_update_notsuccess, Toast.LENGTH_SHORT).show();
                    }
                    dialog.dismiss();
                }
            }
        });

        dialog.show();
    }

    public void showDialogDeleteScore(final DatabaseTinhDiemTHPT mDatabase, final Score score, final DetailedOutcomesActivity detailedOutcomesActivity) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_delete_layout);


        TextView btnClose = dialog.findViewById(R.id.tv_exit);
        TextView btnOk = dialog.findViewById(R.id.tv_ok);


        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabase.deleteScoreById(score);
                Toast.makeText(activity, "Xóa thành công", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                detailedOutcomesActivity.loadData();
            }
        });

        dialog.show();
    }

    public void showDialogEditScore(final DatabaseTinhDiemTHPT mDatabase, final Score score, final DetailedOutcomesActivity detailedOutcomesActivity) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_edit_score_layout);


        final EditText edtScore = dialog.findViewById(R.id.edt_score);

        Button btnClose = dialog.findViewById(R.id.btn_close);
        Button btnUpdate = dialog.findViewById(R.id.btn_update);
        edtScore.setText(score.getDiem().toString());
        Log.d("hjdfhkjdhfkjhak", "showDialogEditScore: ===>> "+score.getMaDiem());


        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtScore.getText().toString().equals("")) {
                    Toast.makeText(activity, R.string.str_update_null, Toast.LENGTH_SHORT).show();

                } else {
                    try {
                        score.setDiem(Float.parseFloat(edtScore.getText().toString()));
                        if (mDatabase.updateScoreByid(score)) {

                            Toast.makeText(activity, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            detailedOutcomesActivity.loadData();
                        } else {
                            Toast.makeText(activity, "Cập nhật không thành công", Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(activity, "Bạn phải nhập đúng định dạng", Toast.LENGTH_SHORT).show();

                    }
                }


            }
        });

        dialog.show();
    }

    public void showDialogExportScheduleTable(final DatabaseTinhDiemTHPT mDatabase, final MainActivity mainActivity) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_export_schedule_table_layout);


        TextView btnClose = dialog.findViewById(R.id.tv_exit);
        TextView btnOk = dialog.findViewById(R.id.tv_ok);
        final EditText edtFileName = dialog.findViewById(R.id.edt_name_file);


        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<ScheduleTable> scheduleTableArrayList = new ArrayList<>();
                scheduleTableArrayList = mDatabase.getScheduleTable();
                if(edtFileName.getText().toString().equals("")){
                    Toast.makeText(mainActivity, "Bạn phải nhập tên file", Toast.LENGTH_SHORT).show();
                }else{
                    mainActivity.saveToJson(edtFileName.getText().toString(),mainActivity.makeJSONObject(scheduleTableArrayList));
                    dialog.dismiss();
                }

            }
        });

        dialog.show();
    }
}