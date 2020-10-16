package com.shindev.rulecalculator.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.c.progress_dialog.BlackProgressDialog;
import com.shindev.rulecalculator.R;
import com.shindev.rulecalculator.util.APIManager;
import com.shindev.rulecalculator.util.AppUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class TestAlgorithmActivity extends AppCompatActivity {

    private TextView lbl_result;
    private Button btn_next;


    private void initWithEvent() {
        btn_next.setOnClickListener(v -> AppUtils.showOtherActivity(TestAlgorithmActivity.this, SaveAlgorithmActivity.class, 0));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);

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
        btn_next = findViewById(R.id.btn_test_next);
        btn_next.setVisibility(View.GONE);

        TextView lbl_algorithm = findViewById(R.id.lbl_test_algorithm);
        lbl_algorithm.setText(AppUtils.gAlgorithm);

        lbl_result = findViewById(R.id.lbl_test_result);

        initWithEvent();
    }

    public void onClickBtnTest(View view) {
        String title = "Test_" + AppUtils.gUserInfo.id;
        String str_url = APIManager.SERVER_URL + title + "/test";

        Map<String, String> params = new HashMap<>();
        for (int i = 0; i < AppUtils.gCreateParams.size(); i++) {
            String key = AppUtils.gCreateParams.get(i).name;
            Random r = new Random();
            int i1 = r.nextInt(150 - 100) + 100;
            String value = String.valueOf(i1);
            params.put(key, value);
        }

        BlackProgressDialog dialog = new BlackProgressDialog(this, getString(R.string.common_connect));
        dialog.show();

        APIManager.onAPIConnectionResponse(str_url, params, APIManager.APIMethod.POST, new APIManager.APIManagerCallback() {
            @Override
            public void onEventCallBack(JSONObject obj, int ret) {
                switch (ret) {
                    case 10000:
                        try {
                            String str_msg = obj.getString("result");
                            lbl_result.setText(str_msg);
                            btn_next.setVisibility(View.VISIBLE);
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

    @Override
    protected void onResume() {
        super.onResume();
        setToolbar();
    }

}
