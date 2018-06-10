package com.onlinetest.vuquang.filemanager.main;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.onlinetest.vuquang.filemanager.R;
import com.onlinetest.vuquang.filemanager.base.BaseActivity;
import com.onlinetest.vuquang.filemanager.data.manager.AppDataManager;
import com.onlinetest.vuquang.filemanager.data.model.file.CustomFile;
import com.onlinetest.vuquang.filemanager.main.adapter.FileAdapter;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

public class MainActivity extends BaseActivity implements MainMvpView{
    private static final String TAG = "MainActivity";

    public static final int LIST_MODE = 0;
    public static final int GRID_MODE = 1;


    FileAdapter mAdapter;
    RecyclerView rvFileList;
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
                                mPresenter.loadQuickAccess();
                                break;
                            case R.id.nav_storage:
                                mPresenter.loadExternalStorage();
                                break;
                            case R.id.nav_recycle_bin:
                                mPresenter.loadRecycleBin();
                                break;
                        }
                        updateMenuItem(menuItem);
                        return true;
                    }
                });

        imbGridMode = findViewById(R.id.imb_navi_grid_mode);
        imbGridMode.setVisibility(View.VISIBLE);
        imbGridMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchMode(LIST_MODE);
            }
        });

        imbListMode = findViewById(R.id.imb_navi_list_mode);
        imbListMode.setVisibility(View.GONE);
        imbListMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchMode(GRID_MODE);
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

        rvFileList = findViewById(R.id.rv_file_list);
        rvFileList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new FileAdapter(this);
        rvFileList.setAdapter(mAdapter);

        mAdapter.setFileItemListener(new FileAdapter.FileItemListener() {
            @Override
            public void onOpenClicked(CustomFile file) {
                mPresenter.openFile(file);
            }

            @Override
            public void onDeleteClicked(CustomFile file) {
                mPresenter.deleteFile(file);
            }

            @Override
            public void onPermanentlyDeleteClicked(CustomFile file) {
                mPresenter.permanentlyDeleteFile(file);
            }

            @Override
            public void onMoveClicked(CustomFile file) {
                showChoseDesFolderDialog();
            }

            @Override
            public void onCopyClicked(CustomFile file) {
                showChoseDesFolderDialog();
            }

            @Override
            public void onPropertiesClicked(CustomFile file) {
                mPresenter.showProperties(file);
            }
        });

        mPresenter.loadQuickAccess();
        navigationView.setCheckedItem(R.id.nav_quick_access);
    }

    private void showChoseDesFolderDialog() {

    }

    private void updateMenuItem(MenuItem menuItem) {
        txtTitle.setText(menuItem.getTitle());
        menuItem.setChecked(true);
        mDrawer.closeDrawers();
        mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, navigationView);
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
        try {
            Field[] fields = pm.getClass().getDeclaredFields();
            for (Field field : fields) {
                if ("mPopup".equals(field.getName())) {
                    field.setAccessible(true);
                    Object menuPopupHelper = field.get(pm);
                    Class<?> classPopupHelper = Class.forName(menuPopupHelper.getClass().getName());
                    Method setForceIcons = classPopupHelper.getMethod("setForceShowIcon", boolean.class);
                    setForceIcons.invoke(menuPopupHelper, true);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        pm.getMenuInflater().inflate(R.menu.menu_action_bar_popup, pm.getMenu());
        pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_sort_by: {
                        showSortOptionDialog();
                        return true;
                    }
                    case R.id.action_undo: {
                        mPresenter.onUndoClicked();
                        return true;
                    }
                    case R.id.action_redo: {
                        mPresenter.onRedoClicked();
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

        mAdapter.setData(fileList);
    }

    @Override
    public void openFile(CustomFile file) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file.getFile()), file.getExtension());
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            showMessage("This file is not supported");
        }
    }

    @Override
    public void updateEmptyListUI() {

    }
}
