package com.app.errorlayout;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

@SuppressWarnings("unused")
public class AppBarErrorLayout extends AppBarLayout {

    private Toolbar tlb; // usual toolbar
    private RelativeLayout tlberr; // error layout container

    private static int APPBAR_START_HEIGHT; // save the initial height of AppBarLayout
    private static int TOOLBAR_START_HEIGHT; // save the initial height of Toolbar
    private static int ERRORLAYOUT_MIN_HEIGHT; // get the minimum height of ErrorLayout

    private final static int DURATION = 300; // duration for expand/collapse animation
    private boolean isErrorVisible = false; // save the collapse/expand state

    public AppBarErrorLayout(Context context) {
        super(context);
    }

    public AppBarErrorLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public boolean getErrorVisibleState() {
        return this.isErrorVisible;
    }

    // set the initial heights, which will be used to retrieve the inital states
    private void setMinimumHeight() {
        APPBAR_START_HEIGHT = this.getHeight(); // appbar's height
        TOOLBAR_START_HEIGHT = tlb.getLayoutParams().height; // toolbar
        ERRORLAYOUT_MIN_HEIGHT = ((ViewGroup)
                this.getParent()).getHeight() - TOOLBAR_START_HEIGHT; // errorlayout
    }

    // initialize the vars and the heights
    public void initErrorViews(final Toolbar toolbar,
                               final RelativeLayout errorlayout) {
        // we need to get the height of the toolbar so we need
        // a thread to wait until the UI is displayed
        this.post(new Runnable() {
            @Override
            public void run() {
                tlb = toolbar;
                tlberr = errorlayout;

                setMinimumHeight();
            }
        });
    }

    // force to collapse the layout
    public void showErrorLayout() {
        // save the current visible state
        this.isErrorVisible = true;
        // expand appbar and toolbar
        expandbar();
    }

    // force to expand the layout
    public void hideErrorLayout() {
        // save the current visible state
        this.isErrorVisible = false;
        // animate the height from current to the initial height
        collapsebar(this, APPBAR_START_HEIGHT);
        collapsebar(tlb, TOOLBAR_START_HEIGHT);
    }

    // animate the appbar and toolbar height to expanded mode
    private void expandbar() {
        // set a animation from current height to target
        ValueAnimator slideAnimator = ValueAnimator
                .ofInt(this.getHeight(), ERRORLAYOUT_MIN_HEIGHT)
                .setDuration(DURATION);

        // set local variable
        final AppBarErrorLayout thiz = this;

        // the animation expands the widgets
        slideAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int newHeight = (Integer) animation.getAnimatedValue();

                thiz.getLayoutParams().height = newHeight;
                thiz.requestLayout();

                tlb.getLayoutParams().height = newHeight;
                tlb.requestLayout();
            }
        });

        // show the error layout
        tlberr.setVisibility(View.VISIBLE);
        slideAnimator.start();
    }

    // animate the widgets height to collapsed mode
    private void collapsebar(final View target, final int targetHeight) {
        // set a animation from current height to target
        ValueAnimator slideAnimator = ValueAnimator
                .ofInt(target.getHeight(), targetHeight)
                .setDuration(DURATION);

        // the animation collapses the widgets
        slideAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                target.getLayoutParams().height = (Integer) animation.getAnimatedValue();
                target.requestLayout();
            }
        });

        // hide the error layout
        tlberr.setVisibility(View.GONE);
        slideAnimator.start();
    }
}
