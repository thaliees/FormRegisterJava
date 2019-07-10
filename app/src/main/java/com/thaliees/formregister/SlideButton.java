package com.thaliees.formregister;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SlideButton extends RelativeLayout {
    public ImageView toSlider;
    public Integer initWidthToSlider;
    public Boolean expanded;
    private Drawable arrow, check;

    public SlideButton(Context context) {
        super(context);
        initSlideButton(context);
    }

    public SlideButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initSlideButton(context);
    }

    public SlideButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initSlideButton(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SlideButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initSlideButton(context);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void initSlideButton(Context context){
        RelativeLayout slideButton = new RelativeLayout(context);
        slideButton.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_rounded_background));
        LayoutParams layoutParamsSlideButton = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParamsSlideButton.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        addView(slideButton, layoutParamsSlideButton);

        // The TextView indicate that the button must be slid
        final TextView text = new TextView(context);
        text.setText(getResources().getString(R.string.text_register));
        text.setTextColor(getResources().getColor(R.color.colorWhite));
        text.setTextSize(20);
        text.setTypeface(null, Typeface.BOLD);
        LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        slideButton.addView(text, layoutParams);

        // Define icons
        arrow = ContextCompat.getDrawable(context, R.drawable.ic_arrow);
        check = ContextCompat.getDrawable(context, R.drawable.ic_check);

        // Indicate that ImageView isn't expanded
        expanded = false;

        // The ImageView will behave like the sliding button
        final ImageView toSlider = new ImageView(context);
        this.toSlider = toSlider;
        toSlider.setImageDrawable(arrow);   // Default drawable
        toSlider.setPadding(60, 40, 60, 40);
        toSlider.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_rounded));
        LayoutParams layoutParamsToSlider = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParamsToSlider.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        layoutParamsToSlider.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        slideButton.addView(toSlider, layoutParamsToSlider);
    }

    public void expandButton(){
        // Indicate the current point of our ImageView is and the point where it should arrive
        final ValueAnimator position = ValueAnimator.ofFloat(toSlider.getX(), 0);
        // Indicate the width of our ImageView and how wide it will take
        final ValueAnimator width = ValueAnimator.ofInt(toSlider.getWidth(), getWidth());

        // Update the position of our ImageView
        position.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float x = (Float) position.getAnimatedValue();
                toSlider.setX(x);
            }
        });

        // Update the width of our ImageView
        width.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                ViewGroup.LayoutParams layoutParamsToMove = toSlider.getLayoutParams();
                layoutParamsToMove.width = (Integer) width.getAnimatedValue();
                toSlider.setLayoutParams(layoutParamsToMove);
            }
        });

        // Initialize animation
        AnimatorSet animator = new AnimatorSet();
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                toSlider.setImageDrawable(check);   // Change icon to show
                expanded = true;                    // ImageView expanded
            }
        });
        animator.playTogether(position, width);
        //animator.setDuration(2000);           // See animation slow
        animator.start();
    }

    public void moveButtonBack(){
        // Indicate the current point of our ImageView is and the point where it should arrive
        final ValueAnimator position = ValueAnimator.ofFloat(toSlider.getX(), 0);
        position.setDuration(1000);  // Also try animator.setDuration(1000);

        // Update the position of our ImageView
        position.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float x = (Float) position.getAnimatedValue();
                toSlider.setX(x);
            }
        });

        // Initialize animation
        AnimatorSet animator = new AnimatorSet();
        animator.playTogether(position);
        animator.start();
    }

    public void collapseButton(){
        // Indicate the width of our ImageView and how wide it will take
        final ValueAnimator width = ValueAnimator.ofInt(toSlider.getWidth(), initWidthToSlider);

        // Update the width of our ImageView
        width.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                ViewGroup.LayoutParams layoutParamsToMove = toSlider.getLayoutParams();
                layoutParamsToMove.width = (Integer) width.getAnimatedValue();
                toSlider.setLayoutParams(layoutParamsToMove);
            }
        });

        // Also can addListener from the ValueAnimator
        width.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                toSlider.setImageDrawable(arrow);   // Change icon to show
                expanded = false;                   // ImageView not expanded
            }
        });

        AnimatorSet animator = new AnimatorSet();
        animator.playTogether(width);
        animator.start();
    }
}
