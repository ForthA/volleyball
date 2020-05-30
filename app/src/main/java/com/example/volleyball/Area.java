package com.example.volleyball;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

class Area {
    private static Paint paint = new Paint();
    private static int colorBack = Color.argb(255, 253, 166, 51);
    private static int colorBackSelect = Color.RED;
    private static int colorBorder = Color.WHITE;

    private RectF rect;
    private boolean selected;
    private float density;

    public static final int LEFT = 0;
    public static final int RIGHT = 1;

    Area(RectF rect, int side, float density) {
        this.rect = rect;
        this.density = density;
    }

    public RectF getRect() {
        return rect;
    }

    void draw(Canvas canvas) {
        paint.setStyle(Paint.Style.FILL);
        paint.setColor((selected) ? colorBackSelect : colorBack);
        canvas.drawRect(rect, paint);

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5 * density);
        paint.setColor(colorBorder);
        canvas.drawRect(rect, paint);
    }

    void setSelected(boolean value) {
        selected = value;
    }

    public boolean isSelected() {
        return selected;
    }
}
