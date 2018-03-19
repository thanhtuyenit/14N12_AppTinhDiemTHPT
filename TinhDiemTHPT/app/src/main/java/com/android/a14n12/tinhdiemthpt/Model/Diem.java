package com.android.a14n12.tinhdiemthpt.Model;

/**
 * Created by Nhi on 3/19/2018.
 */

public class Diem {
    private int maDiem, maMonHoc, hocKi, heSO;
    private Float diem;

    public Diem(int maDiem, int maMonHoc, int hocKi, int heSO, Float diem) {
        this.maDiem = maDiem;
        this.maMonHoc = maMonHoc;
        this.hocKi = hocKi;
        this.heSO = heSO;
        this.diem = diem;
    }

    public int getMaDiem() {
        return maDiem;
    }

    public void setMaDiem(int maDiem) {
        this.maDiem = maDiem;
    }

    public int getMaMonHoc() {
        return maMonHoc;
    }

    public void setMaMonHoc(int maMonHoc) {
        this.maMonHoc = maMonHoc;
    }

    public int getHocKi() {
        return hocKi;
    }

    public void setHocKi(int hocKi) {
        this.hocKi = hocKi;
    }

    public int getHeSO() {
        return heSO;
    }

    public void setHeSO(int heSO) {
        this.heSO = heSO;
    }

    public Float getDiem() {
        return diem;
    }

    public void setDiem(Float diem) {
        this.diem = diem;
    }
}
