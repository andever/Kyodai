package com.andeveloper.kyodai;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.badlogic.gdx.backends.android.AndroidApplication;

public class KyodaiActivity extends AndroidApplication {
	private View gameView;
	private Kyodai kyodai;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		initGame();
	}

	private void initGame() {
		this.kyodai = new Kyodai(this);
		this.gameView = initializeForView(this.kyodai, false);
		LinearLayout interLinearLayout = (LinearLayout) findViewById(R.id.gameLinearLayout);
		interLinearLayout.addView(this.gameView);
	}

	public void onBackPressed() {
	}

	public void setAdViewVisibility(final boolean visiblity) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
			}
		});
	}

}
