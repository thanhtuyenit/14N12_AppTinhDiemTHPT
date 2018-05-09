package com.android.a14n12.tinhdiemthpt.Activity;

import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.a14n12.tinhdiemthpt.Database.DatabaseTinhDiemTHPT;
import com.android.a14n12.tinhdiemthpt.Database.MySharedPreferences;
import com.android.a14n12.tinhdiemthpt.Fragments.BieuDoFragment;
import com.android.a14n12.tinhdiemthpt.Fragments.DiemDuKienFragment;
import com.android.a14n12.tinhdiemthpt.Fragments.SettingFragment;
import com.android.a14n12.tinhdiemthpt.Fragments.EventFragment;
import com.android.a14n12.tinhdiemthpt.Fragments.ScheduleTableFragment;
import com.android.a14n12.tinhdiemthpt.Fragments.OverviewFragment;
import com.android.a14n12.tinhdiemthpt.Fragments.HomeFragment;
import com.android.a14n12.tinhdiemthpt.R;

public class MainActivity extends AppCompatActivity {

    public  FragmentTransaction transaction;
    public  FragmentManager fragmentManager;
    private static final String TAG = "MainActivity";
    private DrawerLayout mDrawerLayout;
    private Toolbar toolbar;
    private DatabaseTinhDiemTHPT mDatabase;
    private SQLiteDatabase db;
    private TextView nav_tv_name, nav_tv_class, nav_tv_semester;
    private View nav_header;
    private MySharedPreferences mySharedPreferences;
    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mySharedPreferences = MySharedPreferences.getInstance(this);

        toolbar = findViewById(R.id.toolbar);
//        nav_header = getLayoutInflater().inflate(R.layout.nav_header, null);


        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
        openFragment(new HomeFragment());
        connectDB();

        //Vi dụ 1 trường hợp lấy dữ kiệu từ database
//        ArrayList<Subject> listMonHoc = mDatabase.getMonHoc();
//        for (int i = 0; i < listMonHoc.size(); i++)
//            System.out.println(listMonHoc.get(i).getTenMonHoc());


        mDrawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        nav_header = navigationView.getHeaderView(0);
        nav_tv_name = nav_header.findViewById(R.id.tv_name);
        nav_tv_class = nav_header.findViewById(R.id.tv_class_name);
        nav_tv_semester = nav_header.findViewById(R.id.tv_semester);

        navigationView.setCheckedItem(R.id.nav_trangChu);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                mDrawerLayout.closeDrawers();
                toolbar.setTitle(item.getTitle().toString());
                switch (item.getItemId()) {
                    case R.id.nav_trangChu:
                        openFragment(new HomeFragment());
                        break;
                    case R.id.nav_tong_quan:
                        openFragment(new OverviewFragment());
                        break;
                    case R.id.nav_diem_du_kien:
                        openFragment(new DiemDuKienFragment());
                        break;
                    case R.id.nav_thoi_khoa_bieu:
                        openFragment(new ScheduleTableFragment());
                        break;
                    case R.id.nav_bieu_do:
                        openFragment(new BieuDoFragment());
                        break;
                    case R.id.nav_su_kien:
                        openFragment(new EventFragment());
                        break;
                    case R.id.nav_cai_dat:
                        openFragment(new SettingFragment());
                        break;

                }

                return true;
            }
        });
        mDrawerLayout.addDrawerListener(
                new DrawerLayout.DrawerListener() {
                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset) {
                    }

                    @Override
                    public void onDrawerOpened(View drawerView) {
                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {
                    }

                    @Override
                    public void onDrawerStateChanged(int newState) {
                    }
                }
        );

        checkFirstUse();

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
        nav_tv_name.setText(mySharedPreferences.getName());
        nav_tv_semester.setText(getResources().getString(R.string.str_semester) + mySharedPreferences.getSemester());
        nav_tv_class.setText(getResources().getString(R.string.str_class_name) + mySharedPreferences.getClassName());

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Log.d(TAG, "onPostResume: ");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart: ");
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        Log.d(TAG, "onContentChanged: ");
    }

    private void connectDB() {
        mDatabase = new DatabaseTinhDiemTHPT(this);
        mDatabase.createDatabase();
        db = mDatabase.openDatabase();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showDialogEditInfo(View view) {
        final Dialog dialogEditInfo = new Dialog(this);
        dialogEditInfo.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialogEditInfo.setContentView(R.layout.dialog_edit_info_layout);
        dialogEditInfo.setCancelable(false);
        String arr[] = {
                "Lớp 10",
                "Lớp 11",
                "Lớp 12"};

        final EditText edtName = dialogEditInfo.findViewById(R.id.edtName);
        TextView tvHuy = dialogEditInfo.findViewById(R.id.tv_huy);
        TextView tvDongy = dialogEditInfo.findViewById(R.id.tv_dong_y);
        final Spinner spinner = dialogEditInfo.findViewById(R.id.spinner_class);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arr);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinner.setAdapter(adapter);
        final RadioGroup radioGroup = dialogEditInfo.findViewById(R.id.radioGroup);
        final RadioButton radioButton1 = dialogEditInfo.findViewById(R.id.rbt_hoc_ky1);
        final RadioButton radioButton2 = dialogEditInfo.findViewById(R.id.rbt_hoc_ky2);
        edtName.setText(mySharedPreferences.getName());
        if (mySharedPreferences.getSemester() == 1) {
            radioButton1.setChecked(true);
        } else {
            radioButton2.setChecked(true);
        }
        if (mySharedPreferences.getClassName().equals("10")) {
            spinner.setSelection(0);
        } else if (mySharedPreferences.getClassName().equals("11")) {
            spinner.setSelection(1);
        } else {
            spinner.setSelection(2);
        }

        tvHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogEditInfo.dismiss();
            }
        });
        tvDongy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mySharedPreferences.setName(edtName.getText().toString());
                nav_tv_name.setText(edtName.getText().toString());
                if (radioGroup.getCheckedRadioButtonId() == R.id.rbt_hoc_ky1) {
                    mySharedPreferences.setSemester(1);
                    nav_tv_semester.setText(getResources().getString(R.string.str_semester) + mySharedPreferences.getSemester());
                } else {
                    mySharedPreferences.setSemester(2);
                    nav_tv_semester.setText(getResources().getString(R.string.str_semester) + mySharedPreferences.getSemester());
                }
                switch (spinner.getSelectedItemPosition()) {
                    case 0:
                        Log.d(TAG, "onClick: 0");
                        mySharedPreferences.setClassName("10");
                        break;
                    case 1:
                        Log.d(TAG, "onClick: 1");
                        mySharedPreferences.setClassName("11");
                        break;
                    case 2:
                        Log.d(TAG, "onClick: 2");
                        mySharedPreferences.setClassName("12");
                        break;
                }
                nav_tv_class.setText(getResources().getString(R.string.str_class_name) + mySharedPreferences.getClassName());
                Toast.makeText(getApplicationContext(), R.string.str_update_success, Toast.LENGTH_SHORT).show();
                dialogEditInfo.dismiss();
                refreshFragment();
            }
        });
        dialogEditInfo.show();
    }

    //Mở fragment
    private void openFragment(final Fragment fragment) {
        fragmentManager = getFragmentManager();
        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_main, fragment);
        transaction.commit();
        currentFragment = fragment;

    }


    public void refreshFragment(){
        Log.d(TAG, "refreshFragment: ");
//        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        transaction = getFragmentManager().beginTransaction();
        transaction.detach(currentFragment);
        transaction.attach(currentFragment);
        transaction.commit();
        Log.d(TAG, "refreshFragment: "+currentFragment);
    }

    private void checkFirstUse(){
        if(mySharedPreferences.getIsFirstUse()){
            showDialogEditInfo(getCurrentFocus());
            mySharedPreferences.setIsFirstUse(false);
        }
    }

}
