package com.shindev.rulecalculator.util;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.dkv.bubblealertlib.AppConstants;
import com.dkv.bubblealertlib.AppLog;
import com.dkv.bubblealertlib.BblContentFragment;
import com.dkv.bubblealertlib.BblDialog;
import com.dkv.bubblealertlib.IAlertClickedCallBack;

import com.shindev.rulecalculator.R;
import com.shindev.rulecalculator.model.CreateParaItem;
import com.shindev.rulecalculator.model.FunctionItem;
import com.shindev.rulecalculator.model.ParamInfo;
import com.shindev.rulecalculator.model.ProjectItem;
import com.shindev.rulecalculator.model.UserInfo;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class AppUtils {

    static public UserInfo gUserInfo = new UserInfo();
    static public String gAlgorithm = "";

    static public List<FunctionItem> gFuncItems = new ArrayList<>();
    static public FunctionItem gSelFunc = new FunctionItem();

    static public List<CreateParaItem> gCreateParams = new ArrayList<>();
    static public CreateParaItem gSelParaItem = new CreateParaItem();
    static public int gSelIndex = 0;

    static public ProjectItem gSelProject = new ProjectItem();

    static public List<String[]> gParamValues = new ArrayList<>();
    static public List<ParamInfo> gSelParams = new ArrayList<>();

    static public int gFuncParams = 0;

    static public int gPaymentIndex = -1;

    static public UserInfo gSelUser = new UserInfo();

    //    wx8e105a688b4b4f24
    public static final String APP_ID_WX = "wx8e105a688b4b4f24";
    public static final String APP_SECRET_WX = "0bb4327be7191a1ac2c45d0b69a1c28d";

    //    1511744901
    public static final String APP_MERCHANT_ID = "1511744901";
    public static final String APP_KEY = "nianxing94xingnian0011nianxing99";



    public static void initUIActivity (AppCompatActivity activity) {
        // Change Status Bar Color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity.getWindow().setStatusBarColor(activity.getColor(R.color.colorMainGreen));
        } else {
            activity.getWindow().setStatusBarColor(ContextCompat.getColor(activity, R.color.colorMainGreen));
        }
    }

    public static void showOtherActivity (Activity activity, Class<?> cls, int direction) {
        Intent myIntent = new Intent(activity, cls);
        ActivityOptions options;
        switch (direction) {
            case 0:
                options = ActivityOptions.makeCustomAnimation(activity, R.anim.slide_in_right, R.anim.slide_out_left);
                activity.startActivity(myIntent, options.toBundle());
                break;
            case 1:
                options = ActivityOptions.makeCustomAnimation(activity, R.anim.slide_in_left, R.anim.slide_out_right);
                activity.startActivity(myIntent, options.toBundle());
                break;
            default:
                activity.startActivity(myIntent);
                break;
        }
    }

    static public void onShowCustomAlert(AppCompatActivity activity,String title,String icon, String content, IAlertClickedCallBack callback) {
        try {
            BblContentFragment fragment = BblContentFragment.newInstance(AppConstants.TAG_FEEDBACK_SUCCESS);
            if (TextUtils.isEmpty(content)) {
                content = activity.getString(R.string.err_server_error);
            }
            fragment.setContent(content, "好的", "取消", null, title);
            fragment.setClickedCallBack(callback);
            BblDialog sampleDialog = new BblDialog();
            sampleDialog.setContentFragment(fragment
                    , R.layout.layout_bbl_content
                    , LayoutInflater.from(activity)
                    , content
                    , icon
                    , activity);
            sampleDialog.setDisMissCallBack(null);
            activity.getSupportFragmentManager().beginTransaction().add(sampleDialog, "Test").commitAllowingStateLoss();
        } catch (Exception e) {
            AppLog.logException(AppConstants.TAG_FEEDBACK_SUCCESS, e);
        }
    }

    static public void onShowCustomNoCallback(AppCompatActivity activity,String title, String icon, String content) {
        try {
            BblContentFragment fragment = BblContentFragment.newInstance(AppConstants.TAG_FEEDBACK_SUCCESS);
            if (TextUtils.isEmpty(content)) {
                content = activity.getString(R.string.err_server_error);
            }
            fragment.setContent(content, "好的", null, null, title);
            fragment.setClickedCallBack(new IAlertClickedCallBack() {
                @Override
                public void onOkClicked(String tag) {

                }

                @Override
                public void onCancelClicked(String tag) {

                }

                @Override
                public void onExitClicked(String tag) {

                }
            });
            BblDialog sampleDialog = new BblDialog();
            sampleDialog.setContentFragment(fragment
                    , R.layout.layout_bbl_content
                    , LayoutInflater.from(activity)
                    , content
                    , icon
                    , activity);
            sampleDialog.setDisMissCallBack(null);
            activity.getSupportFragmentManager().beginTransaction().add(sampleDialog, "Test").commitAllowingStateLoss();
        } catch (Exception e) {
            AppLog.logException(AppConstants.TAG_FEEDBACK_SUCCESS, e);
        }
    }

    public static boolean isCheckSpelling (String str) {
        if (str.length() == 0) {
            return true;
        }
        String str_first_able = "abcdefghijklmnopqrstuvwxyz";
        String str_first = Character.toString(str.charAt(0));
        if (!str_first_able.contains(str_first)) {
            return true;
        }
        String str_able = "abcdefghijklmnopqrstuvwxyz_1234567890";
        for (int i = 1; i < str.length(); i++) {
            String letter = Character.toString(str.charAt(i));
            if (!str_able.contains(letter)) {
                return true;
            }
        }
        return false;
    }

    public static void writeToFile(String data, String filename, Context context) {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), context.getString(R.string.app_name));
        if (!file.exists()) {
            if (!file.mkdirs()) {
                return;
            }
        }
        if(!file.exists()){
            file.mkdir();
        }

        try{
            File gpxfile = new File(file, filename + ".txt");
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(data);
            writer.flush();
            writer.close();

            Toast.makeText(context, gpxfile.getAbsolutePath(), Toast.LENGTH_SHORT).show();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
