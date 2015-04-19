package com.malcolmcrum.draglauncher;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;


public class DragLauncher extends Activity {

    DragMenuItem menuRoot;
    DragView dragView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initializeDefaultMenu();

        dragView = new DragView(this, menuRoot);
        dragView.setBackgroundColor(Color.LTGRAY);
        setContentView(dragView);
    }

    private void initializeDefaultMenu() {
        menuRoot = new DragMenuItem(true);
        menuRoot.label = ":)";
        menuRoot.north = new DragMenuItem();
        menuRoot.north.north = new DragMenuItem();
        menuRoot.east = new DragMenuItem();
        menuRoot.south = new DragMenuItem();
        menuRoot.south.label = "!";
        menuRoot.west = new DragMenuItem();
    }

}
