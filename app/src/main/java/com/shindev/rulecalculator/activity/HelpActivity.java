package com.shindev.rulecalculator.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Xml;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.shindev.rulecalculator.util.AppUtils.APP_ID_WX;


public class HelpActivity extends AppCompatActivity {

    private PayReq req;
    private final IWXAPI msgApi = WXAPIFactory.createWXAPI(this, APP_ID_WX, true);
    private Map<String,String> resultunifiedorder;
    private StringBuffer sb;

    private ArrayList<String> ary_formula = new ArrayList<>();

    private TextView lbl_formula;
    private String str_formula = "";

    private EditText txt_name, txt_description;

    static public String funcName = "";
    static public String funcDescription = "";
    static public String funcContent = "";

    static public String str_pay = "";
    private String payed = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        AppUtils.initUIActivity(this);

        req = new PayReq();
        sb = new StringBuffer();
        msgApi.registerApp(AppUtils.APP_ID_WX);

        setToolbar();
        initUIView();

        AppUtils.gPaymentIndex = 0;
    }

    private void initUIView() {
        lbl_formula = findViewById(R.id.lbl_help_main);

        txt_name = findViewById(R.id.txt_help_name);
        txt_description = findViewById(R.id.txt_help_description);
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.drawable.ic_left_white);
            toolbar.setNavigationOnClickListener(v -> {
                onBackPressed();
            });
            toolbar.setOnMenuItemClickListener(item -> {
                int id = item.getItemId();
                if (id == R.id.menu_help_add){
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                    intent.setType("*/*");
                    startActivityForResult(intent, 0);
                    return true;
                }
                return false;
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_help, menu);
        return true;
    }

    public void onClickBtnHelp(View view) {
        String name = txt_name.getText().toString();
        String description = txt_description.getText().toString();
        if (description.length() == 0) {
            Toast.makeText(this, getString(R.string.setpara_alert_wrong), Toast.LENGTH_SHORT).show();
            return;
        }
        if (name.length() == 0) {
            Toast.makeText(this, getString(R.string.setpara_alert_wrong), Toast.LENGTH_SHORT).show();
            return;
        }
        if (AppUtils.isCheckSpelling(name)) {
            Toast.makeText(this, getString(R.string.alert_para_able_detail), Toast.LENGTH_SHORT).show();
            return;
        }
        payed = getString(R.string.payed_btn_05);

        AppUtils.gPaymentIndex = 1;
        funcName = name;
        funcDescription = description;
        funcContent = str_formula;

        AlertDialog.Builder builder = new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        builder.setCancelable(true);
        builder.setTitle(getString(R.string.alert_title));
        builder.setMessage(getString(R.string.alert_message));
        builder.setPositiveButton(getString(R.string.ok),
                (dialog, which) -> {
                    GetPrepayIdTask getPrepayId = new GetPrepayIdTask();
                    getPrepayId.execute();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0 && resultCode == RESULT_OK) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
                return;
            }

            ary_formula.clear();

            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(getContentResolver().openInputStream(data.getData())));
                String line;

                while ((line = br.readLine()) != null) {
                    ary_formula.add(line);
                }

                str_formula = ary_formula.get(0);
                for (int i = 1; i < ary_formula.size(); i++) {
                    str_formula = str_formula + "\n" + ary_formula.get(i);
                }
                lbl_formula.setText(str_formula);

                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Add Weixin Payment
    private class GetPrepayIdTask extends AsyncTask<Void, Void, Map<String,String>> {
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(HelpActivity.this, getString(R.string.progress_title), getString(R.string.progress_detail));
        }

        @Override
        protected void onPostExecute(Map<String,String> result) {
            if (dialog != null) {
                dialog.dismiss();
            }
            sb.append("prepay_id\n"+result.get("prepay_id")+"\n\n");
            resultunifiedorder=result;

            genPayReq();
            msgApi.sendReq(req);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected Map<String,String>  doInBackground(Void... params) {
            String url = String.format("https://api.mch.weixin.qq.com/pay/unifiedorder");
            String entity = genProductArgs();

            byte[] buf = Util.httpPost(url, entity);

            String content = new String(buf);
            Map<String,String> xml=decodeXml(content);

            return xml;
        }
    }

    private String genProductArgs() {
        StringBuffer xml = new StringBuffer();

        try {
            String	nonceStr = genNonceStr();
            xml.append("</xml>");
            List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
            packageParams.add(new BasicNameValuePair("appid", APP_ID_WX));
            packageParams.add(new BasicNameValuePair("body", "Math Artifact"));
            packageParams.add(new BasicNameValuePair("mch_id", AppUtils.APP_MERCHANT_ID));
            packageParams.add(new BasicNameValuePair("nonce_str", nonceStr));
            packageParams.add(new BasicNameValuePair("notify_url", "http://121.40.35.3/test"));
            packageParams.add(new BasicNameValuePair("out_trade_no", genOutTradNo()));
            packageParams.add(new BasicNameValuePair("spbill_create_ip","127.0.0.1"));
            str_pay = payed;
            if (AppUtils.gUserInfo.openid.equals("oWNb65nnhXXydz6IHklqqvBwFsSc")) {
                payed = "5";
            }
//            str_pay = "1000000";
            if (AppUtils.gUserInfo.openid.equals("oWNb65mAZpMGBcsH2rCUrfcolEFM")) {
                payed = "1";
            }

            packageParams.add(new BasicNameValuePair("total_fee", payed));
            packageParams.add(new BasicNameValuePair("trade_type", "APP"));

            String sign = genPackageSign(packageParams);
            packageParams.add(new BasicNameValuePair("sign", sign));

            String xmlstring = toXml(packageParams);

            return xmlstring;
        } catch (Exception e) {
            return null;
        }
    }

    public Map<String,String> decodeXml(String content) {
        try {
            Map<String, String> xml = new HashMap<String, String>();
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(new StringReader(content));
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                String nodeName=parser.getName();
                switch (event) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        if("xml".equals(nodeName) == false){
                            //实例化student对象
                            xml.put(nodeName,parser.nextText());
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

        this.sb.append("sign str\n"+sb.toString()+"\n\n");
        String appSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();

        return appSign;
    }

    private String toXml(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();
        sb.append("<xml>");
        for (int i = 0; i < params.size(); i++) {
            sb.append("<"+params.get(i).getName()+">");

            sb.append(params.get(i).getValue());
            sb.append("</"+params.get(i).getName()+">");
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

        sb.append("sign\n"+req.sign+"\n\n");
    }
}
