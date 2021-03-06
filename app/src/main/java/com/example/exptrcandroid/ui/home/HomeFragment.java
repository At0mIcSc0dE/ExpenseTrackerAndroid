package com.example.exptrcandroid.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.exptrcandroid.MainActivity;
import com.example.exptrcandroid.R;

import java.io.IOException;
import java.util.ArrayList;

import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    static public EditText txtExpPrice;
    static public EditText txtExpName;
    public TextView lblStatus;
    static public View mRoot;
    static public boolean setText = false;
    static public boolean loaded = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        txtExpPrice = root.findViewById(R.id.txtExpPrice);
        txtExpName = root.findViewById(R.id.txtExpName);
        lblStatus = root.findViewById(R.id.lblStatus);
        if(setText)
            lblStatus.setText("Awaiting User Input...");
        else
            lblStatus.setText("Awaiting Connection to File Server...");

        mRoot = root;
        loaded = true;
        return root;
    }

}