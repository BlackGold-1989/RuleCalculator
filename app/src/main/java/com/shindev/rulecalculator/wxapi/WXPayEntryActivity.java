package com.shindev.rulecalculator.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.shindev.rulecalculator.R;
import com.shindev.rulecalculator.activity.HelpActivity;
import com.shindev.rulecalculator.activity.WechatPayActivity;
import com.shindev.rulecalculator.util.APIManager;
import com.shindev.rulecalculator.util.AppUtils;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler{

    private IWXAPI api;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);
        
    	api = WXAPIFactory.createWXAPI(this, AppUtils.APP_ID_WX, true);
		api.registerApp(AppUtils.APP_ID_WX);
		try {
			boolean result =  api.handleIntent(getIntent(), this);
			if(!result){
				finish();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			if (resp.errCode == 0) {
				switch (AppUtils.gPaymentIndex) {
					case 0: {
						Map<String, String> params = new HashMap<>();
						params.put("id", AppUtils.gUserInfo.id);
						params.put("addpay", WechatPayActivity.str_pay);
						params.put("addfunction", WechatPayActivity.str_function);
						params.put("other", String.format("Buy %s Functions", WechatPayActivity.str_function));

						OkHttpUtils.post().url(APIManager.LAO_BUY_FUNCTIONS)
								.params(params)
								.build()
								.execute(new StringCallback() {
									@Override
									public void onError(okhttp3.Call call, Exception e, int id) {
										Toast.makeText(getApplicationContext(), R.string.alert_error_internet_detail, Toast.LENGTH_SHORT).show();
										finish();
									}

									@Override
									public void onResponse(String response, int id) {
										try {
											JSONObject obj = new JSONObject(response);
											int ret = obj.getInt("ret");
											if (ret == 10000) {
												AppUtils.gUserInfo.initialWithJson(obj.getJSONObject("result"));
											}
										} catch (JSONException e) {
											e.printStackTrace();
										}
									}
								});
					}
						break;
					case 1: {
						Map<String, String> params = new HashMap<>();
						params.put("userid", AppUtils.gUserInfo.id);
						params.put("funcname", HelpActivity.funcName);
						params.put("funcdesc", HelpActivity.funcDescription);
						params.put("funccontent", HelpActivity.funcContent);

						OkHttpUtils.post().url(APIManager.LAO_HELP)
								.params(params)
								.build()
								.execute(new StringCallback() {
									@Override
									public void onError(okhttp3.Call call, Exception e, int id) {
										Toast.makeText(getApplicationContext(), R.string.alert_error_internet_detail, Toast.LENGTH_SHORT).show();
										finish();
									}

									@Override
									public void onResponse(String response, int id) {
										try {
											JSONObject obj = new JSONObject(response);
											int ret = obj.getInt("ret");
											if (ret == 10000) {
												AppUtils.gUserInfo.initialWithJson(obj.getJSONObject("result"));
											}
										} catch (JSONException e) {
											e.printStackTrace();
										}
									}
								});
					}
						break;
					default:
						break;
				}
			}
		}
	}
}