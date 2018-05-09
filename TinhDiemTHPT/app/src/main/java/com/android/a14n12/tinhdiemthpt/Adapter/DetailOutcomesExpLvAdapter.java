package com.android.a14n12.tinhdiemthpt.Adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.a14n12.tinhdiemthpt.Activity.DetailedOutcomesActivity;
import com.android.a14n12.tinhdiemthpt.Database.DatabaseTinhDiemTHPT;
import com.android.a14n12.tinhdiemthpt.Dialog.CustomDialog;
import com.android.a14n12.tinhdiemthpt.Model.Score;
import com.android.a14n12.tinhdiemthpt.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Nhi on 5/2/2018.
 */

public class DetailOutcomesExpLvAdapter extends BaseExpandableListAdapter {
    private DetailedOutcomesActivity mContext;
    private ArrayList<String> mHeaderGroup;
    private HashMap<String, ArrayList<Score>> mDataChild;
    private DatabaseTinhDiemTHPT mDatabase;

    public DetailOutcomesExpLvAdapter(DetailedOutcomesActivity mContext, ArrayList<String> mHeaderGroup, HashMap<String, ArrayList<Score>> mDataChild, DatabaseTinhDiemTHPT mDatabase) {
        this.mContext = mContext;
        this.mHeaderGroup = mHeaderGroup;
        this.mDataChild = mDataChild;
        this.mDatabase = mDatabase;
    }

    @Override
    public int getGroupCount() {
        return mHeaderGroup.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return mDataChild.get(mHeaderGroup.get(i)).size();
    }

    @Override
    public Object getGroup(int i) {
        return mHeaderGroup.get(i);
    }

    @Override
    public Score getChild(int groupPosition, int childPosition) {
        return mDataChild.get(mHeaderGroup.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            LayoutInflater li = LayoutInflater.from(mContext);
            convertView = li.inflate(R.layout.row_groupview_explv_layout, viewGroup, false);
        }
        TextView tvHeader = convertView.findViewById(R.id.tv_header);
        ImageView imgIcon = convertView.findViewById(R.id.img_up_down);

        tvHeader.setText( ( mHeaderGroup.get(groupPosition)));
//        if (getChildrenCount(groupPosition) == 0){
//            imgIcon.setVisibility(convertView.INVISIBLE);
//        } else {
//            imgIcon.setVisibility(convertView.VISIBLE);
            if(isExpanded){
                imgIcon.setImageResource(R.drawable.ic_arrow_drop_up);
            } else {
                imgIcon.setImageResource(R.drawable.ic_arrow_drop_down);
            }
//        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean b, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            LayoutInflater li = LayoutInflater.from(mContext);
            convertView = li.inflate(R.layout.row_item_score_layout, viewGroup, false);
        }
        TextView tvScore = convertView.findViewById(R.id.tv_score);
        LinearLayout lnBtnEdit = convertView.findViewById(R.id.ln_btn_edit);
        LinearLayout lnBtnDelete = convertView.findViewById(R.id.ln_btn_delete);
        final Score score = getChild(groupPosition,childPosition);
        tvScore.setText(String.format("%.2f",score.getDiem()));

        lnBtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(mContext, "delete", Toast.LENGTH_SHORT).show();
            new CustomDialog(mContext).showDialogDeleteScore(mDatabase,score,mContext);

            }
        });

        lnBtnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(mContext, "edit", Toast.LENGTH_SHORT).show();
            new CustomDialog(mContext).showDialogEditScore(mDatabase,score,mContext);
            }
        });

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }
}
