package com.example.sony.bouncingballpacman;

import android.os.Handler;
import android.os.Looper;
import android.view.View;

/**
 *  This is a helper class that wraps up some of the code necessary to initiate
 *  an animation thread that repaints a view at a regular interval.
 */
public class DrawingThread {
    private View view = null;
    private int fps;                // frame per second
    private Thread thread = null;   // thread that handles task of re-drawing
    private Handler handler = null; // handler for log messages
    private volatile boolean is_running = false; // volatile to ensure other threads sees the change to this boolean variable

    /*
     *  Constructs a new DrawingThread to update the given view to
     *  the given number of times per second (fps).
     *  Does NOT start the thread running; instead thread is in ready state and begins
     *  to run when start() gets call.
     */
    public DrawingThread(View view, int fps)
    {
        if (view == null || fps <= 0)
        {
            throw new IllegalArgumentException();
        }
        this.view = view;
        this.fps = fps;
        this.handler = new Handler(Looper.getMainLooper());
    }

    /*
     * Returns true if the drawing thread is in running state.
     */
    public boolean
    isRunning()
    {
        return thread != null;
    }

    /*
     *  Starts the thread by transitioning to running state.
     *  The thread will repaint the view repeatedly at given fps.
     */
    public void
    start()
    {
        if(thread == null)
        {
            thread = new Thread(new DrawingThreadAction());
            thread.start();
        }
    }

    /*
     *  Runnable helper class that contains the drawing thread's main loop
     *  to repeatedly sleep and redraw the view.
     */
    private class DrawingThreadAction implements Runnable {
        public void run()
        {
            is_running = true;
            while(is_running)
            {
                try
                {
                    Thread.sleep(1000/fps);
                }
                catch (InterruptedException ie)
                {
                    is_running = false;
                }
                // post a message that will trigger the view to redraw
                handler.post(new Updater());
            }
        }
    }

    /*
     * Runnable helper class that is necessary for Android to redraw a view.
     */
    private class Updater implements Runnable{
        public void run()
        {
            view.invalidate(); // invalidate() will redraw view by calling View's onDraw() method
        }
    }

    /*
     * Stops the thread from running (terminate state). The view will no longer
     * repaint itself once this method gets call.
     */
    public void
    stop()
    {
        if (thread != null)
        {
            is_running = false;
            try
            {
                thread.join();
            }
            catch (InterruptedException ie)
            {
                // empty
            }
            thread = null;
        }
    }
}