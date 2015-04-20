package com.malcolmcrum.draglauncher;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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
    public void onDraw(Canvas canvas) {

        int width = this.getWidth();
        int height = this.getHeight();
        center = new Point(width/2, height/2); // TODO: make this work out of the draw loop
        drawItem(canvas, center.x, center.y, menu);
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                DragMenuItem selectedItem = itemAtCoords(event.getX(), event.getY(), menu);
                if (selectedItem != null) {
                    selectedItem.select();
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                menu.deselect();
                invalidate();
                break;
        }
        return true;
    }

    private DragMenuItem itemAtCoords(float x, float y, DragMenuItem root) {
        if (rectContainsPoint(root.area, x, y)) {
            return root;
        }
        else {
            return null;
        }
    }

    private boolean rectContainsPoint(Rect rect, float x, float y) {
        return (x > rect.left) && (x < rect.right) && (y > rect.top) && (y < rect.bottom);
    }

    private void drawItem(Canvas canvas, int x, int y, DragMenuItem item) {
        item.area = new Rect(x - item.getSize() / 2, y - item.getSize() / 2, x + item.getSize() / 2, y + item.getSize() / 2);
        canvas.drawRect(item.area, item.getBackgroundPaint());
        canvas.drawText(item.label, x, y, item.getTextPaint());

        if (item.north != null) drawItem(canvas, x, y - item.getSpacing(), item.north);
        if (item.east != null) drawItem(canvas, x + item.getSpacing(), y, item.east);
        if (item.south != null) drawItem(canvas, x, y + item.getSpacing(), item.south);
        if (item.west != null) drawItem(canvas, x - item.getSpacing(), y, item.west);
    }
}
