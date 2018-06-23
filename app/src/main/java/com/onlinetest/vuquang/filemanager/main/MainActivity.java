package com.onlinetest.vuquang.filemanager.main;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.onlinetest.vuquang.filemanager.R;
import com.onlinetest.vuquang.filemanager.app.FileManagerApp;
import com.onlinetest.vuquang.filemanager.base.BaseActivity;
import com.onlinetest.vuquang.filemanager.data.manager.AppDataManager;
import com.onlinetest.vuquang.filemanager.data.model.file.AbstractFile;
import com.onlinetest.vuquang.filemanager.dialog.FolderPickerDialog;
import com.onlinetest.vuquang.filemanager.main.adapter.FileAdapter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements MainMvpView{
    private static final String TAG = "MainActivity";

    private FileAdapter mAdapter;
    private RecyclerView rvFileList;
    private DrawerLayout mDrawer;
    private NavigationView navigationView;
    private ImageButton imbMenu, imbMore, imbBack;
    private TextView txtTitle,txtPath;
    private View layoutPath;
    private PopupMenu pm;
    private ActionMode mActionMode;

    private MainMvpPresenter<MainMvpView> mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPresenter = new MainPresenter<>(AppDataManager.getDataManager(this));
        mPresenter.onAttach(MainActivity.this);

        txtTitle = findViewById(R.id.tv_title);
        mDrawer = findViewById(R.id.drawer_layout);

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

        imbBack = findViewById(R.id.imb_navi_back);
        imbBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.onBackClicked();
            }
        });

        layoutPath = findViewById(R.id.layout_path);
        txtPath = findViewById(R.id.tv_path);

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        switch(menuItem.getItemId()) {
                            case R.id.nav_quick_access:
                                mPresenter.loadQuickAccess();
                                imbMore.setVisibility(View.GONE);
                                layoutPath.setVisibility(View.GONE);
                                recreatePopupLayout(menuItem);
                                break;
                            case R.id.nav_storage:
                                mPresenter.loadExternalStorage();
                                imbMore.setVisibility(View.VISIBLE);
                                layoutPath.setVisibility(View.VISIBLE);
                                recreatePopupLayout(menuItem);
                                break;
                            case R.id.nav_recycle_bin:
                                mPresenter.loadRecycleBin();
                                imbMore.setVisibility(View.GONE);
                                layoutPath.setVisibility(View.GONE);
                                break;
                        }
                        updateMenuItem(menuItem);
                        return true;
                    }
                });

        rvFileList = findViewById(R.id.rv_file_list);
        rvFileList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new FileAdapter(this);
        rvFileList.setAdapter(mAdapter);

        mAdapter.setFileItemListener(new FileAdapter.FileItemListener() {
            @Override
            public void onOpenClicked(AbstractFile file) {
                mPresenter.openFile(file);
            }

            @Override
            public void onDeleteClicked(AbstractFile file) {
                mPresenter.deleteFile(file);
            }

            @Override
            public void onPermanentlyDeleteClicked(AbstractFile file) {
                mPresenter.permanentlyDeleteFile(file);
            }

            @Override
            public void onMoveClicked(final AbstractFile file) {
                FolderPickerDialog folderPickerDialog = new FolderPickerDialog();
                List<String> listPath = new ArrayList<>();
                listPath.add(file.getPath());
                folderPickerDialog.setChosenPathList(listPath);
                folderPickerDialog.setChooseFolderDialogListener(new FolderPickerDialog.ChooseFolderDialogListener() {
                    @Override
                    public void onFolderChosen(String path) {
                        mPresenter.moveFile(file.getPath(), path);
                    }
                });
                folderPickerDialog.show(getSupportFragmentManager(),TAG);
            }

            @Override
            public void onCopyClicked(final AbstractFile file) {
                FolderPickerDialog folderPickerDialog = new FolderPickerDialog();
                List<String> listPath = new ArrayList<>();
                listPath.add(file.getPath());
                folderPickerDialog.setChosenPathList(listPath);
                folderPickerDialog.setChooseFolderDialogListener(new FolderPickerDialog.ChooseFolderDialogListener() {
                    @Override
                    public void onFolderChosen(String path) {
                        mPresenter.copyFile(file.getPath(), path);
                    }
                });
                folderPickerDialog.show(getSupportFragmentManager(),TAG);
            }

            @Override
            public void onPropertiesClicked(AbstractFile file) {
                showPropertiesDialog(file);
            }
        });
        setQuickAccessUI();
    }

    private void setQuickAccessUI() {
        layoutPath.setVisibility(View.GONE);
        imbMore.setVisibility(View.GONE);
        recreatePopupLayout(null);

        txtTitle.setText("Quick Access");
        mPresenter.loadQuickAccess();
        navigationView.setCheckedItem(R.id.nav_quick_access);

    }

    private void updateMenuItem(MenuItem menuItem) {
        txtTitle.setText(menuItem.getTitle());
        menuItem.setChecked(true);
        mDrawer.closeDrawers();
        mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, navigationView);

    }

    private void showPopupMenu(View v) {
        pm.show();
    }

    private void recreatePopupLayout(MenuItem item) {
        pm = new PopupMenu(this, imbMore);
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
        if(item == null || item.getItemId() == R.id.nav_quick_access) {
            pm.getMenuInflater().inflate(R.menu.menu_actionbar_quickaccess_popup, pm.getMenu());
        } else {
            pm.getMenuInflater().inflate(R.menu.menu_action_bar_popup, pm.getMenu());
        }
        pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_select: {
                        mAdapter.setSelect(true);
                        mActionMode = MainActivity.this.startActionMode(new ActionBarCallBack());
                        return true;
                    }
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
                    case R.id.action_create_file: {
                        showCreateFileDialog("Input file name");
                        return true;
                    }
                    case R.id.action_create_folder: {
                        showCreateFolderDialog("Input folder name");
                        return true;
                    }
                    default:{
                        return false;
                    }
                }

            }
        });
    }

    private void showCreateFolderDialog(String s) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(s);

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mPresenter.createFolder(String.valueOf(input.getText()));
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void showCreateFileDialog(String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mPresenter.createFile(String.valueOf(input.getText()));
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void showPropertiesDialog(AbstractFile file) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("View Properties");
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_properties, null);

        TextView tvName = dialogView.findViewById(R.id.tv_name);
        TextView tvLocation = dialogView.findViewById(R.id.tv_location);
        TextView tvType = dialogView.findViewById(R.id.tv_type);
        TextView tvFileSize = dialogView.findViewById(R.id.tv_file_size);
        TextView tvModified = dialogView.findViewById(R.id.tv_modified_date);
        TextView tvCreated = dialogView.findViewById(R.id.tv_created_date);
        TextView tvOpenedDate = dialogView.findViewById(R.id.tv_last_opened_date);

        tvName.setText(file.getName());
        tvLocation.setText(file.getPath());
        if(file.getExtension().isEmpty()) {
            tvType.setText("Folder");
        } else {
            tvType.setText(file.getExtension());
        }
        tvFileSize.setText(file.getSizeInfo());
        tvModified.setText(file.getStrLastModified());
        tvCreated.setText(file.getStrCreatedTime());
        tvOpenedDate.setText(file.getStrLastOpenedTime());

        dialogBuilder.setView(dialogView);
        dialogBuilder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        dialogBuilder.show();
    }

    private void showSortOptionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sort Type");

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_sort, null);

        final RadioGroup radioGroup = dialogView.findViewById(R.id.rg_group);

        radioGroup.check(R.id.rdb_name);

        builder.setView(dialogView);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int checked = radioGroup.getCheckedRadioButtonId();
                switch (checked) {
                    case R.id.rdb_name: {//Name
                        mPresenter.sortListByName();
                        break;
                    }
                    case R.id.rdb_created: {//Created
                        mPresenter.sortListByCreatedTime();
                        break;
                    }
                    case R.id.rdb_modified: {//Modified
                        mPresenter.sortListByModifed();
                        break;
                    }
                    case R.id.rdb_opened: {//Opened Time
                        mPresenter.sortListByOpenedTime();
                        break;
                    }
                    case R.id.rdb_file_type: {//File Type
                        mPresenter.sortListByFileType();
                        break;
                    }
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
    public void openDrawer() {
        mDrawer.openDrawer(navigationView);
    }

    @Override
    public void updateUI(List<AbstractFile> fileList) {
        txtPath.setText(FileManagerApp.getApp().getCurPath());
        mAdapter.setData(fileList);
    }

    @Override
    public boolean openFile(AbstractFile file) {
        boolean openedFile;
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file), file.getMimeType());
            startActivity(intent);
            openedFile = true;
        } catch (ActivityNotFoundException e) {
            openedFile = false;
        }
        return openedFile;
    }

    @Override
    public void setEmptyMode(boolean isEnable) {
        ViewGroup dataView = findViewById(R.id.layout_empty);
        if (dataView != null) {
            dataView.setVisibility(isEnable ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void notifyDelete(AbstractFile file) {
        mAdapter.notifyRemove(file);
    }

    private class ActionBarCallBack implements ActionMode.Callback {

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            // TODO Auto-generated method stub
            switch (item.getItemId()) {
                case R.id.action_delete: {
                    mPresenter.deleteMultiFiles(mAdapter.getSelectedList());
                    mode.finish();
                    return true;
                }
                case R.id.action_copy_to: {
                    FolderPickerDialog folderPickerDialog = new FolderPickerDialog();
                    List<String> listPath = new ArrayList<>();
                    for (AbstractFile file: mAdapter.getSelectedList()) {
                        listPath.add(file.getPath());
                    }
                    folderPickerDialog.setChosenPathList(listPath);
                    folderPickerDialog.setChooseFolderDialogListener(new FolderPickerDialog.ChooseFolderDialogListener() {
                        @Override
                        public void onFolderChosen(String path) {
                            mPresenter.copyMultiFiles(mAdapter.getSelectedList(), path);
                        }
                    });
                    folderPickerDialog.show(getSupportFragmentManager(),TAG);
                    mode.finish();
                    return true;
                }
                case R.id.action_move_to: {
                    FolderPickerDialog folderPickerDialog = new FolderPickerDialog();
                    List<String> listPath = new ArrayList<>();
                    for (AbstractFile file: mAdapter.getSelectedList()) {
                        listPath.add(file.getPath());
                    }
                    folderPickerDialog.setChosenPathList(listPath);
                    folderPickerDialog.setChooseFolderDialogListener(new FolderPickerDialog.ChooseFolderDialogListener() {
                        @Override
                        public void onFolderChosen(String path) {
                            mPresenter.moveMultiFiles(mAdapter.getSelectedList(), path);
                        }
                    });
                    folderPickerDialog.show(getSupportFragmentManager(),TAG);
                    mode.finish();
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.menu_contextual, menu);
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mAdapter.setSelect(false);
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

    }
}
