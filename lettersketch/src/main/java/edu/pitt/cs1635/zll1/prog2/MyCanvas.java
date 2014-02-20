package edu.pitt.cs1635.zll1.prog2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZLL1 on 2/1/14.
 */
public class MyCanvas extends View implements View.OnTouchListener {

    List<Point> points = new ArrayList<Point>();
    private Paint paint = new Paint();
    private Path path = new Path();
    private boolean dragging, firstRun, firstTouch;
    private int canvasHeight, canvasWidth;
    public String encoding;

    public MyCanvas(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setOnTouchListener(this);
        dragging = false;
        firstRun = true;
        firstTouch = true;
        canvasHeight = 0;
        canvasWidth = 0;
        encoding = "";
        setPaint();

      // Log.i("Height", String.valueOf(this.getMeasuredHeight()));
    }

    public void setColor(int c) {
        Log.i("Set to color", "" + c);
        this.paint.setColor(c);
    }

    private void setPaint() {
        Log.i("initial color", "" + Color.BLACK);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        // create a new point
        Point point = new Point();
        point.x = scale(motionEvent.getX(), canvasWidth);//motionEvent.getX();
        point.y = scale(motionEvent.getY(), canvasHeight);//motionEvent.getY();
        points.add(point);

       // Log.i("Scaled Coords", "(" + scale(motionEvent.getX(), canvasWidth) + ", " + scale(motionEvent.getY(), canvasHeight) + ")");

        if(dragging) {
            if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                dragging = false;
            }
            encoding += ", " + point.x + ", " + point.y;

            path.lineTo(motionEvent.getX(), motionEvent.getY());
        }else {
            // the finger just touched the screen
            if(firstTouch) {
                encoding = "[" + point.x + ", " + point.y;
                firstTouch = false;
            }else {
                encoding += ", 255, 0, " + point.x + ", " + point.y;
            }
            dragging = true;
            path.moveTo(motionEvent.getX(), motionEvent.getY());
           // still need to handle drawing a dot
        }

        //Log.i("encoding", encoding);

        invalidate();
        //Log.d("coordinates: ", "point: " + point);
        return true;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(firstRun) {
            firstRun = false;
            canvasHeight = canvas.getHeight();
            canvasWidth = canvas.getWidth();
            Log.i("height" , String.valueOf(canvas.getHeight()));
            Log.i("width", String.valueOf(canvas.getWidth()));
        }

        canvas.drawPath(path, paint);
    }

    private int scale(float num, float r) {
        return (int) (254 * (num / r));
    }

    public void newDrawing() {
        firstTouch = true;
        encoding = "";
        path = new Path();
        invalidate();
    }
}

class Point {
    int x, y;

    @Override
    public String toString() {
        return x + ", " + y;
    }
}
