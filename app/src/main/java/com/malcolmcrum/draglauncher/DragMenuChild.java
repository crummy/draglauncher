package com.malcolmcrum.draglauncher;

/**
 * Child of a DragMenuItem
 * Created by Malcolm on 4/27/2015.
 */
public class DragMenuChild implements DragMenuItem {
    private DragMenuChild left;
    private DragMenuChild right;
    private String packageName;

    public DragMenuChild(String name) {
        packageName = name;
    }

    public DragMenuItem getChild(GestureManager.Direction direction) {
        DragMenuChild child = null;
        switch (direction) {
            case north:
            case east:
                child = left;
                break;
            case south:
            case west:
                child = right;
                break;
        }
        return child;
    }

    public DragMenuItem setChild(GestureManager.Direction direction, String name) {
        DragMenuChild child = new DragMenuChild(name);
        switch (direction) {
            case north:
            case east:
                left = child;
                break;
            case south:
            case west:
                right = child;
                break;
        }
        return child;
    }

    public void selectItem() {
        // run program
    }

    public String getName() {
        return packageName;
    }
}
