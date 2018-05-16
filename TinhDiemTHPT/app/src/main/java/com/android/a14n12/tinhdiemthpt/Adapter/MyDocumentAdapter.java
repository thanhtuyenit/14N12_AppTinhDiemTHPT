package com.android.a14n12.tinhdiemthpt.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.a14n12.tinhdiemthpt.Activity.PDF_Activity;
import com.android.a14n12.tinhdiemthpt.Model.PDFDoc;
import com.android.a14n12.tinhdiemthpt.R;

import java.util.ArrayList;

/**
 * Created by Admin on 5/10/2018.
 */

public class MyDocumentAdapter extends BaseAdapter {
    Context c;
    ArrayList<PDFDoc> pdfDocs;

    public MyDocumentAdapter(Context c, ArrayList<PDFDoc> pdfDocs) {
        this.c = c;
        this.pdfDocs = pdfDocs;
    }

    @Override
    public int getCount() {
        return pdfDocs.size();
    }

    @Override
    public Object getItem(int i) {
        return pdfDocs.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view==null)
        {
            //INFLATE CUSTOM LAYOUT
            view= LayoutInflater.from(c).inflate(R.layout.model,viewGroup,false);
        }

        final PDFDoc pdfDoc= (PDFDoc) this.getItem(i);

        TextView nameTxt= (TextView) view.findViewById(R.id.nameTxt);
        ImageView img= (ImageView) view.findViewById(R.id.pdfImage);

        //BIND DATA
        nameTxt.setText(pdfDoc.getName());
        img.setImageResource(R.drawable.ic_file_pdf);

        //VIEW ITEM CLICK
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPDFView(pdfDoc.getPath());
            }
        });
        return view;
    }

    //OPEN PDF VIEW
    private void openPDFView(String path)
    {
        Intent i=new Intent(c,PDF_Activity.class);
        i.putExtra("PATH",path);
        c.startActivity(i);
    }
}
