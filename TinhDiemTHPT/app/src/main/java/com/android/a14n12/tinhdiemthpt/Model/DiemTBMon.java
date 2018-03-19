package com.android.a14n12.tinhdiemthpt.Model;

/**
 * Created by Nhi on 3/19/2018.
 */

public class DiemTBMon {
    private int maMonHoc, hocKi;
    private Float diemTB;

    public DiemTBMon(int maMonHoc, int hocKi, Float diemTB) {
        this.maMonHoc = maMonHoc;
        this.hocKi = hocKi;
        this.diemTB = diemTB;
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

    public Float getDiemTB() {
        return diemTB;
    }

    public void setDiemTB(Float diemTB) {
        this.diemTB = diemTB;
    }
}
