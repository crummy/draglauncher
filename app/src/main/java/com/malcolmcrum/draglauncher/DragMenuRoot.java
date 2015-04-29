package com.malcolmcrum.draglauncher;

/**
 * Menu object
 * Created by Malcolm Crum on 3/22/2015.
 */
public class DragMenuRoot implements DragMenuItem {
    private DragMenuChild north;
    private DragMenuChild east;
    private DragMenuChild west;
    private DragMenuUnlock unlock;

    public DragMenuRoot() {
        unlock = new DragMenuUnlock();
    }

    public DragMenuItem getChild(GestureManager.Direction direction) {
        DragMenuItem requestedChild = null;
        switch (direction) {
            case north:
                requestedChild = north;
                break;
            case east:
                requestedChild = east;
                break;
            case south:
                requestedChild = unlock;
                break;
            case west:
                requestedChild = west;
                break;
        }
        return requestedChild;
    }

    public DragMenuItem setChild(GestureManager.Direction direction, String name) {
        DragMenuChild child = new DragMenuChild(name);
        child.directionFromParent = direction;
        switch (direction) {
            case north:
                north = child;
                break;
            case east:
                east = child;
                break;
            case south:
                throw new AssertionError("Not allowed to setChild(south) of root (that's for unlocking)");
                // break; commented to silence unaccessible statement warning
            case west:
                west = child;
                break;
        }
        return child;
    }

    public void selectItem() {
        // nothing happens if you select the root.
    }

    public String getName() {
        return "Root";
    }

}