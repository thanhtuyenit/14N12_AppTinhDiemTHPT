package com.android.a14n12.tinhdiemthpt.Fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.a14n12.tinhdiemthpt.R;

/**
 * Created by Nhi on 3/19/2018.
 */
public class SuKienFragment extends Fragment {


    public SuKienFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_su_kien, container, false);
    }

}
