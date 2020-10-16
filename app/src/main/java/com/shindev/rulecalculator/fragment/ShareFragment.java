package com.shindev.rulecalculator.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.shindev.rulecalculator.R;
import com.shindev.rulecalculator.activity.CalcActivity;
import com.shindev.rulecalculator.activity.GuideProjectActivity;
import com.shindev.rulecalculator.activity.MainActivity;
import com.shindev.rulecalculator.activity.ProjectDetailActivity;
import com.shindev.rulecalculator.activity.UserDetailActivity;
import com.shindev.rulecalculator.adapter.ProjectAdapter;
import com.shindev.rulecalculator.adapter.SharedAdapter;
import com.shindev.rulecalculator.dialog.UsersDialog;
import com.shindev.rulecalculator.model.ProjectItem;
import com.shindev.rulecalculator.model.UserInfo;
import com.shindev.rulecalculator.util.APIManager;
import com.shindev.rulecalculator.util.AppUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShareFragment extends Fragment {

    private MainActivity mActivity;

    private LinearLayout llt_share, llt_shared;
    private TextView lbl_share, lbl_shared;
    private ListView lst_project;

    private ArrayList<ProjectItem> share_project = new ArrayList<>();
    private ArrayList<ProjectItem> shared_project = new ArrayList<>();
    private ArrayList<UserInfo> shared_user = new ArrayList<>();

    private ProjectAdapter mShareAdapter;
    private SharedAdapter mSharedAdapter;
    private boolean isShare = true;

    private ProjectAdapter.ProjectAdapterCallback projectAdapterCallback = new ProjectAdapter.ProjectAdapterCallback() {
        @Override
        public void onClickItemView(ProjectItem item) {
            AppUtils.gSelProject = item;
            AppUtils.showOtherActivity(getActivity(), ProjectDetailActivity.class, 0);
        }

        @Override
        public void onClickCalculateBtn(ProjectItem item) {
            AppUtils.gSelProject = item;
            AppUtils.showOtherActivity(getActivity(), CalcActivity.class, 0);
        }

        @Override
        public void onClickDeleteBtn(ProjectItem item) {
            //
        }
    };

    private SharedAdapter.SharedAdapterCallback sharedAdapterCallback = new SharedAdapter.SharedAdapterCallback() {
        @Override
        public void onClickItemView(ProjectItem item) {
            AppUtils.gSelProject = item;
            AppUtils.showOtherActivity(getActivity(), ProjectDetailActivity.class, 0);
        }

        @Override
        public void onClickCalculateBtn(ProjectItem item) {
            AppUtils.gSelProject = item;
            AppUtils.showOtherActivity(getActivity(), CalcActivity.class, 0);
        }

        @Override
        public void onClickUserLlt(UserInfo user) {
            AppUtils.gSelUser = user;
            AppUtils.showOtherActivity(getActivity(), UserDetailActivity.class, 0);
        }
    };


    public ShareFragment(MainActivity activity) {
        mActivity = activity;
    }

    private void initFragmentEvent() {
        llt_share.setOnClickListener(view -> {
            lbl_share.setTextColor(getContext().getColor(R.color.colorMainGreen));
            lbl_shared.setTextColor(getContext().getColor(R.color.colorMainWhite));
            llt_share.setBackground(getContext().getDrawable(R.drawable.share_left_white_12));
            llt_shared.setBackground(getContext().getDrawable(R.drawable.share_right_green_12));

            isShare = true;
            onShowProject();
        });

        llt_shared.setOnClickListener(view -> {
            lbl_share.setTextColor(getContext().getColor(R.color.colorMainWhite));
            lbl_shared.setTextColor(getContext().getColor(R.color.colorMainGreen));
            llt_share.setBackground(getContext().getDrawable(R.drawable.share_left_green_12));
            llt_shared.setBackground(getContext().getDrawable(R.drawable.share_right_white_12));

            isShare = false;
            onShowProject();
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.fragment_share, container, false);
        mActivity.toolbar.setTitle(R.string.main_share);
        mActivity.onHideMenuBtn();

        initFragmentUI(mainView);
        initFragmentEvent();

        return mainView;
    }

    private void initFragmentUI(View view) {
        lst_project = view.findViewById(R.id.lst_share_item);
        mShareAdapter = new ProjectAdapter(getContext(), share_project, projectAdapterCallback);
        mShareAdapter.setHideDeleteBtn();
        lst_project.setAdapter(mShareAdapter);

        mSharedAdapter = new SharedAdapter(getContext(), shared_project, shared_user, sharedAdapterCallback);

        llt_share = view.findViewById(R.id.llt_main_share);
        llt_shared = view.findViewById(R.id.llt_main_shared);
        lbl_share = view.findViewById(R.id.lbl_main_share);
        lbl_shared = view.findViewById(R.id.lbl_main_shared);

        initDatas();
    }

    private void initDatas() {
        mActivity.dialog.show();

        Map<String, String> params = new HashMap<>();
        params.put("id", AppUtils.gUserInfo.id);
        APIManager.onAPIConnectionResponse(APIManager.LAO_GET_SHARE_PROJECT, params, APIManager.APIMethod.POST, new APIManager.APIManagerCallback() {
            @Override
            public void onEventCallBack(JSONObject obj, int ret) {
                if (ret == 10000) {
                    try {
                        JSONArray result = obj.getJSONArray("result");
                        share_project.clear();

                        if (result.length() > 0) {
                            for (int i = 0; i < result.length(); i++) {
                                JSONObject json = result.getJSONObject(i);

                                ProjectItem item = new ProjectItem();
                                item.initialWithJson(json);

                                boolean isRepeat = false;
                                for (ProjectItem projectItem: share_project) {
                                    if (projectItem.id.equals(item.id)) {
                                        isRepeat = true;
                                    }
                                }
                                if (!isRepeat) {
                                    share_project.add(item);
                                }
                            }
                            onShowProject();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
                mActivity.dialog.dismiss();
            }

            @Override
            public void onEventInternetError(Exception e) {
                mActivity.dialog.dismiss();
                Toast.makeText(getContext(), R.string.alert_error_internet_detail, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onEventServerError(Exception e) {
                mActivity.dialog.dismiss();
                Toast.makeText(getContext(), R.string.alert_server_error_detail, Toast.LENGTH_SHORT).show();
            }
        });

        APIManager.onAPIConnectionResponse(APIManager.LAO_GET_SHARED_PROJECT, params, APIManager.APIMethod.POST, new APIManager.APIManagerCallback() {
            @Override
            public void onEventCallBack(JSONObject obj, int ret) {
                if (ret == 10000) {
                    try {
                        JSONArray result = obj.getJSONArray("result");
                        shared_project.clear();
                        shared_user.clear();

                        if (result.length() > 0) {
                            for (int i = 0; i < result.length(); i++) {
                                JSONObject json = result.getJSONObject(i);

                                ProjectItem item = new ProjectItem();
                                item.initialWithJson(json);

                                JSONObject userJson = json.getJSONObject("user");
                                UserInfo user = new UserInfo();
                                user.initialWithJson(userJson);
                                shared_user.add(user);

                                shared_project.add(item);
                            }
                            onShowProject();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
                mActivity.dialog.dismiss();
            }

            @Override
            public void onEventInternetError(Exception e) {
                mActivity.dialog.dismiss();
                Toast.makeText(getContext(), R.string.alert_error_internet_detail, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onEventServerError(Exception e) {
                mActivity.dialog.dismiss();
                Toast.makeText(getContext(), R.string.alert_server_error_detail, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onShowProject() {
        if (isShare) {
            lst_project.setAdapter(mShareAdapter);
            mShareAdapter.notifyDataSetChanged();
        } else {
            lst_project.setAdapter(mSharedAdapter);
            mSharedAdapter.notifyDataSetChanged();
        }

    }

}
