package com.malcolmcrum.draglauncher;

/**
 * Drag Menu manager.
 * Created by Malcolm on 4/29/2015.
 */
public class DragMenu implements GestureListener {
    private DragMenuRoot rootItem;
    private DragMenuItem currentItem;
    private long selectedTime;

    public DragMenu() {
        initializeDefaultMenu();
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
        }
        selectedTime = System.nanoTime();
    }

    public void gestureFinished() {
        if (currentItem != null) {
            currentItem.selectItem(); // TODO: I feel like this should be in the controller?
            currentItem = null;
        }
    }

    public long nanosecondsSinceSelection() {
        return System.nanoTime() - selectedTime;
    }

    private void initializeDefaultMenu() {
        rootItem = new DragMenuRoot();
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
}
