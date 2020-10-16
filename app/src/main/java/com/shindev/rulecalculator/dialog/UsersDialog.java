package com.shindev.rulecalculator.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.shindev.rulecalculator.R;
import com.shindev.rulecalculator.adapter.UserAdapter;
import com.shindev.rulecalculator.model.UserInfo;

import java.util.ArrayList;
import java.util.List;

public class UsersDialog extends Dialog {

    private TextView lbl_select_all;
    private Button btn_share;
    private ListView lst_user;

    private List<UserInfo> mUsers = new ArrayList<>();
    private List<String> mSelected = new ArrayList<>();

    private UserAdapter userAdapter;

    private UsersDialogCallback dialogCallback;


    private void initEvent() {
        lbl_select_all.setOnClickListener(view -> {
            for (int i = 0; i < mSelected.size(); i++) {
                mSelected.set(i, "1");
            }
            userAdapter.notifyDataSetChanged();
        });

        btn_share.setOnClickListener(view -> {
            String all_select = "";
            for (int i = 0; i < mSelected.size(); i++) {
                String select = mSelected.get(i);
                if (select.equals("1")) {
                    UserInfo user = mUsers.get(i);
                    all_select = all_select + user.id + ",";
                }
            }
            if (all_select.length() == 0) {
                Toast.makeText(getContext(), "请选择一些用户。", Toast.LENGTH_SHORT).show();
                return;
            }
            all_select = all_select.substring(0, all_select.length() - 1);
            dialogCallback.onShareProjectToAnother(all_select);

            dismiss();
        });
    }

    public UsersDialog(@NonNull Context context, UsersDialogCallback callback) {
        super(context);
        dialogCallback = callback;

        setContentView(R.layout.dialog_users);

        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setTitle(null);
        setCanceledOnTouchOutside(true);

        initUIDialog();
    }

    private void initUIDialog() {
        lbl_select_all = findViewById(R.id.lbl_dialog_select);
        btn_share = findViewById(R.id.btn_dialog_share);
        lst_user = findViewById(R.id.lst_dialog_user);

        userAdapter = new UserAdapter(getContext(), mUsers, mSelected);
        lst_user.setAdapter(userAdapter);

        initEvent();
    }

    public void setDialogDatas(List<UserInfo> users, List<String> checks) {
        mUsers.clear();
        mSelected.clear();
        mUsers.addAll(users);
        mSelected.addAll(checks);

        userAdapter.notifyDataSetChanged();
    }

    public interface UsersDialogCallback {
        void onShareProjectToAnother(String userid);
    }

}
