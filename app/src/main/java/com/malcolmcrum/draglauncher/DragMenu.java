package com.malcolmcrum.draglauncher;

/**
 * Menu object
 * Created by Malcolm Crum on 3/22/2015.
 */
public class DragMenu {
    public class Item {
        public Item east = null;
        public Item west = null;
        public Item north = null;
        public Item south = null;
        public String label = "?!";
        private boolean selected = false;

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

    private Item root;

    public DragMenu() {
        root = new Item();
        root.east = new Item();
        root.north = new Item();
        root.north.north = new Item();
        root.west = new Item();
        root.south = new Item();
        root.label = ":)";
        root.selected = true;
    }

    public Item getRoot() {
        return root;
    }
}