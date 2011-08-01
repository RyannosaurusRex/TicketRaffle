package net.ryanhayes.ticketraffle;

import java.util.ArrayList;

import com.google.ads.Ad;
import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

public class Main extends Activity {
	private AdView adView;
	private ArrayList<Integer> winners = new ArrayList<Integer>(10);

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		setupEventListeners();

		adView = new AdView(this, AdSize.BANNER, "a14d9fc66893b9c");
		LinearLayout layout = (LinearLayout) findViewById(R.id.adLayout);
		layout.addView(adView);
		AdRequest adRequest = new AdRequest();
		adRequest.setTesting(false);
		adView.loadAd(adRequest);
	}

	/**
	 * Sets up the event listener for the Harvest map.
	 */
	private void setupEventListeners() {

		// Button
		final Button button = (Button) findViewById(R.id.btnCalculate);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				int min = 0;
				int max = 0;

				// Get the lower bound
				EditText tv = (EditText) findViewById(R.id.txtLowerBound);
				if (tv.getText().toString().equals("")) {
					TextView tvResult = (TextView) findViewById(R.id.lblWinningNumber);
					tvResult.setText("You must enter the minimum number!");
					return;
				} else {
					min = Integer.parseInt(tv.getText().toString());
				}

				// Get the upper bound
				tv = (EditText) findViewById(R.id.txtUpperBound);
				if (tv.getText().toString().equals("")) {
					TextView tvResult = (TextView) findViewById(R.id.lblWinningNumber);
					tvResult.setText("You must enter a maximum number!");
					return;
				} else {
					max = Integer.parseInt(tv.getText().toString());
					// Get the random number
					int result = min
							+ (int) (Math.random() * ((max - min) + 1));

					// Can we have duplicates?
					ToggleButton tglDupe = (ToggleButton) findViewById(R.id.tglDuplicates);
					if (tglDupe.isChecked()) {
						if (!winners.contains(result)) {
							winners.add(result);
						} else {
							// Has been picked before
							while (winners.contains(result)) {
								// Get a new result
								result = min
										+ (int) (Math.random() * ((max - min) + 1));

								// If it's not been picked, add it to the list.
								if (!winners.contains(result)) {
									winners.add(result);
									break;
								}

								// Are there any numbers left?
								if (winners.size() >= (max - min)) {
									TextView tvResult = (TextView) findViewById(R.id.lblWinningNumber);
									tvResult.setText("There are no numbers available!");
									return;
								}
							}
						}
					} else // Not counting duplicates, also acts as reset
							// button.
					{
						winners.clear();
					}

					// Announce the winner!
					TextView tvResult = (TextView) findViewById(R.id.lblWinningNumber);
					tvResult.setText("The winning number is: "
							+ String.valueOf(result));
				}
			}
		});
	}

	/**
	 * Tear down. Unload Ads
	 */
	public void onDestroy() {
		// Stop loading the ad.
		adView.stopLoading();

		super.onDestroy();
	}
}