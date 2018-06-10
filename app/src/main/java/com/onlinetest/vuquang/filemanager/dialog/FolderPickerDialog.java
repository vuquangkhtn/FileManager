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
import com.onlinetest.vuquang.filemanager.app.FileManagerApp;
import com.onlinetest.vuquang.filemanager.base.BaseDialog;
import com.onlinetest.vuquang.filemanager.base.DialogMvpView;
import com.onlinetest.vuquang.filemanager.data.model.file.CustomFile;
import com.onlinetest.vuquang.filemanager.dialog.adapter.FolderAdapter;
import com.onlinetest.vuquang.filemanager.utils.LocalPathUtils;

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

    private List<CustomFile> fileList;
    private FolderAdapter mAdapter;
    private RecyclerView rvFolderList;

    private TextView tvPath;
    private ImageButton imbBack;

    private Button btnCancel, btnChoose;

    private String chosenPath;
    private ChooseFolderDialogListener chooseFolderDialogListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.dialog_choose_folder, null);
        fileList = new ArrayList<>();
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
            public void onOpenClicked(CustomFile file) {
                openDirectory(file.getFile());
            }
        });

        openDirectory(new File(chosenPath));
    }

    public void setChooseFolderDialogListener(ChooseFolderDialogListener chooseFolderDialogListener) {
        this.chooseFolderDialogListener = chooseFolderDialogListener;
    }

    public interface ChooseFolderDialogListener {
        void onFolderChosen(String path);
    }

    private void openDirectory(File directory) {
        fileList.clear();
        chosenPath = directory.getPath();
        if(directory.listFiles() != null && directory.listFiles().length != 0) {
            for (File childFile:directory.listFiles()) {
                if(childFile.isFile() || childFile.getName().startsWith(".")) {
                    continue;
                }

                CustomFile customFile = new CustomFile(childFile.getPath());
                fileList.add(customFile);
            }
        } else {
//            getMvpView().updateEmptyListUI();
        }
        sortListByFileType(fileList);
        tvPath.setText(chosenPath);
        mAdapter.setData(fileList);
    }

    private void sortListByFileType(List<CustomFile> fileList) {
        Collections.sort(fileList, new Comparator<CustomFile>() {
            @Override
            public int compare(CustomFile o1, CustomFile o2) {
                return o1.getName().compareToIgnoreCase(o2.getName());
            }
        });
        Collections.sort(fileList, new Comparator<CustomFile>() {
            @Override
            public int compare(CustomFile f1, CustomFile f2) {
                if (f1.getFile().isDirectory() == f2.getFile().isDirectory())
                    return 0;
                else if (f1.getFile().isDirectory() && !f2.getFile().isDirectory())
                    return -1;
                else
                    return 1;
            }
        });

    }
}
