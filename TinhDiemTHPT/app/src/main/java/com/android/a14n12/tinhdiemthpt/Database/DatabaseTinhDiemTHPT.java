package com.android.a14n12.tinhdiemthpt.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.android.a14n12.tinhdiemthpt.Model.Diem;
import com.android.a14n12.tinhdiemthpt.Model.DiemTBMon;
import com.android.a14n12.tinhdiemthpt.Model.MonHoc;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by Nhi on 3/19/2018.
 */

public class DatabaseTinhDiemTHPT extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "TINHDIEMTHPT.sqlite";
    public static final String TB_MONHOC = "TB_MONHOC";
    public static final String TB_DIEM = "TB_DIEM";
    public static final String TB_DIEMTBMON = "TB_DIEM_TB_MON";
    public static final String TB_THOIKHOABIEU = "TB_THOI_KHOA_BIEU";
    public static final String TB_SUKIEN = "TB_SU_KIEN";

    public static final String TB_MONHOC_MAMONHOC = "MaMonHoc";
    public static final String TB_MONHOC_TENMONHOC = "TenMonHoc";

    public static final String TB_DIEM_MADIEM = "MaDiem";
    public static final String TB_DIEM_MAMONHOC = "MaMonHoc";
    public static final String TB_DIEM_HESO = "HeSo";
    public static final String TB_DIEM_HOCKI = "HocKi";
    public static final String TB_DIEM_DIEM = "Diem";

    public static final String TB_DIEMTBMON_MAMONHOC = "MaMonHoc";
    public static final String TB_DIEMTBMON_DIEMTB = "DiemTB";
    public static final String TB_DIEMTBMON_HOCKI = "HocKi";

    public static final String TB_THOIKHOABIEU_ID = "Id";
    public static final String TB_THOIKHOABIEU_BUOI = "Buoi";
    public static final String TB_THOIKHOABIEU_TIET = "SoTiet";
    public static final String TB_THOIKHOABIEU_MONHOC = "TenMonHoc";

    public static final String TB_SUKIEN_MASUKIEN = "MaSuKien";
    public static final String TB_SUKIEN_TENSUKIEN = "TenSuKien";
    public static final String TB_SUKIEN_LOAISUKIEN = "LoaiSuKien";
    public static final String TB_SUKIEN_THOIGIANBD = "ThoiGianBD";
    public static final String TB_SUKIEN_DIADIEM = "DiaDiem";
    public static final String TB_SUKIEN_GHICHU = "GhiChu";
    public static final String TB_SUKIEN_LOAIKIEMTRA = "LoaiKiemTra";
    public static final String TB_SUKIEN_BUOI = "BuoiKiemTra";
    public static final String TB_SUKIEN_TIET = "TietKiemTra";
    Context context;
    String path = "";

    public DatabaseTinhDiemTHPT(Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.context = context;
        path = context.getFilesDir().getParent() + "/databases/" + DATABASE_NAME;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if (newVersion != oldVersion) {
            createDatabase();
        }
    }

    public SQLiteDatabase openDatabase() {
        return SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);
    }

    public void coppyDatabase() {
        try {
            InputStream is = context.getAssets().open(DATABASE_NAME);
            OutputStream os = new FileOutputStream(path);
            byte[] buffer = new byte[1024];
            int lenght = 0;
            while ((lenght = is.read(buffer)) > 0) {
                os.write(buffer, 0, lenght);
            }
            os.flush();
            os.close();
            is.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("Log", "COPPY DATABASE");
    }

    public boolean checkDatabase() {
        SQLiteDatabase checkDB = null;
        try {
            checkDB = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (checkDB != null) {
            checkDB.close();
        }

        return checkDB != null ? true : false;
    }

    public void createDatabase() {
        boolean kt = checkDatabase();
        if (kt) {
            Log.d("LOG ", "connect database");
        } else {
            Log.d("LOG ", "can not connect database");
            this.getWritableDatabase();
            coppyDatabase();
        }

    }

    //Đây là 1 ví dụ hàm Querry tất cả Môn học từ Table TB_MONHOC trong database
    public ArrayList<MonHoc> getMonHoc() {
        ArrayList<MonHoc> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String queryAllchar = "SELECT * FROM " + TB_MONHOC;
        Cursor cs = db.rawQuery(queryAllchar, null);
        try {
            cs.moveToFirst();
            while (cs.isAfterLast() == false) {
                list.add(new MonHoc(cs.getInt(0), cs.getString(1)));
                cs.moveToNext();
            }
        } finally {
            if (cs != null) {
                cs.close();
            }
        }
        return list;
    }

    //Lấy tất vả điểm TB của cả học kì
    public ArrayList<DiemTBMon> getDiemTB(int hocKi) {
        ArrayList<DiemTBMon> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String queryAllchar = "SELECT * FROM " + TB_DIEMTBMON + " WHERE " + TB_DIEMTBMON_HOCKI + " = " + hocKi;
        Cursor cs = db.rawQuery(queryAllchar, null);
        try {
            cs.moveToFirst();
            while (!cs.isAfterLast()) {
                list.add(new DiemTBMon(cs.getInt(0), cs.getInt(2), cs.getFloat(1)));
                cs.moveToNext();
            }
        } finally {
            if (cs != null) {
                cs.close();
            }
        }
        return list;
    }

    //Lấy ra điểm TB theo từng môn học và học kì
    public Float getDiemTBfromIdMonhoc(int id_monhoc, int hocki) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TB_DIEMTBMON + " WHERE " + TB_DIEMTBMON_MAMONHOC + " = " + id_monhoc + " AND " + TB_DIEMTBMON_HOCKI + " = " + hocki;
        Log.d("TAG", "getDiemTBfromIdMonhoc: " + query);
        Cursor cs = db.rawQuery(query, null);

        Float kq = null;
        try {
            if (cs.getCount() >= 1) {
                while (cs.moveToNext()) {
                    kq = cs.getFloat(1);
                }
            }


        } finally {
            if (cs != null) {
                cs.close();
            }
        }
        return kq;
    }

    //Chèn vào bảng Diểm Tb
    public Boolean insertDiemTB(DiemTBMon diemTBMon) {
        SQLiteDatabase database = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("MaMonHoc", diemTBMon.getMaMonHoc());
        values.put("DiemTB", diemTBMon.getDiemTB());
        values.put("HocKi", diemTBMon.getHocKi());
        long i = database.insert(TB_DIEMTBMON, null, values);
        return (i != 0);
    }

    //Chỉnh sửa dữ liệu bảng DiemTB theo Mon hoc
    public boolean updateDiemTB(DiemTBMon diemTBMon) {
        SQLiteDatabase database = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("DiemTB", diemTBMon.getDiemTB());
        int i = database.update(TB_DIEMTBMON, values, " MaMonHoc = ? AND HocKi = ?",
                new String[]{String.valueOf(diemTBMon.getMaMonHoc()), String.valueOf(diemTBMon.getHocKi())});
        return (i != 0);
    }


    public ArrayList<Diem> getDiemTheoMonHoc(int maMonHoc, int hocKi) {
        ArrayList<Diem> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String queryAllchar = "SELECT * FROM " + TB_DIEM + " WHERE " + TB_DIEM_HOCKI + " = " + hocKi + " AND " + TB_DIEM_MAMONHOC + " = " + maMonHoc;
        Cursor cs = db.rawQuery(queryAllchar, null);
        try {
            cs.moveToFirst();
            while (!cs.isAfterLast()) {
                list.add(new Diem(cs.getInt(0), cs.getInt(1), cs.getInt(3), cs.getInt(2), cs.getFloat(4)));
//                Log.d("TAG", "getDiemTheoMonHoc: ma mon=> "+cs.getInt(1)+" he so +> "+cs.getInt(2)+" diem => "+cs.getFloat(4));
                cs.moveToNext();
            }
        } finally {
            if (cs != null) {
                cs.close();
            }
        }
        return list;
    }

    //Chèn vào bảng Diểm
    public Boolean insertDiem(Diem diem) {
        SQLiteDatabase database = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("MaMonHoc", diem.getMaMonHoc());
        values.put("HeSo", diem.getHeSO());
        values.put("HocKi", diem.getHocKi());
        values.put("Diem", diem.getDiem());
        long i = database.insert(TB_DIEM, null, values);
        return (i != 0);
    }

    //Chỉnh sửa dữ liệu cho bảng Diem
    public boolean updateDiem(Diem diem) {
        SQLiteDatabase database = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("Diem", diem.getDiem());
        int i = database.update(TB_DIEM, values, " MaDiem = ? ",
                new String[]{String.valueOf(diem.getMaDiem())});
        return i != 0;
    }

}
