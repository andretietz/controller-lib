package com.andretietz.android.controller;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.content.res.AppCompatResources;
import android.util.AttributeSet;

/**
 * This View can be used for direction input on a game controller. it has 8 different directions,
 * as it was on older controllers.
 */
public class DirectionView extends InputView {

    public static final int DIRECTION_RIGHT = 0x01;
    public static final int DIRECTION_DOWN_RIGHT = 0x02;
    public static final int DIRECTION_DOWN = 0x04;
    public static final int DIRECTION_DOWN_LEFT = 0x08;
    public static final int DIRECTION_LEFT = 0x10;
    public static final int DIRECTION_UP_LEFT = 0x20;
    public static final int DIRECTION_UP = 0x40;
    public static final int DIRECTION_UP_RIGHT = 0x80;
    private static final int BUTTON_COUNT = 8;
    // @formatter:off
    private static final int BUTTON_RIGHT = 0;
    private static final int BUTTON_DOWN_RIGHT = 1;
    private static final int BUTTON_DOWN = 2;
    private static final int BUTTON_DOWN_LEFT = 3;
    private static final int BUTTON_LEFT = 4;
    private static final int BUTTON_UP_LEFT = 5;
    private static final int BUTTON_UP = 6;
    private static final int BUTTON_UP_RIGHT = 7;
    // @formatter:on
    private boolean diagonalMode = false;
    private Drawable[][] drawables = new Drawable[BUTTON_COUNT][2];
    private int[][] resources = new int[BUTTON_COUNT][2];

    public DirectionView(Context context) {
        super(context);
    }

    public DirectionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public DirectionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DirectionView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    protected void init(Context context, AttributeSet attrs) {
        if (null != attrs) {
            TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.DirectionView, 0, R.style.default_directionview);
            super.init(context, attrs, R.style.default_directionview);
            try {
                resources[BUTTON_RIGHT][0] = a.getResourceId(R.styleable.DirectionView_button_r_normal, 0);
                resources[BUTTON_RIGHT][1] = a.getResourceId(R.styleable.DirectionView_button_r_pressed, 0);
                resources[BUTTON_DOWN][0] = a.getResourceId(R.styleable.DirectionView_button_d_normal, 0);
                resources[BUTTON_DOWN][1] = a.getResourceId(R.styleable.DirectionView_button_d_pressed, 0);
                resources[BUTTON_LEFT][0] = a.getResourceId(R.styleable.DirectionView_button_l_normal, 0);
                resources[BUTTON_LEFT][1] = a.getResourceId(R.styleable.DirectionView_button_l_pressed, 0);
                resources[BUTTON_UP][0] = a.getResourceId(R.styleable.DirectionView_button_u_normal, 0);
                resources[BUTTON_UP][1] = a.getResourceId(R.styleable.DirectionView_button_u_pressed, 0);


                diagonalMode = a.getBoolean(R.styleable.DirectionView_diagonal_mode, false);
                if (diagonalMode) {
                    resources[BUTTON_DOWN_RIGHT][0] = a.getResourceId(R.styleable.DirectionView_button_dr_normal, 0);
                    resources[BUTTON_DOWN_RIGHT][1] = a.getResourceId(R.styleable.DirectionView_button_dr_pressed, 0);
                    resources[BUTTON_DOWN_LEFT][0] = a.getResourceId(R.styleable.DirectionView_button_dl_normal, 0);
                    resources[BUTTON_DOWN_LEFT][1] = a.getResourceId(R.styleable.DirectionView_button_dl_pressed, 0);
                    resources[BUTTON_UP_LEFT][0] = a.getResourceId(R.styleable.DirectionView_button_ul_normal, 0);
                    resources[BUTTON_UP_LEFT][1] = a.getResourceId(R.styleable.DirectionView_button_ul_pressed, 0);
                    resources[BUTTON_UP_RIGHT][0] = a.getResourceId(R.styleable.DirectionView_button_ur_normal, 0);
                    resources[BUTTON_UP_RIGHT][1] = a.getResourceId(R.styleable.DirectionView_button_ur_pressed, 0);
                }
            } finally {
                a.recycle();
            }
        }
    }

    @Override
    protected Drawable getStateDrawable(int buttonIndex, ButtonState state) {
        // if diagonal mode is activated and one of the diagonal buttons are pressed
        if (diagonalMode && (buttonIndex == 1 || buttonIndex == 3 || buttonIndex == 5 || buttonIndex == 7)) {
            return null;
        }
        if (null == drawables[buttonIndex][state.ordinal()]) {
            drawables[buttonIndex][state.ordinal()] = AppCompatResources.getDrawable(getContext(), resources[buttonIndex][state.ordinal()]);
        }
        return drawables[buttonIndex][state.ordinal()];
    }

    @SuppressWarnings("PointlessBitwiseExpression")
    @Override
    protected int forceDrawButtons(int buttonPressed) {
        if (diagonalMode) {
            for (int i = 1; i < BUTTON_COUNT; i += 2) {
                if (((0x1 << i) & buttonPressed) > 0) {
                    switch (i) {
                        case BUTTON_DOWN_RIGHT:
                            return buttonPressed | (0x1 << BUTTON_DOWN) | (0x1 << BUTTON_RIGHT);
                        case BUTTON_DOWN_LEFT:
                            return buttonPressed | (0x1 << BUTTON_DOWN) | (0x1 << BUTTON_LEFT);
                        case BUTTON_UP_LEFT:
                            return buttonPressed | (0x1 << BUTTON_UP) | (0x1 << BUTTON_LEFT);
                        case BUTTON_UP_RIGHT:
                            return buttonPressed | (0x1 << BUTTON_UP) | (0x1 << BUTTON_RIGHT);
                    }
                }
            }
        }
        return buttonPressed;
    }

    @Override
    protected int getButtonCount() {
        return BUTTON_COUNT;
    }
}
