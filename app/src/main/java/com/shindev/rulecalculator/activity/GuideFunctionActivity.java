package com.shindev.rulecalculator.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.shindev.rulecalculator.R;
import com.shindev.rulecalculator.util.AppUtils;

public class GuideFunctionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_function);

        AppUtils.initUIActivity(this);

        setToolbar();
        initUI();
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

    private void initUI() {
        Button btn_next = findViewById(R.id.btn_gfunc_next);
        btn_next.setOnClickListener(v ->
            AppUtils.showOtherActivity(this, ParameterActivity.class, 0)
        );
    }

}
