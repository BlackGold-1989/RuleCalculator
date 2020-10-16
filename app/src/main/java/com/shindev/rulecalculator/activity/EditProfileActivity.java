package com.shindev.rulecalculator.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.shindev.rulecalculator.R;
import com.shindev.rulecalculator.util.APIManager;
import com.shindev.rulecalculator.util.AppUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {

    private TextView txt_id, txt_name, txt_opass, txt_npass, txt_cpass;
    private ImageView img_avatar;

    private boolean isChangeAvatar = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        setToolbar();
        AppUtils.initUIActivity(this);

        initUIView();
    }

    private void initUIView() {
        txt_id = findViewById(R.id.txt_edit_id);
        if (AppUtils.gUserInfo.openid.equals("")) {
            txt_id.setText(AppUtils.gUserInfo.email);
        } else {
            txt_id.setText(AppUtils.gUserInfo.openid);
        }
        txt_name = findViewById(R.id.txt_edit_nickname);
        txt_name.setText(AppUtils.gUserInfo.nickname);
        txt_opass = findViewById(R.id.txt_edit_cpass);
        txt_npass = findViewById(R.id.txt_edit_npass);
        txt_cpass = findViewById(R.id.txt_edit_repass);

        img_avatar = findViewById(R.id.img_edit_avatar);
        String url = AppUtils.gUserInfo.headurl;
        if (AppUtils.gUserInfo.openid.equals("")) {
            url = APIManager.UPLOAD_URL + url;
        }
        Picasso.with(this).load(url).fit().centerCrop()
                .placeholder(R.drawable.logo)
                .error(R.drawable.logo)
                .into(img_avatar, null);
    }

    private void setToolbar() {
        setTitle("编辑个人资料");

        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.drawable.ic_left_white);
            toolbar.setNavigationOnClickListener(v -> {
                onBackPressed();
            });
        }
    }

    public void onClickBtnUpdateProfile(View view) {
        String img_str = "";
        if (isChangeAvatar) {
            BitmapDrawable drawable = (BitmapDrawable) img_avatar.getDrawable();
            Bitmap bitmap = drawable.getBitmap();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG,10, bos);
            byte[] bb = bos.toByteArray();
            img_str = Base64.encodeToString(bb, 0);
        }

        String nickname = txt_name.getText().toString();
        if (nickname.length() == 0 || nickname.equals(AppUtils.gUserInfo.nickname)) {
            nickname = "default";
        }

        String opass = txt_opass.getText().toString();
        String npass = txt_npass.getText().toString();
        String cpass = txt_cpass.getText().toString();
        if (opass.length() == 0) {
            opass = "default";
        } else {
            Toast.makeText(this, "密码错误。", Toast.LENGTH_SHORT).show();
        }

        Map<String, String> params = new HashMap<>();
        params.put("id", AppUtils.gUserInfo.id);
        params.put("nickname", nickname);
        params.put("password", npass);
        params.put("orgpassword", opass);
        params.put("base64", img_str);

        APIManager.onAPIConnectionResponse(APIManager.LAO_UPDATE_USER, params, APIManager.APIMethod.POST, new APIManager.APIManagerCallback() {
            @Override
            public void onEventCallBack(JSONObject obj, int ret) {
                switch (ret) {
                    case 10000:
                        try {
                            AppUtils.gUserInfo.initialWithJson(obj.getJSONObject("result"));
                            onBackPressed();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 10001:
                        Toast.makeText(EditProfileActivity.this, "当前密码错误。", Toast.LENGTH_SHORT).show();
                        break;
                    case 10002:
                        Toast.makeText(EditProfileActivity.this, "服务器错误。", Toast.LENGTH_SHORT).show();
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

    public void onClickLltEditAvatar(View view) {
        ImagePicker.create(this) // Activity or Fragment
                .start();
    }

    @Override
    protected void onActivityResult(int requestCode, final int resultCode, Intent data) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            // Get a list of picked images
            List<Image> images = ImagePicker.getImages(data);
            // or get a single image only
            Image image = ImagePicker.getFirstImageOrNull(data);
            img_avatar.setImageURI(Uri.parse(image.getPath()));

            isChangeAvatar = true;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
