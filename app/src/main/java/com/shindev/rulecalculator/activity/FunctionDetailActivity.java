package com.shindev.rulecalculator.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.shindev.rulecalculator.R;
import com.shindev.rulecalculator.model.ParamInfo;
import com.shindev.rulecalculator.util.AppUtils;

public class FunctionDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function_detail);

        AppUtils.initUIActivity(this);
        setToolbar();

        initListView();
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
        toolbar.setTitle(AppUtils.gSelFunc.name);
    }

    private void initListView() {
        TextView txt_params = findViewById(R.id.txt_fdetail_para);
        String str_params = "";
        for (ParamInfo paramInfo: AppUtils.gSelFunc.params) {
            str_params = str_params + paramInfo.name + " - " + paramInfo.description + " : " + paramInfo.regdate + "\n";
        }
        txt_params.setText(str_params);

        TextView txt_algorithm = findViewById(R.id.txt_fdetail_algo);
        txt_algorithm.setText(AppUtils.gSelFunc.content);
    }

    public void onClickBtnEdit(View view) {
        //
    }

    public void onClickLltDelete(View view) {
        //
    }

    @Override
    protected void onResume() {
        super.onResume();
        setToolbar();
    }

}
