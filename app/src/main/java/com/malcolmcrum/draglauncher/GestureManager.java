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
    public class Gesture {
        public Gesture(Direction d, Point p) {
            direction = d;
            startPosition = p;
        }
        final Direction direction;
        final Point startPosition;
    }

    private final List<GestureListener> listeners = new ArrayList<>();

    private final List<Point> touchHistory = new ArrayList<>();
    private final List<Gesture> gestureHistory = new ArrayList<>();
    private final int minGestureDetectionDistance = 128; // TODO: Make this resolution independent

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

    public List<Point> getTouchHistory() {
        return touchHistory;
    }

    public List<Gesture> getGestureHistory() {
        return gestureHistory;
    }

    public boolean isGesturing() {
        return !gestureHistory.isEmpty();
    }

    public boolean isTouching() {
        return !touchHistory.isEmpty();
    }

    public Point currentTouchLocation() {
        if (touchHistory.isEmpty()) {
            return null;
        } else {
            return touchHistory.get(touchHistory.size() - 1);
        }
    }

    public void addListener(GestureListener listener) {
        listeners.add(listener);
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

        Point touchPosition = new Point((int)x, (int)y);
        Point lastTouchPosition = touchHistory.get(touchHistory.size() - 1);
        touchHistory.add(touchPosition); // TODO: Limit size of touchHistory

        Direction newDirection = direction(lastTouchPosition, touchPosition);

        boolean isFirstGesture = gestureHistory.isEmpty();
        if (isFirstGesture) {

            Point initialTouchPosition = touchHistory.get(0);
            boolean movedFarEnough = (Math.abs(touchPosition.x - initialTouchPosition.x) > minGestureDetectionDistance
                                   || Math.abs(touchPosition.y - initialTouchPosition.y) > minGestureDetectionDistance);

            if (movedFarEnough) {
                changeDirection(newDirection);
            }
        } else {

            Direction lastGesturesDirection = prevGesture().direction;
            boolean directionChanged = (newDirection != lastGesturesDirection);

            Point lastGesturePosition = prevGesture().startPosition;
            boolean movedFarEnough = (Math.abs(touchPosition.x - lastGesturePosition.x) > minGestureDetectionDistance
                                   && Math.abs(touchPosition.y - lastGesturePosition.y) > minGestureDetectionDistance);

            if (directionChanged && movedFarEnough) {
                changeDirection(newDirection);
            }

        }
    }

    private Gesture prevGesture() {
        return gestureHistory.isEmpty() ? null : gestureHistory.get(gestureHistory.size() - 1);
    }

    private void changeDirection(Direction newDirection) {
        for (GestureListener listener : listeners) {
            listener.gestureChanged(newDirection);
        }
        gestureHistory.add(new Gesture(newDirection, currentTouchLocation()));
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
        touchHistory.clear();
        gestureHistory.clear();
    }
}
