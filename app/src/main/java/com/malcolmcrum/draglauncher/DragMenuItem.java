package com.malcolmcrum.draglauncher;

/**
 * Interface for all menu items
 * Created by Malcolm on 4/27/2015.
 */
public interface DragMenuItem {
    DragMenuItem getChild(GestureManager.Direction direction);
    DragMenuItem setChild(GestureManager.Direction direction, String name);
    void selectItem(); // TODO: Consider what logic goes in here - remove entirely?
    String getName();
}
