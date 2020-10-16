package com.shindev.rulecalculator.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shindev.rulecalculator.R;
import com.shindev.rulecalculator.model.HistoryItem;

import java.util.List;

public class HistoryAdapter  extends BaseAdapter {

    private Context mContext;
    private List<HistoryItem> mDatas;


    public HistoryAdapter (Context context, List<HistoryItem> datas) {
        this.mContext = context;
        this.mDatas = datas;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint({"InflateParams", "ViewHolder"})
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        view = LayoutInflater.from(mContext).inflate(R.layout.item_history, null);

        HistoryItem item = mDatas.get(position);

        TextView txt_index = view.findViewById(R.id.lbl_item_index);
        txt_index.setText(String.format("%d. ", position + 1));

        TextView lbl_name = view.findViewById(R.id.lbl_item_name);
        lbl_name.setText(String.format("您支付了%.2f元。", Integer.parseInt(item.pay) / 100.0));
        TextView lbl_date = view.findViewById(R.id.lbl_item_date);
        lbl_date.setText(item.regdate);

        return view;
    }

}
