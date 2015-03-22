package com.unicate.controller.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.telly.mrvector.MrVector;
import com.unicate.controller.R;


/**
 * This View abstracts the input of the two main controller units of a game controller.
 */
public abstract class InputView extends View {

	private Mode mode;

	public void setOnButtonListener(OnInputChangeListener onInputChangeListener) {
		listener = onInputChangeListener;
	}

	public interface OnInputChangeListener {
		void onButtonChanged(InputView view, int buttons);
        void onInputFinished(InputView view);
	}

	public enum ButtonState {
		NORMAL,
		PRESSED
	}

	protected enum Mode {
		SINGLE,
		MULTI
	}

    int width;
    int height;

    private Paint paint = new Paint();
    private int radius;

    private Matrix utilMat;
    private final Matrix pointMat = new Matrix();
    private final Rect utilRect = new Rect();
    private final RectF utilRectF = new RectF();

	// Vibration Setup
	private boolean vibratingEnabled = true;
	private static final long VIBRATION_DURATION = 30;
	private Vibrator vibrator;

    // angle
    private float degrees = 0f;

    private int buttonCount;

    private float deadZone = 200;

    private int buttonsPressed = 0;

    private float singleButtonAngle;

    private final float[] pointCenter = new float[]{0,0};

	private float buttonCenterDistance = 0.7f;

	private OnInputChangeListener listener;

	private boolean drawPieces = false;
	private boolean drawDeadZone = false;

	public InputView(Context context) {
        super(context);
    }

    public InputView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public InputView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public InputView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    protected void init(Context context, AttributeSet attrs, int defStyle) {
        MrVector.wrap(context);
		// set the number of buttons that should appear
		buttonCount = getButtonCount();
		singleButtonAngle = 360f/buttonCount;

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.InputView, 0, defStyle);
        if(!isInEditMode()) {
            // setup the vibrator only when not in edit mode
            vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        }
        // read the attributes from xml
        int background = a.getResourceId(R.styleable.InputView_inputBackground, R.drawable.action_background);
        try {
            // this is an additional spin parameter,where you can rotate the whole view
            degrees = a.getFloat(R.styleable.InputView_rotateButtons, 0);

            mode = Mode.values()[a.getInteger(R.styleable.InputView_mode, 0)];
            // enable/disable vibrating
            vibratingEnabled = a.getBoolean(R.styleable.InputView_vibrate, false);
            // set the size of the dead zone of the buttons
            deadZone = a.getFloat(R.styleable.InputView_deadZone, 0);
            // this is the radius of where the bitmaps should appear (from center in percent)
            buttonCenterDistance = a.getFloat(R.styleable.InputView_buttonCenterDistance, 0.5f);
            drawDeadZone = a.getBoolean(R.styleable.InputView_debug_drawDeadZone, false);
            drawPieces = a.getBoolean(R.styleable.InputView_debug_drawPieces, false);
        } finally {
            a.recycle();
        }

        Drawable backgroundDrawable = MrVector.inflate(getResources(), background);
        height = backgroundDrawable.getIntrinsicHeight();
        width = backgroundDrawable.getIntrinsicWidth();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            setBackground(backgroundDrawable);
        else
            setBackgroundDrawable(backgroundDrawable);

		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.WHITE);
    }

	@Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        //Measure Width
        if (widthMode == MeasureSpec.EXACTLY) {
            //Must be this size
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            width = Math.min(width, widthSize);
        }

        //Measure Height
        if (heightMode == MeasureSpec.EXACTLY) {
            //Must be this size
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            height = Math.min(height, heightSize);
        }
        if(height>width) {
            radius=width;
        } else {
            radius = height;
        }
        setMeasuredDimension(radius, radius);
		// radius is just half of the view side
        radius /=2;
        utilMat = null;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
		// reset the centerpoint to 0,0
        pointCenter[0] = pointCenter[1] = 0;
		// when there is no util matrix
        if(null == utilMat) {
			// reset the pointmatrix, cause the sized could've changed
            pointMat.reset();
			// setup matrix for rotating the points
            pointMat.setRotate(degrees);
			// create a util matrix
            utilMat = new Matrix();
			// translate smth to the middle of the screen
            utilMat.postTranslate(radius, radius);
			// apply this transformation onto the centerpoint
            utilMat.mapPoints(pointCenter);
			// get the actual rectangle of the view
            getDrawingRect(utilRect);
			// store it as RectF
            utilRectF.set(utilRect);
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
		float tmp = radius * buttonCenterDistance;
        int drawButtons = forceDrawButtons(buttonsPressed);
        for(int i=0;i<buttonCount;i++) {
            float cAngle = (singleButtonAngle * i);
			Drawable drawable;
			if(((0x1 << i)&drawButtons)>0) {
				if(drawPieces) {
                    paint.setColor(Color.GRAY);
                    canvas.drawArc(utilRectF, cAngle - degrees, singleButtonAngle, true, paint);
                }
                drawable = getStateDrawable(i, ButtonState.PRESSED);
            } else {
                drawable = getStateDrawable(i, ButtonState.NORMAL);
			}
			if(null != drawable) {
                int height = drawable.getIntrinsicHeight();
                int width = drawable.getIntrinsicWidth();
                int left = (int)((tmp * Math.cos(Math.toRadians(cAngle - degrees + singleButtonAngle / 2f))) + radius) - width / 2;
                int top = (int)((tmp * Math.sin(Math.toRadians(cAngle - degrees + singleButtonAngle / 2f))) + radius) - height / 2;
                drawable.setBounds(left, top, left+width,top+height);
                drawable.draw(canvas);
			}
        }
        // Deadpart
		if(drawDeadZone) {
			paint.setColor(Color.DKGRAY);
			canvas.drawCircle(pointCenter[0], pointCenter[1], deadZone * radius, paint);
		}
    }


    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
		int action = MotionEventCompat.getActionMasked(event);
		// Get the index of the pointer associated with the action.
		int index = MotionEventCompat.getActionIndex(event);
        float xPos = (int) MotionEventCompat.getX(event, index);
        float yPos = (int) MotionEventCompat.getY(event, index);
		switch (action) {
			case MotionEvent.ACTION_DOWN:
			case MotionEvent.ACTION_POINTER_DOWN:
			case MotionEvent.ACTION_MOVE:
				update(xPos-radius, yPos-radius);
				return true;
			case MotionEvent.ACTION_UP:
				buttonsPressed = 0;
                if(null != listener) {
                    listener.onInputFinished(this);
                }
				invalidate();
		}

        return super.onTouchEvent(event);
    }

    private void update(float x, float y) {
        float[] point = new float[]{x, y};
		float tmp = radius*deadZone;
		// if touch is outside of deadzone
        if((x*x+y*y) > tmp * tmp) {
            pointMat.mapPoints(point);
            x = point[0];
            y = point[1];
            float angle = (float) ((Math.atan2(y, x) * 180 / Math.PI) + 360f) % 360f;
            int field = (int) (angle / singleButtonAngle);
			int current = (0x1 << field);

			if((buttonsPressed&current)==0) {
				vibrate();
				if(Mode.MULTI.equals(mode)) {
					buttonsPressed |= current;
				} else {
					buttonsPressed = current;
				}
				if(null != listener) {
					listener.onButtonChanged(this, buttonsPressed);
				}
			}
        }
        postInvalidate();
    }

    public void vibrate() {
        if(vibratingEnabled) {
            vibrator.vibrate(VIBRATION_DURATION);
        }
    }

    protected int forceDrawButtons(int buttonPressed) {
        return buttonPressed;
    }

    /**
     * This method has to return a drawable for a button or <code>null</code>
     * @param buttonIndex Index of the button, which needs to be drawn
     * @param state State of the button ({@link ButtonState})
     * @return a Drawable to draw this button
     */
	protected abstract Drawable getStateDrawable(int buttonIndex, ButtonState state);

    /**
     * @return The amount of Buttons this View represents
     */
	protected abstract int getButtonCount();
}
