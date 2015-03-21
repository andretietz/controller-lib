package com.unicate.controller.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.unicate.controller.model.ControllerInput;
import com.unicate.controller.R;

/**
 * This View bundles the {@link ActionView} and the {@link DirectionView} together
 */
public class ControllerView extends RelativeLayout {


	private ControllerInput input;

	private boolean actionActive = false;
	private boolean directionActive = false;

	private ControllerInputListener listener;

	private InputView.OnInputChangeListener controllerInputListener = new InputView.OnInputChangeListener() {
		@Override public void onButtonChanged(InputView view, int buttons) {
			if (null == input) {
				input = new ControllerInput();
			}
			if (view.getId() == R.id.viewAction) {
				actionActive = true;
				input.setAction(buttons);

			} else if (view.getId() == R.id.viewDirection) {
				if (directionActive && null != listener) {
					if (!actionActive) {
						listener.onInputFinished(input);
						input = new ControllerInput();
					} else {
						input.resetDirection();
					}
				}
				directionActive = true;
				input.setDirection(buttons);
			}
			if (null != listener) {
				listener.onInputUpdated(input);
			}
		}

		@Override public void onInputFinished(InputView view) {
			if (view.getId() == R.id.viewAction) {
				actionActive = false;
			} else if (view.getId() == R.id.viewDirection) {
				directionActive = false;

			}
			if (!actionActive && !directionActive && null != listener) {
				listener.onInputFinished(input);
				input = new ControllerInput();
			}
		}
	};

	public ControllerView(Context context) {
		super(context);
		init(context);
	}

	public ControllerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public ControllerView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP) public ControllerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		init(context);
	}

	private void init(Context context) {
		View view = inflate(context, R.layout.controller_view, this);
		if (!isInEditMode()) {
			ActionView actionView = (ActionView) view.findViewById(R.id.viewAction);
			DirectionView directionView = (DirectionView) view.findViewById(R.id.viewDirection);
			actionView.setOnButtonListener(controllerInputListener);
			directionView.setOnButtonListener(controllerInputListener);
		}
	}

	public void setOnControllerInputListener(ControllerInputListener listener) {
		this.listener = listener;
	}


	public interface ControllerInputListener {
		void onInputUpdated(ControllerInput input);

		void onInputFinished(ControllerInput input);
	}
}
