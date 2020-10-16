package com.shindev.rulecalculator.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.c.progress_dialog.BlackProgressDialog;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.shindev.rulecalculator.R;
import com.shindev.rulecalculator.fragment.FuncListFragment;
import com.shindev.rulecalculator.fragment.HistoryFragment;
import com.shindev.rulecalculator.fragment.PostFragment;
import com.shindev.rulecalculator.fragment.ProfileFragment;
import com.shindev.rulecalculator.fragment.ProjectFragment;
import com.shindev.rulecalculator.fragment.ShareFragment;
import com.shindev.rulecalculator.util.AppUtils;

public class MainActivity extends AppCompatActivity {

    public Toolbar toolbar;
    private MenuItem item_toolbar;
    private MainActivityCallback activityCallback;

    public BlackProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppUtils.initUIActivity(this);
        setToolbar();

        dialog = new BlackProgressDialog(this, getString(R.string.common_connect));
    }

    private void setToolbar() {
        toolbar = findViewById(R.id.toolbar);

        if (toolbar != null) {
            setSupportActionBar(toolbar);

            toolbar.setOnMenuItemClickListener(item -> {
                int id = item.getItemId();
                if (id == R.id.action_top){
                    activityCallback.onClickToolbarItem();
                    return true;
                }
                if (id == R.id.action_upload) {
                    activityCallback.onClickShareProject();
                    return true;
                }
                return false;
            });
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
            Fragment fragment = null;
            switch (menuItem.getItemId()) {
                case R.id.action_project:
                    fragment = new ProjectFragment(MainActivity.this);
                    break;
                case R.id.action_function:
                    fragment = new FuncListFragment(MainActivity.this);
                    break;
                case R.id.action_share:
                    fragment = new ShareFragment(MainActivity.this);
                    break;
//                case R.id.action_history:
//                    fragment = new HistoryFragment(MainActivity.this);
//                    break;
                case R.id.action_post:
                    fragment = new PostFragment(MainActivity.this);
                    break;
                case R.id.action_profile:
                    fragment = new ProfileFragment(MainActivity.this);
                    break;
            }
            onShowFragment(fragment);
            return true;
        });

        if (AppUtils.gUserInfo.classname.equals("")) {
            bottomNavigationView.setSelectedItemId(R.id.action_profile);
        } else {
            bottomNavigationView.setSelectedItemId(R.id.action_project);
        }

    }

    private void onShowFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.frg_main_body, fragment);
        fragmentTransaction.commit();
    }

    public void onShowShareBtn() {
        item_toolbar.setVisible(true);
        item_toolbar.setIcon(R.drawable.ic_share_white);
    }

    public void onShowAddBtn() {
        item_toolbar.setVisible(true);
        item_toolbar.setIcon(R.drawable.ic_add_list_white);
    }

    public void onShowCheckBtn() {
        item_toolbar.setVisible(true);
        item_toolbar.setIcon(R.drawable.ic_selecter_white);
    }

    public void onHideMenuBtn() {
        if (item_toolbar != null) {
            item_toolbar.setVisible(false);
        }
    }

    public void onShowMenuBtn() {
        if (item_toolbar != null) {
            item_toolbar.setVisible(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_top, menu);
        item_toolbar = menu.findItem(R.id.action_top);

        return true;
    }

    public void setActivityCallback(MainActivityCallback activityCallback) {
        this.activityCallback = activityCallback;
    }

    public interface MainActivityCallback {
        void onClickToolbarItem();
        void onClickShareProject();
    }

}
