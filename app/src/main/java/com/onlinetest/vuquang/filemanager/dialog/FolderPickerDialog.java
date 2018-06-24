package com.onlinetest.vuquang.filemanager.dialog;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.onlinetest.vuquang.filemanager.R;
import com.onlinetest.vuquang.filemanager.base.BaseDialog;
import com.onlinetest.vuquang.filemanager.base.DialogMvpView;
import com.onlinetest.vuquang.filemanager.data.model.file.AbstractFile;
import com.onlinetest.vuquang.filemanager.data.model.file.CustomFolder;
import com.onlinetest.vuquang.filemanager.dialog.adapter.FolderAdapter;
import com.onlinetest.vuquang.filemanager.app.LocalPathUtils;
import com.onlinetest.vuquang.filemanager.main.sort.FileTypeSort;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by VuQuang on 6/10/2018.
 */

public class FolderPickerDialog extends BaseDialog implements DialogMvpView{
    public static final String TAG = "FolderPickerDialog";

    private List<AbstractFile> folderList;
    private FolderAdapter mAdapter;
    private RecyclerView rvFolderList;

    private TextView tvPath;
    private ImageButton imbBack;

    private Button btnCancel, btnChoose;

    private View rootView;

    private List<String> chosenPathList = new ArrayList<>();
    private String chosenPath;
    private ChooseFolderDialogListener chooseFolderDialogListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.dialog_choose_folder, null);
        rootView = view;
        folderList = new ArrayList<>();
        chosenPath = LocalPathUtils.EXTERNAL_STORAGE;

        imbBack = view.findViewById(R.id.imb_navi_back);
        tvPath = view.findViewById(R.id.tv_path);

        btnCancel = view.findViewById(R.id.btn_cancel);
        btnChoose = view.findViewById(R.id.btn_choose);

        rvFolderList = view.findViewById(R.id.rv_folder_list);
        rvFolderList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mAdapter = new FolderAdapter(getContext());
        rvFolderList.setAdapter(mAdapter);
        return view;
    }

    @Override
    protected void setUp(View view) {
        imbBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!chosenPath.equals(LocalPathUtils.EXTERNAL_STORAGE)) {
                    File file = new File(chosenPath);
                    openDirectory(file.getParentFile());
                }
            }
        });

        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFolderDialogListener.onFolderChosen(chosenPath);
                dismissDialog(TAG);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDialog(TAG);
            }
        });
        mAdapter.setFolderItemListener(new FolderAdapter.FolderItemListener() {
            @Override
            public void onOpenClicked(AbstractFile file) {
                openDirectory(file);
            }
        });

        openDirectory(new File(chosenPath));
    }

    private void openDirectory(File directory) {
        folderList.clear();
        chosenPath = directory.getPath();
        if(directory.listFiles() != null && directory.listFiles().length != 0) {
            for (File childFile:directory.listFiles()) {
                if(childFile.isFile() || childFile.getName().startsWith(".") || contain(childFile.getPath())) {
                    continue;
                }

                AbstractFile folder = new CustomFolder(childFile.getPath());
                folderList.add(folder);
            }
            if(folderList.size() == 0) {
                setEmptyMode(true);
            } else {
                setEmptyMode(false);
                sortListByFileType(folderList);
                mAdapter.setData(folderList);
            }
        } else {
            setEmptyMode(true);
        }
        tvPath.setText(chosenPath);
    }

    private boolean contain(String path) {
        for (String str: chosenPathList) {
            if(str.equals(path)) {
                return true;
            }
        }
        return false;
    }

    private void sortListByFileType(List<AbstractFile> fileList) {
        Collections.sort(fileList, new Comparator<AbstractFile>() {
            @Override
            public int compare(AbstractFile o1, AbstractFile o2) {
                return (new FileTypeSort()).compare(o1,o2);
            }
        });
    }

    public void setChosenPathList(List<String> chosenPathList) {
        this.chosenPathList = new ArrayList<>(chosenPathList);
    }

    public void setEmptyMode(boolean isEnable) {
        ViewGroup dataView = rootView.findViewById(R.id.layout_empty);
        if (dataView != null) {
            dataView.setVisibility(isEnable ? View.VISIBLE : View.GONE);
        }
    }

    public void setChooseFolderDialogListener(ChooseFolderDialogListener chooseFolderDialogListener) {
        this.chooseFolderDialogListener = chooseFolderDialogListener;
    }

    public interface ChooseFolderDialogListener {
        void onFolderChosen(String path);
    }
}
