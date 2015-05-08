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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Canvas for drawing launcher. Input handed off to GestureManager
 * TODO: Split this off into multiple subviews
 * Created by Malcolm on 4/19/2015.
 */
public class DragView extends View {

    private final GestureManager gestureManager = new GestureManager();
    private final Paint gesturePaint = new Paint();
    private final Paint touchPaint = new Paint();
    private Drawable launcherIcon;
    private DragMenu menu;
    private Map<String, Drawable> icons = new HashMap<>();
    private int iconSize = 256; // TODO: Make this dependent on screen size
    private int childIconSize = 128; // TODO: Make this dependent on screen size
    private int childIconDistance = 256; // TODO: Make this dependent on screen size

    public DragView(Context context) {
        // TODO: This constructor should never be used, but triggers a warning if it's missing. Fix?
        super(context);
    }

    public DragView(DragLauncher context, DragMenu menu) {
        super(context);

        this.menu = menu;

        gestureManager.addListener(menu);

        gesturePaint.setColor(Color.GRAY);
        touchPaint.setColor(Color.WHITE);
        touchPaint.setStrokeWidth(8);

        try {
            launcherIcon = context.getPackageManager().getApplicationIcon("com.malcolmcrum.draglauncher");
        } catch (PackageManager.NameNotFoundException e) {
            launcherIcon = null;
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (gestureManager.isGesturing() && menu.getCurrent() != null) {
            drawPoints(canvas);
            drawCurrentSelection(canvas);
            drawGestureRects(canvas);
        } else if (gestureManager.isTouching() && menu.getCurrent() != null) {
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
        List<Point> points = gestureManager.getTouchHistory();
        for (int pointIndex = 1; pointIndex < points.size(); ++pointIndex) {
            Point lineStart = points.get(pointIndex-1);
            Point lineEnd = points.get(pointIndex);
            canvas.drawLine(lineStart.x, lineStart.y, lineEnd.x, lineEnd.y, touchPaint);
        }
    }

    private void drawGestureRects(Canvas canvas) {
        int size = 32;
        List<GestureManager.Gesture> history = gestureManager.getGestureHistory();

        // draw squares for each gesture. debug purposes only
        for (GestureManager.Gesture gesture : history) {
            Rect rect = new Rect(gesture.startPosition.x - size, gesture.startPosition.y - size, gesture.startPosition.x + size, gesture.startPosition.y + size);
            canvas.drawRect(rect, touchPaint);
        }

        // draw previous gestures
        for (int gestureIndex = 0; gestureIndex < history.size() - 1; ++gestureIndex) {
            GestureManager.Gesture gesture = history.get(gestureIndex);
            GestureManager.Gesture nextGesture = history.get(gestureIndex + 1);
            Rect rect = rectBetweenPoints(gesture.startPosition, nextGesture.startPosition, gesture.direction);
            canvas.drawRect(rect, gesturePaint);
        }

        // draw current gesture
        if (!history.isEmpty()) {
            GestureManager.Gesture lastGesture = history.get(history.size() - 1);
            Rect rect = rectBetweenPoints(lastGesture.startPosition, gestureManager.currentTouchLocation(), lastGesture.direction);
            canvas.drawRect(rect, gesturePaint);
        }
    }

    private Rect rectBetweenPoints(Point start, Point end, GestureManager.Direction direction) {
        int padding = 64;
        int left = start.x - padding;
        int right = start.x + padding;
        int bottom = start.y + padding;
        int top = start.y - padding;
        switch (direction) {
            case north:
                top = end.y - padding;
                break;
            case south:
                bottom = end.y + padding;
                break;
            case east:
                right = end.x + padding;
                break;
            case west:
                left = end.x - padding;
                break;
        }
        return new Rect(left, top, right, bottom);
    }

    private void drawAppIcon(Canvas canvas) {
        drawIcon(launcherIcon, canvas.getWidth()/2, 3*canvas.getHeight()/4, iconSize, canvas);
    }

    private void drawCurrentSelection(Canvas canvas) {
        Point touchPoint = gestureManager.currentTouchLocation();
        if (touchPoint == null) throw new AssertionError("Tried to draw selection but could not find touchPoint");

        DragMenuItem selectedItem = menu.getCurrent();
        if (selectedItem == null) throw new AssertionError("Tried to draw selection but nothing is selected");
        Drawable selectedIcon = icons.get(selectedItem.getName());

        Point iconCenter = new Point(canvas.getWidth()/2, 3*canvas.getHeight()/4);
        List<GestureManager.Gesture> gestureHistory = gestureManager.getGestureHistory();

        if (!gestureHistory.isEmpty()) {
            GestureManager.Gesture lastGesture = gestureHistory.get(gestureHistory.size() - 1);

            switch (lastGesture.direction) {
                case north:
                case south:
                    iconCenter.set(lastGesture.startPosition.x, touchPoint.y);
                    break;
                case east:
                case west:
                    iconCenter.set(touchPoint.x, lastGesture.startPosition.y);
                    break;
            }
        }

        drawIcon(selectedIcon, iconCenter.x, iconCenter.y, iconSize, canvas);

        DragMenuItem northChild = selectedItem.getChild(GestureManager.Direction.north);
        if (northChild != null) {
            Drawable northIcon = icons.get(northChild.getName());
            drawIcon(northIcon, iconCenter.x, iconCenter.y - childIconDistance, childIconSize, canvas);
        }

        DragMenuItem southChild = selectedItem.getChild(GestureManager.Direction.south);
        if (southChild != null) {
            Drawable southIcon = icons.get(southChild.getName());
            drawIcon(southIcon, iconCenter.x, iconCenter.y + childIconDistance, childIconSize, canvas);
        }

        DragMenuItem westChild = selectedItem.getChild(GestureManager.Direction.west);
        if (westChild != null) {
            Drawable westIcon = icons.get(westChild.getName());
            drawIcon(westIcon, iconCenter.x - childIconDistance, iconCenter.y, childIconSize, canvas);
        }

        DragMenuItem eastChild = selectedItem.getChild(GestureManager.Direction.east);
        if (eastChild != null) {
            Drawable eastIcon = icons.get(eastChild.getName());
            drawIcon(eastIcon, iconCenter.x + childIconDistance, iconCenter.y, childIconSize, canvas);
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
