package com.shindev.rulecalculator.activity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.c.progress_dialog.BlackProgressDialog;
import com.shindev.rulecalculator.R;
import com.shindev.rulecalculator.adapter.SelFunctionAdapter;
import com.shindev.rulecalculator.model.FunctionItem;
import com.shindev.rulecalculator.util.APIManager;
import com.shindev.rulecalculator.util.AppUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class SelFunctionActivity extends AppCompatActivity {
    private ArrayList<FunctionItem> mFunctionDatas = new ArrayList<>();

    private ListView lst_function;
    private SelFunctionAdapter mAdapterFunction;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sel_function);

        AppUtils.initUIActivity(this);
        setToolbar();

        initDatas();

        lst_function = findViewById(R.id.lst_sel_functions);
        lst_function.setOnItemClickListener((parent, view, position, id) -> {
            FunctionItem item = mFunctionDatas.get(position);
            item.isSelected = !item.isSelected;
            mAdapterFunction.notifyDataSetChanged();
        });
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.drawable.ic_left_white);
            toolbar.setNavigationOnClickListener(v -> {
                onBackPressed();
            });
        }
    }

    private void initDatas() {
        BlackProgressDialog dialog = new BlackProgressDialog(this, getString(R.string.common_connect));
        dialog.show();

        Map<String, String> params = new HashMap<>();
        params.put("id", AppUtils.gUserInfo.id);

        APIManager.onAPIConnectionResponse(APIManager.LAO_GET_FUNCTION_USER, params, APIManager.APIMethod.POST, new APIManager.APIManagerCallback() {
            @Override
            public void onEventCallBack(JSONObject obj, int ret) {
                switch (ret) {
                    case 10000:
                        try {
                            JSONArray jsonArray = obj.getJSONArray("result");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                FunctionItem item = new FunctionItem();
                                item.initialWithJson(object);
                                mFunctionDatas.add(item);
                            }

                            initListView();
                        } catch (JSONException e) {
                            Toast.makeText(SelFunctionActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                        break;
                    default:
                        break;
                }
                dialog.dismiss();
            }

            @Override
            public void onEventInternetError(Exception e) {
                dialog.dismiss();
            }

            @Override
            public void onEventServerError(Exception e) {
                dialog.dismiss();
            }
        });
    }

    private void initListView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Collections.sort(mFunctionDatas, Comparator.comparing(FunctionItem::getName));
            Collections.sort(mFunctionDatas, Comparator.comparing(FunctionItem::getParamCount));
        }

        mAdapterFunction = new SelFunctionAdapter(this, mFunctionDatas);
        lst_function.setAdapter(mAdapterFunction);

    }

    public void onClickSelFunctionNextBtn(View view) {
        AppUtils.gFuncItems.clear();
        for (int i = 0; i < mFunctionDatas.size(); i++) {
            FunctionItem item = mFunctionDatas.get(i);
            if (item.isSelected) {
                AppUtils.gFuncItems.add(item);
            }
        }
        if (AppUtils.gFuncItems.size() > 0) {
            int cnt = AppUtils.gFuncItems.get(0).getParamCount();
            for (int i = 1; i < AppUtils.gFuncItems.size(); i++) {
                int paramCnt = AppUtils.gFuncItems.get(i).getParamCount();
                if (cnt != paramCnt) {
                    Toast.makeText(this, R.string.main_same_select, Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            AppUtils.showOtherActivity(this, CreateProjectActivity.class, 0);
        } else {
            Toast.makeText(this, R.string.main_empty_select, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setToolbar();
        if (mAdapterFunction != null) {
            mAdapterFunction.notifyDataSetChanged();
        }
    }

}
