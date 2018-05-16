package com.android.a14n12.tinhdiemthpt.Fragments;


import android.app.Fragment;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.android.a14n12.tinhdiemthpt.Database.DatabaseTinhDiemTHPT;
import com.android.a14n12.tinhdiemthpt.Database.MySharedPreferences;
import com.android.a14n12.tinhdiemthpt.Model.Score;
import com.android.a14n12.tinhdiemthpt.Model.Subject;
import com.android.a14n12.tinhdiemthpt.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Nhi on 3/19/2018.
 */
public class BieuDoFragment extends Fragment {
    private MySharedPreferences mySharedPreferences;
    private DatabaseTinhDiemTHPT mDatabase;
    private SQLiteDatabase db;
    private BarChart barChart;
    private LineChart lineChart;
    private ArrayList<Subject> listMonHoc;
    private ArrayList<Score> listDiemMon;
    private DecimalFormat precision;
    private Spinner spinnerMonHoc;


    public BieuDoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bieu_do, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mySharedPreferences = MySharedPreferences.getInstance(getActivity());
        connectDB();
        listMonHoc = mDatabase.getMonHoc();
    }

    private void connectDB() {
        mDatabase = new DatabaseTinhDiemTHPT(getActivity());
        mDatabase.createDatabase();
        db = mDatabase.openDatabase();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.init();
    }

    private void init() {
        precision = new DecimalFormat("0.00");

        barChart = getActivity().findViewById(R.id.barChart1);

        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(true);
        barChart.setMaxVisibleValueCount(50);
        barChart.setPinchZoom(true);
        barChart.getDescription().setText("");
        barChart.setDrawGridBackground(false);

        ArrayList<BarEntry> barEntries = new ArrayList<>();
        int i = 0;
        int subjectHavePoint = 0;
        float diemTBHocKi = 0;
        for (Subject mon : listMonHoc) {
            listDiemMon = mDatabase.getDiemTBMon(i + 1, mySharedPreferences.getSemester(),mySharedPreferences.getClassName());
            if (listDiemMon.size() != 0) {
                int tong_he_so = 0;
                float diemTBMon = 0;
                for (Score diem : listDiemMon) {
                    diemTBMon += diem.getDiem() * diem.getHeSO();
                    tong_he_so += diem.getHeSO();
                }
                diemTBMon /= tong_he_so;
                barEntries.add(new BarEntry(i, diemTBMon));
                diemTBHocKi += diemTBMon;
                subjectHavePoint++;
            } else barEntries.add(new BarEntry(i, 0.0f));
            i++;
        }

        diemTBHocKi /= subjectHavePoint;

        BarDataSet barDataSet = new BarDataSet(barEntries, "Tổng kết chung " + String.valueOf(precision.format(diemTBHocKi)));
        barDataSet.setColors(Color.rgb(92, 214, 92), Color.rgb(255, 209, 26), Color.rgb(255, 83, 26), Color.rgb(26, 140, 255));

        BarData data = new BarData(barDataSet);
        data.setBarWidth(0.8f);
        data.setValueFormatter(new MyValueFormatter());

        barChart.setData(data);
        barChart.invalidate();
        barChart.animateY(500);

        String[] point = new String[listMonHoc.size()];
        int j = 0;
        for (Subject monHoc : listMonHoc) {
            point[j++] = monHoc.getTenMonHoc().split("\\s+")[0];
        }

        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new MyXAxisValueFormatter(point));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        xAxis.setLabelCount(listMonHoc.size());

        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setAxisMinValue(0f); // this replaces setStartAtZero
        barChart.getAxisRight().setEnabled(false);

        spinnerMonHoc = getActivity().findViewById(R.id.spinnerBieuDo);
        final ArrayAdapter<Subject> adapter =
                new ArrayAdapter<Subject>(getActivity(), android.R.layout.simple_spinner_item, listMonHoc);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerMonHoc.setAdapter(adapter);
        spinnerMonHoc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                drawLineChart(adapterView.getSelectedItemPosition() + 1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public class MyXAxisValueFormatter implements IAxisValueFormatter {
        private String[] mValues;

        public MyXAxisValueFormatter(String[] values) {
            this.mValues = values;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return mValues[(int) value];
        }
    }

    public class MyValueFormatter implements IValueFormatter {

        private DecimalFormat mFormat;

        public MyValueFormatter() {
            mFormat = new DecimalFormat("###,###,##0.00"); // use one decimal
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            // write your logic here
            return mFormat.format(value); // e.g. append a dollar-sign
        }
    }

    private void drawLineChart(int subjectID) {
        lineChart = getActivity().findViewById(R.id.lineChart1);

        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.setPinchZoom(true);
        lineChart.getDescription().setText("");

        ArrayList<Entry> entries = new ArrayList<>();

        listDiemMon = mDatabase.getDiemTBMon(subjectID, mySharedPreferences.getSemester(),mySharedPreferences.getClassName());
        if (listDiemMon.size() != 0) {
            int z = -1;
            int tong_he_so = 0;
            float diemTBMon = 0;
            for (Score diem : listDiemMon) {
                diemTBMon += diem.getDiem() * diem.getHeSO();
                tong_he_so += diem.getHeSO();
                entries.add(new BarEntry(z++, diemTBMon/tong_he_so));
            }
        } else
            entries.add(new Entry(0, 0.0f));

        LineDataSet lineDataSet = new LineDataSet(entries, "Tổng kết môn: " + spinnerMonHoc.getSelectedItem().toString());
        lineDataSet.setFillAlpha(110);
        lineDataSet.setDrawFilled(true);
        if (Utils.getSDKInt() >= 18) {
            // fill drawable only supported on api level 18 and above
            Drawable drawable = ContextCompat.getDrawable(this.getActivity(), R.drawable.fade_red);
            lineDataSet.setFillDrawable(drawable);
        } else
            lineDataSet.setFillColor(Color.BLACK);


        YAxis yAxis = lineChart.getAxisLeft();
        yAxis.setStartAtZero(true);
        lineChart.getAxisRight().setEnabled(false);
        lineChart.setHighlightPerTapEnabled(true);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet);

        LineData data = new LineData(dataSets);
        data.setValueFormatter(new MyValueFormatter());

        lineChart.invalidate();
        lineChart.animateY(500);

        lineChart.setData(data);
    }
}
