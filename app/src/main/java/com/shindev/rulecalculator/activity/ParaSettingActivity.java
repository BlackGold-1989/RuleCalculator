package com.shindev.rulecalculator.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.dkv.bubblealertlib.ConstantsIcons;
import com.dkv.bubblealertlib.IAlertClickedCallBack;
import com.shindev.rulecalculator.R;
import com.shindev.rulecalculator.model.CreateParaItem;
import com.shindev.rulecalculator.util.AppUtils;

import static com.shindev.rulecalculator.util.AppUtils.gSelIndex;


public class ParaSettingActivity extends AppCompatActivity {

    private EditText txt_name, txt_desc;
    private Button btn_set;

    private MenuItem menuItem;

    boolean isAdd = false;


    private void initWithEvent() {
        btn_set.setOnClickListener(v -> {
            CreateParaItem item = new CreateParaItem();
            item.id = String.valueOf(AppUtils.gCreateParams.size());
            item.name = txt_name.getText().toString();
            if (!isCheckedValue()) {
                onShowCheckedAlert();
                return;
            }
            if (AppUtils.isCheckSpelling(item.name)) {
                onShowAavailableAlert();
                return;
            }
            String str_desc = txt_desc.getText().toString();
            if (str_desc.length() == 0) {
                str_desc = getString(R.string.item_create_description);
            }
            item.description = str_desc;
            if (isAdd) {
                AppUtils.gCreateParams.add(item);
            } else {
                AppUtils.gCreateParams.set(gSelIndex, item);
            }
            onBackPressed();
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setpara);

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
                if (id == R.id.menu_setting_delete){
                    onClickDeleteAlert();
                    return true;
                }
                return false;
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_set_para, menu);
        menuItem = menu.findItem(R.id.menu_setting_delete);
        if (AppUtils.gSelParaItem == null) {
            menuItem.setVisible(false);
        } else {
            menuItem.setVisible(true);
        }
        return true;
    }

    private void initUIView() {
        txt_name = findViewById(R.id.txt_setpara_name);
        txt_desc = findViewById(R.id.txt_setpara_desc);
        btn_set = findViewById(R.id.btn_setpara_set);

        if (AppUtils.gSelParaItem == null) {
            isAdd = true;
        } else {
            isAdd = false;
            txt_name.setText(AppUtils.gSelParaItem.name);
            txt_desc.setText(AppUtils.gSelParaItem.description);
        }

        initWithEvent();
    }

    private void onShowAavailableAlert() {
        AppUtils.onShowCustomNoCallback(this,
                getString(R.string.alert_waring_title),
                ConstantsIcons.ALERT_ICON_INFO, getString(R.string.alert_para_able_detail));
    }

    private void onShowCheckedAlert() {
        AppUtils.onShowCustomNoCallback(this,
                getString(R.string.alert_waring_title),
                ConstantsIcons.ALERT_ICON_INFO, getString(R.string.setpara_alert_wrong));
    }

    private boolean isCheckedValue() {
        String name = txt_name.getText().toString();
        if (name.length() == 0) {
            return false;
        }
        for (int i = 0; i < AppUtils.gCreateParams.size(); i++) {
            CreateParaItem item = AppUtils.gCreateParams.get(i);
            if (item.name.equals(name)) {
                return false;
            }
        }
        return true;
    }

    private void onClickDeleteAlert() {
        AppUtils.onShowCustomAlert(this,
                getString(R.string.setpara_alert_delete),
                ConstantsIcons.ALERT_ICON_INFO,
                getString(R.string.setpara_alert_deletedetail),
                new IAlertClickedCallBack()
                {
                    @Override
                    public void onOkClicked(String tag) {
                        AppUtils.gCreateParams.remove(gSelIndex);
                        onBackPressed();
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

}
