package com.unicate.controllerlib;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.TextView;

import com.unicate.controller.model.ControllerInput;
import com.unicate.controller.views.ControllerView;

/**
 * a simple activity to show a demo of the ControllerView
 */
public class ControllerActivity extends ActionBarActivity {

	private TextView text;

	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_controller);
		ControllerView controller = (ControllerView) findViewById(R.id.viewController);
		text = (TextView)findViewById(R.id.textView);
		controller.setOnControllerInputListener(new ControllerView.ControllerInputListener() {
			@Override public void onInputUpdated(ControllerInput input) {
				text.setText(input.toString());
			}

			@Override public void onInputFinished(ControllerInput input) {
				text.setText(input.toString());
			}
		});
	}
}
