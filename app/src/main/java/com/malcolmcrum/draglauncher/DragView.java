package com.malcolmcrum.draglauncher;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;

/**
 * Canvas for drawing launcher
 * Created by Malcolm on 4/19/2015.
 */
public class DragView extends View {

    private Point center;
    private DragMenuItem menu;

    public DragView(Context context) {
        this(context, null);
    }

    public DragView(Context context, DragMenuItem menuRoot) {
        super(context);
        menu = menuRoot;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (changed) {
            int width = right - left;
            int height = bottom - top;

            layoutMenuItem(menu, width/2, height/2);
        }
    }

    private void layoutMenuItem(DragMenuItem item, int x, int y) {
        item.area = new Rect(x - item.getSize() / 2, y - item.getSize() / 2, x + item.getSize() / 2, y + item.getSize() / 2);

        if (item.north != null) layoutMenuItem(item.north, x, y - item.getSpacing());
        if (item.east != null) layoutMenuItem(item.east, x + item.getSpacing(), y);
        if (item.south != null) layoutMenuItem(item.south, x, y + item.getSpacing());
        if (item.west != null) layoutMenuItem(item.west, x - item.getSpacing(), y);
    }

    @Override
    public void onDraw(Canvas canvas) {
        drawItem(canvas, menu);
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                DragMenuItem selectedItem = itemAtCoords(event.getX(), event.getY(), menu);
                if (selectedItem != null) {
                    selectedItem.select();
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                menu.deselect();
                invalidate();
                break;
        }
        return true;
    }

    private DragMenuItem itemAtCoords(float x, float y, DragMenuItem root) {
        // Not my finest work.
        // TODO: Combine this and probably rectContainsPoint into a nicer recursive function.
        if (root == null) {
            return null;
        } else if (rectContainsPoint(root.area, x, y)) {
            return root;
        } else if (root.isSelected()) {
            DragMenuItem north = itemAtCoords(x, y, root.north);
            DragMenuItem east = itemAtCoords(x, y, root.east);
            DragMenuItem south = itemAtCoords(x, y, root.south);
            DragMenuItem west = itemAtCoords(x, y, root.west);
            if (north != null) return north;
            if (east != null) return east;
            if (south != null) return south;
            if (west != null) return west;
        }
        return null;

    }

    private boolean rectContainsPoint(Rect rect, float x, float y) {
        return (x > rect.left) && (x < rect.right) && (y > rect.top) && (y < rect.bottom);
    }

    private void drawItem(Canvas canvas, DragMenuItem item) {
        canvas.drawRect(item.area, item.getBackgroundPaint());
        //canvas.drawText(item.label, item.getTextPaint());

        if (item.isSelected()) {
            if (item.north != null) drawItem(canvas, item.north);
            if (item.east != null) drawItem(canvas, item.east);
            if (item.south != null) drawItem(canvas, item.south);
            if (item.west != null) drawItem(canvas, item.west);
        }
    }
}
