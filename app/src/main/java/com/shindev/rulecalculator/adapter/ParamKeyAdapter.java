package com.shindev.rulecalculator.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.shindev.rulecalculator.R;
import com.shindev.rulecalculator.model.CreateParaItem;

import java.util.List;

public class ParamKeyAdapter extends BaseAdapter {

    private Context mContext;
    private List<CreateParaItem> mParamItems;

    private ParamKeyAdapterCallback paramKeyAdapterCallback;

    public ParamKeyAdapter(Context context, List<CreateParaItem> paraItems) {
        mContext = context;
        mParamItems = paraItems;
    }

    @Override
    public int getCount() {
        return (mParamItems.size() - 1) / 4 + 1;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        view = LayoutInflater.from(mContext).inflate(R.layout.item_param, null);

        int index1 = i * 4;
        Button btn_1 = view.findViewById(R.id.btn_paramitem_1);
        btn_1.setOnClickListener(view1 -> paramKeyAdapterCallback.onClickParamItem(btn_1));
        if (index1 < mParamItems.size()) {
            btn_1.setText(mParamItems.get(index1).name);
        } else {
            btn_1.setVisibility(View.INVISIBLE);
        }

        int index2 = i * 4 + 1;
        Button btn_2 = view.findViewById(R.id.btn_paramitem_2);
        btn_2.setOnClickListener(view1 -> paramKeyAdapterCallback.onClickParamItem(btn_2));
        if (index2 < mParamItems.size()) {
            btn_2.setText(mParamItems.get(index2).name);
        } else {
            btn_2.setVisibility(View.INVISIBLE);
        }

        int index3 = i * 4 + 2;
        Button btn_3 = view.findViewById(R.id.btn_paramitem_3);
        btn_3.setOnClickListener(view1 -> paramKeyAdapterCallback.onClickParamItem(btn_3));
        if (index3 < mParamItems.size()) {
            btn_3.setText(mParamItems.get(index3).name);
        } else {
            btn_3.setVisibility(View.INVISIBLE);
        }

        int index4 = i * 4 + 3;
        Button btn_4 = view.findViewById(R.id.btn_paramitem_4);
        btn_4.setOnClickListener(view1 -> paramKeyAdapterCallback.onClickParamItem(btn_4));
        if (index4 < mParamItems.size()) {
            btn_4.setText(mParamItems.get(index4).name);
        } else {
            btn_4.setVisibility(View.INVISIBLE);
        }

        return view;
    }

    public void setParamKeyAdapterCallback(ParamKeyAdapterCallback paramKeyAdapterCallback) {
        this.paramKeyAdapterCallback = paramKeyAdapterCallback;
    }

    public interface ParamKeyAdapterCallback {
        void onClickParamItem(Button button);
    }

}
