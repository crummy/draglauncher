package com.malcolmcrum.draglauncher;

import android.graphics.Point;
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
    private final List<Direction> directionHistory = new ArrayList<>();
    private final int minNewDirectionCount = 3;

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

    public boolean isGesturing() {
        return isTouching() && !directionHistory.isEmpty();
    }

    public boolean isTouching() {
        return !touchHistory.isEmpty();
    }

    public Point touchLocation() {
        if (touchHistory.isEmpty()) {
            return null;
        } else {
            return touchHistory.get(touchHistory.size() - 1);
        }
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
        if (touchHistory.isEmpty()) throw new AssertionError("movedTo called but touchHistory is empty: has pressed() not been called?");

        Point touched = new Point((int)x, (int)y);
        Point lastTouched = touchHistory.get(touchHistory.size() - 1);
        touchHistory.add(touched); // TODO: Limit size of touchHistory

        Direction newDirection = direction(lastTouched, touched);

        // Detecting a new gesture requires a certain amount of consistent directions being detected.
        if (newDirection != lastDirection && directionHistory.size() > minNewDirectionCount + 1) {
            boolean newGestureDetected = true;
            for (Direction previousDirection : directionHistory.subList(directionHistory.size() - minNewDirectionCount - 1, directionHistory.size() - 1)) {
                if (previousDirection != newDirection) newGestureDetected = false;
            }
            if (newGestureDetected) {
                changeDirection(newDirection);
            }
        }

        directionHistory.add(newDirection);
    }

    private void changeDirection(Direction newDirection) {
        for (GestureListener listener : listeners) {
            listener.gestureChanged(newDirection);
        }
        lastDirection = newDirection;
    }

    private void pressed(float x, float y) {
        Point touchPosition = new Point((int)x, (int)y);
        touchHistory.add(touchPosition);
        for (GestureListener listener : listeners) {
            listener.gestureStarted();
        }
    }

    private void released() {
        for (GestureListener listener : listeners) {
            listener.gestureFinished();
        }
        lastDirection = null;
        touchHistory.clear();
    }
}
