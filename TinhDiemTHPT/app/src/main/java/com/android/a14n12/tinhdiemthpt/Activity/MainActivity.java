package com.android.a14n12.tinhdiemthpt.Activity;

import android.Manifest;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.a14n12.tinhdiemthpt.Database.DatabaseTinhDiemTHPT;
import com.android.a14n12.tinhdiemthpt.Database.MySharedPreferences;
import com.android.a14n12.tinhdiemthpt.Dialog.CustomDialog;
import com.android.a14n12.tinhdiemthpt.Fragments.BieuDoFragment;
import com.android.a14n12.tinhdiemthpt.Fragments.ExpectedScoreFragment;
import com.android.a14n12.tinhdiemthpt.Fragments.SettingFragment;
import com.android.a14n12.tinhdiemthpt.Fragments.MyDocumentFragment;
import com.android.a14n12.tinhdiemthpt.Fragments.ScheduleTableFragment;
import com.android.a14n12.tinhdiemthpt.Fragments.OverviewFragment;
import com.android.a14n12.tinhdiemthpt.Fragments.HomeFragment;
import com.android.a14n12.tinhdiemthpt.Model.ScheduleTable;
import com.android.a14n12.tinhdiemthpt.R;
import com.android.a14n12.tinhdiemthpt.Service.MyAlarmManage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int FILE_SELECT_CODE = 11;
    public FragmentTransaction transaction;
    public FragmentManager fragmentManager;
    private static final String TAG = "MainActivity";
    private DrawerLayout mDrawerLayout;
    private Toolbar toolbar;
    private DatabaseTinhDiemTHPT mDatabase;
    private SQLiteDatabase db;
    private TextView nav_tv_name, nav_tv_class, nav_tv_semester;
    private View nav_header;
    private MySharedPreferences mySharedPreferences;
    private Fragment currentFragment;
    private LinearLayout lnImport, lnExport;
    private CustomDialog customDialog;
    public MyAlarmManage myAlarmManage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mySharedPreferences = MySharedPreferences.getInstance(this);

        toolbar = findViewById(R.id.toolbar);


        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
        openFragment(new HomeFragment());
        connectDB();
        saveAllDocument();
        customDialog = new CustomDialog(this);
        myAlarmManage = new MyAlarmManage(this,mDatabase,mySharedPreferences);



        mDrawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        nav_header = navigationView.getHeaderView(0);
        nav_tv_name = nav_header.findViewById(R.id.tv_name);
        nav_tv_class = nav_header.findViewById(R.id.tv_class_name);
        nav_tv_semester = nav_header.findViewById(R.id.tv_semester);
        lnExport = findViewById(R.id.ln_export);
        lnImport = findViewById(R.id.ln_import);

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
                        lnImport.setVisibility(View.GONE);
                        lnExport.setVisibility(View.GONE);
                        break;
                    case R.id.nav_tong_quan:
                        openFragment(new OverviewFragment());
                        lnImport.setVisibility(View.GONE);
                        lnExport.setVisibility(View.GONE);
                        break;
                    case R.id.nav_diem_du_kien:
                        openFragment(new ExpectedScoreFragment());
                        lnImport.setVisibility(View.GONE);
                        lnExport.setVisibility(View.GONE);
                        break;
                    case R.id.nav_thoi_khoa_bieu:
                        openFragment(new ScheduleTableFragment());
                        lnImport.setVisibility(View.VISIBLE);
                        lnExport.setVisibility(View.VISIBLE);
                        break;
                    case R.id.nav_bieu_do:
                        openFragment(new BieuDoFragment());
                        lnImport.setVisibility(View.GONE);
                        lnExport.setVisibility(View.GONE);
                        break;
                    case R.id.nav_su_kien:
                        openFragment(new MyDocumentFragment());
                        lnImport.setVisibility(View.GONE);
                        lnExport.setVisibility(View.GONE);
                        break;
                    case R.id.nav_cai_dat:
                        openFragment(new SettingFragment());
                        lnImport.setVisibility(View.GONE);
                        lnExport.setVisibility(View.GONE);
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
        lnExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickExport(view);

            }
        });
        lnImport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickImport(view);
//                readFileJson("nhitest");
            }
        });


        checkFirstUse();
        checkComeFromAlarm();
        checkAllPermisions();
    }
    public void clickImport(View view){
        Log.d(TAG, "onClick: Import ");
        showFileChooser();

    }
    public void clickExport(View view){
        customDialog.showDialogExportScheduleTable(mDatabase, MainActivity.this);
        Log.d(TAG, "onClick: Export ");

    }


    private void checkComeFromAlarm() {
        Intent i = this.getIntent();
        if (i == null) {
            Log.d(TAG, "checkComeFromAlarm: " + "null");

        } else {
            if (i.getStringExtra("goto") == null) {
                Log.d(TAG, "checkComeFromAlarm: " + "hello");
            } else if (i.getStringExtra("goto").equals("Schedule")) {
                Log.d(TAG, "checkComeFromAlarm: " + "from alarm");
            }
        }
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
        if (mySharedPreferences.getClassName()==10) {
            spinner.setSelection(0);
        } else if (mySharedPreferences.getClassName()==11) {
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
                if(!edtName.getText().toString().trim().equals("")){
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
                            mySharedPreferences.setClassName(10);
                            break;
                        case 1:
                            Log.d(TAG, "onClick: 1");
                            mySharedPreferences.setClassName(11);
                            break;
                        case 2:
                            Log.d(TAG, "onClick: 2");
                            mySharedPreferences.setClassName(12);
                            break;
                    }
                    nav_tv_class.setText(getResources().getString(R.string.str_class_name) + mySharedPreferences.getClassName());
                    Toast.makeText(getApplicationContext(), R.string.str_update_success, Toast.LENGTH_SHORT).show();
                    dialogEditInfo.dismiss();
                    refreshFragment();
                } else{
                    Toast.makeText(MainActivity.this, "Bạn phải nhập tên", Toast.LENGTH_SHORT).show();
                }

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


    public void refreshFragment() {
        Log.d(TAG, "refreshFragment: ");
//        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        transaction = getFragmentManager().beginTransaction();
        transaction.detach(currentFragment);
        transaction.attach(currentFragment);
        transaction.commit();
        Log.d(TAG, "refreshFragment: " + currentFragment);
    }

    private void checkFirstUse() {
        if (mySharedPreferences.getIsFirstUse()) {
            showDialogEditInfo(getCurrentFocus());
            mySharedPreferences.setIsFirstUse(false);
            saveAllDocument();
        }
    }

    private void checkAllPermisions() {
        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.WAKE_LOCK};

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private void saveAllDocument() {
        copyAsset("bang tuan hoan.pdf");
        copyAsset("bat quy tac.pdf");
        copyAsset("dai so.pdf");
        copyAsset("hinh hoc.pdf");
        copyAsset("hoa hoc.pdf");
        copyAsset("tieng anh.pdf");
        copyAsset("vat li.pdf");
    }

    private void copyAsset(String fileName) {
        String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/TinhDiemTHPT/Tai lieu";
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        AssetManager assetManager = getAssets();
        InputStream in = null;
        OutputStream out = null;
        try {
            in = assetManager.open(fileName);
            File outFile = new File(dirPath, fileName);
            out = new FileOutputStream(outFile);
            copyFile(in, out);
            Log.d(TAG, "copyAsset: save file " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }


    }

    public JSONArray makeJSONObject(ArrayList<ScheduleTable> scheduleTableArrayList) {
        JSONArray jsonArray = new JSONArray();

        try {
            for (ScheduleTable scheduleTable : scheduleTableArrayList) {
                JSONObject obj = new JSONObject();
                obj.put("id", scheduleTable.getId());
                obj.put("time", scheduleTable.getTime());
                obj.put("period", scheduleTable.getNumberOfPeriod());
                obj.put("subject", scheduleTable.getNameSubject());
                obj.put("day", scheduleTable.getDay());
                jsonArray.put(obj);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonArray;
    }

    public void saveToJson(String name, JSONArray jsonArray) {
        String dirpath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/TinhDiemTHPT/TKB/";
        File dir = new File(dirpath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        try {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/TinhDiemTHPT/TKB/" + name + ".json";
            Writer output = null;
            File file = new File(path);
            output = new BufferedWriter(new FileWriter(file));
            output.write(jsonArray.toString());
            output.close();
            Toast.makeText(getApplicationContext(), "Đã xuất dữ liệu vào đường dẫn " + path, Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            e.printStackTrace();
//            Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public String readFileJson(String path) {

//        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/TinhDiemTHPT/TKB/" + name + ".json";
        String jString = null;
        try {

            File yourFile = new File(path);
            FileInputStream stream = new FileInputStream(yourFile);

            try {
                FileChannel fc = stream.getChannel();
                MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
                /* Instead of using default, pass in a decoder. */
                jString = Charset.defaultCharset().decode(bb).toString();
            } finally {
                stream.close();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return jString;
    }

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        // special intent for Samsung file manager
        Intent sIntent = new Intent("com.sec.android.app.myfiles.PICK_DATA");
        // if you want any file type, you can skip next line
        sIntent.putExtra("CONTENT_TYPE", "*/*");
        sIntent.addCategory(Intent.CATEGORY_DEFAULT);

        Intent chooserIntent;
        if (getPackageManager().resolveActivity(sIntent, 0) != null) {
            // it is device with samsung file manager
            chooserIntent = Intent.createChooser(sIntent, "Open file");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{intent});
        } else {
            chooserIntent = Intent.createChooser(intent, "Open file");
        }

        try {
            startActivityForResult(chooserIntent, FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getApplicationContext(), "No suitable File Manager was found.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_SELECT_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
                Log.d(TAG, "onActivityResult: uri " + uri);
                try {
                    String path = uri.getPath();
                    Log.d(TAG, "onActivityResult: path " + path);
                    String type = path.substring(path.length() - 5);
                    if (type.equals(".json")) {
                        Log.d(TAG, "onActivityResult: json");
                        showDialogImport(path);
                    } else {
                        Log.d(TAG, "onActivityResult: NOT json");
                        Toast.makeText(this, "Import không thành công! Bạn phải chọn file định dạng .json", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void showDialogImport(final String path) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_warning_import);


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
                mDatabase.deleteAllSchedule();
                insertScheduleFromJsonArrayToDatabase(readFileJson(path));
                Toast.makeText(MainActivity.this, "Import thời khóa biểu thành công", Toast.LENGTH_SHORT).show();
                refreshFragment();
                if(mySharedPreferences.getIsSetAlarm()){
                    myAlarmManage.cancelAllAlarm();
                    myAlarmManage.setAlarmForSchedule();
                }
                dialog.dismiss();

            }
        });

        dialog.show();
    }
    private void insertScheduleFromJsonArrayToDatabase(String strJsonArr){
        try {
            JSONArray array = new JSONArray(strJsonArr);
            for(int i = 0; i<array.length();i++){
                JSONObject obj = array.getJSONObject(i);
                int id = obj.getInt("id");
                int time = obj.getInt("time");
                int period = obj.getInt("period");
                String subject = obj.getString("subject");
                int day = obj.getInt("day");
                ScheduleTable scheduleTable = new ScheduleTable(id,time,period,subject,day);
                mDatabase.insertSchedule(scheduleTable);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
