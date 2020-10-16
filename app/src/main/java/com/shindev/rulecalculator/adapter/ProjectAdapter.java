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
import com.shindev.rulecalculator.activity.ProjectDetailActivity;
import com.shindev.rulecalculator.model.ProjectItem;
import com.shindev.rulecalculator.util.AppUtils;

import java.util.List;

public class ProjectAdapter  extends BaseAdapter {

    private Context mContext;
    private List<ProjectItem> mDatas;
    private boolean isCheckable;
    private ProjectAdapterCallback callback;
    private boolean isCalculate;
    private boolean isHideDelete;
    private LinearLayout llt_delete;


    public ProjectAdapter (Context context, List<ProjectItem> datas, ProjectAdapterCallback projectAdapterCallback) {
        this.mContext = context;
        this.mDatas = datas;
        this.isCheckable = false;
        this.isCalculate = true;
        this.isHideDelete = false;
        this.callback = projectAdapterCallback;
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

        ProjectItem item = mDatas.get(position);

        TextView txt_index = view.findViewById(R.id.lbl_item_index);
        txt_index.setText(String.format("%d. ", position + 1));

        TextView txt_name = view.findViewById(R.id.lbl_item_name);
        txt_name.setText(item.name);

        TextView txt_date = view.findViewById(R.id.lbl_item_date);
        txt_date.setText(item.regdate);

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

        view.setOnClickListener(view12 -> {
            if (isCheckable) {
                item.isSelected = !item.isSelected;
                notifyDataSetChanged();
            } else {
                callback.onClickItemView(item);
            }
        });

        Button btn_calc = view.findViewById(R.id.btn_item_calc);
        llt_delete = view.findViewById(R.id.llt_item_delete);
        if (isCheckable) {
            btn_calc.setVisibility(View.INVISIBLE);
            llt_delete.setVisibility(View.INVISIBLE);
        } else {
            btn_calc.setVisibility(View.VISIBLE);
            llt_delete.setVisibility(View.VISIBLE);
        }
        btn_calc.setOnClickListener(view1 -> callback.onClickCalculateBtn(item));
        llt_delete.setOnClickListener(view13 -> callback.onClickDeleteBtn(item));

        if (!isCalculate) {
            btn_calc.setVisibility(View.INVISIBLE);
            llt_delete.setVisibility(View.INVISIBLE);
        }

        if (isHideDelete) {
            llt_delete.setVisibility(View.GONE);
        }

        LinearLayout llt_params = view.findViewById(R.id.llt_item_params);
        llt_params.setVisibility(View.GONE);

        return view;
    }

    public void setCheckable(boolean checkable) {
        isCheckable = checkable;
        notifyDataSetChanged();
    }

    public void setCalculatable(boolean flg) {
        isCalculate = flg;
        notifyDataSetChanged();
    }

    public void setHideDeleteBtn() {
        isHideDelete = true;
        notifyDataSetChanged();
    }

    public interface ProjectAdapterCallback {
        void onClickItemView(ProjectItem item);
        void onClickCalculateBtn(ProjectItem item);
        void onClickDeleteBtn(ProjectItem item);
    }

}
