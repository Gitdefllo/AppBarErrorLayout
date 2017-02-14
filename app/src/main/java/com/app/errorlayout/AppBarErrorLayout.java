package com.app.errorlayout;

import android.animation.Animator;
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

    private Toolbar toolbar;
    private RelativeLayout toolbarError; // error layout container

    private boolean isErrorVisible = false; // save the collapse/expand state
    private static int APPBAR_START_HEIGHT; // save the initial height of AppBarLayout
    private static int TOOLBAR_START_HEIGHT; // save the initial height of Toolbar

    public AppBarErrorLayout(Context context) {
        super(context);
    }

    public AppBarErrorLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public boolean getErrorVisibleState() {
        return this.isErrorVisible;
    }

    // set the initial height, which will be used to retrieve the inital state
    private void setMinimumHeight() {
        APPBAR_START_HEIGHT = this.getHeight(); // appbar's height
        TOOLBAR_START_HEIGHT = toolbar.getLayoutParams().height; // toolbar
    }

    // initialize the vars and the heights
    public void initErrorViews(Toolbar toolbar, RelativeLayout toolbarError) {
        this.toolbar = toolbar;
        this.toolbarError = toolbarError;
        this.setMinimumHeight();
    }

    // force to collapse the layout
    public void showErrorLayout() {
        // get the window height to expand the error layout
        final int parentHeight = ((ViewGroup) this.getParent()).getHeight();
        // final target height to keep a small space at bottom (optional)
        int targetHeight = parentHeight - TOOLBAR_START_HEIGHT;
        // animate the height of error layout
        animateAppbar(targetHeight, 3000, true);
    }

    // force to expand the layout
    public void hideErrorLayout() {
        // animate the height from current to the initial height
        animateAppbar(APPBAR_START_HEIGHT, 3000, false);
    }

    // target (int) = target height for expanding or collapsing to
    // duration (int) = duration of the animation
    // expanded (boolean) = for the listener to do specific things on
    //                      collapse or expand effect
    private void animateAppbar(final int target, int duration, final boolean expanded) {
        // set local variable
        final AppBarErrorLayout thiz = this;

        // save the current visible state
        this.isErrorVisible = expanded;

        // set a animation from current height to target
        ValueAnimator slideAnimator = ValueAnimator
                .ofInt(this.getHeight(), target)
                .setDuration(duration);

        // set a listener on animation
        slideAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                if (!expanded) {
                    // if we collapse, remove the error layout
                    toolbarError.setVisibility(View.GONE);
                } else {
                    // otherwise, show the error layout
                    toolbarError.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (!expanded) {
                    // if we force the collapse, we reset the initial height
                    // at the end of the animation
                    thiz.getLayoutParams().height = APPBAR_START_HEIGHT;
                    thiz.requestLayout();

                    toolbar.getLayoutParams().height = TOOLBAR_START_HEIGHT;
                    toolbar.requestLayout();
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) { }
            @Override
            public void onAnimationRepeat(Animator animator) { }
        });

        // the animation expands or collapses the widgets
        slideAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int newHeight = (Integer) animation.getAnimatedValue();

                thiz.getLayoutParams().height = newHeight;
                thiz.requestLayout();

                toolbar.getLayoutParams().height = newHeight;
                toolbar.requestLayout();
            }
        });

        slideAnimator.start();
    }
}
