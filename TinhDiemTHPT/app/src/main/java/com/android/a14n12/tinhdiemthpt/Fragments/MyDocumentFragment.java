package com.android.a14n12.tinhdiemthpt.Fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.a14n12.tinhdiemthpt.Adapter.MyDocumentAdapter;
import com.android.a14n12.tinhdiemthpt.Model.PDFDoc;
import com.android.a14n12.tinhdiemthpt.R;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Nhi on 3/19/2018.
 */
public class MyDocumentFragment extends Fragment {
    private ListView lvMyDocument;
    private MyDocumentAdapter myDocumentAdapter;

    public MyDocumentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_document, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lvMyDocument = view.findViewById(R.id.lv_document);
        myDocumentAdapter = new MyDocumentAdapter(getActivity(),getPDFs());
        lvMyDocument.setAdapter(myDocumentAdapter);
    }

    private ArrayList<PDFDoc> getPDFs()

    {
        ArrayList<PDFDoc> pdfDocs=new ArrayList<>();
        //TARGET FOLDER
        File downloadsFolder= new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"//TinhDiemTHPT/Tai lieu");

        PDFDoc pdfDoc;

        if(downloadsFolder.exists())
        {
            //GET ALL FILES IN DOWNLOAD FOLDER
            File[] files=downloadsFolder.listFiles();

            //LOOP THRU THOSE FILES GETTING NAME AND URI
            for (int i=0;i<files.length;i++)
            {
                File file=files[i];

                if(file.getPath().endsWith("pdf"))
                {
                    pdfDoc=new PDFDoc();
                    pdfDoc.setName(file.getName());
                    pdfDoc.setPath(file.getAbsolutePath());

                    pdfDocs.add(pdfDoc);
                }

            }
        }

        return pdfDocs;
    }



}
