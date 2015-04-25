package com.malcolmcrum.draglauncher;

/**
 * Observer interface for GestureManager
 * Created by Malcolm on 4/23/2015.
 */
public interface GestureListener {
    void gestureChanged(GestureManager.Direction direction);
    void gestureFinished();
}
