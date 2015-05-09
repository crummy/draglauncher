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
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import org.apache.http.MethodNotSupportedException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Canvas for drawing launcher. Input handed off to GestureManager
 * TODO: Split this off into multiple subviews
 * Created by Malcolm on 4/19/2015.
 */
public class DragView extends View {

    private final GestureManager gestureManager;
    private final Point dragStartPoint;
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
        this.gestureManager = new GestureManager(new Point(0,0));
        throw new AssertionError("Calling an unused constructor");
    }

    public DragView(DragLauncher context, DragMenu menu) {
        super(context);

        this.menu = menu;

        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        dragStartPoint = new Point(dm.widthPixels/2, 2*dm.heightPixels/3);

        gestureManager = new GestureManager(dragStartPoint);
        gestureManager.addListener(menu);

        gesturePaint.setColor(Color.WHITE);
        touchPaint.setColor(Color.RED);
        touchPaint.setStrokeWidth(2);

        try {
            launcherIcon = context.getPackageManager().getApplicationIcon("com.malcolmcrum.draglauncher");
        } catch (PackageManager.NameNotFoundException e) {
            launcherIcon = null;
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (gestureManager.isGesturing() && menu.getCurrent() != null) {
            drawGestureRects(canvas);
            drawCurrentSelection(canvas);
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

    private void drawGestureRects(Canvas canvas) {
        List<GestureManager.Gesture> history = gestureManager.getGestureHistory();

        // draw squares for each gesture. debug purposes only
        int size = 8;
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
            Rect rect = rectBetweenPoints(lastGesture.startPosition, gestureManager.getTouchLocation(), lastGesture.direction);
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
        drawIcon(launcherIcon, dragStartPoint.x, dragStartPoint.y, iconSize, canvas);
    }

    private void drawCurrentSelection(Canvas canvas) {
        Point touchPoint = gestureManager.getTouchLocation();
        if (touchPoint == null) throw new AssertionError("Tried to draw selection but could not find touchPoint");

        DragMenuItem selectedItem = menu.getCurrent();
        if (selectedItem == null) throw new AssertionError("Tried to draw selection but nothing is selected");
        Drawable selectedIcon = icons.get(selectedItem.getName());

        Point iconCenter = dragStartPoint;
        List<GestureManager.Gesture> gestureHistory = gestureManager.getGestureHistory();

        if (!gestureHistory.isEmpty()) {
            GestureManager.Gesture lastGesture = gestureHistory.get(gestureHistory.size() - 1);

            switch (lastGesture.direction) {
                case north:
                case south:
                    iconCenter = new Point(lastGesture.startPosition.x, touchPoint.y);
                    break;
                case east:
                case west:
                    iconCenter = new Point(touchPoint.x, lastGesture.startPosition.y);
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
