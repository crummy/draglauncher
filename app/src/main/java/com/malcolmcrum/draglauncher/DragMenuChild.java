package com.malcolmcrum.draglauncher;

import android.gesture.Gesture;

/**
 * Child of a DragMenuItem.
 * A DragMenuChild may only have two children - out to the sides of the gesture. As a result,
 * we only store "left" and "right" (though these names are just for storage, their direction may
 * be different). Telling which way they should be drawn (relative to their parent) involves
 * examining directionFromParent.
 * Created by Malcolm on 4/27/2015.
 */
public class DragMenuChild implements DragMenuItem {
    private DragMenuChild left;
    private DragMenuChild right;
    private String packageName;
    protected GestureManager.Direction directionFromParent;

    public DragMenuChild(String name) {
        packageName = name;
    }

    public DragMenuItem getChild(GestureManager.Direction direction) {
        DragMenuChild child = null;
        switch (direction) {
            case north:
            case east:
                if (left != null && left.directionFromParent == direction) child = left;
                break;
            case south:
            case west:
                if (right != null && right.directionFromParent == direction) child = right;
                break;
        }
        return child;
    }

    public DragMenuItem setChild(GestureManager.Direction direction, String name) {
        DragMenuChild child = new DragMenuChild(name);
        child.directionFromParent = direction;
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
