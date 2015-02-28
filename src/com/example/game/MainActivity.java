package com.example.game;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {
	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		Button startButton = (Button) findViewById(R.id.startButton);
		final EditText InputView = (EditText) findViewById(R.id.nameInput);
		startButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String username = InputView.getText().toString();
				Intent intent = new Intent(MainActivity.this, gameActivity.class);
				intent.putExtra("username", username);
				startActivity(intent);
			}
		});
	}
}
