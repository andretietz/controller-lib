package com.unicate.controllerlib;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.TextView;

import com.unicate.controller.ControllerHelper;
import com.unicate.controller.model.ControllerInput;
import com.unicate.controller.views.ActionView;
import com.unicate.controller.views.DirectionView;

/**
 * a simple activity to show a demo of the ControllerView
 */
public class ControllerActivity extends ActionBarActivity {

	private TextView text;

	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_controller);
		text = (TextView)findViewById(R.id.textView);
		ControllerHelper controller = new ControllerHelper((DirectionView) findViewById(R.id.viewDirection), (ActionView) findViewById(R.id.viewAction));
		controller.setControllerInputListener(new ControllerHelper.ControllerInputListener() {
			@Override public void onInputChanged(ControllerInput input) {
				Log.e("CHANGE", input.toString());
				text.setText(input.toString());
			}

			@Override public void onInputFinished(ControllerInput input) {
				Log.e("FINISH", input.toString());
				text.setText(input.toString());
			}
		});

	}
}
