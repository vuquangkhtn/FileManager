package com.onlinetest.vuquang.filemanager.dialog.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.onlinetest.vuquang.filemanager.R;
import com.onlinetest.vuquang.filemanager.data.model.file.AbstractFile;
import com.onlinetest.vuquang.filemanager.data.model.file.AbstractFile;

import java.util.List;

/**
 * Created by VuQuang on 6/10/2018.
 */

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.FolderHolder> {
    private List<AbstractFile> fileList;
    private Context mContext;

    private FolderItemListener folderItemListener;

    public FolderAdapter(Context context) {
        this.mContext = context;
    }

    public void setData(List<AbstractFile> listData) {
        this.fileList = listData;
        notifyDataSetChanged();
    }

    @Override
    public FolderHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FolderHolder(LayoutInflater.from(mContext).inflate(R.layout.item_file_list, parent, false));
    }

    @Override
    public void onBindViewHolder(FolderHolder holder, int position) {
        final AbstractFile file = fileList.get(position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                folderItemListener.onOpenClicked(file);
            }
        });

        holder.tvName.setText(file.getName());
        holder.tvInfo.setText(file.getDetail());
        holder.imvIcon.setBackgroundResource(R.drawable.ic_folder);
    }

    @Override
    public int getItemCount() {
        if (fileList != null) {
            return fileList.size();
        }
        return 0;
    }

    public void setFolderItemListener(FolderItemListener folderItemListener) {
        this.folderItemListener = folderItemListener;
    }

    public class FolderHolder extends RecyclerView.ViewHolder {
        private ImageView imvIcon;
        private TextView tvName;
        private TextView tvInfo;

        public FolderHolder(View itemView) {
            super(itemView);
            imvIcon = itemView.findViewById(R.id.imv_file_icon);
            tvName = itemView.findViewById(R.id.tv_file_name);
            tvInfo = itemView.findViewById(R.id.tv_file_info);
        }
    }

    public interface FolderItemListener {
        void onOpenClicked(AbstractFile file);
    }
}