package com.shindev.rulecalculator.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.widget.Toast;

import com.shindev.rulecalculator.R;
import com.shindev.rulecalculator.activity.FormulaActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class APIManager {
    public static final String SERVER_URL = "https://rule.daxiao-itdev.com/";

    private static final String BASE_URL = SERVER_URL +  "Backend/";
    public static final String DOWNLOAD_URL = SERVER_URL +  "download/";
    public static final String UPLOAD_URL = SERVER_URL +  "uploads/";

    //    New Updated Version
    public static final String LAO_LOGIN = BASE_URL + "lao_wechat_login";
    public static final String LAO_NORMAL_LOGIN = BASE_URL + "lao_normal_login";
    public static final String LAO_GET_ALL_USERS = BASE_URL + "lao_get_all_users";
    public static final String LAO_SHARE_PROJECT = BASE_URL + "lao_share_project";
    public static final String LAO_GET_SHARE_PROJECT = BASE_URL + "lao_get_share_project";
    public static final String LAO_GET_SHARED_PROJECT = BASE_URL + "lao_get_shared_project";
    public static final String LAO_GET_PROJECT = BASE_URL + "lao_get_project";
    public static final String LAO_SAVE_CLASSNAME = BASE_URL + "lao_classname";
    public static final String LAO_TEST = BASE_URL + "lao_test";
    public static final String LAO_SAVE_FORMULA = BASE_URL + "lao_save_formula";
    public static final String LAO_GET_FUNCTION_USER = BASE_URL + "lao_get_func";
    public static final String LAO_CREATE_PROJECT = BASE_URL + "lao_create_project";
    public static final String LAO_GET_FUNCTION_PROJECT = BASE_URL + "lao_get_function";
    public static final String LAO_BUY_FUNCTIONS = BASE_URL + "lao_buy_function";
    public static final String LAO_HELP = BASE_URL + "lao_help";
    public static final String LAO_CALC = BASE_URL + "lao_calc";
    public static final String LAO_SAVE_RESULT = BASE_URL + "lao_save";
    public static final String LAO_SHOW_RESULT = BASE_URL + "lao_result";
    public static final String LAO_FORGOT_PASSWORD = BASE_URL + "lao_forgot_pass";
    public static final String LAO_UPDATE_USER = BASE_URL + "lao_profile_update";
    public static final String LAO_GET_HISTORY = BASE_URL + "lao_get_history";
    public static final String LAO_DEL_PROJECT = BASE_URL + "lao_del_project";
    public static final String LAO_POST_PROJECT = BASE_URL + "lao_post_project";
    public static final String LAO_POST_ALL = BASE_URL + "lao_post_all";


    public enum APIMethod {
        GET, POST
    }

    public static void onAPIConnectionResponse (String url
            , Map<String, String> params
            , APIMethod method
            , APIManagerCallback apiResponse)
    {
        if (method == APIMethod.POST) {
            OkHttpUtils.post().url(url)
                    .params(params)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(okhttp3.Call call, Exception e, int id) {
                            apiResponse.onEventInternetError(e);
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            try {
                                JSONObject obj = new JSONObject(response);
                                int ret = obj.getInt("ret");
                                apiResponse.onEventCallBack(obj, ret);
                            } catch (JSONException e) {
                                apiResponse.onEventServerError(e);
                                e.printStackTrace();
                            }
                        }
                    });
        } else {
            OkHttpUtils.get().url(url)
                    .params(params)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(okhttp3.Call call, Exception e, int id) {
                            apiResponse.onEventInternetError(e);
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            try {
                                JSONObject obj = new JSONObject(response);
                                int ret = obj.getInt("ret");
                                apiResponse.onEventCallBack(obj, ret);
                            } catch (JSONException e) {
                                apiResponse.onEventServerError(e);
                                e.printStackTrace();
                            }
                        }
                    });
        }
    }

    public interface APIManagerCallback {
        void onEventCallBack(JSONObject obj, int ret);
        void onEventInternetError(Exception e);
        void onEventServerError(Exception e);
    }

}
