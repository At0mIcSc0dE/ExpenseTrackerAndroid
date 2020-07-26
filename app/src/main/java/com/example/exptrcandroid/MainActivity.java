package com.example.exptrcandroid;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.Menu;
import android.view.inputmethod.InputMethodManager;

import com.example.exptrcandroid.ui.home.HomeFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.IOException;
import java.util.Calendar;

import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;


/***************************************
 * try{ ri.thread.join(); } catch(InterruptedException e) { throw new RuntimeException(e); }
 *
 * Bug where Adding it on the phone and then deleting it on the PC only increments the bank balance
 * Solution: Calculate and update it here, too
/***************************************/

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private RI ri;
    private int rbChecked = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        ri = new RI("FM-Thread");
        ri.start();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navAdd, R.id.navShow, R.id.navStatus)
                .setDrawerLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
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

    public void OnAddExpClick(View view) throws IOException, InterruptedException
    {
        Calendar calendar = Calendar.getInstance();
        ExpenseData expenseData = new ExpenseData(HomeFragment.txtExpName.getText().toString() , Double.parseDouble(HomeFragment.txtExpPrice.getText().toString()), "", calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR));

        switch(rbChecked)
        {
            case 1:
                AwaitConnection("smb://192.168.178.45/share/ExpTrc/OneTimeExpenses.exptrc");
                break;
            case 2:
                AwaitConnection("smb://192.168.178.45/share/ExpTrc/MonthlyExpenses.exptrc");
                break;
            case 3:
                AwaitConnection("smb://192.168.178.45/share/ExpTrc/OneTimeTakings.exptrc");
                break;
            case 4:
                AwaitConnection("smb://192.168.178.45/share/ExpTrc/MonthlyTakings.exptrc");
                break;
        }

        ri.thread.join();
        ri.fm.WriteExpense(rbChecked, expenseData);
        HomeFragment.txtExpName.setText("");
        HomeFragment.txtExpPrice.setText("");
    }

    public void OnTxtExpNameClick(View view)
    {
        HomeFragment.txtExpPrice.requestFocus();
    }

    public void OnTxtExpPriceClick(View view)
    {
        hideKeyboard(this);
    }

    public void OnRb1Click(View view)
    {
        rbChecked = 1;
    }

    public void OnRb2Click(View view)
    {
        rbChecked = 2;
    }

    public void OnRb3Click(View view)
    {
        rbChecked = 3;
    }

    public void OnRb4Click(View view)
    {
        rbChecked = 4;
    }

    // Thanks to rmirabelle, Angus H, Moustafa EL-Saghier, Sayed Mohd Ali, Micer, oxied, ToolmakerSteve, friederbluemle on stackoverflow
    // https://stackoverflow.com/questions/1109022/how-do-you-close-hide-the-android-soft-keyboard-using-java?page=1&tab=votes#tab-top
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}