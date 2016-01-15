package com.zad10.analogclock;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.text.format.Time;
import android.util.AttributeSet;
import android.view.View;

public class AnalogClock extends View {

    private Time mCalendar;
    private Drawable mHourHand;
    private Drawable mMinuteHand;
    private Drawable mSecondHand;
    private Drawable mDial;
    private int mDialWidth;
    private int mDialHeight;
    private boolean mAttached;
    private float mMinutes;
    private float mHour;
    private boolean mChanged;
    private Context mContext;

    boolean mSeconds = false;
    float mSecond = 0;

    public AnalogClock(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AnalogClock(Context context, AttributeSet attrs,
                        int defStyle) {
        super(context, attrs, defStyle);
        Resources r = context.getResources();
        TypedArray a =
                context.obtainStyledAttributes(
                        attrs, R.styleable.AnalogClock, defStyle, 0);
        mContext = context;

        mDial = r.getDrawable(R.drawable.clock_dial);

        mHourHand = r.getDrawable(R.drawable.clock_hour);

        mMinuteHand = r.getDrawable(R.drawable.clock_minute);
        mSecondHand = r.getDrawable(R.drawable.clockgoog_minute);

        mCalendar = new Time();

        mDialWidth = mDial.getIntrinsicWidth();
        mDialHeight = mDial.getIntrinsicHeight();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mAttached) {
            mAttached = false;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        float hScale = 1.0f;
        float vScale = 1.0f;

        if (widthMode != MeasureSpec.UNSPECIFIED && widthSize < mDialWidth) {
            hScale = (float) widthSize / (float) mDialWidth;
        }

        if (heightMode != MeasureSpec.UNSPECIFIED && heightSize < mDialHeight) {
            vScale = (float) heightSize / (float) mDialHeight;
        }

        float scale = Math.min(hScale, vScale);

        setMeasuredDimension(resolveSize((int) (mDialWidth * scale), widthMeasureSpec),
                resolveSize((int) (mDialHeight * scale), heightMeasureSpec));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mChanged = true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        boolean changed = mChanged;
        if (changed) {
            mChanged = false;
        }
        boolean seconds = mSeconds;
        if (seconds) {
            mSeconds = false;
        }
        int availableWidth = 600;
        int availableHeight = 600;

        int x = availableWidth / 2;
        int y = availableHeight / 2;

        final Drawable dial = mDial;
        int w = dial.getIntrinsicWidth();
        int h = dial.getIntrinsicHeight();

        boolean scaled = false;

        if (availableWidth < w || availableHeight < h) {
            scaled = true;
            float scale = Math.min((float) availableWidth / (float) w,
                    (float) availableHeight / (float) h);
            canvas.save();
            canvas.scale(scale, scale, x, y);
        }

        if (changed) {
            dial.setBounds(x - (w / 2), y - (h / 2), x + (w / 2), y + (h / 2));
        }
        dial.draw(canvas);

        canvas.save();
        canvas.rotate(mHour / 12.0f * 360.0f, x, y);
        final Drawable hourHand = mHourHand;
        if (changed) {
            w = hourHand.getIntrinsicWidth();
            h = hourHand.getIntrinsicHeight();
            hourHand.setBounds(x - (w / 2), y - (h / 2), x + (w / 2), y + (h / 2));
        }
        hourHand.draw(canvas);
        canvas.restore();

        canvas.save();
        canvas.rotate(mMinutes / 60.0f * 360.0f, x, y);
        //canvas.rotate(mSecond, x, y);
        final Drawable minuteHand = mMinuteHand;
        if (changed) {
            w = minuteHand.getIntrinsicWidth();
            h = minuteHand.getIntrinsicHeight();
            minuteHand.setBounds(x - (w / 2), y - (h / 2), x + (w / 2), y + (h / 2));
        }
        minuteHand.draw(canvas);
        canvas.restore();
        canvas.save();
        canvas.rotate(mSecond, x, y);

        //minuteHand = mMinuteHand;
        if (seconds) {
            w = mSecondHand.getIntrinsicWidth();
            h = mSecondHand.getIntrinsicHeight();
            mSecondHand.setBounds(x - (w / 2), y - (h / 2), x + (w / 2), y + (h / 2));
        }
        mSecondHand.draw(canvas);
        canvas.restore();
        if (scaled) {
            canvas.restore();
        }
    }


    public void setTime(int hour, int minute, int second) {
        mMinutes = minute + second / 60.0f;
        mHour = hour + mMinutes / 60.0f;
        mSecond = 6.0f * second;
            mSeconds = true;
        mChanged = true;
    }
}
