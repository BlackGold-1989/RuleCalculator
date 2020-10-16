package com.shindev.rulecalculator.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shindev.rulecalculator.R;
import com.shindev.rulecalculator.model.CreateParaItem;

import java.util.List;

public class CreateParaAdapter  extends BaseAdapter {

    private Context mContext;
    private List<CreateParaItem> mDatas;

    public CreateParaAdapter (Context context, List<CreateParaItem> datas) {
        this.mContext = context;
        this.mDatas = datas;
    }

    @Override
    public int getCount() {
        return (mDatas.size() + 1);
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        view = LayoutInflater.from(mContext).inflate(R.layout.item_create_para, null);

        TextView txt_name = view.findViewById(R.id.lbl_item_para_name);
        TextView txt_desc = view.findViewById(R.id.lbl_item_para_description);
        if (position == 0) {
            txt_name.setText(R.string.item_create_name);
            txt_desc.setText(R.string.item_create_description);
        } else {
            txt_name.setText(mDatas.get(position - 1).name);
            txt_desc.setText(mDatas.get(position - 1).description);
        }

        return view;
    }
}
