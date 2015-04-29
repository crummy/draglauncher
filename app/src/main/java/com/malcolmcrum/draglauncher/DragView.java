package com.malcolmcrum.draglauncher;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;

import junit.framework.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Canvas for drawing launcher. Input handed off to GestureManager
 * Created by Malcolm on 4/19/2015.
 */
public class DragView extends View {

    private final GestureManager gestureManager = new GestureManager();
    private final Paint gesturePaint = new Paint();
    private Drawable launcherIcon;
    private DragMenu menu;
    private Map<String, Drawable> icons = new HashMap<>();
    private int iconSize = 256; // TODO: Make this dependent on screen size
    private int childIconSize = 128; // TODO: Make this dependent on screen size
    private int childIconDistance = 512; // TODO: ake this dependent on screen size

    public DragView(Context context) {
        // TODO: This constructor should never be used, but triggers a warning if it's missing. Fix?
        super(context);
    }

    public DragView(DragLauncher context, DragMenu menu) {
        super(context);

        this.menu = menu;

        gestureManager.addListener(menu);

        gesturePaint.setColor(Color.BLACK);
        gesturePaint.setStrokeWidth(4);

        try {
            launcherIcon = context.getPackageManager().getApplicationIcon("com.malcolmcrum.draglauncher");
        } catch (PackageManager.NameNotFoundException e) {
            launcherIcon = null;
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (gestureManager.isGesturing()) {
            drawPoints(canvas);
            drawCurrentSelection(canvas);
        } else if (gestureManager.isTouching()) {
            drawCurrentSelection(canvas);
            drawAppIcon(canvas);
        } else {
            drawAppIcon(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        boolean handledGesture = gestureManager.handleInput(event);
        if (handledGesture) {
            invalidate();
        }
        return handledGesture;
    }

    public void loadIcon(String name) {
        try {
            icons.put(name, getContext().getPackageManager().getApplicationIcon(name));
        } catch (PackageManager.NameNotFoundException e) {
            icons.put(name, null);
        }
    }

    private void drawPoints(Canvas canvas) {
        List<Point> points = gestureManager.getHistory();
        for (int pointIndex = 1; pointIndex < points.size(); pointIndex++) {
            Point lineStart = points.get(pointIndex-1);
            Point lineEnd = points.get(pointIndex);
            canvas.drawLine(lineStart.x, lineStart.y, lineEnd.x, lineEnd.y, gesturePaint);
        }
    }

    private void drawAppIcon(Canvas canvas) {
        drawIcon(launcherIcon, canvas.getWidth()/2, 3*canvas.getHeight()/4, iconSize, canvas);
    }

    private void drawCurrentSelection(Canvas canvas) {
        Point touchPoint = gestureManager.touchLocation();
        if (touchPoint == null) throw new AssertionError("Tried to draw selection but could not find touchPoint");

        DragMenuItem selectedItem = menu.getCurrent();
        if (selectedItem == null) throw new AssertionError("Tried to draw selection but nothing is selected");
        Drawable selectedIcon = icons.get(selectedItem.getName());

        drawIcon(selectedIcon, touchPoint.x, touchPoint.y, iconSize, canvas);

        DragMenuItem northChild = selectedItem.getChild(GestureManager.Direction.north);
        if (northChild != null) {
            Drawable northIcon = icons.get(northChild.getName());
            drawIcon(northIcon, touchPoint.x, touchPoint.y - childIconDistance, childIconSize, canvas);
        }

        DragMenuItem southChild = selectedItem.getChild(GestureManager.Direction.south);
        if (southChild != null) {
            Drawable southIcon = icons.get(southChild.getName());
            drawIcon(southIcon, touchPoint.x, touchPoint.y + childIconDistance, childIconSize, canvas);
        }

        DragMenuItem westChild = selectedItem.getChild(GestureManager.Direction.west);
        if (westChild != null) {
            Drawable westIcon = icons.get(westChild.getName());
            drawIcon(westIcon, touchPoint.x - childIconDistance, touchPoint.y, childIconSize, canvas);
        }

        DragMenuItem eastChild = selectedItem.getChild(GestureManager.Direction.east);
        if (eastChild != null) {
            Drawable eastIcon = icons.get(eastChild.getName());
            drawIcon(eastIcon, touchPoint.x - childIconDistance, touchPoint.y, childIconSize, canvas);
        }
    }

    // Simple helper function - returns a square of a given size centered on a point.
    private void drawIcon(Drawable icon, int x, int y, int size, Canvas canvas) {
        if (icon == null) return;

        Rect rect = new Rect(x - size/2, y - size/2, x + size/2, y + size/2);
        icon.setBounds(rect);
        icon.draw(canvas);
    }

}
