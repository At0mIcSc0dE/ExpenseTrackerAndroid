package com.example.exptrcandroid.ui.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exptrcandroid.R;
import com.example.exptrcandroid.RI;

public class GalleryFragment extends Fragment {

    private GalleryViewModel galleryViewModel;

    public RecyclerView lstbox;
    public RecyclerView lstboxMonthly;
    public RecyclerView lstboxTakings;
    public RecyclerView lstboxMonthlyTakings;

//    private RecyclerView.Adapter mAdapter;
//    private RecyclerView.LayoutManager layoutManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);

        lstbox = root.findViewById(R.id.lst1);
        lstboxMonthly = root.findViewById(R.id.lst2);
        lstboxTakings = root.findViewById(R.id.lst3);
        lstboxMonthlyTakings = root.findViewById(R.id.lst4);

        InsertIntoListboxes();

        return root;
    }

    public void AddItem(RecyclerView view, String text)
    {

    }

    public void InsertIntoListboxes()
    {
//        for(int i = 1; i <= RI.fm.m_GeneralData.CurrOneTimeExpCount; ++i)
//        {
////            AddItem(lstbox, RI.fm.m_OneTimeExpData.get(i).get(0) + " || " + RI.fm.m_OneTimeExpData.get(i).get(1));
//        }
    }

}