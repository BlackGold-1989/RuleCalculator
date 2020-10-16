package com.shindev.rulecalculator.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;

import com.shindev.rulecalculator.R;
import com.shindev.rulecalculator.dialog.AgreementDialog;
import com.shindev.rulecalculator.dialog.DownloadDialog;
import com.shindev.rulecalculator.dialog.TermDialog;
import com.shindev.rulecalculator.util.AppUtils;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        AppUtils.initUIActivity(this);

        onShowDownloadUsed();
    }

    private void onShowDownloadUsed() {
        DownloadDialog dialog = new DownloadDialog(this);
        dialog.setDownloadDialogCallback(new DownloadDialog.DownloadDialogCallback() {
            @Override
            public void onClickDownloadBtn() {
                new Handler().postDelayed(SplashActivity.this::onNextActivity, 1500);
            }

            @Override
            public void onClickCancelBtn() {
                new Handler().postDelayed(SplashActivity.this::onNextActivity, 1500);
            }

            @Override
            public void onClickPrivacyLbl() {
                onShowTermDialog();
            }

            @Override
            public void onClickAgreementLbl() {
                onShowAgreementDialog();
            }
        });
        dialog.show();
    }

    private void onShowTermDialog() {
        TermDialog dialog = new TermDialog(this);
        dialog.show();
    }

    private void onShowAgreementDialog() {
        AgreementDialog dialog = new AgreementDialog(this);
        dialog.show();
    }

    private void onNextActivity() {
        AppUtils.showOtherActivity(this, LoginActivity.class, -1);
    }

}
