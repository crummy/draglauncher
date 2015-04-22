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
        menuRoot.packageName = "com.malcolmcrum.draglauncher";
        menuRoot.setNorth(new DragMenuItem());
        menuRoot.getNorth().packageName = "com.android.chrome";
        menuRoot.getNorth().setNorth(new DragMenuItem());
        menuRoot.getNorth().getNorth().packageName = "com.google.android.youtube";
        menuRoot.getNorth().setWest(new DragMenuItem());
        menuRoot.getNorth().getWest().packageName = "com.google.android.apps.inbox";
        menuRoot.getNorth().getWest().setNorth(new DragMenuItem());
        menuRoot.getNorth().getWest().getNorth().packageName = "com.facebook.katana";
        menuRoot.getNorth().getWest().setSouth(new DragMenuItem());
        menuRoot.getNorth().getWest().getSouth().packageName = "com.twitter.android";
        menuRoot.getNorth().setEast(new DragMenuItem());
        menuRoot.getNorth().getEast().packageName = "com.google.android.talk";
        menuRoot.setEast(new DragMenuItem());
        menuRoot.getEast().packageName = "com.google.android.music";
        menuRoot.setSouth(new DragMenuItem());
        menuRoot.getSouth().packageName = "!";
        menuRoot.setWest(new DragMenuItem());
        menuRoot.getWest().packageName = "com.google.android.apps.maps";
    }

}
