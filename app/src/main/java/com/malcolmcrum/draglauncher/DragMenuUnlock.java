package com.malcolmcrum.draglauncher;

import android.content.res.Resources;

/**
 * Single use class - handles unlocking the phone, no children, just a special selectItem action.
 * Created by Malcolm on 4/27/2015.
 */
public class DragMenuUnlock implements DragMenuItem {

    private final DragMenuChild editItem;
    private final DragMenuChild settingsItem;
    private final String name;

    public DragMenuUnlock(Resources resources) {
        name = resources.getString(R.string.item_unlock);
        editItem = new DragMenuChild(resources.getString(R.string.item_edit));
        editItem.isEditable = false;
        editItem.directionFromParent = GestureManager.Direction.west;
        settingsItem = new DragMenuChild(resources.getString(R.string.item_settings));
        settingsItem.isEditable = false;
        settingsItem.directionFromParent = GestureManager.Direction.east;
    }

    public DragMenuItem setChild(GestureManager.Direction direction, String name) {
        throw new AssertionError("Not allowed to set child of DragMenuUnlock.");
    }

    public DragMenuItem getChild(GestureManager.Direction direction) {
        switch (direction) {
            case east:
                return settingsItem;
            case west:
                return editItem;
        }
        return null;
    }

    public void selectItem() {
        // unlock
    }

    public String getName() {
        return "Unlock";
    }

    public boolean isEditable() {
        return false;
    }
}
