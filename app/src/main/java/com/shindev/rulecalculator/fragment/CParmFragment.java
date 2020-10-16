package com.shindev.rulecalculator.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.shindev.rulecalculator.R;

public class CParmFragment extends Fragment {

    private CParmFragmentCallback cParmFragmentCallback;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_cparam, container, false);
        initUIView(fragment);
        return fragment;
    }

    private void initUIView(View fragment) {
        //number
        Button btn_0 = fragment.findViewById(R.id.btn_formula_zero);
        btn_0.setOnClickListener(view -> cParmFragmentCallback.onClickCParamItem(btn_0));
        Button btn_1 = fragment.findViewById(R.id.btn_formula_one);
        btn_1.setOnClickListener(view -> cParmFragmentCallback.onClickCParamItem(btn_1));
        Button btn_2 = fragment.findViewById(R.id.btn_formula_two);
        btn_2.setOnClickListener(view -> cParmFragmentCallback.onClickCParamItem(btn_2));
        Button btn_3 = fragment.findViewById(R.id.btn_formula_three);
        btn_3.setOnClickListener(view -> cParmFragmentCallback.onClickCParamItem(btn_3));
        Button btn_4 = fragment.findViewById(R.id.btn_formula_four);
        btn_4.setOnClickListener(view -> cParmFragmentCallback.onClickCParamItem(btn_4));
        Button btn_5 = fragment.findViewById(R.id.btn_formula_five);
        btn_5.setOnClickListener(view -> cParmFragmentCallback.onClickCParamItem(btn_5));
        Button btn_6 = fragment.findViewById(R.id.btn_formula_six);
        btn_6.setOnClickListener(view -> cParmFragmentCallback.onClickCParamItem(btn_6));
        Button btn_7 = fragment.findViewById(R.id.btn_formula_seven);
        btn_7.setOnClickListener(view -> cParmFragmentCallback.onClickCParamItem(btn_7));
        Button btn_8 = fragment.findViewById(R.id.btn_formula_eight);
        btn_8.setOnClickListener(view -> cParmFragmentCallback.onClickCParamItem(btn_8));
        Button btn_9 = fragment.findViewById(R.id.btn_formula_nine);
        btn_9.setOnClickListener(view -> cParmFragmentCallback.onClickCParamItem(btn_9));

        //operate
        Button btn_plus = fragment.findViewById(R.id.btn_formula_plus);
        btn_plus.setOnClickListener(view -> cParmFragmentCallback.onClickCParamItem(btn_plus));
        Button btn_minu = fragment.findViewById(R.id.btn_formula_minu);
        btn_minu.setOnClickListener(view -> cParmFragmentCallback.onClickCParamItem(btn_minu));
        Button btn_multi = fragment.findViewById(R.id.btn_formula_multi);
        btn_multi.setOnClickListener(view -> cParmFragmentCallback.onClickCParamItem(btn_multi));
        Button btn_divide = fragment.findViewById(R.id.btn_formula_divide);
        btn_divide.setOnClickListener(view -> cParmFragmentCallback.onClickCParamItem(btn_divide));

        //other
        Button btn_equal = fragment.findViewById(R.id.btn_formula_equal);
        btn_equal.setOnClickListener(view -> cParmFragmentCallback.onClickCParamItem(btn_equal));
        Button btn_point = fragment.findViewById(R.id.btn_formula_point);
        btn_point.setOnClickListener(view -> cParmFragmentCallback.onClickCParamItem(btn_point));
    }

    public void setcParmFragmentCallback(CParmFragmentCallback cParmFragmentCallback) {
        this.cParmFragmentCallback = cParmFragmentCallback;
    }

    public interface CParmFragmentCallback {
        void onClickCParamItem(Button button);
    }

}
