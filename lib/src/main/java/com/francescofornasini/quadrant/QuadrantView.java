package com.francescofornasini.quadrant;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.hardware.SensorEvent;
import android.text.format.Time;
import android.util.AttributeSet;
import android.view.View;

import com.francescofornasini.percentcontainer.R;


/**
 * Created by francesco on 02/01/15.
 */
public class QuadrantView extends CircularView {

    private static final int MIDDLE = 0;
    private static final int EDGE = 1;

    private static final int DEFAULT_SEGMENT_TYPE = MIDDLE;
    private static final int DEFAULT_SEGMENT_COUNT = 3;
    private static final float DEFAULT_SEGMENT_WIDTH = 1f;
    private static final float DEFAULT_SEGMENT_HEIGHT = 0.1f;
    private static final float DEFAULT_SEGMENT_CENTER_PADDING = 0.9f;
    private static final int DEFAULT_SEGMENT_COLOR = Color.RED;


    private int mSegmentType;
    private int mSegmentCount;

    private float mSegmentWidth;
    private float mSegmentHeight;
    private float mSegmentCenterPadding;

    private int mSegmentColor;
    private final Paint mPaint;
    private RectF mRect = new RectF();

    public QuadrantView(Context context) {
        this(context, null);
    }

    public QuadrantView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QuadrantView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initAttrs(context, attrs);

        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setColor(mSegmentColor);

        //TODO :p
        mPaint.setAntiAlias(true);

    }

    private void initAttrs(Context context, AttributeSet attrs) {

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.QuadrantView,
                0, 0);

        try {

            mSegmentType = a.getInt(R.styleable.QuadrantView_segment_type, DEFAULT_SEGMENT_TYPE);
            mSegmentCount = a.getInt(R.styleable.QuadrantView_segment_count, DEFAULT_SEGMENT_COUNT);

            mSegmentWidth = a.getFloat(R.styleable.QuadrantView_segment_width, DEFAULT_SEGMENT_WIDTH);
            mSegmentHeight = a.getFloat(R.styleable.QuadrantView_segment_height, DEFAULT_SEGMENT_HEIGHT);
            mSegmentCenterPadding = a.getFloat(R.styleable.QuadrantView_segment_center_padding, DEFAULT_SEGMENT_CENTER_PADDING);

            mSegmentColor = a.getColor(R.styleable.QuadrantView_segment_color, DEFAULT_SEGMENT_COLOR);

        } finally {
            a.recycle();
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {

        float availableDegrees = (mRadiansMax - mRadiansMin) * 180f;
        float sweep = (availableDegrees / (float)mSegmentCount);
        float segmentWidth = sweep * mSegmentWidth;
        float segmentPadding = sweep * ((1f - mSegmentWidth) / 2f);

        float availableSegmentHeight = Math.min(getWidth() / 2f, getHeight() / 2f);
        float segmentHeight = availableSegmentHeight * mSegmentHeight;
        float segmentCenterPadding = availableSegmentHeight * mSegmentCenterPadding;

        mRect.top = availableSegmentHeight - (segmentCenterPadding + (segmentHeight / 2f));
        mRect.left = availableSegmentHeight - (segmentCenterPadding + (segmentHeight / 2f));
        mRect.right = (float)getWidth() - mRect.left;
        mRect.bottom = (float)getHeight() - mRect.top;

        mPaint.setStrokeWidth(segmentHeight);

        float startPadding;
        if (mSegmentType == EDGE) {
            startPadding = segmentPadding;
        } else if (mSegmentType == MIDDLE) {
            startPadding = - (sweep / 2f);
        } else {
            //CLog.i(this, "ERROR!! startPadding at zero");
            startPadding = 0f;
        }

        /*
         * drawarc starts from the angle at three Oclock
         * so i have to adjust the offset also according to this
         */
        startPadding = (startPadding + mRadiansMin * 180f) - 90f;


        for (int i = 0; i < mSegmentCount; i++) {

            canvas.drawArc(mRect, i * sweep + startPadding, segmentWidth, false, mPaint);
        }
    }
}