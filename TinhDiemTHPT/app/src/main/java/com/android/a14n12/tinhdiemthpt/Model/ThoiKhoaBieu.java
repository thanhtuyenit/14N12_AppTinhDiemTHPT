package com.android.a14n12.tinhdiemthpt.Model;

/**
 * Created by Nhi on 3/19/2018.
 */

public class ThoiKhoaBieu {
    private int id, buoi, soTiet;
    private String tenMonHoc;

    public ThoiKhoaBieu(int id, int buoi, int soTiet, String tenMonHoc) {
        this.id = id;
        this.buoi = buoi;
        this.soTiet = soTiet;
        this.tenMonHoc = tenMonHoc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBuoi() {
        return buoi;
    }

    public void setBuoi(int buoi) {
        this.buoi = buoi;
    }

    public int getSoTiet() {
        return soTiet;
    }

    public void setSoTiet(int soTiet) {
        this.soTiet = soTiet;
    }

    public String getTenMonHoc() {
        return tenMonHoc;
    }

    public void setTenMonHoc(String tenMonHoc) {
        this.tenMonHoc = tenMonHoc;
    }
}
