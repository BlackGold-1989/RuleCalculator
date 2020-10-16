package com.shindev.rulecalculator.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.shindev.rulecalculator.R;
import com.shindev.rulecalculator.activity.MainActivity;
import com.shindev.rulecalculator.adapter.HistoryAdapter;
import com.shindev.rulecalculator.model.HistoryItem;
import com.shindev.rulecalculator.util.APIManager;
import com.shindev.rulecalculator.util.AppUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HistoryFragment extends Fragment {

    private MainActivity mActivity;

    private List<HistoryItem> allHistories = new ArrayList<>();
    private HistoryAdapter adapter;

    public HistoryFragment(MainActivity activity) {
        mActivity = activity;
    }

    private void initFragmentEvent() {
        //
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.fragment_history, container, false);
        mActivity.toolbar.setTitle(R.string.main_history);
        mActivity.onHideMenuBtn();

        initFragmentUI(mainView);
        initFragmentEvent();

        return mainView;
    }

    private void initFragmentUI(View view) {
        ListView lst_project = view.findViewById(R.id.lst_history_item);
        adapter = new HistoryAdapter(getContext(), allHistories);
        lst_project.setAdapter(adapter);

        initDatas();
    }

    private void initDatas() {
        mActivity.dialog.show();

        Map<String, String> params = new HashMap<>();
        params.put("id", AppUtils.gUserInfo.id);
        APIManager.onAPIConnectionResponse(APIManager.LAO_GET_HISTORY, params, APIManager.APIMethod.POST, new APIManager.APIManagerCallback() {
            @Override
            public void onEventCallBack(JSONObject obj, int ret) {
                if (ret == 10000) {
                    try {
                        JSONArray result = obj.getJSONArray("result");
                        allHistories.clear();

                        if (result.length() > 0) {
                            for (int i = 0; i < result.length(); i++) {
                                JSONObject json = result.getJSONObject(i);

                                HistoryItem item = new HistoryItem();
                                item.initialWithJson(json);

                                allHistories.add(item);
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
