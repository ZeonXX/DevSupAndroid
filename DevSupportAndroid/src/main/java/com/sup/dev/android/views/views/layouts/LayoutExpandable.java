package com.sup.dev.android.views.views.layouts;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.AnyThread;
import android.support.annotation.MainThread;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.RotateAnimation;
import android.widget.LinearLayout;

import com.sup.dev.android.R;

public class LayoutExpandable extends LinearLayout {

    private static final int DEFAULT_DURATION = 300;

    private int mWidth;
    private int mHeight;
    private int mDuration;
    private int mExpandDuration;
    private int mCollapseDuration;

    private boolean mIsInited;
    private boolean mIsExpand;
    private boolean mIsExecuting;
    private boolean mIsClickToToggle;

    private View mSwitcher;
    private OnStateChangedListener mListener;
    private Interpolator mExpandInterpolator;
    private Interpolator mCollapseInterpolator;


    @MainThread
    public LayoutExpandable(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LayoutExpandable);
        mDuration = a.getInt(R.styleable.LayoutExpandable_LayoutExpandable_duration, DEFAULT_DURATION);
        mIsClickToToggle = a.getBoolean(R.styleable.LayoutExpandable_LayoutExpandable_clickToToggle, false);
        mIsExpand = a.getBoolean(R.styleable.LayoutExpandable_LayoutExpandable_expanded, true);
        a.recycle();

        mExpandDuration = (mDuration == 0 ? DEFAULT_DURATION : mDuration);
        mCollapseDuration = (mDuration == 0 ? DEFAULT_DURATION : mDuration);
        setOnClickListener(mIsClickToToggle ? (OnClickListener) v -> toggle() : null);
    }

    //
    //  Expand control
    //

    @MainThread
    public void expand() {
        if (mIsExpand || mIsExecuting) return;

        executeExpand(this);
        startSwitcherAnimation();
    }

    @MainThread
    public void collapse() {
        if (!mIsExpand || mIsExecuting) return;

        executeCollapse(this);
        startSwitcherAnimation();
    }

    @MainThread
    public void toggle() {
        if (mIsExpand) collapse();
        else expand();

    }

    //
    //  Methods
    //

    @MainThread
    private void startSwitcherAnimation() {
        if (mSwitcher != null) {
            int duration = (mDuration == 0 ? DEFAULT_DURATION : (mIsExpand ? mExpandDuration : mCollapseDuration));
            Animation rotateAnimation = createRotateAnimation(mSwitcher, duration);
            mSwitcher.startAnimation(rotateAnimation);
        }
    }

    @MainThread
    private int measureChildWidth() {
        int width = 0;
        int cnt = getChildCount();
        for (int i = 0; i < cnt; i++) width += getChildAt(i).getMeasuredWidth();

        return width;
    }

    @MainThread
    private int measureChildHeight() {
        int height = 0;
        int cnt = getChildCount();
        for (int i = 0; i < cnt; i++) height += getChildAt(i).getMeasuredHeight();

        return height;
    }

    @MainThread
    private int measureDimension(int defaultSize, int measureSpec) {
        int result;

        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        switch (specMode) {
            case MeasureSpec.UNSPECIFIED:
                result = specSize;
                break;
            case MeasureSpec.EXACTLY:
                result = specSize;
                break;
            case MeasureSpec.AT_MOST:
                result = Math.min(defaultSize, specSize);
                break;
            default:
                result = specSize;
                break;
        }

        return result;
    }

    @MainThread
    private RotateAnimation createRotateAnimation(final View view, int duration) {
        int pivotX = view.getWidth() >> 1;
        int pivotY = view.getHeight() >> 1;
        RotateAnimation animation = new RotateAnimation(mIsExpand ? 0 : -180, mIsExpand ? -180 : 0, pivotX, pivotY);
        animation.setDuration(duration);
        animation.setFillAfter(true);
        return animation;
    }

    @MainThread
    private ValueAnimator createAnimator(final View view, int startPos, int endPos) {
        boolean isExpand = startPos < endPos;
        int duration = (mDuration == 0 ? DEFAULT_DURATION : (isExpand ? mExpandDuration : mCollapseDuration));
        return this.createAnimator(view, startPos, endPos, duration);
    }

    @MainThread
    private ValueAnimator createAnimator(final View view, int startPos, int endPos, int duration) {
        ValueAnimator animator = ValueAnimator.ofInt(startPos, endPos);
        animator.setDuration(duration);
        animator.addUpdateListener(animation -> {
            int newPos = (Integer) animation.getAnimatedValue();
            int orientation = ((LayoutExpandable) view).getOrientation();
            ViewGroup.LayoutParams params = view.getLayoutParams();
            if (LinearLayout.HORIZONTAL == orientation) {
                params.width = newPos;
                params.height = getMeasuredHeight();
                view.setLayoutParams(params);
                return;
            }
            if (LinearLayout.VERTICAL == orientation) {
                params.width = getMeasuredWidth();
                params.height = newPos;
                view.setLayoutParams(params);
            }
        });
        return animator;
    }

    @MainThread
    private void executeExpand(final View view) {
        mIsExpand = !mIsExpand;
        setVisibility(View.VISIBLE);
        int newPos = (getOrientation() == HORIZONTAL ? mWidth : mHeight);
        Animator animator = createAnimator(view, 0, newPos);
        animator.setInterpolator(mExpandInterpolator);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                setExecuting(true);
                if (mListener != null) {
                    mListener.onPreExpand();
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                setExecuting(false);
                if (mListener != null) {
                    mListener.onExpanded();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
    }

    @MainThread
    private void executeCollapse(final View view) {
        mIsExpand = !mIsExpand;
        int newPos = (getOrientation() == HORIZONTAL ? mWidth : mHeight);
        ValueAnimator animator = createAnimator(view, newPos, 0);
        animator.setInterpolator(mCollapseInterpolator);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                setExecuting(true);
                if (mListener != null) {
                    mListener.onPreCollapse();
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                setExecuting(false);
                setVisibility(View.GONE);
                if (mListener != null) {
                    mListener.onCollapsed();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
    }

    //
    //  Setters
    //
    @AnyThread
    public void setClickToToggle(boolean isClickToToggle) {
        mIsClickToToggle = isClickToToggle;
    }

    @AnyThread
    public void setOnStateChangedListener(OnStateChangedListener l) {
        mListener = l;
    }

    @AnyThread
    public void setDuration(int duration) {
        mDuration = duration;
        this.setExpandDuration(duration);
        this.setCollapseDuration(duration);
    }

    @AnyThread
    public void setExpandDuration(int expandDuration) {
        mExpandDuration = expandDuration;
    }

    @AnyThread
    public void setCollapseDuration(int collapseDuration) {
        mCollapseDuration = collapseDuration;
    }

    @MainThread
    public void setExpand(boolean isExpand) {
        mIsExpand = isExpand;
        requestLayout();
    }

    @AnyThread
    public void setSwitcher(View switcher) {
        mSwitcher = switcher;
    }

    @AnyThread
    public void setInterpolator(Interpolator interpolator) {
        this.setExpandInterpolator(interpolator);
        this.setCollapseInterpolator(interpolator);
    }

    @AnyThread
    public void setExpandInterpolator(Interpolator expandInterpolator) {
        mExpandInterpolator = expandInterpolator;
    }

    @AnyThread
    public void setCollapseInterpolator(Interpolator collapseInterpolator) {
        mCollapseInterpolator = collapseInterpolator;
    }

    @AnyThread
    private void setExecuting(boolean isExecuting) {
        mIsExecuting = isExecuting;
    }

    //
    //  Events
    //

    @Override
    @MainThread
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = measureDimension(measureChildWidth(), widthMeasureSpec);
        int height = measureDimension(measureChildHeight(), heightMeasureSpec);
        mWidth = Math.max(mWidth, width);
        mHeight = Math.max(mHeight, height);
        setMeasuredDimension(width, height);
    }

    @Override
    @MainThread
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (!changed) return;

        if (!mIsInited) {
            setVisibility(mIsExpand ? VISIBLE : GONE);
            mIsInited = true;
        }
    }

    //
    //  Listener
    //

    public interface OnStateChangedListener {
        @MainThread
        void onPreExpand();

        @MainThread
        void onPreCollapse();

        @MainThread
        void onExpanded();

        @MainThread
        void onCollapsed();
    }
}
