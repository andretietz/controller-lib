package com.unicate.controller.model;

/**
 * The ControllerInput is a model class, which represents an input into the controller.
 * This can contain directions and actions
 */
public class ControllerInput {

	private int direction = 0;
	private int action = 0;

	public ControllerInput() {
	}

	public void resetDirection() {
		direction = 0;
	}

	public int getAction() {
		return action;
	}

	public void setAction(int action) {
		this.action = action;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public boolean isPressed(DirectionButton button) {
		return (((0x1 << button.getMap()) & direction) > 0);
	}

	public boolean isPressed(ActionButton button) {
		return (((0x1 << button.getMap()) & action) > 0);
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Direction: ");
		for (DirectionButton button : DirectionButton.values()) {
			if (isPressed(button)) {
				sb.append(button.name()).append(' ');
			}
		}
		sb.append("Action: ");
		for (ActionButton button : ActionButton.values()) {
			if (isPressed(button)) {
				sb.append(button.name()).append(' ');
			}
		}
		return sb.toString();
	}

	public enum DirectionButton {
		RIGHT(0),
		DOWN_RIGHT(1),
		DOWN(2),
		DOWN_LEFT(3),
		LEFT(4),
		UP_LEFT(5),
		UP(6),
		UP_RIGHT(7);

		private int map;

		DirectionButton(int map) {
			this.map = map;
		}

		public int getMap() {
			return map;
		}
	}

	public enum ActionButton {
		BUTTON_1(2),
		BUTTON_2(3),
		BUTTON_3(1),
		BUTTON_4(0);

		private int map;

		ActionButton(int map) {
			this.map = map;
		}

		public int getMap() {
			return map;
		}
	}
}
