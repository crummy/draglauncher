package com.malcolmcrum.draglauncher;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.View;

/**
 * Canvas for drawing launcher
 * Created by Malcolm on 4/19/2015.
 */
public class DragView extends View {

    private int itemSize = 128;
    private int itemSpacing = 256;

    private Paint squarePaint = new Paint();
    private Paint textPaint = new Paint();
    private Point center;
    private DragMenuItem menu;

    public DragView(Context context) {
        this(context, null);
    }

    public DragView(Context context, DragMenuItem menuRoot) {
        super(context);
        menu = menuRoot;
        squarePaint.setColor(Color.BLUE);
        textPaint.setColor(Color.BLACK);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(72);


    }

    @Override
    public void onDraw(Canvas canvas) {

        int width = this.getWidth();
        int height = this.getHeight();
        center = new Point(width/2, height/2); // TODO: make this work out of the draw loop
        drawItem(canvas, center.x, center.y, menu);
    }

    private void drawItem(Canvas canvas, int x, int y, DragMenuItem item) {
        canvas.drawRect(x - itemSize / 2, y - itemSize / 2, x + itemSize / 2, y + itemSize / 2, squarePaint);
        canvas.drawText(item.label, x, y, textPaint);

        if (item.north != null) drawItem(canvas, x, y - itemSpacing, item.north);
    }
}
