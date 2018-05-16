package com.android.a14n12.tinhdiemthpt.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.android.a14n12.tinhdiemthpt.Model.ScheduleTable;
import com.android.a14n12.tinhdiemthpt.Model.Score;
import com.android.a14n12.tinhdiemthpt.Model.Subject;

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
    public static final String TB_THOIKHOABIEU = "TB_THOI_KHOA_BIEU";
    public static final String TB_SUKIEN = "TB_SU_KIEN";

    public static final String TB_MONHOC_MAMONHOC = "MaMonHoc";
    public static final String TB_MONHOC_TENMONHOC = "TenMonHoc";

    public static final String TB_DIEM_MADIEM = "MaDiem";
    public static final String TB_DIEM_MAMONHOC = "MaMonHoc";
    public static final String TB_DIEM_HESO = "HeSo";
    public static final String TB_DIEM_HOCKI = "HocKi";
    public static final String TB_DIEM_DIEM = "Diem";


    public static final String TB_THOIKHOABIEU_ID = "Id";
    public static final String TB_THOIKHOABIEU_BUOI = "Buoi";
    public static final String TB_THOIKHOABIEU_TIET = "SoTiet";
    public static final String TB_THOIKHOABIEU_MONHOC = "TenMonHoc";
    public static final String TB_THOIKHOABIEU_NGAY = "Ngay";

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

    //get all Subject
    public ArrayList<Subject> getMonHoc() {
        ArrayList<Subject> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String queryAllchar = "SELECT * FROM " + TB_MONHOC;
        Cursor cs = db.rawQuery(queryAllchar, null);
        try {
            cs.moveToFirst();
            while (cs.isAfterLast() == false) {
                list.add(new Subject(cs.getInt(0), cs.getString(1)));
                cs.moveToNext();
            }
        } finally {
            if (cs != null) {
                cs.close();
            }
        }
        return list;
    }

    public ArrayList<Score> getDiemTheoMonHoc(int maMonHoc, int hocKi,int lop) {
        ArrayList<Score> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String queryAllchar = "SELECT * FROM " + TB_DIEM + " WHERE " + TB_DIEM_HOCKI + " = " + hocKi + " AND " + TB_DIEM_MAMONHOC + " = " + maMonHoc + " AND Lop = "+lop;
        Cursor cs = db.rawQuery(queryAllchar, null);
        try {
            cs.moveToFirst();
            while (!cs.isAfterLast()) {
                list.add(new Score(cs.getInt(0), cs.getInt(1), cs.getInt(3), cs.getInt(2), cs.getFloat(4)));
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

    //insert into Score table Db
    public Boolean insertDiem(Score score,int lop) {
        SQLiteDatabase database = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("MaMonHoc", score.getMaMonHoc());
        values.put("HeSo", score.getHeSO());
        values.put("HocKi", score.getHocKi());
        values.put("Diem", score.getDiem());
        values.put("Lop", lop);
        long i = database.insert(TB_DIEM, null, values);
        Log.d("TAG", "insertDiem: " + i);
        return (i > 0);
    }

//    update Score by id
    public boolean updateScoreByid(Score score) {
        SQLiteDatabase database = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(TB_DIEM_DIEM, score.getDiem());
        int i = database.update(TB_DIEM, values, " "+TB_DIEM_MADIEM+" = ? ",
                new String[]{String.valueOf(score.getMaDiem())});
        Log.d("jhfjdhs", "updateScoreByid: "+i);
        return i > 0;
    }


    //Get score by id subject and coefficient
    public ArrayList<Score> getScoreByIdSubjectAndCoefficient(int idSubject, int semester, int coefficient,int numclass) {
        ArrayList<Score> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String queryAllchar = "SELECT * FROM " + TB_DIEM + " WHERE " + TB_DIEM_HOCKI + " = " + semester + " AND " + TB_DIEM_MAMONHOC + " = " + idSubject + " AND " + TB_DIEM_HESO + " = " + coefficient+ " AND Lop = "+numclass;
        Cursor cs = db.rawQuery(queryAllchar, null);
        try {
            cs.moveToFirst();
            while (!cs.isAfterLast()) {
                list.add(new Score(cs.getInt(0), cs.getInt(1), cs.getInt(3), cs.getInt(2), cs.getFloat(4)));
//                Log.d("TAG", "getDiemTheoMonHoc: madiem=> "+cs.getInt(0)+" he so +> "+cs.getInt(2)+" diem => "+cs.getFloat(4));
                cs.moveToNext();
            }
        } finally {
            if (cs != null) {
                cs.close();
            }
        }
        return list;
    }

    //get name subject by id subject
    public String getNameSubject(int idSubject) {
        String name = null;
        SQLiteDatabase db = this.getReadableDatabase();
        String queryAllchar = "SELECT * FROM " + TB_MONHOC + " WHERE " + TB_MONHOC_MAMONHOC + " = " + idSubject;
        Cursor cs = db.rawQuery(queryAllchar, null);
        try {
            cs.moveToFirst();
            while (!cs.isAfterLast()) {
                name = cs.getString(1);
                Log.d("áhjhkjashja", "getNameSubject: "+ name);
                cs.moveToNext();

            }
        } finally {
            if (cs != null) {
                cs.close();
            }
        }
        return name;
    }

    //Update name subject
    public boolean updateNameSubject(Subject subject) {
        SQLiteDatabase database = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(TB_MONHOC_TENMONHOC, subject.getTenMonHoc());
        int i = database.update(TB_MONHOC, values, " "+TB_MONHOC_MAMONHOC+" = ? ",
                new String[]{String.valueOf(subject.getMaMonHoc())});
        return i > 0;
    }

    //Delete score by id
    public void deleteScoreById(Score score) {
        SQLiteDatabase database = this.getReadableDatabase();
        String queryAllchar = "DELETE FROM " + TB_DIEM + " WHERE " + TB_DIEM_MADIEM + " = " + score.getMaDiem();
        database.execSQL(queryAllchar);

    }

    //get Schedule table
    public ArrayList<ScheduleTable> getScheduleTable() {
        ArrayList<ScheduleTable> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String queryAllchar = "SELECT * FROM " + TB_THOIKHOABIEU ;
        Cursor cs = db.rawQuery(queryAllchar, null);
        try {
            cs.moveToFirst();
            while (cs.isAfterLast() == false) {
                list.add(new ScheduleTable(cs.getInt(0),cs.getInt(1),cs.getInt(2),cs.getString(3),cs.getInt(4)));
                cs.moveToNext();
            }
        } finally {
            if (cs != null) {
                cs.close();
            }
        }
        return list;
    }

    //Select shedule by time, period, day
    public ScheduleTable getScheduleBytime(int time, int day, int period) {
        ScheduleTable scheduleTable = null;
        SQLiteDatabase db = this.getReadableDatabase();
        String queryAllchar = "SELECT * FROM " + TB_THOIKHOABIEU + " WHERE " + TB_THOIKHOABIEU_BUOI + " = " + time +
                " AND "+TB_THOIKHOABIEU_TIET + " = "+ period+" AND "+TB_THOIKHOABIEU_NGAY +" = "+day;
        Cursor cs = db.rawQuery(queryAllchar, null);
        try {
            cs.moveToFirst();
            while (!cs.isAfterLast()) {
                scheduleTable = new ScheduleTable(cs.getInt(0),cs.getInt(1),cs.getInt(2),cs.getString(3),cs.getInt(4));
                Log.d("áhjhkjashja", "getScheduleBytime: "+ scheduleTable.getNameSubject());
                cs.moveToNext();

            }
        } finally {
            if (cs != null) {
                cs.close();
            }
        }
        return scheduleTable;
    }

    //insert schedule to db
    public Boolean insertSchedule(ScheduleTable scheduleTable) {
        SQLiteDatabase database = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(TB_THOIKHOABIEU_TIET, scheduleTable.getNumberOfPeriod());
        values.put(TB_THOIKHOABIEU_BUOI, scheduleTable.getTime());
        values.put(TB_THOIKHOABIEU_NGAY, scheduleTable.getDay());
        values.put(TB_THOIKHOABIEU_MONHOC, scheduleTable.getNameSubject());
        long i = database.insert(TB_THOIKHOABIEU, null, values);
        Log.d("TAG", "insertSchedule: " + i);
        return (i > 0);
    }

    //Update schedule
    public boolean updateSchedule(ScheduleTable scheduleTable) {
        SQLiteDatabase database = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(TB_THOIKHOABIEU_MONHOC, scheduleTable.getNameSubject());
        int i = database.update(TB_THOIKHOABIEU, values, " "+TB_THOIKHOABIEU_ID+" = ? ",
                new String[]{String.valueOf(scheduleTable.getId())});
        return i > 0;
    }

    public ArrayList<Score> getDiemTBMon(int maMonHoc, int HocKi,int lop) {
        ArrayList<Score> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String queryAllchar = "SELECT * FROM " + TB_DIEM + " WHERE " + TB_DIEM_MAMONHOC + " = " + maMonHoc + " AND " + TB_DIEM_HOCKI + " = " + HocKi+ " AND Lop = "+lop;
        Cursor cs = db.rawQuery(queryAllchar, null);
        try {
            cs.moveToFirst();
            while (!cs.isAfterLast()) {
                list.add(new Score(cs.getInt(0), cs.getInt(1), cs.getInt(3), cs.getInt(2), cs.getFloat(4)));
                cs.moveToNext();
            }
        } finally {
            if (cs != null) {
                cs.close();
            }
        }
        return list;
    }

    public void deleteAllSchedule(){
        SQLiteDatabase database = this.getReadableDatabase();
        String query = "DELETE FROM " + TB_THOIKHOABIEU;
        database.execSQL(query);
        Log.d("", "deleteAllSchedule: ");
        ArrayList<ScheduleTable> list = getScheduleTable();
        Log.d("", "deleteAllSchedule: list ===> "+list.size());
    }
}
