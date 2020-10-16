package com.shindev.rulecalculator.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.c.progress_dialog.BlackProgressDialog;
import com.dkv.bubblealertlib.ConstantsIcons;
import com.dkv.bubblealertlib.IAlertClickedCallBack;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.shindev.rulecalculator.R;
import com.shindev.rulecalculator.dialog.AgreementDialog;
import com.shindev.rulecalculator.dialog.DownloadDialog;
import com.shindev.rulecalculator.dialog.TermDialog;
import com.shindev.rulecalculator.util.APIManager;
import com.shindev.rulecalculator.util.AppUtils;
import com.shindev.rulecalculator.util.SharePreferenceUtil;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.shindev.rulecalculator.util.AppUtils.APP_ID_WX;


public class LoginActivity extends AppCompatActivity {

    private IWXAPI api;
    static public LoginActivity activity;

    private TextInputEditText txt_id, txt_pass;
    private TextInputLayout tll_id, tll_pass;
    private CheckBox chb_term;

    public void onClickLblForgot(View view) {
        AppUtils.showOtherActivity(this, ForgotPassActivity.class, 0);
    }

    public void onClickBtnLogin(View view) {
        String idStr = txt_id.getText().toString();
        if (idStr.length() == 0) {
            tll_id.setHelperText("用户标识为空。 请输入一些字符。");
            return;
        }
        String passStr = txt_pass.getText().toString();
        if (passStr.length() < 6) {
            tll_pass.setHelperText("密码短。 请输入至少6个字符。");
            return;
        }

        final String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        Map<String, String> params = new HashMap<>();
        params.put("email", idStr);
        params.put("password", passStr);
        params.put("deviceid", deviceId);


        BlackProgressDialog dialog = new BlackProgressDialog(this, getString(R.string.common_connect));
        dialog.show();

        APIManager.onAPIConnectionResponse(APIManager.LAO_NORMAL_LOGIN, params, APIManager.APIMethod.POST, new APIManager.APIManagerCallback() {
            @Override
            public void onEventCallBack(JSONObject obj, int ret) {
                if (ret == 10000) {
                    try {
                        JSONObject result = obj.getJSONObject("result");

                        if (chb_term.isChecked()) {
                            SharePreferenceUtil.putString(LoginActivity.this, "ID", idStr);
                            SharePreferenceUtil.putString(LoginActivity.this, "PASSWORD", passStr);
                        }

                        AppUtils.gUserInfo.initialWithJson(result);
                        AppUtils.showOtherActivity(LoginActivity.this, MainActivity.class, -1);
                    } catch (JSONException e) {
                        Toast.makeText(LoginActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                } else if (ret == 10001) {
                    Toast.makeText(LoginActivity.this, "您的登录信息不正确。", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }

            @Override
            public void onEventInternetError(Exception e) {
                dialog.dismiss();
                Toast.makeText(LoginActivity.this, R.string.alert_error_internet_detail, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onEventServerError(Exception e) {
                dialog.dismiss();
                Toast.makeText(LoginActivity.this, R.string.alert_server_error_detail, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onClickWechatLogin(View view) {
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "none";
        api.sendReq(req);
    }

    public void onClickLblDownloadUsed(View view) {
        DownloadDialog dialog = new DownloadDialog(this);
        dialog.setDownloadDialogCallback(new DownloadDialog.DownloadDialogCallback() {
            @Override
            public void onClickDownloadBtn() {
                //
            }

            @Override
            public void onClickCancelBtn() {
                //
            }

            @Override
            public void onClickPrivacyLbl() {
                LoginActivity.this.onClickLblContact(null);
            }

            @Override
            public void onClickAgreementLbl() {
                LoginActivity.this.onClickLblUserAgreement(null);
            }
        });
        dialog.show();
    }

    public void onClickLblContact(View view) {
        TermDialog dialog = new TermDialog(this);
        dialog.show();
    }

    public void onClickLblUserAgreement(View view) {
        AgreementDialog dialog = new AgreementDialog(this);
        dialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        AppUtils.initUIActivity(this);

        initUI();

        activity = this;
        api = WXAPIFactory.createWXAPI(this, APP_ID_WX, false);
    }

    private void initUI() {
        chb_term = findViewById(R.id.chk_login_term);

        tll_id = findViewById(R.id.tll_login_userid);
        txt_id = findViewById(R.id.txt_login_id);
        txt_id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //
            }

            @Override
            public void afterTextChanged(Editable s) {
                tll_id.setHelperText("");
            }
        });
        tll_pass = findViewById(R.id.tll_login_pass);
        txt_pass = findViewById(R.id.txt_login_pass);
        txt_pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //
            }

            @Override
            public void afterTextChanged(Editable s) {
                tll_pass.setHelperText("");
            }
        });

        String idStr = SharePreferenceUtil.getString(this, "ID");
        String password = SharePreferenceUtil.getString(this, "PASSWORD");
        if ((idStr != null) && (password != null)) {
            txt_id.setText(idStr);
            txt_pass.setText(password);
            chb_term.setChecked(true);
        }
    }

    public void onEventWechatLogin(Map<String, String> params) {
        BlackProgressDialog dialog = new BlackProgressDialog(this, getString(R.string.common_connect));
        dialog.show();

        APIManager.onAPIConnectionResponse(APIManager.LAO_LOGIN, params, APIManager.APIMethod.POST, new APIManager.APIManagerCallback() {
            @Override
            public void onEventCallBack(JSONObject obj, int ret) {
                if (ret == 10000) {
                    try {
                        JSONObject result = obj.getJSONObject("result");
                        AppUtils.gUserInfo.initialWithJson(result);

                        AppUtils.showOtherActivity(LoginActivity.this, MainActivity.class, -1);
                    } catch (JSONException e) {
                        Toast.makeText(LoginActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
                dialog.dismiss();
            }

            @Override
            public void onEventInternetError(Exception e) {
                dialog.dismiss();
            }

            @Override
            public void onEventServerError(Exception e) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onBackPressed() {
        AppUtils.onShowCustomAlert(this,"退出", ConstantsIcons.ALERT_ICON_ERROR, "您确定要立即退出此应用程序吗？", new IAlertClickedCallBack() {
            @Override
            public void onOkClicked(String tag) {
                moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }

            @Override
            public void onCancelClicked(String tag) {
                //
            }

            @Override
            public void onExitClicked(String tag) {
                //
            }
        });
    }


}
