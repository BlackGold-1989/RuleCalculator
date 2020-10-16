package com.shindev.rulecalculator.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.shindev.rulecalculator.R;
import com.shindev.rulecalculator.fragment.CParmFragment;
import com.shindev.rulecalculator.fragment.LParmFragment;
import com.shindev.rulecalculator.fragment.PParmFragment;
import com.shindev.rulecalculator.fragment.UParmFragment;
import com.shindev.rulecalculator.model.CreateParaItem;
import com.shindev.rulecalculator.util.APIManager;
import com.shindev.rulecalculator.util.AppUtils;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FormulaActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView lbl_formula;
    private LinearLayout llt_next, llt_key;

    private String str_formula = "";

    private boolean isEndBundle = false;
    private boolean isResult = false;

    @Override
    public void onClick(View v) {
        Button btn = (Button) v;
        String str_tag = (String) btn.getTag();
        String str_value = btn.getText().toString();

        switch (str_tag) {
            case "point":
            case "equal":
            case "number":
            case "operate":
            case "bundle":
            case "logical":
                str_formula = str_formula + str_value;
                lbl_formula.setText(str_formula);
                break;
            case "param":
                str_formula = str_formula + "$" + str_value;
                lbl_formula.setText(str_formula);
                break;
            case "result":
                str_formula = str_formula + "$Re";
                lbl_formula.setText(str_formula);
                isResult = true;
                break;
            case "if":
                str_formula = str_formula + "if";
                lbl_formula.setText(str_formula);
                break;
            case "space":
                str_formula = str_formula + " ";
                lbl_formula.setText(str_formula);
                break;
            case "enter":
                if (isEndBundle) {
                    str_formula = str_formula + " \n";
                } else {
                    str_formula = str_formula + "; \n";
                }
                lbl_formula.setText(str_formula);
                if (isResult) {
                    onCompleteAlgorithmEditing();
                }
                break;
            case "back":
                onClickBackKeyboard();
                break;
            case "clear":
                onClickClearKeyboard();
                break;
        }
        isEndBundle = str_value.equals("{") || str_value.equals("}");
    }

    private void onCompleteAlgorithmEditing() {
        llt_key.setVisibility(View.GONE);
        llt_next.setVisibility(View.VISIBLE);
    }

    private void onClickBackKeyboard() {
        if (str_formula.length() == 0) {
            return;
        }
        List<String> ary_formula = Arrays.asList(str_formula.split("\n"));
        List<String> ary_new = new ArrayList<>();
        for (int i = 0; i < ary_formula.size() - 1; i++) {
            String formula = ary_formula.get(i);
            ary_new.add(formula);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            str_formula = String.join("\n", ary_new);
        } else {
            str_formula = TextUtils.join("\n", ary_new);
        }
        if (str_formula.length() > 0) {
            str_formula = str_formula + "\n";
        }
        lbl_formula.setText(str_formula);
        isEndBundle = false;
    }

    private void onClickClearKeyboard() {
        str_formula = "";
        lbl_formula.setText(str_formula);
        isEndBundle = false;
    }

    public void onClickBtnTest (View view) {
        AppUtils.gAlgorithm = lbl_formula.getText().toString();

        Map<String, String> params = new HashMap<>();
        params.put("id", AppUtils.gUserInfo.id);
        params.put("body", getFormulaBody("test"));
        APIManager.onAPIConnectionResponse(APIManager.LAO_TEST, params, APIManager.APIMethod.POST, new APIManager.APIManagerCallback() {
            @Override
            public void onEventCallBack(JSONObject obj, int ret) {
                AppUtils.showOtherActivity(FormulaActivity.this, TestAlgorithmActivity.class, 0);
            }

            @Override
            public void onEventInternetError(Exception e) {

            }

            @Override
            public void onEventServerError(Exception e) {

            }
        });
    }

    private void setToolbar() {
        setTitle(R.string.formula_title);
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.drawable.ic_left_white);
            toolbar.setNavigationOnClickListener(v -> {
                onBackPressed();
            });
            toolbar.setOnMenuItemClickListener(item -> {
                int id = item.getItemId();
                if (id == R.id.menu_formula_add){
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
        getMenuInflater().inflate(R.menu.menu_formula, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formula);

        AppUtils.initUIActivity(this);
        setToolbar();

        initUIView();
    }

    private void initUIView() {
        lbl_formula = findViewById(R.id.lbl_formula_main);
        llt_key = findViewById(R.id.llt_formula_key);
        llt_next = findViewById(R.id.llt_formula_next);

        ViewPager viewPager = findViewById(R.id.vpr_formula);
        viewPager.setAdapter(new FormulaAdapter(getSupportFragmentManager()));
    }

    private void onClickLltAdd () {
        if (lbl_formula.getText().toString().length() > 0 ) {
            return;
        }
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("*/*");
        startActivityForResult(intent, 0);
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

            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(getContentResolver().openInputStream(data.getData())));
                String line;
                String str_data = "";

                while ((line = br.readLine()) != null) {
                    if (line.equals("\n")) {
                        continue;
                    }
                    str_data = str_data + line + "\n";
                }
                br.close();

                lbl_formula.setText(str_data);
                llt_next.setVisibility(View.VISIBLE);
                llt_key.setVisibility(View.GONE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getFormulaBody(String name) {
        str_formula = AppUtils.gAlgorithm;
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

    class FormulaAdapter extends FragmentPagerAdapter {

        public FormulaAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    PParmFragment pFragment = new PParmFragment();
                    pFragment.setpParmFragmentCallback(FormulaActivity.this::onClick);
                    return pFragment;
                case 1:
                    UParmFragment uPragment = new UParmFragment(AppUtils.gCreateParams);
                    uPragment.setuParmFragmentCallback(FormulaActivity.this::onClick);
                    return uPragment;
                case 2:
                    CParmFragment cFragment = new CParmFragment();
                    cFragment.setcParmFragmentCallback(FormulaActivity.this::onClick);
                    return cFragment;
                case 3:
                    LParmFragment lFragment = new LParmFragment();
                    lFragment.setlParmFragmentCallback(FormulaActivity.this::onClick);
                    return lFragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 4;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setToolbar();
    }

}
