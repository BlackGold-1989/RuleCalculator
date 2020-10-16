package com.shindev.rulecalculator.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.shindev.rulecalculator.R;
import com.shindev.rulecalculator.paybase.MD5;
import com.shindev.rulecalculator.paybase.Util;
import com.shindev.rulecalculator.util.AppUtils;

import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParser;

import java.io.StringReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.shindev.rulecalculator.util.AppUtils.APP_ID_WX;


public class WechatPayActivity extends AppCompatActivity {

    private PayReq req;
    final IWXAPI msgApi = WXAPIFactory.createWXAPI(this, APP_ID_WX, true);
    private Map<String, String> resultunifiedorder;
    private StringBuffer sb;

    private String payed = "";
    static public String str_pay = "";
    static public String str_function = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wechat);

        setToolbar();
        AppUtils.initUIActivity(this);

        req = new PayReq();
        sb = new StringBuffer();
        msgApi.registerApp(APP_ID_WX);

        AppUtils.gPaymentIndex = 0;
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.drawable.ic_left_white);
            toolbar.setNavigationOnClickListener(v -> {
                onBackPressed();
            });
        }
    }

    public void onClickBtnPayment01(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        builder.setCancelable(true);
        builder.setTitle(getString(R.string.alert_title));
        builder.setMessage(getString(R.string.alert_message));
        builder.setPositiveButton(getString(R.string.ok),
                (dialog, which) -> {
                    GetPrepayIdTask getPrepayId = new GetPrepayIdTask();
                    getPrepayId.execute();
                    payed = getString(R.string.payed_btn_01);
                    str_function = "1";
                });
        builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> {
        });

        AlertDialog dialog = builder.create();
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

    public void onClickBtnPayment02(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        builder.setCancelable(true);
        builder.setTitle(getString(R.string.alert_title));
        builder.setMessage(getString(R.string.alert_message));
        builder.setPositiveButton(getString(R.string.ok),
                (dialog, which) -> {
                    GetPrepayIdTask getPrepayId = new GetPrepayIdTask();
                    getPrepayId.execute();
                    payed = getString(R.string.payed_btn_02);
                    str_function = "60";
                });
        builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> {
        });

        AlertDialog dialog = builder.create();
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

    public void onClickBtnPayment03(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        builder.setCancelable(true);
        builder.setTitle(getString(R.string.alert_title));
        builder.setMessage(getString(R.string.alert_message));
        builder.setPositiveButton(getString(R.string.ok),
                (dialog, which) -> {
                    GetPrepayIdTask getPrepayId = new GetPrepayIdTask();
                    getPrepayId.execute();
                    payed = getString(R.string.payed_btn_03);
                    str_function = "200";
                });
        builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> {
        });

        AlertDialog dialog = builder.create();
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

    public void onClickBtnPayment04(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        builder.setCancelable(true);
        builder.setTitle(getString(R.string.alert_title));
        builder.setMessage(getString(R.string.alert_message));
        builder.setPositiveButton(getString(R.string.ok),
                (dialog, which) -> {
                    GetPrepayIdTask getPrepayId = new GetPrepayIdTask();
                    getPrepayId.execute();
                    payed = getString(R.string.payed_btn_04);
                    str_function = "500";
                });
        builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> {
        });

        AlertDialog dialog = builder.create();
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

    // Add Weixin Payment
    @SuppressLint("StaticFieldLeak")
    private class GetPrepayIdTask extends AsyncTask<Void, Void, Map<String, String>> {
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(WechatPayActivity.this, getString(R.string.progress_title), getString(R.string.progress_detail));
        }

        @Override
        protected void onPostExecute(Map<String, String> result) {
            if (dialog != null) {
                dialog.dismiss();
            }
            sb.append("prepay_id\n" + result.get("prepay_id") + "\n\n");
            resultunifiedorder = result;

            genPayReq();
            msgApi.registerApp(APP_ID_WX);
            msgApi.sendReq(req);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected Map<String, String> doInBackground(Void... params) {
            String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
            String entity = genProductArgs();

            byte[] buf = Util.httpPost(url, entity);

            String content = new String(buf);

            return decodeXml(content);
        }
    }

    private String genProductArgs() {
        StringBuffer xml = new StringBuffer();

        try {
            String nonceStr = genNonceStr();
            xml.append("</xml>");
            List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
            packageParams.add(new BasicNameValuePair("appid", APP_ID_WX));
            packageParams.add(new BasicNameValuePair("body", "Math Artifact"));
            packageParams.add(new BasicNameValuePair("mch_id", AppUtils.APP_MERCHANT_ID));
            packageParams.add(new BasicNameValuePair("nonce_str", nonceStr));
            packageParams.add(new BasicNameValuePair("notify_url", "http://121.40.35.3/test"));
            packageParams.add(new BasicNameValuePair("out_trade_no", genOutTradNo()));
            packageParams.add(new BasicNameValuePair("spbill_create_ip", "127.0.0.1"));
            str_pay = payed;
            if (AppUtils.gUserInfo.openid.equals("oWNb65nnhXXydz6IHklqqvBwFsSc")) {
                payed = "5";
            }

            if (AppUtils.gUserInfo.openid.equals("oWNb65mAZpMGBcsH2rCUrfcolEFM")) {
                payed = "1";
            }

            packageParams.add(new BasicNameValuePair("total_fee", payed));
            packageParams.add(new BasicNameValuePair("trade_type", "APP"));

            String sign = genPackageSign(packageParams);
            packageParams.add(new BasicNameValuePair("sign", sign));

            return toXml(packageParams);
        } catch (Exception e) {
            return null;
        }
    }

    public Map<String, String> decodeXml(String content) {
        try {
            Map<String, String> xml = new HashMap<String, String>();
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(new StringReader(content));
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                String nodeName = parser.getName();
                switch (event) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        if ("xml".equals(nodeName) == false) {
                            //实例化student对象
                            xml.put(nodeName, parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                event = parser.next();
            }

            return xml;
        } catch (Exception e) {
        }

        return null;
    }

    private String genNonceStr() {
        Random random = new Random();
        return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
    }

    private long genTimeStamp() {
        return System.currentTimeMillis() / 1000;
    }

    private String genOutTradNo() {
        Random random = new Random();
        return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
    }

    private String genPackageSign(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(AppUtils.APP_KEY);

        String packageSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();

        return packageSign;
    }

    private String genAppSign(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(AppUtils.APP_KEY);

        this.sb.append("sign str\n" + sb.toString() + "\n\n");
        String appSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();

        return appSign;
    }

    private String toXml(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();
        sb.append("<xml>");
        for (int i = 0; i < params.size(); i++) {
            sb.append("<" + params.get(i).getName() + ">");

            sb.append(params.get(i).getValue());
            sb.append("</" + params.get(i).getName() + ">");
        }
        sb.append("</xml>");

        return sb.toString();
    }

    private void genPayReq() {
        req.appId = APP_ID_WX;
        req.partnerId = AppUtils.APP_MERCHANT_ID;
        req.prepayId = resultunifiedorder.get("prepay_id");
        req.packageValue = "Sign=WXPay";
        req.nonceStr = genNonceStr();
        req.timeStamp = String.valueOf(genTimeStamp());


        List<NameValuePair> signParams = new LinkedList<NameValuePair>();
        signParams.add(new BasicNameValuePair("appid", req.appId));
        signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
        signParams.add(new BasicNameValuePair("package", req.packageValue));
        signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
        signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
        signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));

        req.sign = genAppSign(signParams);

        sb.append("sign\n" + req.sign + "\n\n");
    }
}
