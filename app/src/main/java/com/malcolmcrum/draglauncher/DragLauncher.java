package com.malcolmcrum.draglauncher;

import android.app.Activity;
import android.os.Bundle;


public class DragLauncher extends Activity {

    DragMenu menu;
    DragView dragView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        menu = new DragMenu();

        dragView = new DragView(this, menu);
        loadIcons(menu.getRoot());
        setContentView(dragView);
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
