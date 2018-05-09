package com.android.a14n12.tinhdiemthpt.Model;

/**
 * Created by Nhi on 3/19/2018.
 */

public class Subject {
    private int maMonHoc;
    private String tenMonHoc;
    private Float average;

    public Subject(int maMonHoc, String tenMonHoc) {
        this.maMonHoc = maMonHoc;
        this.tenMonHoc = tenMonHoc;
    }

    public Subject(int maMonHoc, String tenMonHoc, Float average) {
        this.maMonHoc = maMonHoc;
        this.tenMonHoc = tenMonHoc;
        this.average = average;
    }

    public Float getAverage() {
        return average;
    }

    public void setAverage(Float average) {
        this.average = average;
    }

    public int getMaMonHoc() {
        return maMonHoc;
    }

    public void setMaMonHoc(int maMonHoc) {
        this.maMonHoc = maMonHoc;
    }

    public String getTenMonHoc() {
        return tenMonHoc;
    }

    public void setTenMonHoc(String tenMonHoc) {
        this.tenMonHoc = tenMonHoc;
    }

    @Override
    public String toString() {
        return tenMonHoc;
    }
}
