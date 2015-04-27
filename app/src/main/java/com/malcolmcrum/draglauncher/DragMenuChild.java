package com.malcolmcrum.draglauncher;

/**
 * Child of a DragMenuItem
 * Created by Malcolm on 4/27/2015.
 */
public class DragMenuChild implements DragMenuItem {
    private String packageName;

    public DragMenuChild(String name) {
        packageName = name;
    }

    public void selectItem() {
        // run program
    }

    public String getName() {
        return packageName;
    }
}
