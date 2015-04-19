package com.malcolmcrum.draglauncher;

/**
 * Menu object
 * Created by Malcolm Crum on 3/22/2015.
 */
public class DragMenuItem {
    // Connections should be one way only!!
    public DragMenuItem east = null;
    public DragMenuItem west = null;
    public DragMenuItem north = null;
    public DragMenuItem south = null;
    public String label = "?!";
    private boolean selected = false;
    private boolean root;

    public DragMenuItem() {
        this(false);
    }

    public DragMenuItem(boolean isRoot) {
        root = isRoot;
    }

    public boolean isSelected() {
        return selected;
    }

    public void select() {
        selected = true;
    }

    public void deselect() {
        selected = false;
    }

}