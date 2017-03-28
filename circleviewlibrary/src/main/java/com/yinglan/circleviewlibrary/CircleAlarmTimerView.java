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
    private static final int DEFAULT_CIRCLE_BUTTON_COLOR = 0xFF000000; //hour circle color
    private static final int DEFAULT_LINE_COLOR = 0xFFFFFFFF;
    private static final int DEFAULT_ARC_COLOR = 0xFF0099CC;
    private static final int DEFAULT_HIGHLIGHT_LINE_COLOR = 0xFF68C5D7;
    private static final int DEFAULT_NUMBER_COLOR = 0xFFFFFFFF; //outer circle
    private static final int DEFAULT_TIMER_NUMBER_COLOR = 0xFFFFFFFF;
    private static final int DEFAULT_TIMER_COLON_COLOR = 0xFF000000; //minute circle color
    private static final int DEFAULT_TIMER_TEXT_COLOR = 0x99F0F9FF;
    private static final int DEFAULT_TEXT_IN_CIRCLE_COLOR = 0xFF68C5D7;
    private static final int DEFAULT_INNER_CIRCLE_COLOR = 0xFF2F2F2F;

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
    private float mCurrentRadian;
    private float mCurrentRadian1;
    private float mPreRadian;
    private boolean mInCircleButton;
    private boolean mInCircleButton1;
    private boolean ismInCircleButton;
    private int mCurrentTime; // seconds

    private int mHours;
    private int mMinutes;

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
        mTextInCircleSize = mTimerNumberSize / 2;
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
//        canvas.save();
//        canvas.rotate(-90, mCx, mCy);

        drawInnerCircle(canvas, mainCircleRadius);

//        RectF rect = new RectF(mCx - (mRadius - mCircleStrokeWidth / 2 - mGapBetweenCircleAndLine
//        ), mCy - (mRadius - mCircleStrokeWidth / 2 - mGapBetweenCircleAndLine
//        ), mCx + (mRadius - mCircleStrokeWidth / 2 - mGapBetweenCircleAndLine
//        ), mCy + (mRadius - mCircleStrokeWidth / 2 - mGapBetweenCircleAndLine
//        ));

//        arc
//        if (mCurrentRadian1 > mCurrentRadian) {
//            canvas.drawArc(rect, (float) Math.toDegrees(mCurrentRadian1), (float) Math.toDegrees(2 * (float) Math.PI) - (float) Math.toDegrees(mCurrentRadian1) + (float) Math.toDegrees(mCurrentRadian), false, mArcPaint);
//        } else {
//            canvas.drawArc(rect, (float) Math.toDegrees(mCurrentRadian1), (float) Math.toDegrees(mCurrentRadian) - (float) Math.toDegrees(mCurrentRadian1), false, mArcPaint);
//        }
//        canvas.restore();
//        canvas.save();
//      ard


//        drawDigitalTime(canvas);
        drawCircleButtons(canvas);
        drawHandCircle(canvas);

        drawNumerals(canvas, mainCircleRadius);
        drawNumeralLines(canvas, mainCircleRadius);

        if (null != mListener) {
            mListener.start(String.valueOf(mHours));
            mListener.end(String.valueOf(mMinutes));
        }

//        float[] points = {mCy - 100, mCx / 2,
//                mCy / 2 + 100, mCx / 2,
//                mCy / 2, mCx / 2 - 100,
//                mCy / 2, mCx / 2 + 100};
//        canvas.drawPoints(points, mTimerColonPaint);

        canvas.restore();
        // Timer Text
        canvas.save();
        canvas.restore();
        super.onDraw(canvas);
    }

    private void drawHandCircle(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
//        paint.setStrokeWidth(20);
        canvas.drawCircle(mCx, mCy, 12, paint);
    }

    private void drawNumerals(Canvas canvas, float mainCircleRadius) {
        Paint paint = new Paint();
        paint.setTextSize((float) (mTextInCircleSize / 1.5));
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setColor(Color.WHITE);
        for (int number : numbers) {
            String tmp = String.valueOf(number);
            //поиск ширины
//            paint.getTextBounds(tmp, 0, tmp.length(), rect);
            double angle = Math.PI / 6 * (number - 3);
            float x = (float) (mCx + Math.cos(angle) * mainCircleRadius / 1.6);
            float y = (float) (mCy + Math.sin(angle) * mainCircleRadius / 1.6);
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
            float x = (float) (mCx + Math.cos(angle) * mainCircleRadius / 1.25);
            float y = (float) (mCy + Math.sin(angle) * mainCircleRadius / 1.25);
            canvas.drawCircle(x, y, 3, paint);
        }
    }

    private void drawMainCircle(Canvas canvas, float mainCircleRadius) {
        canvas.drawCircle(mCx, mCy, mainCircleRadius, mNumberPaint);
    }

    private void drawInnerCircle(Canvas canvas, float mainCircleRadius) {
        float innerCircleRadius = mainCircleRadius - mCircleButtonRadius - mInnerCircleStrokeWidth; //250
        Log.d("mainC", String.valueOf(mainCircleRadius));
        Log.d("innerC", String.valueOf(innerCircleRadius));
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
        canvas.rotate((float) Math.toDegrees(mCurrentRadian), mCx, mCy);
        canvas.drawCircle(mCx, getMeasuredHeight() / 2 - mRadius + mCircleStrokeWidth / 2 + mGapBetweenCircleAndLine
                , 0.01f, mLinePaint);
        canvas.restore();
        // TimerNumber
        canvas.save();
        canvas.rotate((float) Math.toDegrees(mCurrentRadian1), mCx, mCy);
        canvas.drawCircle(mCx, getMeasuredHeight() / 2 - mRadius + mCircleStrokeWidth / 2 + mGapBetweenCircleAndLine, 0.01f, mLinePaint);
        canvas.restore();
        // TimerNumber
        canvas.save();


        Log.d("measuredHeight=", String.valueOf(getMeasuredHeight())); //520
        float circleY = (getMeasuredHeight() / 2) - mRadius + (mCircleStrokeWidth / 2) + mGapBetweenCircleAndLine; //50
        float circleTextY = circleY + (mCircleButtonRadius / 2); //65
        //circles
        if (ismInCircleButton) {
//            Path path = new Path();
//            canvas.drawTextOnPath();
            canvas.rotate((float) Math.toDegrees(mCurrentRadian), mCx, mCy);
            canvas.drawCircle(mCx, circleY, mCircleButtonRadius, mCircleButtonPaint);
            drawTextInCircleButton(canvas, getHours(), mCx, circleTextY);
            canvas.restore();
            canvas.save();


            canvas.rotate((float) Math.toDegrees(mCurrentRadian1), mCx, mCy);
            canvas.drawCircle(mCx, circleY, mCircleButtonRadius, mTimerColonPaint);
            drawTextInCircleButton(canvas, getMinutes(), mCx, circleTextY);
            canvas.restore();
            canvas.save();

        } else {

            canvas.rotate((float) Math.toDegrees(mCurrentRadian1), mCx, mCy);
            canvas.drawCircle(mCx, circleY, mCircleButtonRadius, mTimerColonPaint);
            drawTextInCircleButton(canvas, getMinutes(), mCx, circleTextY);
            canvas.restore();
            canvas.save();

            canvas.rotate((float) Math.toDegrees(mCurrentRadian), mCx, mCy);
            canvas.drawCircle(mCx, circleY, mCircleButtonRadius, mCircleButtonPaint);
            drawTextInCircleButton(canvas, getHours(), mCx, circleTextY);
            canvas.restore();
            canvas.save();
        }
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
                    mPreRadian = getRadian(event.getX(), event.getY());
                    Log.d(TAG, "In circle button");
                } else if (mInCircleButton1(event.getX(), event.getY()) && isEnabled()) {
                    mInCircleButton1 = true;
                    ismInCircleButton = true;
                    mPreRadian = getRadian(event.getX(), event.getY());
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (mInCircleButton && isEnabled()) {
                    float temp = getRadian(event.getX(), event.getY());
                    if (mPreRadian > Math.toRadians(270) && temp < Math.toRadians(90)) {
                        mPreRadian -= 2 * Math.PI;
                    } else if (mPreRadian < Math.toRadians(90) && temp > Math.toRadians(270)) {
                        mPreRadian = (float) (temp + (temp - 2 * Math.PI) - mPreRadian);
                    }
                    mCurrentRadian += (temp - mPreRadian);
                    mPreRadian = temp;
                    if (mCurrentRadian > 2 * Math.PI) {
                        mCurrentRadian -= (float) (2 * Math.PI);
                    }
                    if (mCurrentRadian < 0) {
                        mCurrentRadian += (float) (2 * Math.PI);
                    }
                    mCurrentTime = (int) (60 / (2 * Math.PI) * mCurrentRadian * 60);
                    mHours = (int) (mCurrentRadian * RAD_TO_HOURS_MULTIPLIER);
                    invalidate();
                } else if (mInCircleButton1 && isEnabled()) {
                    float temp = getRadian(event.getX(), event.getY());
                    if (mPreRadian > Math.toRadians(270) && temp < Math.toRadians(90)) {
                        mPreRadian -= 2 * Math.PI;
                    } else if (mPreRadian < Math.toRadians(90) && temp > Math.toRadians(270)) {
                        mPreRadian = (float) (temp + (temp - 2 * Math.PI) - mPreRadian);
                    }
                    mCurrentRadian1 += (temp - mPreRadian);
                    mPreRadian = temp;
                    if (mCurrentRadian1 > 2 * Math.PI) {
                        mCurrentRadian1 -= (float) (2 * Math.PI);
                    }
                    if (mCurrentRadian1 < 0) {
                        mCurrentRadian1 += (float) (2 * Math.PI);
                    }
                    mCurrentTime = (int) (60 / (2 * Math.PI) * mCurrentRadian1 * 60);
                    mMinutes = (int) (mCurrentRadian1 * RAD_TO_HOURS_MULTIPLIER * 5);
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mInCircleButton && isEnabled()) {
                    mInCircleButton = false;
                } else if (mInCircleButton1 && isEnabled()) {
                    mInCircleButton1 = false;
                }
                break;
        }
        return true;
    }


    // Whether the down event inside circle button
    private boolean mInCircleButton1(float x, float y) {
        float r = mRadius - mCircleStrokeWidth / 2 - mGapBetweenCircleAndLine;
        float x2 = (float) (mCx + r * Math.sin(mCurrentRadian1));
        float y2 = (float) (mCy - r * Math.cos(mCurrentRadian1));
        if (Math.sqrt((x - x2) * (x - x2) + (y - y2) * (y - y2)) < mCircleButtonRadius) {
            return true;
        }
        return false;
    }

    // Whether the down event inside circle button
    private boolean mInCircleButton(float x, float y) {
        float r = mRadius - mCircleStrokeWidth / 2 - mGapBetweenCircleAndLine;
        float x2 = (float) (mCx + r * Math.sin(mCurrentRadian));
        float y2 = (float) (mCy - r * Math.cos(mCurrentRadian));
        if (Math.sqrt((x - x2) * (x - x2) + (y - y2) * (y - y2)) < mCircleButtonRadius) {
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

        Log.d("/", "///////////////////");
        Log.d("h", String.valueOf(height));
        Log.d("w", String.valueOf(width));
        Log.d("mcy", String.valueOf(mCy));
        Log.d("mcx", String.valueOf(mCx));
        Log.d("/", "///////////////////");
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
        bundle.putFloat(STATUS_RADIAN, mCurrentRadian);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE_STATUS));
            mCurrentRadian = bundle.getFloat(STATUS_RADIAN);
            mCurrentTime = (int) (60 / (2 * Math.PI) * mCurrentRadian * 60);
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
}
