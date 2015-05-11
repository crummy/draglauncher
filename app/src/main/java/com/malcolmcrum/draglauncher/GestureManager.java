package com.malcolmcrum.draglauncher;

import android.content.res.Resources;
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

    private Point touchPosition;
    private Point gestureStartPoint;

    private final List<GestureListener> listeners = new ArrayList<>();
    private final List<Gesture> gestureHistory = new ArrayList<>();

    private Resources resources;

    public GestureManager(Point gestureOriginPoint, Resources resources) {
        this.gestureStartPoint = gestureOriginPoint;
        this.resources = resources;
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

    public List<Gesture> getGestureHistory() {
        return gestureHistory;
    }

    public boolean isGesturing() {
        return !gestureHistory.isEmpty();
    }

    public boolean isTouching() {
        return touchPosition != null;
    }

    public Point getTouchLocation() {
        return touchPosition;
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
        Point lastTouchPosition = touchPosition;
        touchPosition = new Point((int)x, (int)y);

        Direction newDirection = direction(lastTouchPosition, touchPosition);

        boolean isFirstGesture = gestureHistory.isEmpty();
        int minGestureDetectionDistance = resources.getDimensionPixelSize(R.dimen.min_gesture_detection_distance);
        if (isFirstGesture) {

            boolean movedFarEnough = (Math.abs(touchPosition.x - gestureStartPoint.x) > minGestureDetectionDistance
                                      || Math.abs(touchPosition.y - gestureStartPoint.y) > minGestureDetectionDistance);

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
        gestureHistory.add(new Gesture(newDirection, getTouchLocation()));
    }

    private void pressed(float x, float y) {
        touchPosition = new Point((int)x, (int)y);
        for (GestureListener listener : listeners) {
            listener.gestureStarted();
        }
    }

    private void released() {
        for (GestureListener listener : listeners) {
            listener.gestureFinished();
        }
        touchPosition = null;
        gestureHistory.clear();
    }
}
