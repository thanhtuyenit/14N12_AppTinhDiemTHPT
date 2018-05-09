package com.android.a14n12.tinhdiemthpt.Model;

import java.util.Date;

/**
 * Created by Nhi on 3/19/2018.
 */

public class Event {
    private int maSuKien, loaiSuKien, loaiKiemTra, buoi, soTiet;
    private String tenSuKien, diaDiem, ghiChu;
    private Date thoiGianBD;

    public Event(int maSuKien, int loaiSuKien, String tenSuKien, String diaDiem, String ghiChu, Date thoiGianBD, int loaiKiemTra, int buoi, int soTiet) {
        this.maSuKien = maSuKien;
        this.loaiSuKien = loaiSuKien;
        this.loaiKiemTra = loaiKiemTra;
        this.buoi = buoi;
        this.soTiet = soTiet;
        this.tenSuKien = tenSuKien;
        this.diaDiem = diaDiem;
        this.ghiChu = ghiChu;
        this.thoiGianBD = thoiGianBD;
    }

    public int getMaSuKien() {
        return maSuKien;
    }

    public void setMaSuKien(int maSuKien) {
        this.maSuKien = maSuKien;
    }

    public int getLoaiSuKien() {
        return loaiSuKien;
    }

    public void setLoaiSuKien(int loaiSuKien) {
        this.loaiSuKien = loaiSuKien;
    }

    public int getLoaiKiemTra() {
        return loaiKiemTra;
    }

    public void setLoaiKiemTra(int loaiKiemTra) {
        this.loaiKiemTra = loaiKiemTra;
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

    public String getTenSuKien() {
        return tenSuKien;
    }

    public void setTenSuKien(String tenSuKien) {
        this.tenSuKien = tenSuKien;
    }

    public String getDiaDiem() {
        return diaDiem;
    }

    public void setDiaDiem(String diaDiem) {
        this.diaDiem = diaDiem;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public Date getThoiGianBD() {
        return thoiGianBD;
    }

    public void setThoiGianBD(Date thoiGianBD) {
        this.thoiGianBD = thoiGianBD;
    }
}
