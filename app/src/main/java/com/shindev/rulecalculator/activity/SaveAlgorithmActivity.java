package com.shindev.rulecalculator.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.c.progress_dialog.BlackProgressDialog;
import com.dkv.bubblealertlib.ConstantsIcons;
import com.dkv.bubblealertlib.IAlertClickedCallBack;
import com.shindev.rulecalculator.R;
import com.shindev.rulecalculator.model.CreateParaItem;
import com.shindev.rulecalculator.util.APIManager;
import com.shindev.rulecalculator.util.AppUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SaveAlgorithmActivity extends AppCompatActivity {

    private EditText txt_name, txt_description;
    private String str_name;
    private String str_description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_algorithm);

        AppUtils.initUIActivity(this);
        setToolbar();

        initUIView();
    }

    private void setToolbar() {
        setTitle(R.string.save_algorithm_title);
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
        TextView lbl_detail = findViewById(R.id.lbl_save_detail);
        lbl_detail.setText(AppUtils.gAlgorithm);

        txt_name = findViewById(R.id.txt_save_algo_name);
        txt_description = findViewById(R.id.txt_save_algo_description);
    }

    private void onShowAavailableAlert() {
        new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
                .setTitle(R.string.alert_waring_title)
                .setMessage(R.string.alert_para_able_detail)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    // Continue with delete operation
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void onEventSaveAlgorithm () {
        if (AppUtils.gUserInfo.product == 0) {
            AppUtils.onShowCustomAlert(this,
                    "华林",
                    ConstantsIcons.ALERT_ICON_ERROR,
                    "您的产品计数为零。 请买一些。",
                    new IAlertClickedCallBack()
                    {
                        @Override
                        public void onOkClicked(String tag) {
                            AppUtils.showOtherActivity(SaveAlgorithmActivity.this, WechatPayActivity.class, 0);
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

        Map<String, String> params = new HashMap<>();
        params.put("id", AppUtils.gUserInfo.id);
        params.put("title", str_name);
        params.put("description", str_description);

        String str_param = AppUtils.gCreateParams.get(0).name + "," + AppUtils.gCreateParams.get(0).description;
        for (int i = 1; i < AppUtils.gCreateParams.size(); i++) {
            CreateParaItem item = AppUtils.gCreateParams.get(i);
            String str_item = item.name + "," + item.description;
            str_param = str_param + ":" + str_item;
        }
        params.put("params", str_param);

        String str_body = getFormulaBody(str_name);
        params.put("body", str_body);

        params.put("algorithm", AppUtils.gAlgorithm);

        BlackProgressDialog dialog = new BlackProgressDialog(this, getString(R.string.common_connect));
        dialog.show();

        APIManager.onAPIConnectionResponse(APIManager.LAO_SAVE_FORMULA, params, APIManager.APIMethod.POST, new APIManager.APIManagerCallback() {
            @Override
            public void onEventCallBack(JSONObject obj, int ret) {
                switch (ret) {
                    case 10000:
                        try {
                            JSONObject json_usr = obj.getJSONObject("result");
                            AppUtils.gUserInfo.initialWithJson(json_usr);

                            AppUtils.showOtherActivity(SaveAlgorithmActivity.this, MainActivity.class, 1);
                            finish();
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

    private String getFormulaBody(String name) {
        String str_formula = AppUtils.gAlgorithm;
        // Create Formula Header
        String body = String.format("function %s () {", name);
        body = body + "\n";

        // Add Formula Parameters
        for (int i = 0; i < AppUtils.gCreateParams.size(); i++) {
            CreateParaItem item = AppUtils.gCreateParams.get(i);
            String str_item = String.format("$%s = $this->input->post('%s');", item.name, item.name);
            body = body + str_item + "\n";
        }
        body = body + "\n";

        // Add Formula Parameters Spilt
        for (int i = 0; i < AppUtils.gCreateParams.size(); i++) {
            CreateParaItem item = AppUtils.gCreateParams.get(i);
            String str_item = String.format("$ary_%s = explode(',', $%s);", item.name, item.name);
            body = body + str_item + "\n";
        }
        body = body + "\n";

        // Add Formula Body
        body = body + String.format("for ($i=0; $i < sizeof($ary_%s); $i++) {", AppUtils.gCreateParams.get(0).name) + " \n";
        for (int i = 0; i < AppUtils.gCreateParams.size(); i++) {
            CreateParaItem item = AppUtils.gCreateParams.get(i);
            String str_before = "$" + item.name;
            String str_after = "$ary_" + item.name + "[$i]";
            str_formula = str_formula.replace(str_before, str_after);
        }
        str_formula = str_formula.replace("$Re", "$result[$i]");
        body = body + str_formula;
        body = body + "}" + "\n";
        body = body + "\n";

        // Add Formula Output
        String output = "echo json_encode(array('ret' => 10000, 'msg' => 'Success', 'result' => $result));";
        body = body + output + "\n";
        body = body + "\n";

        body = body + "}" + "\n";
        return body;
    }

    public void onClickBtnSave(View view) {
        str_name = txt_name.getText().toString();
        if (AppUtils.isCheckSpelling(str_name)) {
            onShowAavailableAlert();
            return;
        }

        str_description = txt_description.getText().toString();
        if (str_description.length() == 0) {
            str_description = getResources().getString(R.string.item_create_description);
        }

        onEventSaveAlgorithm();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setToolbar();
    }

}
