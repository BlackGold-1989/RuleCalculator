package com.shindev.rulecalculator.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.shindev.rulecalculator.R;

public class TermDialog extends Dialog {

    public TermDialog(@NonNull Context context) {
        super(context);

        setContentView(R.layout.dialog_term);

        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setTitle(null);
        setCanceledOnTouchOutside(false);

        initUI();
    }

    private void initUI() {
        Button btnAccept = findViewById(R.id.dialog_term_accept);
        btnAccept.setOnClickListener(view -> dismiss());

        Button btnCancel = findViewById(R.id.dialog_term_cancel);
        btnCancel.setOnClickListener(view -> dismiss());
    }

}
