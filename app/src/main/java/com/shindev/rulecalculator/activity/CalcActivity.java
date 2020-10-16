package com.shindev.rulecalculator.activity;

import android.Manifest;
import android.actionsheet.demo.com.khoiron.actionsheetiosforandroid.ActionSheet;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.c.progress_dialog.BlackProgressDialog;
import com.dkv.bubblealertlib.ConstantsIcons;
import com.dkv.bubblealertlib.IAlertClickedCallBack;
import com.shindev.rulecalculator.R;
import com.shindev.rulecalculator.adapter.ValueAdapter;
import com.shindev.rulecalculator.model.FunctionItem;
import com.shindev.rulecalculator.util.APIManager;
import com.shindev.rulecalculator.util.AppUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CalcActivity extends AppCompatActivity {

    private ValueAdapter mAdapter;
    private List<FunctionItem> mFunctions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calc);

        AppUtils.initUIActivity(this);
        setToolbar();

        initDatas();
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.drawable.ic_left_white);
            toolbar.setNavigationOnClickListener(v -> {
                onBackPressed();
            });
            toolbar.setOnMenuItemClickListener(item -> {
                int id = item.getItemId();
                if (id == R.id.menu_create_add){
                    onClickLltAdd();
                    return true;
                }
                return false;
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create, menu);
        return true;
    }

    private void initViewUI() {
        ListView lst_value = findViewById(R.id.lst_calc_para);
        mAdapter = new ValueAdapter(this, AppUtils.gFuncItems.get(0).params, AppUtils.gParamValues);
        lst_value.setAdapter(mAdapter);
    }

    private void initCalculateData() {
        AppUtils.gFuncItems.clear();
        AppUtils.gFuncItems = new ArrayList<>(mFunctions);
        AppUtils.gSelParams.clear();
        AppUtils.gSelParams = new ArrayList<>(mFunctions.get(0).params);

        initViewUI();
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
                            initCalculateData();
                        } catch (JSONException e) {
                            Toast.makeText(CalcActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
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

    public void onShowResultActivity() {
        if (AppUtils.gUserInfo.product == 0) {
            AppUtils.onShowCustomAlert(this,
                    "华林",
                    ConstantsIcons.ALERT_ICON_ERROR,
                    "您的产品计数为零。 请买一些。",
                    new IAlertClickedCallBack()
                    {
                        @Override
                        public void onOkClicked(String tag) {
//                            AppUtils.showOtherActivity(this, WechatPayActivity.class, 0);
                        }

                        @Override
                        public void onCancelClicked(String tag) {
                            //
                        }

                        @Override
                        public void onExitClicked(String tag) {
                            //
                        }
                    });
            return;
        }

        BlackProgressDialog dialog = new BlackProgressDialog(this, getString(R.string.common_connect));
        dialog.show();

        Map<String, String> params = new HashMap<>();
        params.put("id", AppUtils.gUserInfo.id);

        APIManager.onAPIConnectionResponse(APIManager.LAO_CALC, params, APIManager.APIMethod.POST, new APIManager.APIManagerCallback() {
            @Override
            public void onEventCallBack(JSONObject obj, int ret) {
                switch (ret) {
                    case 10000:
                        try {
                            JSONObject result = obj.getJSONObject("result");
                            AppUtils.gUserInfo.initialWithJson(result);

                            AppUtils.showOtherActivity(CalcActivity.this, ResultActivity.class, 0);
                        } catch (JSONException e) {
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

    private void onClickLltAdd() {
        ArrayList<String> data = new ArrayList<>();

        data.add(getString(R.string.act_content_01));
        data.add(getString(R.string.act_content_02));

        new ActionSheet(this, data)
                .setTitle(getString(R.string.actionsheet_title))
                .setCancelTitle(getString(R.string.common_cancel))
                .setColorTitle(Color.parseColor("#999999"))
                .setColorTitleCancel(Color.parseColor("#d25841"))
                .setColorData(Color.parseColor("#278ae7"))
                .create((data1, position) -> {
                    switch (position){
                        case 0:
                            AppUtils.showOtherActivity(this, AddParaValueActivity.class, 0);
                            break;
                        case 1:
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                            intent.setType("*/*");
                            startActivityForResult(intent, 0);
                            break;
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
                return;
            }

            AppUtils.gParamValues.clear();

            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(Objects.requireNonNull(getContentResolver().openInputStream(data.getData()))));
                String line;

                while ((line = br.readLine()) != null) {
                    if (line.equals("\n")) {
                        continue;
                    }
                    String[] ary_values = line.split(",");
                    AppUtils.gParamValues.add(ary_values);
                }
                br.close();

                mAdapter.notifyDataSetChanged();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void onEventValueDelete (final int index) {
        AppUtils.onShowCustomAlert(this,
                getString(R.string.calc_alert_delete_title),
                ConstantsIcons.ALERT_ICON_INFO,
                getString(R.string.calc_alert_delete_detail),
                new IAlertClickedCallBack()
                {
                    @Override
                    public void onOkClicked(String tag) {
                        AppUtils.gParamValues.remove(index);
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelClicked(String tag) {
                        //
                    }

                    @Override
                    public void onExitClicked(String tag) {
                        //
                    }
                });
    }

    public void onClickCalcBtn(View view) {
        this.onShowResultActivity();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setToolbar();
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

}
