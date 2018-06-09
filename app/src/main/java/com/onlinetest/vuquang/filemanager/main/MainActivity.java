package com.onlinetest.vuquang.filemanager.main;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.onlinetest.vuquang.filemanager.R;
import com.onlinetest.vuquang.filemanager.base.BaseActivity;
import com.onlinetest.vuquang.filemanager.data.manager.AppDataManager;
import com.onlinetest.vuquang.filemanager.data.model.file.CustomFile;

import java.util.List;

public class MainActivity extends BaseActivity implements MainMvpView{
    private static final String TAG = "MainActivity";

    public static final int LIST_MODE = 0;
    public static final int GRID_MODE = 1;


    private DrawerLayout mDrawer;
    private NavigationView navigationView;
    private ImageButton imbMenu, imbGridMode, imbListMode, imbMore;
    private TextView txtTitle;

    private MainMvpPresenter<MainMvpView> mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mPresenter = new MainPresenter<>(AppDataManager.getDataManager(this));
        mPresenter.onAttach(MainActivity.this);

        txtTitle = findViewById(R.id.tv_title);
        mDrawer = findViewById(R.id.drawer_layout);

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        switch(menuItem.getItemId()) {
                            case R.id.nav_quick_access:
                                break;
                            case R.id.nav_storage:
                                break;
                            case R.id.nav_recycle_bin:
                                break;
                        }
                        return true;
                    }
                });

        imbGridMode = findViewById(R.id.imb_navi_grid_mode);
        imbGridMode.setVisibility(View.VISIBLE);
        imbGridMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchMode(GRID_MODE);
            }
        });

        imbListMode = findViewById(R.id.imb_navi_list_mode);
        imbListMode.setVisibility(View.GONE);
        imbListMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchMode(LIST_MODE);
            }
        });

        imbMenu = findViewById(R.id.imb_navi_menu);
        imbMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDrawer();
            }
        });

        imbMore = findViewById(R.id.imb_navi_more);
        imbMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(view);
            }
        });
//        mPresenter.loadExternalStorage();
    }

    public void switchMode(int mode) {
        switch (mode) {
            case LIST_MODE: {
                imbGridMode.setVisibility(View.GONE);
                imbListMode.setVisibility(View.VISIBLE);
                break;
            }
            case GRID_MODE: {
                imbGridMode.setVisibility(View.VISIBLE);
                imbListMode.setVisibility(View.GONE);
                break;
            }
        }
    }

    private void showPopupMenu(View v) {
        PopupMenu pm = new PopupMenu(this, v);
        pm.getMenuInflater().inflate(R.menu.menu_more_func_popup, pm.getMenu());
        pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_sort_by: {
                        showSortOptionDialog();
                        return true;
                    }
                    default:{
                        return false;
                    }
                }

            }
        });
        pm.show();
    }

    private void showSortOptionDialog() {

    }

    public void openDrawer() {
        mDrawer.openDrawer(navigationView);
    }

    @Override
    public void updateUI(List<CustomFile> fileList) {

    }
}
