package com.example.chatapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CurvedBottomNavigationView extends BottomNavigationView {

    private Path mPath;
    private Paint mPaint;

    //radius of fab unit
    public final int CURVE_CIRCLE_RADIUS = 90;

    //The coordinate of the 1st curve
    public Point firstCurveStartPoint = new Point();
    public Point firstCurveEndPoint = new Point();
    public Point firstCurveControlPoint = new Point();
    public Point firstCurveControlPoint2 = new Point();

    //The coordinate of the 2st curve
    public Point secondCurveStartPoint = new Point();
    public Point secondCurveEndPoint = new Point();
    public Point secondCurveControlPoint = new Point();
    public Point secondCurveControlPoint2 = new Point();

    public int navigationBarWidth, navigationBarHeight;

    public CurvedBottomNavigationView(Context context) {
        super(context);
        initVIEW();
    }

    public CurvedBottomNavigationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initVIEW();
    }

    public CurvedBottomNavigationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initVIEW();
    }

    private void initVIEW() {
        mPath = new Path();
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setColor(Color.WHITE);
        setBackgroundColor(Color.TRANSPARENT);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        //Get width and height of the navigation bar
        navigationBarWidth = getWidth();
        navigationBarHeight = getHeight();

        //The coordinates (x,y) of the start point before curve
        firstCurveStartPoint.set((navigationBarWidth/2)
                -(CURVE_CIRCLE_RADIUS * 2)
                -(CURVE_CIRCLE_RADIUS / 3), 0);

        //The coordinates (x,y) of the start point after curve
        firstCurveEndPoint.set((navigationBarWidth/2), CURVE_CIRCLE_RADIUS + (CURVE_CIRCLE_RADIUS/4));

        //Same for the second curve
        secondCurveStartPoint = firstCurveEndPoint;

        secondCurveEndPoint.set(
                (navigationBarWidth/2)+ (CURVE_CIRCLE_RADIUS*2) +
                        (CURVE_CIRCLE_RADIUS/3),0);

        //The coordinates (x,y) of the 1st control point on cubic curve
        firstCurveControlPoint.set(
                firstCurveStartPoint.x +(CURVE_CIRCLE_RADIUS)+(CURVE_CIRCLE_RADIUS/4),
                firstCurveStartPoint.y);

        //The coordinates (x,y) of the 2nd control point on cubic curve
        firstCurveControlPoint2.set(
                firstCurveEndPoint.x -(CURVE_CIRCLE_RADIUS*2)+CURVE_CIRCLE_RADIUS,
                firstCurveEndPoint.y);

        secondCurveControlPoint.set(
                secondCurveStartPoint.x +(CURVE_CIRCLE_RADIUS*2)-CURVE_CIRCLE_RADIUS,
                secondCurveStartPoint.y);

        secondCurveControlPoint2.set(
                secondCurveEndPoint.x -(CURVE_CIRCLE_RADIUS+ (CURVE_CIRCLE_RADIUS/4)),
                secondCurveEndPoint.y);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPath.reset();
        mPath.moveTo(0,0);
        mPath.lineTo(firstCurveStartPoint.x, firstCurveStartPoint.y);

        mPath.cubicTo(firstCurveControlPoint.x, firstCurveControlPoint.y,
                firstCurveControlPoint2.x, firstCurveControlPoint2.y,
                firstCurveEndPoint.x, firstCurveEndPoint.y);

        mPath.cubicTo(secondCurveControlPoint.x, secondCurveControlPoint.y,
                secondCurveControlPoint2.x, secondCurveControlPoint2.y,
                secondCurveEndPoint.x, secondCurveEndPoint.y);

        mPath.lineTo(navigationBarWidth, 0);
        mPath.lineTo(navigationBarWidth, navigationBarHeight);
        mPath.lineTo(0, navigationBarHeight);
        mPath.close();

        canvas.drawPath(mPath, mPaint);
    }
}
