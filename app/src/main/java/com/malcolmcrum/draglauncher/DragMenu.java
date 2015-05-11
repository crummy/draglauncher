package com.malcolmcrum.draglauncher;

import android.content.res.Resources;

import java.util.ArrayList;
import java.util.List;

/**
 * Drag Menu manager.
 * Created by Malcolm on 4/29/2015.
 */
public class DragMenu implements GestureListener {
    private DragMenuRoot rootItem;
    private DragMenuItem currentItem;
    private long selectedTime;
    private List<MenuListener> listeners = new ArrayList<>();

    public DragMenu(Resources resources) {
        initializeDefaultMenu(resources);
    }

    public void addListener(MenuListener listener) {
        listeners.add(listener);
    }

    public DragMenuItem getCurrent() {
        return currentItem;
    }

    public DragMenuItem getRoot() {
        return rootItem;
    }

    public void gestureStarted() {
        currentItem = rootItem;
    }

    public void gestureChanged(GestureManager.Direction direction) {
        if (currentItem != null) {
            currentItem = currentItem.getChild(direction);
            if (currentItem != null) {
                for (MenuListener listener : listeners) {
                    listener.gestureChanged();
                }
            }
        }
        selectedTime = System.nanoTime();
    }

    public void gestureFinished() {
        if (currentItem != null) {
            for (MenuListener listener : listeners) {
                listener.itemSelected(currentItem);
            }
            currentItem = null;
        }
    }

    public long nanosecondsSinceSelection() {
        return System.nanoTime() - selectedTime;
    }

    private void initializeDefaultMenu(Resources resources) {
        rootItem = new DragMenuRoot(resources);
        rootItem.setChild(GestureManager.Direction.east, "com.htc.camera");
        rootItem.setChild(GestureManager.Direction.west, "com.google.android.music")
                .setChild(GestureManager.Direction.north, "com.sonos.acr");
        rootItem.setChild(GestureManager.Direction.north, "com.android.chrome")
                .setChild(GestureManager.Direction.east, "com.google.android.talk")
                .setChild(GestureManager.Direction.north, "com.facebook.katana");
        rootItem.getChild(GestureManager.Direction.north)
                .getChild(GestureManager.Direction.east)
                .setChild(GestureManager.Direction.south, "com.twitter.android");
        rootItem.getChild(GestureManager.Direction.north)
                .setChild(GestureManager.Direction.west, "com.google.android.apps.inbox");
    }

    public DragMenuItem getSettings() {
        return rootItem.getChild(GestureManager.Direction.south).getChild(GestureManager.Direction.east);
    }

    public DragMenuItem getUnlock() {
        return rootItem.getChild(GestureManager.Direction.south);
    }

    public DragMenuItem getEdit() {
        return rootItem.getChild(GestureManager.Direction.south).getChild(GestureManager.Direction.west);
    }
}
