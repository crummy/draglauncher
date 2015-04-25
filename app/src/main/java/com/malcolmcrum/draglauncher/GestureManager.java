package com.malcolmcrum.draglauncher;

import android.graphics.Point;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages input and translates it into "gestures"
 * Created by Malcolm on 4/23/2015.
 */
public class GestureManager {
    private List<GestureListener> listeners = new ArrayList<>();

    private List<Point> touchHistory = new ArrayList<>();

    public GestureManager() {
        // init
    }

    public boolean handleInput(MotionEvent input) {
        boolean handledGesture = false;
        switch (input.getAction()) {
            case MotionEvent.ACTION_DOWN:
                handledGesture = true; // Just so we get the later events.
                break;
            case MotionEvent.ACTION_UP:
                touchHistory.clear();
                released();
                handledGesture = true;
                break;
            case MotionEvent.ACTION_MOVE:
                touchHistory.add(new Point((int)input.getX(), (int)input.getY())); // TODO: Limit size of touchHistory
                handledGesture = true;
                break;

        }
        return handledGesture;
    }

    public List<Point> getHistory() {
        return touchHistory;
    }

    public void addListener(GestureListener listener) {
        listeners.add(listener);
    }

    private void released() {
        for (GestureListener listener : listeners) {
            listener.gestureFinished();
        }
    }
}
