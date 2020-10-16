package com.shindev.rulecalculator.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.shindev.rulecalculator.R;
import com.shindev.rulecalculator.activity.CalcActivity;
import com.shindev.rulecalculator.activity.GuideProjectActivity;
import com.shindev.rulecalculator.activity.MainActivity;
import com.shindev.rulecalculator.activity.ProjectDetailActivity;
import com.shindev.rulecalculator.adapter.ProjectAdapter;
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

public class ProjectFragment extends Fragment {

    private MainActivity mActivity;

    private ArrayList<ProjectItem> ary_project = new ArrayList<>();
    private ProjectAdapter mAdapter;

    private boolean isSelector = false;
    private LinearLayout llt_floating;

    private List<UserInfo> allUsers = new ArrayList<>();
    private List<String> mChecks = new ArrayList<>();

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
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(mActivity, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
            alertDialog.setTitle(getString(R.string.alert_delete_project_title));
            alertDialog.setMessage(getString(R.string.alert_delete_project_detail));

            alertDialog.setPositiveButton(R.string.ok, (dialog, which) -> {
                onEventDeleteProject(item);
            });

            alertDialog.setNegativeButton(R.string.formula_cancel, (dialog, which) -> dialog.cancel());

            AlertDialog dialog = alertDialog.create();
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
    };

    private void onEventDeleteProject(ProjectItem item) {
        mActivity.dialog.show();

        Map<String, String> params = new HashMap<>();
        params.put("projectid", item.id);
        APIManager.onAPIConnectionResponse(APIManager.LAO_DEL_PROJECT, params, APIManager.APIMethod.POST, new APIManager.APIManagerCallback() {
            @Override
            public void onEventCallBack(JSONObject obj, int ret) {
                if (ret == 10000) {
                    ary_project.remove(item);
                    mAdapter.notifyDataSetChanged();
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


    public ProjectFragment(MainActivity activity) {
        mActivity = activity;
    }

    private void initFragmentEvent() {
        mActivity.setActivityCallback(new MainActivity.MainActivityCallback() {
            @Override
            public void onClickToolbarItem() {
                if (isSelector) {
                    int cnt = 0;
                    for (ProjectItem item: ary_project) {
                        if (item.isSelected) {
                            cnt++;
                        }
                    }
                    if (cnt == 0) {
                        isSelector = false;
                        mAdapter.setCheckable(false);
                        mActivity.onShowCheckBtn();
                    } else {
                        onShowShareDialog();
                    }
                } else {
                    isSelector = true;
                    mAdapter.setCheckable(true);
                    mActivity.onShowShareBtn();
                }
            }

            @Override
            public void onClickShareProject() {
                int cnt = 0;
                for (ProjectItem item: ary_project) {
                    if (item.isSelected) {
                        cnt++;
                    }
                }
                if (cnt > 0) {
                    onPostProjectToServer();
                }
            }
        });

        llt_floating.setOnClickListener(view -> {
            // New Project Creation
            AppUtils.showOtherActivity(getActivity(), GuideProjectActivity.class, 0);
        });
    }

    private void onPostProjectToServer() {
        String projectId = "";
        for (ProjectItem item: ary_project) {
            if (item.isSelected) {
                projectId = projectId + item.id + ",";
            }
        }
        projectId = projectId.substring(0, projectId.length() - 1);

        mActivity.dialog.show();

        Map<String, String> params = new HashMap<>();
        params.put("id", AppUtils.gUserInfo.id);
        params.put("projectid", projectId);

        APIManager.onAPIConnectionResponse(APIManager.LAO_POST_PROJECT, params, APIManager.APIMethod.POST, new APIManager.APIManagerCallback() {
            @Override
            public void onEventCallBack(JSONObject obj, int ret) {
                if (ret == 10000) {
                    Toast.makeText(getContext(), "成功岗位。", Toast.LENGTH_SHORT).show();
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

        mChecks.clear();
        for (UserInfo user: allUsers) {
            String str = "0";
            mChecks.add(str);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.fragment_project, container, false);
        mActivity.toolbar.setTitle(R.string.main_project);
        mActivity.onShowMenuBtn();

        initFragmentUI(mainView);
        initFragmentEvent();

        return mainView;
    }

    private void initFragmentUI(View view) {
        ListView lst_project = view.findViewById(R.id.lst_project_item);
        mAdapter = new ProjectAdapter(getContext(), ary_project, projectAdapterCallback);
        lst_project.setAdapter(mAdapter);

        llt_floating = view.findViewById(R.id.llt_floating);

        initDatas();
    }

    private void initDatas() {
        mActivity.dialog.show();

        Map<String, String> params = new HashMap<>();
        params.put("id", AppUtils.gUserInfo.id);
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

        APIManager.onAPIConnectionResponse(APIManager.LAO_GET_ALL_USERS, null, APIManager.APIMethod.POST, new APIManager.APIManagerCallback() {
            @Override
            public void onEventCallBack(JSONObject obj, int ret) {
                if (ret == 10000) {
                    try {
                        JSONArray result = obj.getJSONArray("result");
                        allUsers.clear();

                        if (result.length() > 0) {
                            for (int i = 0; i < result.length(); i++) {
                                JSONObject json = result.getJSONObject(i);

                                UserInfo item = new UserInfo();
                                item.initialWithJson(json);
                                if (item.id.equals(AppUtils.gUserInfo.id)) {
                                    continue;
                                }

                                allUsers.add(item);
                            }
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

    private void onShowShareDialog() {
        UsersDialog dialog = new UsersDialog(getContext(), userid -> {
            String projectId = "";
            for (ProjectItem item: ary_project) {
                if (item.isSelected) {
                    projectId = projectId + item.id + ",";
                }
            }
            projectId = projectId.substring(0, projectId.length() - 1);

            mActivity.dialog.show();

            Map<String, String> params = new HashMap<>();
            params.put("id", AppUtils.gUserInfo.id);
            params.put("userid", userid);
            params.put("projectid", projectId);

            APIManager.onAPIConnectionResponse(APIManager.LAO_SHARE_PROJECT, params, APIManager.APIMethod.POST, new APIManager.APIManagerCallback() {
                @Override
                public void onEventCallBack(JSONObject obj, int ret) {
                    if (ret == 10000) {
                        Toast.makeText(getContext(), "分享成功。", Toast.LENGTH_SHORT).show();
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
        });
        mChecks.clear();
        for (UserInfo user: allUsers) {
            String str = "0";
            mChecks.add(str);
        }
        dialog.setDialogDatas(allUsers, mChecks);
        dialog.show();
    }

}
