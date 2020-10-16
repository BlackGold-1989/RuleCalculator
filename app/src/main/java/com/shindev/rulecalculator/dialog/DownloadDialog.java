package com.shindev.rulecalculator.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.shindev.rulecalculator.R;

public class DownloadDialog extends Dialog {

    private DownloadDialogCallback downloadDialogCallback;

    public DownloadDialog(@NonNull Context context) {
        super(context);

        setContentView(R.layout.dialog_download);

        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setTitle(null);
        setCanceledOnTouchOutside(false);

        initUI();
    }

    private void initUI() {
        Button btnDownload = findViewById(R.id.dialog_download_user);
        btnDownload.setOnClickListener(v -> {
            DownloadDialog.this.dismiss();
            downloadDialogCallback.onClickDownloadBtn();
        });

        Button btnCancel = findViewById(R.id.dialog_download_cancel);
        btnCancel.setOnClickListener(view -> {
            dismiss();
            downloadDialogCallback.onClickCancelBtn();
        });

        TextView txt_privacy = findViewById(R.id.txt_dialog_privacy);
        txt_privacy.setOnClickListener(view -> {
            downloadDialogCallback.onClickPrivacyLbl();
        });

        TextView txt_agreement = findViewById(R.id.txt_dialog_agreement);
        txt_agreement.setOnClickListener(view -> {
            downloadDialogCallback.onClickAgreementLbl();
        });
    }

    public void setDownloadDialogCallback(DownloadDialogCallback downloadDialogCallback) {
        this.downloadDialogCallback = downloadDialogCallback;
    }

    public interface DownloadDialogCallback {
        void onClickDownloadBtn();
        void onClickCancelBtn();
        void onClickPrivacyLbl();
        void onClickAgreementLbl();
    }

}
