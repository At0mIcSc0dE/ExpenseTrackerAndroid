package com.example.exptrcandroid.ui.slideshow;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.exptrcandroid.R;
import com.example.exptrcandroid.RI;

import java.io.IOException;

import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;


public class SlideshowFragment extends Fragment {

    private SlideshowViewModel slideshowViewModel;

    private TextView lblTotalIncome;
    private TextView lblTotalExpenses;
    private TextView lblRemainingBudget;
    private TextView lblBankBalance;

    private double totalIncome;
    private double totalExpenses;

    @SuppressLint("SetTextI18n")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                ViewModelProviders.of(this).get(SlideshowViewModel.class);
        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);

        lblTotalIncome = root.findViewById(R.id.lblTotalIncome);
        lblTotalExpenses = root.findViewById(R.id.lblTotalExpenses);
        lblRemainingBudget = root.findViewById(R.id.lblRemainingBudget);
        lblBankBalance = root.findViewById(R.id.lblBankBalance);

        totalIncome = CalculateIncome();
        totalExpenses = CalculateExpenses();

        lblTotalIncome.setText("Your total Income: " + (Math.round(totalIncome * 100.0) / 100.0) + "€");
        lblTotalExpenses.setText("Your total Expenses: " + (Math.round(totalExpenses * 100.0) /100.0) + "€");
        lblRemainingBudget.setText("Your remaining Budget: " + (Math.round((totalIncome - totalExpenses) * 100.0) / 100.0) + "€");
        lblBankBalance.setText("Your bank balance: " + (Math.round(RI.fm.m_GeneralData.balance * 100.0) / 100.0) + "€");

        return root;
    }

    private void AwaitConnection(String filePath) throws IOException
    {
        SmbFile file = new SmbFile(filePath);
        SmbFileInputStream reader;

        while(true)
        {
            try
            {
                reader = new SmbFileInputStream(file);
                break;
            }
            catch (IOException e)
            {
            }
        }

        reader.close();
    }

    private double CalculateIncome()
    {
        double income = 0;

        for(int i = 1; i <= RI.fm.m_GeneralData.CurrOneTimeTakCount; ++i)
        {
            income += Double.parseDouble(RI.fm.m_OneTimeTakData.get(i).get(1));
        }

        for(int i = 1; i <= RI.fm.m_GeneralData.CurrMonthlyTakCount; ++i)
        {
            income += Double.parseDouble(RI.fm.m_MonthlyTakData.get(i).get(1));
        }

        return income;
    }

    private double CalculateExpenses()
    {
        double expenses = 0;

        for(int i = 1; i <= RI.fm.m_GeneralData.CurrOneTimeExpCount; ++i)
        {
            expenses += Double.parseDouble(RI.fm.m_OneTimeExpData.get(i).get(1));
        }

        for(int i = 1; i <= RI.fm.m_GeneralData.CurrMonthlyExpCount; ++i)
        {
            expenses += Double.parseDouble(RI.fm.m_MonthlyExpData.get(i).get(1));
        }

        return expenses;
    }

}