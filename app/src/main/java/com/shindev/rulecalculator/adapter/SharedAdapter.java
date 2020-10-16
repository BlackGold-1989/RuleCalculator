package com.shindev.rulecalculator.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shindev.rulecalculator.R;
import com.shindev.rulecalculator.activity.ProjectDetailActivity;
import com.shindev.rulecalculator.model.ProjectItem;
import com.shindev.rulecalculator.model.UserInfo;
import com.shindev.rulecalculator.util.APIManager;
import com.shindev.rulecalculator.util.AppUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SharedAdapter  extends BaseAdapter {

    private Context mContext;
    private List<ProjectItem> mDatas;
    private List<UserInfo> mUsers;
    private SharedAdapterCallback callback;

    public SharedAdapter (Context context, List<ProjectItem> datas, List<UserInfo> users, SharedAdapterCallback sharedAdapterCallback) {
        this.mContext = context;
        this.mDatas = datas;
        this.mUsers = users;
        this.callback = sharedAdapterCallback;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint({"InflateParams", "ViewHolder"})
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        view = LayoutInflater.from(mContext).inflate(R.layout.item_share, null);


        ProjectItem item = mDatas.get(position);

        TextView txt_index = view.findViewById(R.id.lbl_item_index);
        txt_index.setText(String.format("%d. ", position + 1));

        TextView txt_name = view.findViewById(R.id.lbl_item_name);
        txt_name.setText(item.name);

        TextView txt_date = view.findViewById(R.id.lbl_item_date);
        txt_date.setText(item.regdate);


        UserInfo user = mUsers.get(position);

        ImageView img_user = view.findViewById(R.id.img_user_avatar);
        String url = user.headurl;
        if (user.openid.equals("")) {
            url = APIManager.UPLOAD_URL + url;
        }
        Picasso.with(mContext).load(url).fit().centerCrop()
                .placeholder(R.drawable.logo)
                .error(R.drawable.logo)
                .into(img_user, null);

        TextView lbl_user = view.findViewById(R.id.lbl_user_name);
        lbl_user.setText(user.nickname);

        view.setOnClickListener(view12 -> {
            callback.onClickItemView(item);
        });

        Button btn_calc = view.findViewById(R.id.btn_item_calc);
        btn_calc.setOnClickListener(view1 -> {
            callback.onClickCalculateBtn(item);
        });

        LinearLayout llt_user = view.findViewById(R.id.llt_item_user);
        llt_user.setOnClickListener(view13 -> callback.onClickUserLlt(user));

        return view;
    }

    public interface SharedAdapterCallback {
        void onClickItemView(ProjectItem item);
        void onClickCalculateBtn(ProjectItem item);
        void onClickUserLlt(UserInfo user);
    }

}
