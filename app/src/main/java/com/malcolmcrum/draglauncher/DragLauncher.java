package com.malcolmcrum.draglauncher;

import android.app.Activity;
import android.os.Bundle;


public class DragLauncher extends Activity implements GestureListener {

    DragMenuItem rootItem;
    DragMenuItem currentItem = null;
    DragView dragView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initializeDefaultMenu();

        dragView = new DragView(this);
        setContentView(dragView);
    }

    public void gestureChanged(GestureManager.Direction direction) {
        // update currentItem
    }

    public void gestureFinished() {
        // get gesture
        // perform action (launch currentItem probably)
        // else currentItem = null? rootItem?
    }

    private void initializeDefaultMenu() {
        rootItem = new DragMenuItem("com.malcolmcrum.draglauncher");
    }

}
