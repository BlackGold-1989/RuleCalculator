package com.shindev.rulecalculator.activity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.c.progress_dialog.BlackProgressDialog;
import com.shindev.rulecalculator.R;
import com.shindev.rulecalculator.adapter.FunctionAdapter;
import com.shindev.rulecalculator.model.FunctionItem;
import com.shindev.rulecalculator.model.ParamInfo;
import com.shindev.rulecalculator.util.APIManager;
import com.shindev.rulecalculator.util.AppUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProjectDetailActivity extends AppCompatActivity {
    private TextView txt_para;

    private ListView lst_function;
    private List<FunctionItem> mFunctions = new ArrayList<>();

    private FunctionAdapter.FunctionAdapterCallback functionAdapterCallback = item -> {
        AppUtils.gSelFunc = item;
        AppUtils.showOtherActivity(ProjectDetailActivity.this, FunctionDetailActivity.class, 0);
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projectdetail);

        AppUtils.initUIActivity(this);
        setToolbar();

        initUIView();
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setTitle(AppUtils.gSelProject.name);
            toolbar.setNavigationIcon(R.drawable.ic_left_white);
            toolbar.setNavigationOnClickListener(v -> {
                onBackPressed();
            });
        }
    }

    private void initUIView() {
        lst_function = findViewById(R.id.lst_function_detail);
        txt_para= findViewById(R.id.lbl_project_detail_params);

        initDatas();
    }

    private void initDatas () {
        BlackProgressDialog dialog = new BlackProgressDialog(this, getString(R.string.common_connect));
        dialog.show();

        Map<String, String> params = new HashMap<>();
        params.put("id", AppUtils.gSelProject.id);

        APIManager.onAPIConnectionResponse(APIManager.LAO_GET_FUNCTION_PROJECT, params, APIManager.APIMethod.POST, new APIManager.APIManagerCallback() {
            @Override
            public void onEventCallBack(JSONObject obj, int ret) {
                switch (ret) {
                    case 10000:
                        try {
                            JSONArray objAry = obj.getJSONArray("result");
                            mFunctions.clear();
                            for (int i = 0; i < objAry.length(); i++) {
                                JSONObject object = objAry.getJSONObject(i);
                                FunctionItem item = new FunctionItem();
                                item.initialWithJson(object);
                                mFunctions.add(item);
                            }
                            initListView();
                        } catch (JSONException e) {
                            Toast.makeText(ProjectDetailActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
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
        AppUtils.gFuncItems.clear();
        AppUtils.gFuncItems = new ArrayList<>(mFunctions);

        AppUtils.gSelParams.clear();
        AppUtils.gSelParams = new ArrayList<>(mFunctions.get(0).params);

        FunctionAdapter mAdapter = new FunctionAdapter(this, mFunctions, functionAdapterCallback);
        lst_function.setAdapter(mAdapter);
        lst_function.setOnItemClickListener((adapterView, view, i, l) -> {
            AppUtils.gSelIndex = i;
            AppUtils.showOtherActivity(this, FunctionDetailActivity.class, 0);
        });

        String str_para = "";
        for (int i = 0; i < mFunctions.get(0).getParamCount(); i++) {
            ParamInfo info = mFunctions.get(0).params.get(i);
            if (i == mFunctions.get(0).getParamCount() - 1) {
                str_para = str_para + String.valueOf(i + 1) + ". " + info.name + " ( " + info.description + " )";
            } else {
                str_para = str_para + String.valueOf(i + 1) + ". " + info.name + " ( " + info.description + " )\n";
            }
        }
        txt_para.setText(str_para);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setToolbar();
    }

}
