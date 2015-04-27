package com.malcolmcrum.draglauncher;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;


public class DragLauncher extends Activity implements GestureListener {

    DragMenuRoot rootItem;
    DragMenuItem currentItem = null;
    DragView dragView;
    Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initializeDefaultMenu();

        dragView = new DragView(this);
        setContentView(dragView);

        toast = Toast.makeText(this, "empty", Toast.LENGTH_SHORT);
    }

    public void gestureStarted() {
        currentItem = rootItem;
    }

    public void gestureChanged(GestureManager.Direction direction) {
        if (currentItem != null) {
            currentItem = currentItem.getChild(direction);
            toast.setText(currentItem.getName());
            toast.show();
        }
    }

    public void gestureFinished() {
        if (currentItem != null) {
            currentItem.selectItem();
        } else {
            currentItem = null;
        }
    }

    private void initializeDefaultMenu() {
        rootItem = new DragMenuRoot();
        rootItem.setChild(GestureManager.Direction.east, "com.htc.camera");
        rootItem.setChild(GestureManager.Direction.west, "com.google.android.music")
                .setChild(GestureManager.Direction.north, "com.sonos.acr");
    }

}
