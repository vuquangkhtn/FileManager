package com.onlinetest.vuquang.filemanager.main.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.onlinetest.vuquang.filemanager.R;
import com.onlinetest.vuquang.filemanager.app.FileManagerApp;
import com.onlinetest.vuquang.filemanager.data.model.file.AbstractFile;
import com.onlinetest.vuquang.filemanager.app.LocalPathUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by VuQuang on 6/10/2018.
 */

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.FileHolder> {
    private List<AbstractFile> fileList;
    private boolean showCheckbox;
    private Context mContext;

    private String[] arrFileType = {"avi","doc","exe","jpg","html","mp3","mp4","pdf","png","txt","xls","zip"};
    private int[] arrFileIco = {
            R.drawable.ic_avi,
            R.drawable.ic_doc,
            R.drawable.ic_exe,
            R.drawable.ic_jpg,
            R.drawable.ic_html,
            R.drawable.ic_mp3,
            R.drawable.ic_mp4,
            R.drawable.ic_pdf,
            R.drawable.ic_png,
            R.drawable.ic_txt,
            R.drawable.ic_xls,
            R.drawable.ic_zip};

    private FileItemListener fileItemListener;

    public FileAdapter(Context context) {
        this.mContext = context;
        this.fileList = new ArrayList<>();
        showCheckbox = false;
    }

    public void enableSelect(boolean isEnable) {
        this.showCheckbox = isEnable;
    }

    public void setData(List<AbstractFile> listData) {
        this.fileList = listData;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FileHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FileHolder(LayoutInflater.from(mContext).inflate(R.layout.item_file_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final FileHolder holder, final int position) {
        final AbstractFile file = fileList.get(position);

        if(showCheckbox) {
            holder.checkboxSelect.setVisibility(View.VISIBLE);
        } else {
            holder.checkboxSelect.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!FileManagerApp.getApp().getCurPath().equals(LocalPathUtils.RECYCLE_BIN_DIR)) {
                    if(!showCheckbox) {
                        fileItemListener.onOpenClicked(file);
                    } else {
                        file.isSelected = !file.isSelected;
                        notifyItemChanged(position);
                    }
                }
            }
        });

        holder.checkboxSelect.setChecked(file.isSelected);

        holder.tvName.setText(file.getName());
        holder.tvInfo.setText(file.getStrInfo());
        boolean knownType = false;
        if(file.isDirectory()) {
            knownType = true;
            holder.imvIcon.setBackgroundResource(R.drawable.ic_folder);
        } else {
            for (int i=0;i<arrFileType.length;i++) {
                if(file.getExtension().toLowerCase().equals(arrFileType[i])) {
                    holder.imvIcon.setBackgroundResource(arrFileIco[i]);
                    knownType = true;
                    break;
                }
            }
        }
        if(!knownType) {
            holder.imvIcon.setBackgroundResource(R.drawable.ic_unknown_file);
        }

        holder.imbMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!showCheckbox) {
                    showPopupMenu(v, file);
                }
            }
        });
    }

    private void showPopupMenu(View v, final AbstractFile file) {
        PopupMenu pm = new PopupMenu(mContext, v);
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
        if(FileManagerApp.getApp().getCurPath().equals(LocalPathUtils.RECYCLE_BIN_DIR)) {
            pm.getMenuInflater().inflate(R.menu.menu_recycle_file_popup, pm.getMenu());
        } else if(FileManagerApp.getApp().getCurPath().equals("")){
            pm.getMenuInflater().inflate(R.menu.menu_quick_access_file_popup, pm.getMenu());
        } else {
            pm.getMenuInflater().inflate(R.menu.menu_file_popup, pm.getMenu());
        }
        pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_open: {
                        fileItemListener.onOpenClicked(file);
                        return true;
                    }
                    case R.id.action_delete: {
                        fileItemListener.onDeleteClicked(file);
                        return true;
                    }
                    case R.id.action_permanently_delete: {
                        fileItemListener.onPermanentlyDeleteClicked(file);
                        return true;
                    }
                    case R.id.action_copy_to: {
                        fileItemListener.onCopyClicked(file);
                        return true;
                    }
                    case R.id.action_move_to: {
                        fileItemListener.onMoveClicked(file);
                        return true;
                    }
                    case R.id.action_properties: {
                        fileItemListener.onPropertiesClicked(file);
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

    @Override
    public int getItemCount() {
        if (fileList != null) {
            return fileList.size();
        }
        return 0;
    }

    public void notifyRemove(AbstractFile file) {
        for (int i=0;i<fileList.size();i++) {
            if(fileList.get(i).getPath().equals(file.getPath())) {
                fileList.remove(i);
                notifyItemRemoved(i);
                return;
            }
        }
    }

    public void setSelect(boolean showCheckbox) {
        this.showCheckbox = showCheckbox;
        if(showCheckbox) {
            for (AbstractFile file:fileList) {
                file.isSelected = false;
            }
        }
        notifyDataSetChanged();
    }

    public void setFileItemListener(FileItemListener fileItemListener) {
        this.fileItemListener = fileItemListener;
    }

    public List<AbstractFile> getSelectedList() {
        List<AbstractFile> selectedFile = new ArrayList<>();
        for (AbstractFile file:fileList) {
            if(file.isSelected) {
                selectedFile.add(file);
            }
        }
        return selectedFile;
    }

    class FileHolder extends RecyclerView.ViewHolder {
        private CheckBox checkboxSelect;
        private ImageView imvIcon;
        private TextView tvName;
        private TextView tvInfo;
        private ImageButton imbMore;
        FileHolder(View itemView) {
            super(itemView);
            checkboxSelect = itemView.findViewById(R.id.item_select);
            imvIcon = itemView.findViewById(R.id.imv_file_icon);
            tvName = itemView.findViewById(R.id.tv_file_name);
            tvInfo = itemView.findViewById(R.id.tv_file_info);
            imbMore = itemView.findViewById(R.id.imb_more);
        }
    }

    public interface FileItemListener {
        void onOpenClicked(AbstractFile file);
        void onDeleteClicked(AbstractFile file);
        void onPermanentlyDeleteClicked(AbstractFile file);
        void onMoveClicked(AbstractFile file);
        void onCopyClicked(AbstractFile file);
        void onPropertiesClicked(AbstractFile file);
    }
}
