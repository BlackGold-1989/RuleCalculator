package com.shindev.rulecalculator.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.shindev.rulecalculator.R;
import com.shindev.rulecalculator.adapter.AddAdapter;
import com.shindev.rulecalculator.util.AppUtils;


public class AddParaValueActivity extends AppCompatActivity {

    private AddAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_para_value);

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
                if (id == R.id.menu_para_add){
                    onClickLltAddValue();
                    return true;
                }
                return false;
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_para, menu);
        return true;
    }

    private void initUIView() {
        AppUtils.gParamValues.clear();

        ListView lst_value = findViewById(R.id.lst_add_para);
        mAdapter = new AddAdapter(this, AppUtils.gSelParams, AppUtils.gParamValues);
        lst_value.setAdapter(mAdapter);
    }

    private void onClickLltAddValue() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_custom, null);
        dialogBuilder.setView(dialogView);

        EditText editText = dialogView.findViewById(R.id.alert_txt_content);

        dialogBuilder.setTitle(R.string.alert_custom_title)
                .setMessage(R.string.alert_custom_detail)
                .setPositiveButton(android.R.string.yes, (dialog1, which) -> {
                    // Continue with delete operation
                    String str_values = editText.getText().toString();
                    String[] str_value = str_values.split(",");
                    if (str_value.length == AppUtils.gSelParams.size()) {
                        AppUtils.gParamValues.add(str_value);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(this, R.string.alert_custom_failed, Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(android.R.string.no, null);

        AlertDialog dialog = dialogBuilder.create();
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

    public void onClickSaveBtn(View view) {
        if (AppUtils.gParamValues.size() == 0) {
            Toast.makeText(this, R.string.add_para_save_wrong, Toast.LENGTH_SHORT).show();
            return;
        }
        AppUtils.showOtherActivity(this, CalcActivity.class, 1);
    }

    public void onEventValueDelete(int index) {
        new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
                .setTitle(R.string.calc_alert_delete_title)
                .setMessage(R.string.calc_alert_delete_detail)
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    // Continue with delete operation
                    AppUtils.gParamValues.remove(index);
                    mAdapter.notifyDataSetChanged();
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
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
