package com.shindev.rulecalculator.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ShareCompat;

import com.shindev.rulecalculator.R;
import com.shindev.rulecalculator.adapter.ResultAdapter;
import com.shindev.rulecalculator.model.FunctionItem;
import com.shindev.rulecalculator.model.ParamInfo;
import com.shindev.rulecalculator.util.APIManager;
import com.shindev.rulecalculator.util.AppUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ResultActivity extends AppCompatActivity {

    private ArrayList<String[]> ary_results = new ArrayList<>();
    private String[] list_results;
    private ResultAdapter mAdapter;

    private int cnt_func = 0;

    private String str_data = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        AppUtils.initUIActivity(this);
        setToolbar();

        cnt_func = AppUtils.gFuncItems.size();
        getResultArray(0);
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.drawable.ic_left_white);
            toolbar.setNavigationOnClickListener(v -> onBackPressed());
            toolbar.setOnMenuItemClickListener(item -> {
                int id = item.getItemId();
                if (id == R.id.menu_share){
                    onClickLltResultShare();
                    return true;
                }
                return false;
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_result, menu);
        return true;
    }

    private void getResultArray(final int index) {
        FunctionItem item = AppUtils.gFuncItems.get(index);
        String str_url = APIManager.SERVER_URL + AppUtils.gSelProject.classname + "/" + item.name + "/";

        Map<String, String> params = new HashMap<>();
        for (int i = 0; i < item.params.size(); i++) {
            String key = item.params.get(i).name;
            StringBuilder value = new StringBuilder(AppUtils.gParamValues.get(0)[i]);
            for (int j = 1; j < AppUtils.gParamValues.size(); j++) {
                value.append(",").append(AppUtils.gParamValues.get(j)[i]);
            }
            params.put(key, value.toString());
        }

        OkHttpUtils.post().url(str_url)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Call call, Exception e, int id) {
                        Toast.makeText(getApplicationContext(), R.string.alert_error_internet_detail, Toast.LENGTH_SHORT).show();
                        AppUtils.showOtherActivity(ResultActivity.this, LoginActivity.class, 1);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            list_results = new String[AppUtils.gParamValues.size()];
                            JSONObject obj = new JSONObject(response);
                            JSONArray result = obj.getJSONArray("result");
                            for (int i = 0; i < result.length(); i++) {
                                String str_result = result.getString(i);
                                list_results[i] = str_result;
                            }
                            ary_results.add(list_results);
                            int new_index = index + 1;
                            if (new_index == cnt_func) {
                                onShowResultEvent();
                            } else {
                                getResultArray(new_index);
                            }
                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(), R.string.alert_server_error_detail, Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void onShowResultEvent() {
        ListView lst_result = findViewById(R.id.lst_result);
        mAdapter = new ResultAdapter(this, AppUtils.gParamValues, ary_results);
        lst_result.setAdapter(mAdapter);
    }

    public void onClickSaveBtn(View view) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        alertDialog.setTitle(R.string.result_alert_title);
        alertDialog.setMessage(R.string.result_alert_detail);

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_custom, null);
        alertDialog.setView(dialogView);

        EditText editText = dialogView.findViewById(R.id.alert_txt_content);

        alertDialog.setPositiveButton(R.string.ok, (dialog, which) -> {
                    String str_value = editText.getText().toString();
                    if (str_value.length() == 0) {
                        return;
                    }
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(ResultActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                        return;
                    }
                    onSaveFileWithName(str_value);
                });

        alertDialog.setNegativeButton(R.string.formula_cancel, (dialog, which) -> dialog.cancel());

        AlertDialog dialog = alertDialog.create();
        dialog.setOnShowListener(dialogInterface -> {
            Button posButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(20,0,0,0);
            posButton.setLayoutParams(params);
        });
        dialog.show();
    }

    private void onMakeFileString(String filename) {
//        String data = "";
//        String str_result = "";
//        for (int i = 0; i < ary_results.get(0).length; i++) {
//            String str_col = "";
//            for (int j = 0; j < ary_results.size(); j++) {
//                str_col = str_col + "   " + ary_results.get(j)[i];
//            }
//            str_result = str_result + str_col + " \n";
//        }
//        data = data + str_result + " \n";
//        data = data + " \n";

        StringBuilder data = new StringBuilder();
        List<ParamInfo> params = AppUtils.gSelParams;

        // Add function name
        for (int i = 0; i < AppUtils.gFuncItems.size(); i++) {
            FunctionItem item = AppUtils.gFuncItems.get(i);
            data.append(item.name).append("(").append(item.title).append(") -> ").append(String.format(Locale.US, "%02d", i + 1)).append(" \n");
        }
        data.append(" \n \n");

        // Add paramerters
        data.append(getString(R.string.result_para_title)).append(" \n \n");

        StringBuilder str_param_title = new StringBuilder();
        for (int i = 0; i < params.size(); i++) {
            ParamInfo paramInfo = params.get(i);
            str_param_title.append("   ").append(paramInfo.name).append("(").append(paramInfo.description).append(")");
        }
        data.append(str_param_title).append(" \n");

        for (int i = 0; i < AppUtils.gParamValues.size(); i++) {
            StringBuilder str_param_value = new StringBuilder();
            String[] values = AppUtils.gParamValues.get(i);
            for (String value : values) {
                str_param_value.append("   ").append(value);
            }
            data.append(str_param_value).append(" \n");
        }
        data.append(" \n");

        // Add result
        data.append(getString(R.string.result_title)).append(" \n \n");
        StringBuilder str_result = new StringBuilder();
        for (int i = 0; i < ary_results.size(); i++) {
            str_result.append("   ").append(String.format(Locale.US, "%02d", i + 1));
        }
        str_result.append(" \n \n");
        for (int i = 0; i < ary_results.get(0).length; i++) {
            StringBuilder str_col = new StringBuilder();
            for (int j = 0; j < ary_results.size(); j++) {
                str_col.append("   ").append(ary_results.get(j)[i]);
            }
            str_result.append(str_col).append(" \n");
        }
        data.append(str_result).append(" \n");
        data.append(" \n");

        // Add date and maker
        Date date = new Date();
        SimpleDateFormat postFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        String str_date = postFormater.format(date);
        data.append(getString(R.string.result_date)).append("   ").append(str_date).append(" \n");
        data.append(getString(R.string.result_maker)).append("   ").append(AppUtils.gUserInfo.nickname).append(" \n");

        str_data = data.toString();
    }

    private void onSaveFileWithName(String filename) {
        onMakeFileString(filename);
        AppUtils.writeToFile(str_data, filename, this);

        Map<String, String> params = new HashMap<>();
        params.put("id", AppUtils.gUserInfo.id);
        params.put("projectid", AppUtils.gSelProject.id);
        params.put("content", str_data);
        APIManager.onAPIConnectionResponse(APIManager.LAO_SAVE_RESULT, params, APIManager.APIMethod.POST, new APIManager.APIManagerCallback() {
            @Override
            public void onEventCallBack(JSONObject obj, int ret) {
                if (ret == 10000) {
                    AppUtils.showOtherActivity(ResultActivity.this, MainActivity.class, 1);
                }
            }

            @Override
            public void onEventInternetError(Exception e) {
                Toast.makeText(ResultActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onEventServerError(Exception e) {
                Toast.makeText(ResultActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onClickLltResultShare() {
        onMakeFileString(String.format("Result_%s.txt", AppUtils.gUserInfo.id));
        String shareText = str_data;
        Intent shareIntent = ShareCompat.IntentBuilder.from(this)
                .setType("text/plain").setText(shareText).getIntent();
        startActivity(Intent.createChooser(shareIntent, "Share"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        setToolbar();
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    public void onClickGoMainBtn(View view) {
        AppUtils.showOtherActivity(this, MainActivity.class, 0);
        finish();
    }

}
