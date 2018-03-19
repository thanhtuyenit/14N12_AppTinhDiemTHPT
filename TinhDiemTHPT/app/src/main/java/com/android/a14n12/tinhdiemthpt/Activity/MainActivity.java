package com.android.a14n12.tinhdiemthpt.Activity;

import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.a14n12.tinhdiemthpt.Database.DatabaseTinhDiemTHPT;
import com.android.a14n12.tinhdiemthpt.Fragments.BieuDoFragment;
import com.android.a14n12.tinhdiemthpt.Fragments.DiemDuKienFragment;
import com.android.a14n12.tinhdiemthpt.Fragments.SettingFragment;
import com.android.a14n12.tinhdiemthpt.Fragments.SuKienFragment;
import com.android.a14n12.tinhdiemthpt.Fragments.ThoiKhoaBieuFragment;
import com.android.a14n12.tinhdiemthpt.Fragments.TongQuanFragment;
import com.android.a14n12.tinhdiemthpt.Fragments.TrangChuFragment;
import com.android.a14n12.tinhdiemthpt.Model.MonHoc;
import com.android.a14n12.tinhdiemthpt.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private Toolbar toolbar;
    private DatabaseTinhDiemTHPT mDatabase;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
        openFragment(new TrangChuFragment());
        connectDB();

        //Vid dụ 1 trường hợp lấy dữ kiệu từ database:
        ArrayList<MonHoc> listMonHoc = mDatabase.getMonHoc();
        for (int i=0; i<listMonHoc.size(); i++)
        System.out.println(listMonHoc.get(i).getTenMonHoc());


        mDrawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_trangChu);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                mDrawerLayout.closeDrawers();
                toolbar.setTitle(item.getTitle().toString());
                switch (item.getItemId()){
                    case R.id.nav_trangChu:
                        openFragment(new TrangChuFragment());
                        break;
                    case R.id.nav_tong_quan:
                        openFragment(new TongQuanFragment());
                        break;
                    case R.id.nav_diem_du_kien:
                        openFragment(new DiemDuKienFragment());
                        break;
                    case R.id.nav_thoi_khoa_bieu:
                        openFragment(new ThoiKhoaBieuFragment());
                        break;
                    case R.id.nav_bieu_do:
                        openFragment(new BieuDoFragment());
                        break;
                    case R.id.nav_su_kien:
                        openFragment(new SuKienFragment());
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
                        Log.d("Main Log: ","onDrawerSlide");
                    }

                    @Override
                    public void onDrawerOpened(View drawerView) {
                        Log.d("Main Log: ","onDrawerOpened");
                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {
                        Log.d("Main Log: ","onDrawerClosed");
                    }

                    @Override
                    public void onDrawerStateChanged(int newState) {
                        Log.d("Main Log: ","onDrawerStateChanged");
                    }
                }
        );

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
    public void editInfo(View view){
        final Dialog dialogEditInfo = new Dialog(this);
        dialogEditInfo.setContentView(R.layout.dialog_edit_info_layout);
        EditText edtNamre = dialogEditInfo.findViewById(R.id.edtName);
        TextView tvHuy = dialogEditInfo.findViewById(R.id.tv_huy);
        TextView tvDongy = dialogEditInfo.findViewById(R.id.tv_dong_y);

        tvHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogEditInfo.dismiss();
            }
        });
        tvDongy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TO DO
                Toast.makeText(getApplicationContext(), "Click dong y", Toast.LENGTH_SHORT).show();
                dialogEditInfo.dismiss();
            }
        });
        dialogEditInfo.show();
    }
    //Mở fragment
    private void openFragment(final Fragment fragment)   {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_main, fragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }
}
