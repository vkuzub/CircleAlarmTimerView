/*
 *
 *  * Copyright 2015 Jiahuan
 *  * Copyright 2016 yinglan
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 *
 */

package com.yinglan.circleviewlibrary;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;


public class CircleAlarmTimerView extends View {
    private static final String TAG = "CircleTimerView";

    public static final double RAD_TO_HOURS_MULTIPLIER = 1.909859;
    public static final double ONE_MIN_ANGLE = 2 * Math.PI / 60;
    public static final int HOURS_SECTIONS_NUM = 5;
    private final int[] numbers = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};

    // Status
    private static final String INSTANCE_STATUS = "instance_status";
    private static final String STATUS_RADIAN = "status_radian";

    // Default dimension in dp/pt
    private static final float DEFAULT_GAP_BETWEEN_CIRCLE_AND_LINE = 30;
    private static final float DEFAULT_NUMBER_SIZE = 10;
    private static final float DEFAULT_LINE_WIDTH = 0.5f;
    private static final float DEFAULT_CIRCLE_BUTTON_RADIUS = 15;
    private static final float DEFAULT_CIRCLE_STROKE_WIDTH = 1;
    private static final float DEFAULT_TIMER_NUMBER_SIZE = 38;
    private static final float DEFAULT_TIMER_TEXT_SIZE = 18;

    // Default color
    private static final int DEFAULT_CIRCLE_COLOR = 0xFFE9E2D9;
    private static final int DEFAULT_CIRCLE_BUTTON_COLOR = 0xFF403E3F; //hour circle color
    private static final int DEFAULT_LINE_COLOR = 0xFFFFFFFF;
    private static final int DEFAULT_ARC_COLOR = 0xFF0099CC;
    private static final int DEFAULT_HIGHLIGHT_LINE_COLOR = 0xFF68C5D7;
    private static final int DEFAULT_NUMBER_COLOR = 0xFFFFFFFF; //outer circle
    private static final int DEFAULT_TIMER_NUMBER_COLOR = 0xFFFFFFFF;
    private static final int DEFAULT_TIMER_COLON_COLOR = 0xFF000000; //minute circle color
    private static final int DEFAULT_TIMER_TEXT_COLOR = 0x99F0F9FF;
    private static final int DEFAULT_TEXT_IN_CIRCLE_COLOR = 0xFFFFFFFF;
    private static final int DEFAULT_INNER_CIRCLE_COLOR = 0xFF403E3F;

    // Paint
    private Paint mCirclePaint;
    private Paint mInnerCirclePaint;
    private Paint mHighlightLinePaint;
    private Paint mLinePaint;
    private Paint mCircleButtonPaint;
    private Paint mNumberPaint;
    private Paint mTimerNumberPaint;
    private Paint mTimerTextPaint;
    private Paint mTextInCirclePaint;
    private Paint mTimerColonPaint;
    private Paint mArcPaint;

    // Dimension
    private float mGapBetweenCircleAndLine;
    private float mNumberSize;
    private float mLineWidth;
    private float mCircleButtonRadius;
    private float mCircleStrokeWidth;
    private float mInnerCircleStrokeWidth;
    private float mTimerNumberSize;
    private float mTimerTextSize;
    private float mTextInCircleSize;

    // Color
    private int mCircleColor;
    private int mInnerCircleColor;
    private int mCircleButtonColor;
    private int mLineColor;
    private int mArcColor;
    private int mHighlightLineColor;
    private int mNumberColor;
    private int mTimerNumberColor;
    private int mTimerTextColor;
    private int mTextInCircleColor;

    // Parameters
    private float mCx;
    private float mCy;
    private float mRadius;
    private float hourRadian;
    private float hourRadianFake;
    private float minuteRadian;
    private float preRadian;
    private boolean mInCircleButton;
    private boolean mInCircleButton1;
    private boolean ismInCircleButton;
    private int mCurrentTime; // seconds

    private int mHours = 12;
    private int mMinutes;
    private boolean drawDigital;

    private double prevMinuteRadian;

    private OnTimeChangedListener mListener;

    public CircleAlarmTimerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    public CircleAlarmTimerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleAlarmTimerView(Context context) {
        this(context, null);
    }

    private void initialize() {
        Log.d(TAG, "initialize");
        // Set default dimension or read xml attributes
        //indent beetween main circle and view top outside edge
        mGapBetweenCircleAndLine = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_GAP_BETWEEN_CIRCLE_AND_LINE,
                getContext().getResources().getDisplayMetrics());
        mNumberSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_NUMBER_SIZE, getContext().getResources()
                .getDisplayMetrics());
        mLineWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_LINE_WIDTH, getContext().getResources()
                .getDisplayMetrics());
        //small circle size
        mCircleButtonRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_CIRCLE_BUTTON_RADIUS, getContext()
                .getResources().getDisplayMetrics());
        mCircleStrokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_CIRCLE_STROKE_WIDTH, getContext()
                .getResources().getDisplayMetrics());
        mTimerNumberSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_TIMER_NUMBER_SIZE, getContext()
                .getResources().getDisplayMetrics());
        mTimerTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_TIMER_TEXT_SIZE, getContext()
                .getResources().getDisplayMetrics());
        mTextInCircleSize = mTimerNumberSize / 2.3f;
        mInnerCircleStrokeWidth = mCircleStrokeWidth * 2;

        // Set default color or read xml attributes
        mCircleColor = DEFAULT_CIRCLE_COLOR;
        mCircleButtonColor = DEFAULT_CIRCLE_BUTTON_COLOR;
        mLineColor = DEFAULT_LINE_COLOR;
        mHighlightLineColor = DEFAULT_HIGHLIGHT_LINE_COLOR;
        mNumberColor = DEFAULT_NUMBER_COLOR;
        mTimerNumberColor = DEFAULT_TIMER_NUMBER_COLOR;
        mTimerTextColor = DEFAULT_TIMER_TEXT_COLOR;
        mTextInCircleColor = DEFAULT_TEXT_IN_CIRCLE_COLOR;
        mArcColor = DEFAULT_ARC_COLOR;
        mInnerCircleColor = DEFAULT_INNER_CIRCLE_COLOR;

        // Init all paints
        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCircleButtonPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHighlightLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mNumberPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTimerNumberPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTimerTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTimerColonPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mArcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextInCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mInnerCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        // CirclePaint
        mCirclePaint.setColor(mCircleColor);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setStrokeWidth(mCircleStrokeWidth);

        // CircleInnerPaint
        mInnerCirclePaint.setColor(mInnerCircleColor);
        mInnerCirclePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mInnerCirclePaint.setStrokeWidth(mInnerCircleStrokeWidth);

        // CircleButtonPaint
        mCircleButtonPaint.setColor(mCircleButtonColor);
        mCircleButtonPaint.setAntiAlias(true);
        mCircleButtonPaint.setStyle(Paint.Style.FILL);

        // LinePaint
        mLinePaint.setColor(mLineColor);
        mLinePaint.setStrokeWidth(mCircleButtonRadius * 2 + 8);
        mLinePaint.setStyle(Paint.Style.STROKE);

        // LinePaint
        mArcPaint.setColor(mArcColor);
        mArcPaint.setStrokeWidth(mCircleButtonRadius * 2 + 8);
        mArcPaint.setStyle(Paint.Style.STROKE);

        // HighlightLinePaint
        mHighlightLinePaint.setColor(mHighlightLineColor);
        mHighlightLinePaint.setStrokeWidth(mLineWidth);

        // NumberPaint
        mNumberPaint.setColor(mNumberColor);
        mNumberPaint.setTextSize(mNumberSize);
        mNumberPaint.setTextAlign(Paint.Align.CENTER);
        mNumberPaint.setStyle(Paint.Style.STROKE);
        mNumberPaint.setStrokeWidth(mCircleButtonRadius * 2 + 8);

        // TimerNumberPaint
        mTimerNumberPaint.setColor(mTimerNumberColor);
        mTimerNumberPaint.setTextSize(mTimerNumberSize);
        mTimerNumberPaint.setTextAlign(Paint.Align.CENTER);

        // TimerTextPaint
        mTimerTextPaint.setColor(mTimerTextColor);
        mTimerTextPaint.setTextSize(mTimerTextSize);
        mTimerTextPaint.setTextAlign(Paint.Align.CENTER);

        // TimerColonPaint
        mTimerColonPaint.setColor(DEFAULT_TIMER_COLON_COLOR);
        mTimerColonPaint.setTextAlign(Paint.Align.CENTER);
        mTimerColonPaint.setTextSize(mTimerNumberSize);

        // TextInCirclePaint
        mTextInCirclePaint.setColor(mTextInCircleColor);
        mTextInCirclePaint.setTextSize(mTextInCircleSize);
        mTextInCirclePaint.setTextAlign(Paint.Align.CENTER);

        // Solve the target version related to shadow
        // setLayerType(View.LAYER_TYPE_SOFTWARE, null); // use this, when targetSdkVersion is greater than or equal to api 14
    }

    @Override
    protected void onDraw(Canvas canvas) {

        canvas.save();

        //main circle with 2 small circles
        float mainCircleRadius = mRadius - mCircleStrokeWidth / 2 - mGapBetweenCircleAndLine; //300
        drawMainCircle(canvas, mainCircleRadius);

        drawInnerCircle(canvas, mainCircleRadius);

        if (drawDigital) {
            drawDigitalTime(canvas);
        }

        drawCircleButtons(canvas);

        drawNumerals(canvas, mainCircleRadius);
        drawNumeralLines(canvas, mainCircleRadius);

        drawHandHour(canvas, mHours);
        drawHandMinute(canvas, mMinutes);

        drawHandCircle(canvas);

        if (null != mListener) {
            mListener.start(String.valueOf(mHours));
            mListener.end(String.valueOf(mMinutes));
        }

        super.onDraw(canvas);
    }

    private void drawHandMinute(Canvas canvas, double pos) {
        Paint paint = new Paint();
        paint.setColor(Color.GRAY);
        paint.setStrokeWidth(7);
        double angle =
                ONE_MIN_ANGLE * pos - Math.PI / 2;
        float handRadius = mRadius / 1.7f;
        if (mInCircleButton1) {
            paint.setColor(Color.WHITE);
        }
        canvas.drawLine(mCx, mCy,
                (float) (mCx + Math.cos(angle) * handRadius),
                (float) (mCy + Math.sin(angle) * handRadius),
                paint);
    }

    private void drawHandHour(Canvas canvas, double pos) {
        Paint paint = new Paint();
        paint.setStrokeWidth(8);
        paint.setColor(Color.GRAY);
        double minuteAngle = ONE_MIN_ANGLE * mMinutes;
        double angle = ONE_MIN_ANGLE * pos * 5 - Math.PI / 2 + minuteAngle / 12;
        float handRadius = mRadius / 2.5f;
        if (mInCircleButton) {
            paint.setColor(Color.WHITE);
        }
        canvas.drawLine(mCx, mCy,
                (float) (mCx + Math.cos(angle) * handRadius),
                (float) (mCy + Math.sin(angle) * handRadius),
                paint);
    }

    private void drawHandCircle(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.GRAY);
        canvas.drawCircle(mCx, mCy, 12, paint);
    }


    private void drawNumerals(Canvas canvas, float mainCircleRadius) {
        Paint paint = new Paint();
        paint.setTextSize((mTextInCircleSize / 1.8f));
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setColor(Color.WHITE);
        for (int number : numbers) {
            String tmp = String.valueOf(number);
            //поиск ширины
//            paint.getTextBounds(tmp, 0, tmp.length(), rect);
            double angle = Math.PI / 6 * (number - 3);
            float x = (float) (mCx + Math.cos(angle) * mainCircleRadius / 1.7);
            float y = (float) (mCy + Math.sin(angle) * mainCircleRadius / 1.7);
            canvas.drawText(tmp, x, y, paint);
        }
    }

    private void drawNumeralLines(Canvas canvas, float mainCircleRadius) {
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(15);
        int n = 60;
        double angle = 0;
        for (int i = 0; i < n; i++) {
            angle += 2 * Math.PI / n;
            float x = (float) (mCx + Math.cos(angle) * mainCircleRadius / 1.35);
            float y = (float) (mCy + Math.sin(angle) * mainCircleRadius / 1.35);
            if (i % 5 == 4) {
                canvas.drawCircle(x, y, 4, paint);
            } else {
                canvas.drawCircle(x, y, 3, paint);
            }
            Log.d("numer line:", angle + "");
            Log.d("numer line:", x + " " + y);
        }
        Log.d("numer line:", "====");
    }

    private void drawMainCircle(Canvas canvas, float mainCircleRadius) {
        canvas.drawCircle(mCx, mCy, mainCircleRadius, mNumberPaint);
    }

    private void drawInnerCircle(Canvas canvas, float mainCircleRadius) {
        float innerCircleRadius = mainCircleRadius - mCircleButtonRadius - mInnerCircleStrokeWidth; //250
        canvas.drawCircle(mCx, mCy, innerCircleRadius, mInnerCirclePaint);
    }

    private void drawDigitalTime(Canvas canvas) {
        //time in center
        canvas.drawText(getHours() + " " + getMinutes(), mCx, mCy + getFontHeight(mTimerNumberPaint) / 2, mTimerNumberPaint);
        canvas.drawText(":", mCx, mCy + getFontHeight(mTimerNumberPaint) / 2, mTimerColonPaint);
    }

    private String getHours() {
        return mHours < 10 ? String.valueOf("0" + mHours) : String.valueOf(mHours);
    }

    private String getMinutes() {
        return mMinutes < 10 ? String.valueOf("0" + mMinutes) : String.valueOf(mMinutes);
    }

    private void drawCircleButtons(Canvas canvas) {

//        Log.d("measuredHeight=", String.valueOf(getMeasuredHeight())); //520
        float circleY = (getMeasuredHeight() / 2) - mRadius + (mCircleStrokeWidth / 2) + mGapBetweenCircleAndLine; //50
        float circleTextY = circleY + (mCircleButtonRadius / 2); //65
        //circles
        if (ismInCircleButton) {
//            Path path = new Path();
//            canvas.drawTextOnPath();
            drawCircleHourButton(canvas, circleY, circleTextY);
            drawCircleMinuteButton(canvas, circleY, circleTextY);
        } else {
            drawCircleMinuteButton(canvas, circleY, circleTextY);
            drawCircleHourButton(canvas, circleY, circleTextY);
        }
        Log.d("radians", "" + hourRadian + " " + minuteRadian);
    }

    // FIXME: 30.03.2017
    private void drawCircleHourButton(Canvas canvas, float circleY, float circleTextY) {
        double degrees = Math.toDegrees(hourRadian) + Math.toDegrees(minuteRadian) / 12; //hour circle touch issue
//        double degrees = Math.toDegrees(hourRadian); //stable
        Log.d("circleHour", "mr:" + minuteRadian + " prev" + prevMinuteRadian);
        double diff = Math.abs(minuteRadian - prevMinuteRadian);
        Log.d("circleHour", "diff:" + diff);
        if (diff >= (2 * Math.PI) / 60) {
            Log.d("circleHour", "diff");
        }
        hourRadianFake = (float) Math.toRadians(degrees);
        Log.d("circleHour", "d:" + degrees + " hourRad:" + hourRadian + " hourRadFake: " + hourRadianFake + " minRad1:" + minuteRadian);
        canvas.rotate((float) degrees, mCx, mCy);
        canvas.drawCircle(mCx, circleY, mCircleButtonRadius, mCircleButtonPaint);
        drawTextInCircleButton(canvas, getHours(), mCx, circleTextY);
        canvas.restore();
        canvas.save();

    }

    private void drawCircleMinuteButton(Canvas canvas, float circleY, float circleTextY) {
        canvas.rotate((float) Math.toDegrees(minuteRadian), mCx, mCy);
        canvas.drawCircle(mCx, circleY, mCircleButtonRadius, mTimerColonPaint);
        drawTextInCircleButton(canvas, getMinutes(), mCx, circleTextY);
        canvas.restore();
        canvas.save();
    }

    private void drawTextInCircleButton(Canvas canvas, String time, float x, float y) {
        canvas.drawText(time, x, y, mTextInCirclePaint);
    }

    private float getFontHeight(Paint paint) {
        // FontMetrics sF = paint.getFontMetrics();
        // return sF.descent - sF.ascent;
        Rect rect = new Rect();
        paint.getTextBounds("1", 0, 1, rect);
        return rect.height();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                // If the point in the circle button
                if (mInCircleButton(event.getX(), event.getY()) && isEnabled()) {
                    mInCircleButton = true;
                    ismInCircleButton = false;
                    preRadian = getRadian(event.getX(), event.getY());
                    Log.d(TAG, "In circle button x=" + event.getX() + " y=" + event.getY());
                } else if (mInCircleButton1(event.getX(), event.getY()) && isEnabled()) {
                    mInCircleButton1 = true;
                    ismInCircleButton = true;
                    preRadian = getRadian(event.getX(), event.getY());
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (mInCircleButton && isEnabled()) {
                    float temp = getRadian(event.getX(), event.getY());
                    if (preRadian > Math.toRadians(270) && temp < Math.toRadians(90)) {
                        preRadian -= 2 * Math.PI;
                    } else if (preRadian < Math.toRadians(90) && temp > Math.toRadians(270)) {
                        preRadian = (float) (temp + (temp - 2 * Math.PI) - preRadian);
                    }
                    hourRadian += (temp - preRadian);
                    preRadian = temp;
                    if (hourRadian > 2 * Math.PI) {
                        hourRadian -= (float) (2 * Math.PI);
                    }
                    if (hourRadian < 0) {
                        hourRadian += (float) (2 * Math.PI);
                    }
                    calcHours();

                    invalidate();
                } else if (mInCircleButton1 && isEnabled()) {
                    prevMinuteRadian = minuteRadian;

                    float temp = getRadian(event.getX(), event.getY());
                    if (preRadian > Math.toRadians(270) && temp < Math.toRadians(90)) {
                        preRadian -= 2 * Math.PI;
                    } else if (preRadian < Math.toRadians(90) && temp > Math.toRadians(270)) {
                        preRadian = (float) (temp + (temp - 2 * Math.PI) - preRadian);
                    }
                    minuteRadian += (temp - preRadian);
                    preRadian = temp;
                    if (minuteRadian > 2 * Math.PI) {
                        minuteRadian -= (float) (2 * Math.PI);
                    }
                    if (minuteRadian < 0) {
                        minuteRadian += (float) (2 * Math.PI);
                    }
//                    hourRadian += minuteRadian / 60;

                    calcMinutes();

                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mInCircleButton && isEnabled()) {
                    mInCircleButton = false;
                    invalidate();
                } else if (mInCircleButton1 && isEnabled()) {
                    mInCircleButton1 = false;
                    invalidate();
                }
                break;
        }
        return true;
    }

    private void calcHourRadian() {

    }

    private void calcHours() {
        mCurrentTime = (int) (60 / (2 * Math.PI) * hourRadian * 60);
        int hours = (int) (hourRadian * RAD_TO_HOURS_MULTIPLIER);
        setHours(hours);
    }

    private void calcMinutes() {
        mCurrentTime = (int) (60 / (2 * Math.PI) * minuteRadian * 60);
        int now = (int) (minuteRadian * RAD_TO_HOURS_MULTIPLIER * 5);
        if (now != mMinutes) {
            int mPrevMinute = mMinutes;
            setMinutes(now);
            //if minuteCircleButton will move very fast, it will possible skip several minutes
            if ((now >= 0 && now <= 10) && (mPrevMinute <= 59 && mPrevMinute >= 50)) {
                setHours(++mHours);
                hourRadian += ONE_MIN_ANGLE * HOURS_SECTIONS_NUM;
            }
            if ((now <= 59 && now >= 50) && (mPrevMinute >= 0 && mPrevMinute <= 10)) {
                setHours(--mHours);
                hourRadian -= ONE_MIN_ANGLE * HOURS_SECTIONS_NUM;
            }
        }
    }

    private void setHours(int newHours) {
        if (newHours < 0) {
            newHours = 12 + newHours;
        }
        mHours = Math.abs(newHours % 12);
        if (mHours == 0) {
            mHours = 12;
        }
    }

    private void setMinutes(int newMinutes) {
        mMinutes = newMinutes;
    }

    // Whether the down event inside circle button
    private boolean mInCircleButton1(float x, float y) {
        float r = mRadius - mCircleStrokeWidth / 2 - mGapBetweenCircleAndLine;
        float x2 = (float) (mCx + r * Math.sin(minuteRadian));
        float y2 = (float) (mCy - r * Math.cos(minuteRadian));
        if (Math.sqrt((x - x2) * (x - x2) + (y - y2) * (y - y2)) < mCircleButtonRadius) {
            return true;
        }
        return false;
    }

    // Whether the down event inside circle button
    private boolean mInCircleButton(float x, float y) {
        float r = mRadius - mCircleStrokeWidth / 2 - mGapBetweenCircleAndLine;
        float x2 = (float) (mCx + r * Math.sin(hourRadian));
        float y2 = (float) (mCy - r * Math.cos(hourRadian));
        float x2f = (float) (mCx + r * Math.sin(hourRadianFake));
        float y2f = (float) (mCy - r * Math.cos(hourRadianFake));
        double square = Math.sqrt((x - x2) * (x - x2) + (y - y2) * (y - y2));
        double squareFake = Math.sqrt((x - x2f) * (x - x2f) + (y - y2f) * (y - y2f));
        if (square < mCircleButtonRadius) {
            return true;
        }
        if (squareFake < mCircleButtonRadius) {
            return true;
        }
        return false;
    }

    // Use tri to cal radian
    private float getRadian(float x, float y) {
        float alpha = (float) Math.atan((x - mCx) / (mCy - y));
        // Quadrant
        if (x > mCx && y > mCy) {
            // 2
            alpha += Math.PI;
        } else if (x < mCx && y > mCy) {
            // 3
            alpha += Math.PI;
        } else if (x < mCx && y < mCy) {
            // 4
            alpha = (float) (2 * Math.PI + alpha);
        }
        return alpha;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d(TAG, "onMeasure");
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // Ensure width = height
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        this.mCx = width / 2;
        this.mCy = height / 2;

//        Log.d("/", "///////////////////");
//        Log.d("h", String.valueOf(height));
//        Log.d("w", String.valueOf(width));
//        Log.d("mcy", String.valueOf(mCy));
//        Log.d("mcx", String.valueOf(mCx));
//        Log.d("/", "///////////////////");
        // Radius
        if (mGapBetweenCircleAndLine + mCircleStrokeWidth >= mCircleButtonRadius) {
            this.mRadius = width / 2 - mCircleStrokeWidth / 2;
        } else {
            this.mRadius = width / 2 - (mCircleButtonRadius - mGapBetweenCircleAndLine -
                    mCircleStrokeWidth / 2);
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(INSTANCE_STATUS, super.onSaveInstanceState());
        bundle.putFloat(STATUS_RADIAN, hourRadian);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE_STATUS));
            hourRadian = bundle.getFloat(STATUS_RADIAN);
            mCurrentTime = (int) (60 / (2 * Math.PI) * hourRadian * 60);
            return;
        }
        super.onRestoreInstanceState(state);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    public void setOnTimeChangedListener(OnTimeChangedListener listener) {
        if (null != listener) {
            this.mListener = listener;
        }
    }

    public interface OnTimeChangedListener {
        void start(String starting);

        void end(String ending);
    }

    public void setCurrentHour(int hour) {
        setHours(hour);
        hourRadian = (float) (ONE_MIN_ANGLE * hour * HOURS_SECTIONS_NUM);
        invalidate();
    }

    public void setCurrentMinute(int minute) {
        setMinutes(minute);
        minuteRadian = (float) (ONE_MIN_ANGLE * minute);
        invalidate();
    }

    public String getCurrentHour() {
        return getHours();
    }

    public String getCurrentHour24() {
        return String.valueOf(mHours + 12);
    }

    public String getCurrentMinute() {
        return getMinutes();
    }

    public void drawDigitalClock(boolean draw) {
        this.drawDigital = draw;
        invalidate();
    }

}
