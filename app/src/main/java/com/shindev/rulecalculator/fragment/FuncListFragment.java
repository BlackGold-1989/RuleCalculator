package com.shindev.rulecalculator.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.shindev.rulecalculator.R;
import com.shindev.rulecalculator.activity.FunctionDetailActivity;
import com.shindev.rulecalculator.activity.GuideFunctionActivity;
import com.shindev.rulecalculator.activity.MainActivity;
import com.shindev.rulecalculator.adapter.FunctionAdapter;
import com.shindev.rulecalculator.model.FunctionItem;
import com.shindev.rulecalculator.util.APIManager;
import com.shindev.rulecalculator.util.AppUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FuncListFragment extends Fragment {

    private MainActivity mActivity;

    private ArrayList<FunctionItem> ary_functions = new ArrayList<>();
    private FunctionAdapter mAdapter;

    private boolean isSelector = false;
    private LinearLayout llt_floating;

    private FunctionAdapter.FunctionAdapterCallback functionAdapterCallback = item -> {
        AppUtils.gSelFunc = item;
        AppUtils.showOtherActivity(getActivity(), FunctionDetailActivity.class, 0);
    };

    public FuncListFragment(MainActivity activity) {
        mActivity = activity;
    }

    private void initFragmentEvent() {
        mActivity.setActivityCallback(new MainActivity.MainActivityCallback() {
            @Override
            public void onClickToolbarItem() {
                if (isSelector) {
                    int cnt = 0;
                    for (FunctionItem item: ary_functions) {
                        if (item.isSelected) {
                            cnt++;
                        }
                    }
                    if (cnt > 0) {
                        // New Project Creation
                    } else {
                        isSelector = false;
                        mAdapter.setCheckable(false);
                        mActivity.onShowCheckBtn();
                    }
                } else {
                    isSelector = true;
                    mAdapter.setCheckable(true);
                    mActivity.onShowAddBtn();
                }
            }

            @Override
            public void onClickShareProject() {

            }
        });

        llt_floating.setOnClickListener(view -> {
            // New Function Creation
            AppUtils.showOtherActivity(getActivity(), GuideFunctionActivity.class, 0);
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.fragment_func_list, container, false);
        mActivity.toolbar.setTitle(R.string.main_function);
        mActivity.onHideMenuBtn();

        initFragmentUI(mainView);
        initFragmentEvent();
        return mainView;
    }

    private void initFragmentUI (View view) {
        ListView lst_function = view.findViewById(R.id.lst_func_item);
        mAdapter = new FunctionAdapter(getContext(), ary_functions, functionAdapterCallback);
        lst_function.setAdapter(mAdapter);

        llt_floating = view.findViewById(R.id.llt_floating);

        initDatas();
    }

    private void initDatas() {
        mActivity.dialog.show();

        Map<String, String> params = new HashMap<>();
        params.put("id", AppUtils.gUserInfo.id);
        APIManager.onAPIConnectionResponse(APIManager.LAO_GET_FUNCTION_USER, params, APIManager.APIMethod.POST, new APIManager.APIManagerCallback() {
            @Override
            public void onEventCallBack(JSONObject obj, int ret) {
                if (ret == 10000) {
                    try {
                        JSONArray result = obj.getJSONArray("result");
                        ary_functions.clear();
                        AppUtils.gFuncItems.clear();

                        if (result.length() > 0) {
                            for (int i = 0; i < result.length(); i++) {
                                JSONObject json = result.getJSONObject(i);

                                FunctionItem item = new FunctionItem();
                                item.initialWithJson(json);

                                ary_functions.add(item);
                            }
                            AppUtils.gFuncItems.addAll(ary_functions);
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
    }

}
