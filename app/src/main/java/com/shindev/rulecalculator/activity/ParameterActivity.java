package com.shindev.rulecalculator.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.dkv.bubblealertlib.ConstantsIcons;
import com.shindev.rulecalculator.R;
import com.shindev.rulecalculator.adapter.CreateParaAdapter;
import com.shindev.rulecalculator.util.AppUtils;

public class ParameterActivity extends AppCompatActivity {

    private CreateParaAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parameter);

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
            toolbar.setOnMenuItemClickListener(item -> {
                int id = item.getItemId();
                if (id == R.id.action_top) {
                    AppUtils.gSelParaItem = null;
                    AppUtils.showOtherActivity(this, ParaSettingActivity.class, 0);
                    return true;
                }
                return false;
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }

    private void initUIView() {
        ListView lst_para = findViewById(R.id.lst_parameter_main);
        lst_para.setOnItemClickListener((parent, view, position, id) -> {
            if (position == 0) {
                return;
            }
            AppUtils.gSelParaItem = AppUtils.gCreateParams.get(position - 1);
            AppUtils.gSelIndex = position - 1;

            AppUtils.showOtherActivity(ParameterActivity.this, ParaSettingActivity.class, 0);
        });
        mAdapter = new CreateParaAdapter(this, AppUtils.gCreateParams);
        lst_para.setAdapter(mAdapter);
    }

    private void onShowWaringAlert() {
        AppUtils.onShowCustomNoCallback(this, getString(R.string.setpara_alert_waring), ConstantsIcons.ALERT_ICON_INFO, getString(R.string.alert_create_add));
    }

    public void onClickParameterNextBtn (View view) {
        if (AppUtils.gCreateParams.size() == 0) {
            onShowWaringAlert();
            return;
        }
        AppUtils.showOtherActivity(this, FormulaActivity.class, 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

}
