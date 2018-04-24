package com.android.a14n12.tinhdiemthpt.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.android.a14n12.tinhdiemthpt.R;

/**
 * Created by Nhi on 3/19/2018.
 */
public class TongQuanFragment extends Fragment {


    public TongQuanFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_tong_quan, container, false);
        return view;
    }


}
