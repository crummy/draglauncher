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
        menuRoot.label = "GO";
        menuRoot.setNorth(new DragMenuItem());
        menuRoot.getNorth().label = "WEB";
        menuRoot.getNorth().setNorth(new DragMenuItem());
        menuRoot.getNorth().getNorth().label = "CHROME";
        menuRoot.getNorth().setWest(new DragMenuItem());
        menuRoot.getNorth().getWest().label = "SOCIAL";
        menuRoot.getNorth().getWest().setNorth(new DragMenuItem());
        menuRoot.getNorth().getWest().getNorth().label = "FACE";
        menuRoot.getNorth().getWest().setSouth(new DragMenuItem());
        menuRoot.getNorth().getWest().getSouth().label = "TWTR";
        menuRoot.getNorth().setEast(new DragMenuItem());
        menuRoot.getNorth().getEast().label = "H OUTS";
        menuRoot.setEast(new DragMenuItem());
        menuRoot.getEast().label = "PHONE";
        menuRoot.setSouth(new DragMenuItem());
        menuRoot.getSouth().label = "!";
        menuRoot.setWest(new DragMenuItem());
        menuRoot.getWest().label = "MAPS";
    }

}
