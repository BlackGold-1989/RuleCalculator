package com.shindev.rulecalculator.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.shindev.rulecalculator.R;
import com.shindev.rulecalculator.activity.EditProfileActivity;
import com.shindev.rulecalculator.activity.GuideActivity;
import com.shindev.rulecalculator.activity.HelpActivity;
import com.shindev.rulecalculator.activity.LoginActivity;
import com.shindev.rulecalculator.activity.MainActivity;
import com.shindev.rulecalculator.activity.SupportActivity;
import com.shindev.rulecalculator.activity.WechatPayActivity;
import com.shindev.rulecalculator.util.APIManager;
import com.shindev.rulecalculator.util.AppUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment {

    private MainActivity mActivity;
    private View mainView;


    public ProfileFragment(MainActivity activity) {
        mActivity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_profile, container, false);

        String str_classname = AppUtils.gUserInfo.classname;
        if (str_classname.length() == 0) {
            onShowAlertClassName(mainView);
        } else {
            initFragmentUI(mainView);
        }

        return mainView;
    }

    private void onShowAlertClassName(final View view) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        alertDialog.setTitle(R.string.profile_alert_detail);

        final EditText input = new EditText(getActivity());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        lp.setMargins(10, 0, 10, 0);
        input.setLayoutParams(lp);
        alertDialog.setView(input);

        alertDialog.setPositiveButton(R.string.ok,
                (dialog, which) -> {
                    String str_value = input.getText().toString();
                    if (AppUtils.isCheckSpelling(str_value)) {
                        onShowAavailableAlert(view);
                        return;
                    }
                    onRegisterClassName(view, str_value);
                });

        alertDialog.show();
    }

    private void onShowAavailableAlert(final View view) {
        new AlertDialog.Builder(getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
                .setTitle(R.string.alert_waring_title)
                .setMessage(R.string.alert_para_able_detail)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    // Continue with delete operation
                    onShowAlertClassName(view);
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void onRegisterClassName(final View view, String name) {
        Map<String, String> params = new HashMap<>();
        params.put("id", AppUtils.gUserInfo.id);
        params.put("classname", name);
        APIManager.onAPIConnectionResponse(APIManager.LAO_SAVE_CLASSNAME, params
                , APIManager.APIMethod.POST, new APIManager.APIManagerCallback() {
                    @Override
                    public void onEventCallBack(JSONObject obj, int ret) {
                        switch (ret) {
                            case 10000:
                                try {
                                    JSONObject obj_result = obj.getJSONObject("result");
                                    AppUtils.gUserInfo.initialWithJson(obj_result);
                                    initFragmentUI(view);
                                } catch (JSONException e) {
                                    Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                                    e.printStackTrace();
                                }
                                break;
                            case 10001:
                                onShowAlertClassName(view);
                                break;
                            default:
                                break;
                        }
                    }

                    @Override
                    public void onEventInternetError(Exception e) {

                    }

                    @Override
                    public void onEventServerError(Exception e) {

                    }
                });
    }

    private void initFragmentUI(View mainView) {
        mActivity.toolbar.setTitle(R.string.menu_profile);
        mActivity.onHideMenuBtn();

        LinearLayout llt_payed = mainView.findViewById(R.id.llt_profile_pay);
        llt_payed.setOnClickListener(v -> AppUtils.showOtherActivity(getActivity(), WechatPayActivity.class, 0));

        LinearLayout llt_report = mainView.findViewById(R.id.llt_profile_report);
        llt_report.setOnClickListener(v -> AppUtils.showOtherActivity(getActivity(), HelpActivity.class, 0));

        LinearLayout llt_guide = mainView.findViewById(R.id.llt_profile_guide);
        llt_guide.setOnClickListener(v -> AppUtils.showOtherActivity(getActivity(), GuideActivity.class, 0));

        LinearLayout llt_support = mainView.findViewById(R.id.llt_profile_support);
        llt_support.setOnClickListener(v -> AppUtils.showOtherActivity(getActivity(), SupportActivity.class, 0));

        LinearLayout llt_logout = mainView.findViewById(R.id.llt_profile_logout);
        llt_logout.setOnClickListener(v -> {
            AppUtils.gUserInfo.init();
            AppUtils.showOtherActivity(getActivity(), LoginActivity.class, 1);
            getActivity().finish();
        });

        LinearLayout llt_profile_edit = mainView.findViewById(R.id.llt_profile_edit);
        llt_profile_edit.setOnClickListener(view -> AppUtils.showOtherActivity(getActivity(), EditProfileActivity.class, 0));

        LinearLayout llt_profile_pay_history = mainView.findViewById(R.id.llt_profile_payhistory);
        llt_profile_pay_history.setVisibility(View.GONE);
        llt_profile_pay_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
            }
        });

        ImageView img_avatar = mainView.findViewById(R.id.img_profile_avatar);
        String url = AppUtils.gUserInfo.headurl;
        if (AppUtils.gUserInfo.openid.equals("")) {
            url = APIManager.UPLOAD_URL + url;
        }
        Picasso.with(getActivity()).load(url).fit().centerCrop()
                .placeholder(R.drawable.logo)
                .error(R.drawable.logo)
                .into(img_avatar, null);

        TextView lbl_name = mainView.findViewById(R.id.lbl_profile_name);
        lbl_name.setText(AppUtils.gUserInfo.nickname);

        TextView lbl_location = mainView.findViewById(R.id.lbl_profile_location);
        if (AppUtils.gUserInfo.openid.equals("")) {
            lbl_location.setText(AppUtils.gUserInfo.email);
        } else {
            lbl_location.setText(AppUtils.gUserInfo.openid);
        }

        TextView lbl_payed = mainView.findViewById(R.id.lbl_profile_payed);
        String str_payed;
        if (AppUtils.gUserInfo.payed > 50000) {
            str_payed = String.format("%.1f", (float) AppUtils.gUserInfo.payed / 100000.0f) + "K";
        } else if (AppUtils.gUserInfo.payed > 10000) {
            str_payed = AppUtils.gUserInfo.payed / 100 + getString(R.string.pay_unit);
        } else {
            str_payed = String.format("%.1f %s", (float) AppUtils.gUserInfo.payed / 100.0f, getActivity().getString(R.string.pay_unit));
        }
        lbl_payed.setText(str_payed);

        TextView lbl_func = mainView.findViewById(R.id.lbl_profile_func);
        lbl_func.setText(String.valueOf(AppUtils.gUserInfo.product));

        TextView lbl_rfunc = mainView.findViewById(R.id.lbl_profile_rfunc);
        lbl_rfunc.setText(String.valueOf(AppUtils.gUserInfo.functions));

        TextView lbl_update = mainView.findViewById(R.id.lbl_profile_regdate);
        lbl_update.setText(String.valueOf(AppUtils.gUserInfo.regDate));
    }

    @Override
    public void onResume() {
        super.onResume();
        initFragmentUI(mainView);
    }
}
