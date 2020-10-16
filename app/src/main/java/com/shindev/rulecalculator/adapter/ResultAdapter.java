package com.shindev.rulecalculator.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shindev.rulecalculator.R;
import com.shindev.rulecalculator.util.AppUtils;

import java.util.List;

public class ResultAdapter  extends BaseAdapter {

    private Context mContext;
    private List<String[]> mDatas;
    private List<String[]> mResults;

    public ResultAdapter (Context context, List<String[]> datas, List<String[]> results) {
        this.mContext = context;
        this.mDatas = datas;
        this.mResults = results;
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

    @SuppressLint({"InflateParams", "ViewHolder", "DefaultLocale"})
    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        view = LayoutInflater.from(mContext).inflate(R.layout.item_result, null);

        LinearLayout llt_main = view.findViewById(R.id.llt_value_list);

        if (position == 0) {
            for (int i = 0; i < AppUtils.gSelParams.size(); i++) {
                String value = AppUtils.gSelParams.get(i).name;

                TextView textView = new TextView(mContext);
                textView.setText(value);
                textView.setTextColor(mContext.getColor(R.color.colorTextBlack));
                textView.setTextSize((float) 15.0);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        150, LinearLayout.LayoutParams.MATCH_PARENT
                );
                textView.setGravity(Gravity.CENTER);
                params.setMargins(10, 0, 0, 0);
                textView.setLayoutParams(params);

                llt_main.addView(textView);
            }

            for (int i = 0; i < mResults.size(); i++) {
                TextView textView = new TextView(mContext);
                textView.setText(String.format("%02d", i + 1));
                textView.setTextColor(mContext.getColor(R.color.colorTextBlack));
                textView.setTextSize((float) 15.0);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        200, LinearLayout.LayoutParams.MATCH_PARENT
                );
                textView.setGravity(Gravity.CENTER);
                params.setMargins(10, 0, 0, 0);
                textView.setLayoutParams(params);

                llt_main.addView(textView);
            }
        } else {
            for (int i = 0; i < mDatas.get(position - 1).length; i++) {
                String value = mDatas.get(position - 1)[i];

                TextView textView = new TextView(mContext);
                textView.setText(value);
                textView.setTextColor(mContext.getColor(R.color.colorTextBlack));
                textView.setTextSize((float) 15.0);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        150, LinearLayout.LayoutParams.MATCH_PARENT
                );
                textView.setGravity(Gravity.CENTER);
                params.setMargins(10, 0, 0, 0);
                textView.setLayoutParams(params);

                llt_main.addView(textView);
            }

            for (int i = 0; i < mResults.size(); i++) {
                String[] vResults = mResults.get(i);

                TextView textView = new TextView(mContext);
                textView.setText(vResults[position - 1]);
                textView.setTextColor(mContext.getColor(R.color.colorTextBlack));
                textView.setTextSize((float) 15.0);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        200, LinearLayout.LayoutParams.MATCH_PARENT
                );
                textView.setGravity(Gravity.CENTER);
                params.setMargins(10, 0, 0, 0);
                textView.setLayoutParams(params);

                llt_main.addView(textView);
            }
        }

        return view;
    }
}
