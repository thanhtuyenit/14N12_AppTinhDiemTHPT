package com.android.a14n12.tinhdiemthpt.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.a14n12.tinhdiemthpt.Database.MySharedPreferences;
import com.android.a14n12.tinhdiemthpt.R;

/**
 * Created by Nhi on 3/19/2018.
 */
public class TrangChuFragment extends Fragment {
    private static final String TAG = "TrangChuFragment";
    private MySharedPreferences mySharedPreferences;

    public TrangChuFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trang_chu, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mySharedPreferences = MySharedPreferences.getInstance(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();

    }
}
