package com.shindev.rulecalculator.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.c.progress_dialog.BlackProgressDialog;
import com.shindev.rulecalculator.R;
import com.shindev.rulecalculator.adapter.ProjectAdapter;
import com.shindev.rulecalculator.model.ProjectItem;
import com.shindev.rulecalculator.util.APIManager;
import com.shindev.rulecalculator.util.AppUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserDetailActivity extends AppCompatActivity {

    private ArrayList<ProjectItem> ary_project = new ArrayList<>();
    private ProjectAdapter mAdapter;
    private ProjectAdapter.ProjectAdapterCallback projectAdapterCallback = new ProjectAdapter.ProjectAdapterCallback() {
        @Override
        public void onClickItemView(ProjectItem item) {
            AppUtils.gSelProject = item;
            AppUtils.showOtherActivity(UserDetailActivity.this, ProjectDetailActivity.class, 0);
        }

        @Override
        public void onClickCalculateBtn(ProjectItem item) {
//            AppUtils.gSelProject = item;
//            AppUtils.showOtherActivity(getActivity(), CalcActivity.class, 0);
        }

        @Override
        public void onClickDeleteBtn(ProjectItem item) {
            //
        }
    };


    private void initWithEvent() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        AppUtils.initUIActivity(this);
        setToolbar();

        initUIView();
        initWithData();
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
        ImageView img_avatar = findViewById(R.id.img_user_avatar);
        String url = AppUtils.gSelUser.headurl;
        if (AppUtils.gSelUser.openid.equals("")) {
            url = APIManager.UPLOAD_URL + url;
        }
        Picasso.with(this).load(url).fit().centerCrop()
                .placeholder(R.drawable.logo)
                .error(R.drawable.logo)
                .into(img_avatar, null);

        TextView lbl_name = findViewById(R.id.lbl_user_name);
        lbl_name.setText(AppUtils.gSelUser.nickname);

        TextView lbl_id = findViewById(R.id.lbl_user_id);
        String str = "";
        if (AppUtils.gSelUser.openid.length() == 0) {
            str = AppUtils.gSelUser.email;
        } else {
            str = AppUtils.gSelUser.openid;
        }
        lbl_id.setText(str);

        TextView lbl_reg = findViewById(R.id.lbl_user_regdate);
        lbl_reg.setText(AppUtils.gSelUser.regDate);

        ListView lst_project = findViewById(R.id.lst_user_project);
        mAdapter = new ProjectAdapter(this, ary_project, projectAdapterCallback);
        mAdapter.setCalculatable(false);
        lst_project.setAdapter(mAdapter);

        initWithEvent();
    }

    private void initWithData() {
        BlackProgressDialog dialog = new BlackProgressDialog(this, getString(R.string.common_connect));
        dialog.show();

        Map<String, String> params = new HashMap<>();
        params.put("id", AppUtils.gSelUser.id);

        APIManager.onAPIConnectionResponse(APIManager.LAO_GET_PROJECT, params, APIManager.APIMethod.POST, new APIManager.APIManagerCallback() {
            @Override
            public void onEventCallBack(JSONObject obj, int ret) {
                if (ret == 10000) {
                    try {
                        JSONArray result = obj.getJSONArray("result");
                        ary_project.clear();

                        if (result.length() > 0) {
                            for (int i = 0; i < result.length(); i++) {
                                JSONObject json = result.getJSONObject(i);

                                ProjectItem item = new ProjectItem();
                                item.initialWithJson(json);

                                ary_project.add(item);
                            }
                            mAdapter.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(UserDetailActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
                dialog.dismiss();
            }

            @Override
            public void onEventInternetError(Exception e) {
                dialog.dismiss();
                Toast.makeText(UserDetailActivity.this, R.string.alert_error_internet_detail, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onEventServerError(Exception e) {
                dialog.dismiss();
                Toast.makeText(UserDetailActivity.this, R.string.alert_server_error_detail, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
