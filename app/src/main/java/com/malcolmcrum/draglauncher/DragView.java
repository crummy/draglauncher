package com.malcolmcrum.draglauncher;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;

import java.util.List;

/**
 * Canvas for drawing launcher. Handles input, too.
 *
 * Created by Malcolm on 4/19/2015.
 */
public class DragView extends View {

    private final GestureManager gestureManager = new GestureManager();
    private final Paint gesturePaint = new Paint();
    private Drawable launcherIcon;

    public DragView(Context context) {
        // TODO: This constructor should never be used, but triggers a warning if it's missing. Fix?
        super(context);
    }

    public DragView(DragLauncher context) {
        super(context);
        gestureManager.addListener(context);

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
        drawPoints(canvas);
        drawCurrentSelection(canvas);
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        boolean handledGesture = gestureManager.handleInput(event);
        if (handledGesture) {
            invalidate();
        }
        return handledGesture;
    }

    private void drawPoints(Canvas canvas) {
        List<Point> points = gestureManager.getHistory();
        for (int pointIndex = 1; pointIndex < points.size(); pointIndex++) {
            Point lineStart = points.get(pointIndex-1);
            Point lineEnd = points.get(pointIndex);
            canvas.drawLine(lineStart.x, lineStart.y, lineEnd.x, lineEnd.y, gesturePaint);
        }
    }

    private void drawCurrentSelection(Canvas canvas) {
        launcherIcon.setBounds(canvas.getWidth()/2 - 128, 3*canvas.getHeight()/4 - 128, canvas.getWidth()/2 + 128, 3*canvas.getHeight()/4 + 128);
        launcherIcon.draw(canvas);
    }

}
