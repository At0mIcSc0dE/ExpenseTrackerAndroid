package com.example.exptrcandroid.Debug;

public class Timer {

    private long startTime;
    private long endTime;

    public Timer()
    {
        startTime = System.nanoTime();
    }

    /**
     * @returns time since constructor in milliseconds
     * */
    public long EndTimer()
    {
        endTime = System.nanoTime();
        return ((endTime - startTime) / 1000000);
    }

}
