package com.malcolmcrum.draglauncher;

import android.graphics.Point;
import android.util.Log;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages input and translates it into "gestures"
 * Created by Malcolm on 4/23/2015.
 */
public class GestureManager {
    public enum Direction {north, east, south, west}
    private Direction lastDirection;

    private final List<GestureListener> listeners = new ArrayList<>();
    private final List<Point> touchHistory = new ArrayList<>();
    private final int dragIgnoreAmount = 128; // TODO: Size this relative to screen res
    private Point lastGesturePoint;

    public GestureManager() {
        // init
    }

    public boolean handleInput(MotionEvent input) {
        boolean handledGesture = false;
        switch (input.getAction()) {
            case MotionEvent.ACTION_DOWN:
                pressed(input.getX(), input.getY());
                handledGesture = true; // Just so we get the later events.
                break;
            case MotionEvent.ACTION_UP:
                released();
                handledGesture = true;
                break;
            case MotionEvent.ACTION_MOVE:
                movedTo(input.getX(), input.getY());
                handledGesture = true;
                break;

        }
        return handledGesture;
    }

    public List<Point> getHistory() {
        return touchHistory;
    }

    public void addListener(GestureListener listener) {
        listeners.add(listener);
    }

    private double distanceBetween(Point p1, Point p2) {
        int dx = p2.x - p1.x;
        int dy = p2.y - p1.x;
        return Math.sqrt(dx * dx + dy * dy);
    }

    private Direction direction(Point from, Point to) {
        int dx = to.x - from.x;
        int dy = to.y - from.y;

        if (Math.abs(dx) > Math.abs(dy)) {
            if (dx > 0) return Direction.east;
            else return Direction.west;
        } else {
            if (dy > 0) return Direction.south;
            else return Direction.north;
        }
    }

    private void movedTo(float x, float y) {
        assert lastGesturePoint != null;

        Point touched = new Point((int)x, (int)y);
        if (distanceBetween(touched, lastGesturePoint) > dragIgnoreAmount) {
            try {
                Point lastTouched = touchHistory.get(touchHistory.size() - 1);
                Direction newDirection = direction(lastTouched, touched);
                if (newDirection != lastDirection) changeDirection(newDirection);
            } catch (IndexOutOfBoundsException e) {
                Log.d("gesture", "Tried to get previous element in empty touchHistory");
            }
        }

        touchHistory.add(touched); // TODO: Limit size of touchHistory
    }

    private void changeDirection(Direction newDirection) {
        for (GestureListener listener : listeners) {
            listener.gestureChanged(newDirection);
        }
        lastDirection = newDirection;
        lastGesturePoint = touchHistory.get(touchHistory.size() - 1);
    }

    private void pressed(float x, float y) {
        lastGesturePoint = new Point((int)x, (int)y);
    }

    private void released() {
        lastGesturePoint = null;
        lastDirection = null;
        touchHistory.clear();
        for (GestureListener listener : listeners) {
            listener.gestureFinished();
        }
    }
}
