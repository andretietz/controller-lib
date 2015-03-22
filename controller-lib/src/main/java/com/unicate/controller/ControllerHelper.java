package com.unicate.controller;

import com.unicate.controller.model.ControllerInput;
import com.unicate.controller.views.ActionView;
import com.unicate.controller.views.DirectionView;
import com.unicate.controller.views.InputView;

/**
 *
 */
public class ControllerHelper {
	private ControllerInput input;

	private boolean actionActive = false;
	private boolean directionActive = false;

	private ControllerInputListener listener;
	private ActionView actionView;
	private DirectionView directionView;

	private InputView.OnInputChangeListener controllerInputListener = new InputView.OnInputChangeListener() {
		@Override public void onButtonChanged(InputView view, int buttons) {
			if (null == input) {
				input = new ControllerInput();
			}
			if (null != actionView && view.getId() == actionView.getId()) {
				actionActive = true;
				input.setAction(buttons);

			} else if (null != directionView && view.getId() == directionView.getId()) {
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
				listener.onInputChanged(input);
			}
		}

		@Override public void onInputFinished(InputView view) {
			if (null != actionView && view.getId() == actionView.getId()) {
				actionActive = false;
			} else if (null != directionView && view.getId() == directionView.getId()) {
				directionActive = false;

			}
			if (!actionActive && !directionActive && null != listener) {
				listener.onInputFinished(input);
				input = new ControllerInput();
			}
		}
	};

	public ControllerHelper(ActionView actionView) {
		this(null, actionView);
	}

	public ControllerHelper(DirectionView directionView) {
		this(directionView, null);
	}

	public ControllerHelper(DirectionView directionView, ActionView actionView) {
		this.actionView = actionView;
		this.directionView = directionView;
		setupListeners();
	}

	public void setControllerInputListener(ControllerInputListener listener) {
		this.listener = listener;
	}

	private void setupListeners() {
		if (null != actionView) {
			actionView.setOnButtonListener(controllerInputListener);
		}
		if (null != directionView) {
			directionView.setOnButtonListener(controllerInputListener);
		}
	}

	public interface ControllerInputListener {
		/**
		 * This is called as soon as the user presses anything on the inputviews
		 * @param input the input the user made
		 */
		void onInputChanged(ControllerInput input);

		/**
		 * This is called, when the user releases the buttons
		 * @param input
		 */
		void onInputFinished(ControllerInput input);
	}
}
