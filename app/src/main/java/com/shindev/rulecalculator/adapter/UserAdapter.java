package com.shindev.rulecalculator.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shindev.rulecalculator.R;
import com.shindev.rulecalculator.model.UserInfo;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UserAdapter extends BaseAdapter {

    private Context mContext;
    private List<UserInfo> mUsers;
    private List<String> mChecks;


    public UserAdapter(Context context, List<UserInfo> users, List<String> checks) {
        mContext = context;
        mUsers = users;
        mChecks = checks;
    }

    @Override
    public int getCount() {
        return mUsers.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(mContext).inflate(R.layout.item_user, null);

        TextView lbl_index = view.findViewById(R.id.lbl_item_index);
        lbl_index.setText((i + 1) + ". ");

        UserInfo user = mUsers.get(i);
        String isChecked = mChecks.get(i);

        ImageView img_avatar = view.findViewById(R.id.img_item_avatar);
        Picasso.with(mContext).load(user.headurl).fit().centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(img_avatar, null);
        TextView lbl_name = view.findViewById(R.id.lbl_item_name);
        lbl_name.setText(user.nickname);
        TextView lbl_id = view.findViewById(R.id.lbl_item_id);
        if (user.openid.length() == 0) {
            lbl_id.setText(user.email);
        } else {
            lbl_id.setText(user.openid);
        }

        ImageView img_check = view.findViewById(R.id.img_item_check);
        if (isChecked.equals("1")) {
            img_check.setImageResource(R.drawable.ic_check_circle_green);
        } else {
            img_check.setImageResource(R.drawable.ic_circle_green);
        }

        LinearLayout llt_spec = view.findViewById(R.id.llt_item_spec);
        if (i == mUsers.size() - 1) {
            llt_spec.setVisibility(View.GONE);
        }

        view.setOnClickListener(view1 -> {
            String isChecked1 = mChecks.get(i);
            if (isChecked1.equals("1")) {
                isChecked1 = "0";
            } else {
                isChecked1 = "1";
            }
            mChecks.set(i, isChecked1);
            notifyDataSetChanged();
        });

        return view;
    }
}
