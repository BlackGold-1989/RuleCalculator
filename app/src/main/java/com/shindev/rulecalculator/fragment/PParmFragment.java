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

import java.util.ArrayList;
import java.util.List;

public class PParmFragment extends Fragment {

    private PParmFragmentCallback pParmFragmentCallback;

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
        ListView lst_pparam = fragment.findViewById(R.id.lst_param_item);
        List<CreateParaItem> paraItems = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            CreateParaItem item = new CreateParaItem();
            item.name = "P" + (i + 1);
            paraItems.add(item);
        }
        ParamKeyAdapter adapter = new ParamKeyAdapter(getContext(), paraItems);
        lst_pparam.setAdapter(adapter);
        adapter.setParamKeyAdapterCallback(button -> pParmFragmentCallback.onClickPParamItem(button));
    }

    public void setpParmFragmentCallback(PParmFragmentCallback pParmFragmentCallback) {
        this.pParmFragmentCallback = pParmFragmentCallback;
    }

    public interface PParmFragmentCallback {
        void onClickPParamItem(Button button);
    }

}
