package com.onlinetest.vuquang.filemanager.externalstorage;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.onlinetest.vuquang.filemanager.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExternalStorageFragment extends Fragment {


    public ExternalStorageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_external_storage, container, false);
    }

}
