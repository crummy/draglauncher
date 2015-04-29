package com.malcolmcrum.draglauncher;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;


public class DragLauncher extends Activity implements GestureListener {

    DragMenuRoot rootItem;
    DragMenuItem currentItem = null;
    DragView dragView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initializeDefaultMenu();

        dragView = new DragView(this);
        loadIcons(rootItem);
        setContentView(dragView);
    }

    public void gestureStarted() {
        currentItem = rootItem;
    }

    public void gestureChanged(GestureManager.Direction direction) {
        if (currentItem != null) {
            currentItem = currentItem.getChild(direction);
        }
    }

    public void gestureFinished() {
        if (currentItem != null) {
            currentItem.selectItem();
            Toast toast = Toast.makeText(this, currentItem.getName(), Toast.LENGTH_SHORT);
            toast.show();
        } else {
            Toast toast = Toast.makeText(this, "null", Toast.LENGTH_SHORT);
            toast.show();
        }
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

    private void loadIcons(DragMenuItem item) {
        if (item == null) return;

        dragView.loadIcon(item.getName());

        loadIcons(item.getChild(GestureManager.Direction.east));
        loadIcons(item.getChild(GestureManager.Direction.west));
        loadIcons(item.getChild(GestureManager.Direction.south));
        loadIcons(item.getChild(GestureManager.Direction.north));
    }

}
