package com.malcolmcrum.draglauncher;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;


public class DragLauncher extends Activity implements GestureListener {

    DragMenuItem rootItem;
    DragMenuItem currentItem = null;
    DragView dragView;
    Toast toast;
    String allGestures = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initializeDefaultMenu();

        dragView = new DragView(this);
        setContentView(dragView);

        toast = Toast.makeText(this, "empty", Toast.LENGTH_SHORT);
    }

    public void gestureChanged(GestureManager.Direction direction) {
        allGestures += direction + " ";
        toast.setText(allGestures);
        toast.show();
    }

    public void gestureFinished() {
        allGestures = "";
        // get gesture
        // perform action (launch currentItem probably)
        // else currentItem = null? rootItem?
    }

    private void initializeDefaultMenu() {
        rootItem = new DragMenuItem("com.malcolmcrum.draglauncher");
    }

}
