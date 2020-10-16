package com.shindev.rulecalculator.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shindev.rulecalculator.R;
import com.shindev.rulecalculator.model.FunctionItem;
import com.shindev.rulecalculator.util.AppUtils;

import java.util.ArrayList;

public class SelFunctionAdapter  extends BaseAdapter {

    private Context mContext;
    private ArrayList<FunctionItem> mDatas;

    public SelFunctionAdapter (Context context, ArrayList<FunctionItem> datas) {
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

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        view = LayoutInflater.from(mContext).inflate(R.layout.item_sel_function, null);

        TextView txt_name = view.findViewById(R.id.lbl_item_name);
        txt_name.setText(mDatas.get(position).name);

        TextView txt_date = view.findViewById(R.id.lbl_item_date);
        txt_date.setText(mDatas.get(position).regdate);

        ImageView img_check = view.findViewById(R.id.img_item_check);
        if (!mDatas.get(position).isSelected) {
            img_check.setImageResource(R.drawable.ic_circle_green);
        } else {
            img_check.setImageResource(R.drawable.ic_check_circle_green);
        }

        LinearLayout llt_param = view.findViewById(R.id.llt_item_param);
        if (AppUtils.gFuncParams != mDatas.get(position).getParamCount()) {
            AppUtils.gFuncParams = mDatas.get(position).getParamCount();
            TextView txt_param = view.findViewById(R.id.txt_item_param);
            txt_param.setText(String.format(mContext.getString(R.string.func_item_param), AppUtils.gFuncParams));
            llt_param.setVisibility(View.VISIBLE);
        } else {
            llt_param.setVisibility(View.GONE);
        }

        if (position == mDatas.size() - 1) {
            AppUtils.gFuncParams = 0;
        }

        return view;
    }
}
