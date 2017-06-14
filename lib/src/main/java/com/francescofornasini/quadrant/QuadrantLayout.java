package com.francescofornasini.quadrant;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.francescofornasini.percentcontainer.R;


/**
 * Created by francesco on 10/01/15.
 */
public class QuadrantLayout extends CircularLayout {

    private static final int MIDDLE = 0;
    private static final int EDGE = 1;

    private static final int OVAL = 0;
    private static final int RECT = 1;

    private static final int DEFAULT_SECTOR_COUNT = 12;
    private static final int DEFAULT_SECTOR_TYPE = MIDDLE;
    private static final int DEFAULT_SHAPE = OVAL;

    private int mSectorCount;
    private int mSectorType;//TODO support this
    private int mShape;

    private PointF mCoordsHolder = new PointF();
    private PointF mCoeffsHolder = new PointF();

    public QuadrantLayout(Context context) {
        this(context, null);
    }

    public QuadrantLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QuadrantLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
    }

    public int getSectorCount() {
        return mSectorCount;
    }

    public void setSectorCount(int sectorCount) {
        this.mSectorCount = sectorCount;
        requestLayout();
    }

    public int getSectorType() {
        return mSectorType;
    }

    public void setSectorType(int sectorType) {
        this.mSectorType = sectorType;
        requestLayout();
    }

    public int getShape() {
        return mShape;
    }

    public void setShape(int shape) {
        this.mShape = shape;
        requestLayout();
    }

    private void initAttrs(Context context, AttributeSet attrs) {

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.QuadrantLayout,
                0, 0);

        try {

            mShape = a.getInt(R.styleable.QuadrantLayout_shape, DEFAULT_SHAPE);
            mSectorType = a.getInt(R.styleable.QuadrantLayout_sector_type, DEFAULT_SECTOR_TYPE);
            mSectorCount = a.getInt(R.styleable.QuadrantLayout_sector_count, DEFAULT_SECTOR_COUNT);

        } finally {
            a.recycle();
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {

        //size in radians of a single sector (sector == distance from center child to next center child)
        float sectorRadians = (mRadiansMax - mRadiansMin) / (float)mSectorCount;

        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);

            LayoutParams childParams = (LayoutParams) child.getLayoutParams();

            int sectorGravity = childParams.sectorGravity;
            float centerPadding = childParams.centerPadding;

            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();

            float coeffX = (float) Math.sin((sectorRadians * i + mRadiansMin) * Math.PI);
            float coeffY = (float) -Math.cos((sectorRadians * i + mRadiansMin) * Math.PI);

            float centerX = getWidth() / 2f;
            float centerY = getHeight() / 2f;

            computeChildCenterCoords(
                    sectorGravity, centerPadding,
                    coeffX, coeffY,
                    centerX, centerY,
                    childWidth, childHeight,
                    mShape, mCoordsHolder);

            float childCenterX = mCoordsHolder.x;
            float childCenterY = mCoordsHolder.y;

            int childLeft = (int) (childCenterX - childWidth / 2f);
            int childTop = (int) (childCenterY - childHeight / 2f);

            child.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);
        }
    }

    private void computeChildCenterCoords(
            int sectorGravity, float centerPadding,
            float coeffX, float coeffY,
            float centerX, float centerY,
            float childWidth, float childHeight,
            int shape, PointF result) {

        if (shape == OVAL) {

            float radiusX = centerX;
            float radiusY = centerY;


            //TODO i think this is a good approximation of the real value but it fails when child w !!= h
            if (sectorGravity == LayoutParams.SECTOR_GRAVITY_EXTERNAL_EDGE) {
                radiusX -= childWidth / 2f;
                radiusY -= childHeight / 2f;
            } else if (sectorGravity == LayoutParams.SECTOR_GRAVITY_CENTER) {
                radiusX *= centerPadding;
                radiusY *= centerPadding;
            }

            result.x = centerX + coeffX * radiusX;
            result.y = centerY + coeffY * radiusY;

        } else {
            //mShape == RECT

            float maxRadiusX = centerX;
            float maxRadiusY = centerY;

            //TODO i think this is a good approximation of the real value but it fails when child w !!= h
            if (sectorGravity == LayoutParams.SECTOR_GRAVITY_EXTERNAL_EDGE) {
                maxRadiusX -= childWidth / 2f;
                maxRadiusY -= childHeight / 2f;
            } else if (sectorGravity == LayoutParams.SECTOR_GRAVITY_CENTER) {
                maxRadiusX *= centerPadding;
                maxRadiusY *= centerPadding;
            }
            computeRectCoeffs(coeffX, coeffY, mCoeffsHolder);

            float newCoeffX = mCoeffsHolder.x;
            float newCoeffY = mCoeffsHolder.y;

            result.x = centerX + newCoeffX * maxRadiusX;
            result.y = centerY + newCoeffY * maxRadiusY;
        }
    }

    private void computeRectCoeffs(float coeffX, float coeffY, PointF result) {

        if (Math.abs(coeffX) > Math.abs(coeffY)) {
            result.y = Math.signum(coeffY) * Math.abs(coeffY / coeffX);
            result.x = Math.signum(coeffX);
        } else {
            result.x = Math.signum(coeffX) * Math.abs(coeffX / coeffY);
            result.y = Math.signum(coeffY);
        }
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return super.checkLayoutParams(p);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(getContext(), null);
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    public static class LayoutParams extends FrameLayout.LayoutParams {

        public static final int SECTOR_GRAVITY_EXTERNAL_EDGE = 0;
        public static final int SECTOR_GRAVITY_CENTER_EDGE = 1;
        public static final int SECTOR_GRAVITY_CENTER = 2;

        private static final int DEFAULT_SECTOR_GRAVITY = SECTOR_GRAVITY_CENTER_EDGE;
        private static final float DEFAULT_SECTOR_CENTER_PADDING = -1f;

        public float centerPadding;
        public int sectorGravity;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            initAttrs(c, attrs);
        }

        private void initAttrs(Context context, AttributeSet attrs) {

            TypedArray a = context.getTheme().obtainStyledAttributes(
                    attrs,
                    R.styleable.QuadrantLayout_LayoutParams,
                    0, 0);

            try {

                sectorGravity = a.getInt(R.styleable.QuadrantLayout_LayoutParams_layout_sector_gravity, DEFAULT_SECTOR_GRAVITY);
                centerPadding = a.getFloat(R.styleable.QuadrantLayout_LayoutParams_layout_sector_center_padding, DEFAULT_SECTOR_CENTER_PADDING);

            } finally {
                a.recycle();
            }
        }

        public LayoutParams(int width, int height) {
            super(width, height);
            throw new UnsupportedOperationException("Trying to use an unsupported LayoutParams constructor");
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);

            if (source instanceof LayoutParams) {

                LayoutParams lp = (LayoutParams) source;
                this.sectorGravity = lp.sectorGravity;
                this.centerPadding = lp.centerPadding;

            } else {

                this.sectorGravity = DEFAULT_SECTOR_GRAVITY;
                this.centerPadding = DEFAULT_SECTOR_CENTER_PADDING;
            }
        }
    }
}