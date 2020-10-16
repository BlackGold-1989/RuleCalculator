package com.shindev.rulecalculator.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shindev.rulecalculator.R;
import com.shindev.rulecalculator.model.FunctionItem;

import java.util.List;

public class FunctionAdapter  extends BaseAdapter {

    private Context mContext;
    private List<FunctionItem> mDatas;
    private boolean isCheckable;

    private FunctionAdapterCallback functionAdapterCallback;

    public FunctionAdapter (Context context, List<FunctionItem> datas, FunctionAdapterCallback callback) {
        this.mContext = context;
        this.mDatas = datas;
        this.functionAdapterCallback = callback;
        isCheckable = false;
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
        view = LayoutInflater.from(mContext).inflate(R.layout.item_function, null);

        FunctionItem item = mDatas.get(position);

        TextView txt_index = view.findViewById(R.id.lbl_item_index);
        txt_index.setText(String.format("%d. ", position + 1));

        TextView txt_name = view.findViewById(R.id.lbl_item_name);
        txt_name.setText(item.title);

        TextView txt_date = view.findViewById(R.id.lbl_item_date);
        txt_date.setText(item.regdate);

        Button btn_calc = view.findViewById(R.id.btn_item_calc);
        btn_calc.setVisibility(View.INVISIBLE);

        LinearLayout llt_check = view.findViewById(R.id.llt_list_item_check);
        ImageView img_check = view.findViewById(R.id.img_item_check);
        if (isCheckable) {
            llt_check.setVisibility(View.VISIBLE);
            if (item.isSelected) {
                img_check.setImageResource(R.drawable.ic_check_circle_green);
            } else {
                img_check.setImageResource(R.drawable.ic_circle_green);
            }
        } else {
            llt_check.setVisibility(View.GONE);
        }

        view.setOnClickListener(view1 -> {
            if (isCheckable) {
                item.isSelected = !item.isSelected;
                notifyDataSetChanged();
            } else {
                // Show Function Detail
                functionAdapterCallback.onClickFunctionDetailLlt(item);
            }
        });

        LinearLayout llt_params = view.findViewById(R.id.llt_item_params);
        TextView lbl_params = view.findViewById(R.id.lbl_item_params);
        if (position == 0) {
            llt_params.setVisibility(View.VISIBLE);
            lbl_params.setText(mContext.getString(R.string.main_params) + " " + item.params.size());
        } else {
            FunctionItem bItem = mDatas.get(position - 1);
            if (item.params.size() != bItem.params.size()) {
                lbl_params.setText(mContext.getString(R.string.main_params) + " " + item.params.size());
            } else {
                llt_params.setVisibility(View.GONE);
            }
        }

        LinearLayout llt_delete = view.findViewById(R.id.llt_item_delete);
        llt_delete.setVisibility(View.GONE);

        return view;
    }

    public void setCheckable(boolean checkable) {
        isCheckable = checkable;
        notifyDataSetChanged();
    }

    public interface FunctionAdapterCallback {
        void onClickFunctionDetailLlt(FunctionItem item);
    }

}
