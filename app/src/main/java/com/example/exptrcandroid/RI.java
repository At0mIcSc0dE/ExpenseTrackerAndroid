package com.example.exptrcandroid;


import java.io.IOException;

public class RI implements Runnable
{
    public Thread thread;
    private String threadName;
    public FileManager fm;

    RI( String name) {
        threadName = name;
    }

    @Override
    public void run()
    {
        try
        {
            fm = new FileManager("smb://192.168.178.45/share/ExpTrc/OneTimeExpenses.exptrc", "smb://192.168.178.45/share/ExpTrc/MonthlyExpenses.exptrc", "smb://192.168.178.45/share/ExpTrc/OneTimeTakings.exptrc", "smb://192.168.178.45/share/ExpTrc/MonthlyTakings.exptrc", "smb://192.168.178.45/share/ExpTrc/General.exptrc");
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public void start()
    {
        if(thread == null)
            thread = new Thread(this, threadName);
        thread.start();
    }
}
