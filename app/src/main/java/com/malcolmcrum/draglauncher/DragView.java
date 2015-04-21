package com.malcolmcrum.draglauncher;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;

/**
 * Canvas for drawing launcher. Handles input, too.
 *
 * Created by Malcolm on 4/19/2015.
 */
public class DragView extends View {

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

            menu.layout(width/2, height/2);
        }
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
                    selectedItem.deselectChildren();
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
        // Maybe put collision detection code into DragMenuItemView equivalent?
        if (root == null) {
            return null;
        } else if (rectContainsPoint(root.getRectForTouching(), x, y)) {
            return root;
        } else if (root.isVisible()) {
            DragMenuItem north = itemAtCoords(x, y, root.getNorth());
            DragMenuItem east = itemAtCoords(x, y, root.getEast());
            DragMenuItem south = itemAtCoords(x, y, root.getSouth());
            DragMenuItem west = itemAtCoords(x, y, root.getWest());
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
        if (item == null) return;
        canvas.drawRect(item.getRectForDrawing(), item.getBackgroundPaint());
        //canvas.drawText(item.label, item.getTextPaint());

        if (item.getNorth() != null && item.getNorth().isVisible()) drawItem(canvas, item.getNorth());
        if (item.getEast() != null && item.getEast().isVisible()) drawItem(canvas, item.getEast());
        if (item.getSouth() != null && item.getSouth().isVisible()) drawItem(canvas, item.getSouth());
        if (item.getWest() != null && item.getWest().isVisible()) drawItem(canvas, item.getWest());
    }
}
