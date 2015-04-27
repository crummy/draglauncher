package com.malcolmcrum.draglauncher;

/**
 * Single use class - handles unlocking the phone, no children, just a special selectItem action.
 * Created by Malcolm on 4/27/2015.
 */
public class DragMenuUnlock implements DragMenuItem {

    public DragMenuUnlock() {

    }

    public DragMenuItem setChild(GestureManager.Direction direction, String name) {
        throw new AssertionError("Not allowed to set child of DragMenuUnlock.");
    }

    public DragMenuItem getChild(GestureManager.Direction direction) {
        return null;
    }

    public void selectItem() {
        // unlock
    }

    public String getName() {
        return "Unlock";
    }
}
