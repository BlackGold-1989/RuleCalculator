package com.shindev.rulecalculator.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.shindev.rulecalculator.R;
import com.shindev.rulecalculator.adapter.ParamKeyAdapter;
import com.shindev.rulecalculator.model.CreateParaItem;

import java.util.List;

public class UParmFragment extends Fragment {

    private List<CreateParaItem> mParaItems;

    private UParmFragmentCallback uParmFragmentCallback;

    public UParmFragment(List<CreateParaItem> paraItems) {
        mParaItems = paraItems;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_param, container, false);

        initUIView(fragment);

        return fragment;
    }

    private void initUIView(View fragment) {
        ListView lst_uparam = fragment.findViewById(R.id.lst_param_item);
        ParamKeyAdapter adapter = new ParamKeyAdapter(getContext(), mParaItems);
        lst_uparam.setAdapter(adapter);

        adapter.setParamKeyAdapterCallback(button -> uParmFragmentCallback.onClickUParamItem(button));
    }

    public void setuParmFragmentCallback(UParmFragmentCallback uParmFragmentCallback) {
        this.uParmFragmentCallback = uParmFragmentCallback;
    }

    public interface UParmFragmentCallback {
        void onClickUParamItem(Button button);
    }

}
