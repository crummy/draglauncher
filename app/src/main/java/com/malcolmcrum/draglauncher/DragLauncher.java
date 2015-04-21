package com.malcolmcrum.draglauncher;

import android.app.Activity;
import android.os.Bundle;


public class DragLauncher extends Activity {

    DragMenuItem menuRoot;
    DragView dragView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initializeDefaultMenu();

        dragView = new DragView(this, menuRoot);
        setContentView(dragView);
    }

    private void initializeDefaultMenu() {
        menuRoot = new DragMenuItem(true);
        menuRoot.label = ":)";
        menuRoot.setNorth(new DragMenuItem());
        menuRoot.getNorth().setNorth(new DragMenuItem());
        menuRoot.getNorth().setWest(new DragMenuItem());
        menuRoot.getNorth().getWest().setNorth(new DragMenuItem());
        menuRoot.getNorth().getWest().setSouth(new DragMenuItem());
        menuRoot.setEast(new DragMenuItem());
        menuRoot.setSouth(new DragMenuItem());
        menuRoot.getSouth().label = "!";
        menuRoot.setWest(new DragMenuItem());
    }

}
