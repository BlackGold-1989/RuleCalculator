package com.shindev.rulecalculator.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.shindev.rulecalculator.R;

public class LParmFragment extends Fragment {

    private LParmFragmentCallback lParmFragmentCallback;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_lparam, container, false);
        initUIView(fragment);
        return fragment;
    }

    private void initUIView(View fragment) {
        //logical 5
        Button btn_if = fragment.findViewById(R.id.btn_formula_if);
        btn_if.setOnClickListener(view -> lParmFragmentCallback.onClickLParamItem(btn_if));
        Button btn_less = fragment.findViewById(R.id.btn_formula_less);
        btn_less.setOnClickListener(view -> lParmFragmentCallback.onClickLParamItem(btn_less));
        Button btn_more = fragment.findViewById(R.id.btn_formula_more);
        btn_more.setOnClickListener(view -> lParmFragmentCallback.onClickLParamItem(btn_more));
        Button btn_lequal = fragment.findViewById(R.id.btn_formula_lequal);
        btn_lequal.setOnClickListener(view -> lParmFragmentCallback.onClickLParamItem(btn_lequal));
        Button btn_lunequal = fragment.findViewById(R.id.btn_formula_lunequal);
        btn_lunequal.setOnClickListener(view -> lParmFragmentCallback.onClickLParamItem(btn_lunequal));

        //bundle 4
        Button btn_01_open = fragment.findViewById(R.id.btn_formula_b01o);
        btn_01_open.setOnClickListener(view -> lParmFragmentCallback.onClickLParamItem(btn_01_open));
        Button btn_01_close = fragment.findViewById(R.id.btn_formula_b01c);
        btn_01_close.setOnClickListener(view -> lParmFragmentCallback.onClickLParamItem(btn_01_close));
        Button btn_02_open = fragment.findViewById(R.id.btn_formula_b02o);
        btn_02_open.setOnClickListener(view -> lParmFragmentCallback.onClickLParamItem(btn_02_open));
        Button btn_02_close = fragment.findViewById(R.id.btn_formula_b02c);
        btn_02_close.setOnClickListener(view -> lParmFragmentCallback.onClickLParamItem(btn_02_close));

        //other 5
        Button btn_back = fragment.findViewById(R.id.btn_formula_back);
        btn_back.setOnClickListener(view -> lParmFragmentCallback.onClickLParamItem(btn_back));
        Button btn_clear = fragment.findViewById(R.id.btn_formula_clear);
        btn_clear.setOnClickListener(view -> lParmFragmentCallback.onClickLParamItem(btn_clear));
        Button btn_enter = fragment.findViewById(R.id.btn_formula_enter);
        btn_enter.setOnClickListener(view -> lParmFragmentCallback.onClickLParamItem(btn_enter));
        Button btn_space = fragment.findViewById(R.id.btn_formula_space);
        btn_space.setOnClickListener(view -> lParmFragmentCallback.onClickLParamItem(btn_space));
        Button btn_result = fragment.findViewById(R.id.btn_formula_result);
        btn_result.setOnClickListener(view -> lParmFragmentCallback.onClickLParamItem(btn_result));
    }

    public void setlParmFragmentCallback(LParmFragmentCallback lParmFragmentCallback) {
        this.lParmFragmentCallback = lParmFragmentCallback;
    }

    public interface LParmFragmentCallback {
        void onClickLParamItem(Button button);
    }

}
