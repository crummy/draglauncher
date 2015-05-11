package com.malcolmcrum.draglauncher;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.widget.Toast;

public class DragLauncher extends Activity implements MenuListener {

    DragMenu menu;
    DragView dragView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        menu = new DragMenu();
        menu.addListener(this);

        dragView = new DragView(this, menu);
        setContentView(dragView);
        loadIcons(menu.getRoot());
        dragView.loadIcon("Root", R.drawable.four_way_arrow);
        dragView.loadIcon("Unlock", R.drawable.unlock);
    }

    private void loadIcons(DragMenuItem item) {
        if (item == null) return;

        dragView.loadIcon(item.getName());

        loadIcons(item.getChild(GestureManager.Direction.east));
        loadIcons(item.getChild(GestureManager.Direction.west));
        loadIcons(item.getChild(GestureManager.Direction.south));
        loadIcons(item.getChild(GestureManager.Direction.north));
    }

    @Override
    public void itemSelected(DragMenuItem item) {
        if (item == null) throw new AssertionError("itemSelected called with null item");

        Toast toast = Toast.makeText(this, item.getName(), Toast.LENGTH_SHORT);
        toast.show();
        item.selectItem();
    }

    @Override
    public void gestureChanged() {
        Vibrator vibe = (Vibrator)this.getSystemService(Context.VIBRATOR_SERVICE);
        int vibrateDurationMs = getResources().getInteger(R.integer.highlight_vibrate_ms);
        vibe.vibrate(vibrateDurationMs);
    }
}
