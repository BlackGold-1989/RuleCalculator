package com.shindev.rulecalculator.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.c.progress_dialog.BlackProgressDialog;
import com.shindev.rulecalculator.R;
import com.shindev.rulecalculator.adapter.FunctionAdapter;
import com.shindev.rulecalculator.model.FunctionItem;
import com.shindev.rulecalculator.util.APIManager;
import com.shindev.rulecalculator.util.AppUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CreateProjectActivity extends AppCompatActivity {

    private EditText txt_project;

    private FunctionAdapter.FunctionAdapterCallback functionAdapterCallback = item -> {
        //
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createproject);

        AppUtils.initUIActivity(this);
        setToolbar();

        initUIView();
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

    private void initUIView() {
        ListView lst_function = findViewById(R.id.lst_create_project);
        FunctionAdapter mAdapter = new FunctionAdapter(this, AppUtils.gFuncItems, functionAdapterCallback);
        lst_function.setAdapter(mAdapter);

        txt_project = findViewById(R.id.txt_create_project);
    }

    public void onClickBtnCreateProject(View view) {
        String str_name = txt_project.getText().toString();
        if (str_name.length() == 0) {
            Toast.makeText(this, R.string.create_toast_name, Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, String> params = new HashMap<>();
        params.put("id", AppUtils.gUserInfo.id);
        params.put("name", str_name);
        String func_id = AppUtils.gFuncItems.get(0).id;
        for (int i = 1; i < AppUtils.gFuncItems.size(); i++) {
            FunctionItem item = AppUtils.gFuncItems.get(i);
            func_id = func_id + "," + item.id;
        }
        params.put("funcid", func_id);

        BlackProgressDialog dialog = new BlackProgressDialog(this, getString(R.string.common_connect));
        dialog.show();

        APIManager.onAPIConnectionResponse(APIManager.LAO_CREATE_PROJECT, params, APIManager.APIMethod.POST, new APIManager.APIManagerCallback() {
            @Override
            public void onEventCallBack(JSONObject obj, int ret) {
                switch (ret) {
                    case 10000:
                        Toast.makeText(CreateProjectActivity.this, R.string.create_project_title, Toast.LENGTH_SHORT).show();
                        AppUtils.showOtherActivity(CreateProjectActivity.this, MainActivity.class, 1);
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

    @Override
    protected void onResume() {
        super.onResume();
        setToolbar();
    }

}
