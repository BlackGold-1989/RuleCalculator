package com.shindev.rulecalculator.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.shindev.rulecalculator.R;
import com.shindev.rulecalculator.activity.CalcActivity;
import com.shindev.rulecalculator.activity.MainActivity;
import com.shindev.rulecalculator.activity.ProjectDetailActivity;
import com.shindev.rulecalculator.activity.UserDetailActivity;
import com.shindev.rulecalculator.adapter.SharedAdapter;
import com.shindev.rulecalculator.model.ProjectItem;
import com.shindev.rulecalculator.model.UserInfo;
import com.shindev.rulecalculator.util.APIManager;
import com.shindev.rulecalculator.util.AppUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PostFragment extends Fragment {

    private MainActivity mActivity;

    private List<ProjectItem> allPostProject = new ArrayList<>();
    private List<UserInfo> allPostUser = new ArrayList<>();
    private SharedAdapter adapter;
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


    public PostFragment(MainActivity activity) {
        mActivity = activity;
    }

    private void initFragmentEvent() {
        //
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.fragment_post, container, false);
        mActivity.toolbar.setTitle(R.string.main_post);
        mActivity.onHideMenuBtn();

        initFragmentUI(mainView);
        initFragmentEvent();

        return mainView;
    }

    private void initFragmentUI(View view) {
        ListView lst_project = view.findViewById(R.id.lst_post_item);
        adapter = new SharedAdapter(getContext(), allPostProject, allPostUser, sharedAdapterCallback);
        lst_project.setAdapter(adapter);

        initDatas();
    }

    private void initDatas() {
        mActivity.dialog.show();

        APIManager.onAPIConnectionResponse(APIManager.LAO_POST_ALL, null, APIManager.APIMethod.POST, new APIManager.APIManagerCallback() {
            @Override
            public void onEventCallBack(JSONObject obj, int ret) {
                if (ret == 10000) {
                    try {
                        JSONArray result = obj.getJSONArray("result");
                        allPostProject.clear();
                        allPostUser.clear();

                        if (result.length() > 0) {
                            for (int i = 0; i < result.length(); i++) {
                                JSONObject json = result.getJSONObject(i);

                                ProjectItem item = new ProjectItem();
                                item.initialWithJson(json);

                                JSONObject userJson = json.getJSONObject("user");
                                UserInfo user = new UserInfo();
                                user.initialWithJson(userJson);
                                allPostUser.add(user);

                                allPostProject.add(item);
                            }
                            adapter.notifyDataSetChanged();
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


}
