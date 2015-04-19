package com.malcolmcrum.draglauncher;

import android.app.Activity;
import android.opengl.Visibility;
import android.os.Bundle;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.content.Context;
import android.widget.RelativeLayout;


public class DragLauncher extends Activity implements View.OnTouchListener {

    DragMenu menu;
    RelativeLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        layout = new RelativeLayout(this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        setContentView(layout, params);

        menu = new DragMenu();
        drawMenu(menu.getRoot(), null, null);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && v instanceof DragMenuItemView) {
            ((DragMenuItemView) v).pressed();
            return true;
        } else if (event.getAction() == MotionEvent.ACTION_UP && v instanceof DragMenuItemView) {
            ((DragMenuItemView) v).released();
            return true;
        }
        return false;
    }

    private DragMenuItemView drawMenu(DragMenu.Item item, DragMenu.Item parent, DragMenuItemView parentsView) {
        DragMenuItemView view = new DragMenuItemView(this, item);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(view.getSize(), view.getSize());
        int margin = view.getSpacing();
        params.setMargins(margin, margin, margin, margin);
        view.hide();
        view.setOnTouchListener(this);

        // draw cell, in relation to parent
        if (parent == null) {
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            view.setBackgroundColor(Color.BLUE);
            view.show();
        } else if (parent.north == item) {
            params.addRule(RelativeLayout.ABOVE, parentsView.getId());
            params.addRule(RelativeLayout.CENTER_HORIZONTAL);
            view.setBackgroundColor(Color.GREEN);
        } else if (parent.east == item) {
            params.addRule(RelativeLayout.RIGHT_OF, parentsView.getId());
            params.addRule(RelativeLayout.CENTER_VERTICAL);
            view.setBackgroundColor(Color.RED);
        } else if (parent.south == item) {
            params.addRule(RelativeLayout.BELOW, parentsView.getId());
            params.addRule(RelativeLayout.CENTER_HORIZONTAL);
            view.setBackgroundColor(Color.YELLOW);
        } else if (parent.west == item) {
            params.addRule(RelativeLayout.LEFT_OF, parentsView.getId());
            params.addRule(RelativeLayout.CENTER_VERTICAL);
            view.setBackgroundColor(Color.MAGENTA);
        }



        if (item.north != null) view.north = drawMenu(item.north, item, view);
        if (item.east != null) view.east = drawMenu(item.east, item, view);
        if (item.south != null) view.south = drawMenu(item.south, item, view);
        if (item.west != null) view.west = drawMenu(item.west, item, view);

        layout.addView(view, params);

        return view;
    }

}
