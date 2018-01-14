package com.andretietz.controllerlib.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.andretietz.android.controller.ActionView;
import com.andretietz.android.controller.DirectionView;
import com.andretietz.android.controller.InputView;

/**
 * a simple activity to show a demo of the ControllerView
 */
public class ControllerActivity extends AppCompatActivity implements InputView.InputEventListener {

	private TextView textAction;
	private TextView textDirection;

	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_controller);
		textAction = findViewById(R.id.textAction);
		textDirection = findViewById(R.id.textDirection);
		DirectionView directionView = findViewById(R.id.viewDirection);
		ActionView actionView = findViewById(R.id.viewAction);
		actionView.setOnButtonListener(this);
		directionView.setOnButtonListener(this);

	}

	@Override public void onInputEvent(View view, int buttons) {
		switch (view.getId()) {
			case R.id.viewAction:
				Log.e("ACTION", "buttons: " + buttons);
				textAction.setText(String.format("Action: %s", actionButtonsToString(buttons)));
				break;
			case R.id.viewDirection:
				Log.e("DIRECTION", "buttons: " + buttons);
				textDirection.setText(String.format("Direction: %s", directionButtonsToString(buttons)));
				break;
		}
	}

	/**
	 * <p>Action buttons can be multiple presses at the same time. Therefore they are
	 * binary coded. The last four bits of the parameter are used to do so.</p>
	 * <p>0b0001 is Button 1</p>
	 * <p>0b0010 is Button 2</p>
	 * <p>0b0100 is Button 3</p>
	 * <p>0b1000 is Button 4</p>
	 * <p>On that way you can receive multiple touches in one integer</p>
	 * <p>0b0101 or 0x05 i.e. would mean that button 1 and 3 is pressed</p>
	 *
	 * @param buttons Bitcoded button touches
	 * @return simple string to see which buttons are pressed
	 */
	private String actionButtonsToString(int buttons) {
		StringBuilder sb = new StringBuilder();
		// foreach button (there are 4 action buttons)
		for(int i=0;i<4;i++) {
			// if the bit on position i is set
			if(((0x01 << i)&buttons)>0) {
				// add the number of the button to the string
				sb.append((i+1)).append(',');
			}
		}
		return sb.toString();
	}

	/**
	 * Works almost the same as the ActionView but only one button can be active
	 * @param buttons
	 * @return
	 */
	private String directionButtonsToString(int buttons) {
		String direction = "NONE";
		switch (buttons&0xff) {
			case DirectionView.DIRECTION_DOWN:
				direction = "Down";
				break;
			case DirectionView.DIRECTION_LEFT:
				direction = "Left";
				break;
			case DirectionView.DIRECTION_RIGHT:
				direction = "Right";
				break;
			case DirectionView.DIRECTION_UP:
				direction = "Up";
				break;
			case DirectionView.DIRECTION_DOWN_LEFT:
				direction = "Down Left";
				break;
			case DirectionView.DIRECTION_UP_LEFT:
				direction = "Up Left";
				break;
			case DirectionView.DIRECTION_DOWN_RIGHT:
				direction = "Down Right";
				break;
			case DirectionView.DIRECTION_UP_RIGHT:
				direction = "Up Right";
				break;

		}
		return direction;
	}
}
