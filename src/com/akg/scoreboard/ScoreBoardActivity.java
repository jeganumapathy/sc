package com.akg.scoreboard;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class ScoreBoardActivity extends Activity implements Constants {

	TextView timer_text, player_a_score, player_x_score, player_x_total_set, player_a_total_set, player_a_name,
			player_x_name;
	RelativeLayout player_x_layout, player_a_layout;
	LinearLayout final_score_board;
	private long startTime = 0L;
	private Handler customHandler = new Handler();
	long timeInMilliseconds = 0L;
	long timeSwapBuff = 0L;
	long updatedTime = 0L;
	Button start_btn, finish_game, delete_a_point, delete_x_point;

	private int player_a_score_val = 0;
	private int player_x_score_val = 0;

	private int player_a_set_val = 0;
	private int player_x_set_val = 0;

	private int max_set_limit = 0;
	private int current_set_limit = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_scoreboard);
		start_btn = (Button) findViewById(R.id.start_btn);
		delete_a_point = (Button) findViewById(R.id.delete_a_point);
		delete_x_point = (Button) findViewById(R.id.delete_x_point);
		player_x_layout = (RelativeLayout) findViewById(R.id.player_x_layout);
		player_a_layout = (RelativeLayout) findViewById(R.id.player_a_layout);
		final_score_board = (LinearLayout) findViewById(R.id.final_score_board);
		player_a_name = (TextView) findViewById(R.id.player_a_name);
		player_x_name = (TextView) findViewById(R.id.player_x_name);
		timer_text = (TextView) findViewById(R.id.timer_text);
		player_x_total_set = (TextView) findViewById(R.id.player_x_total_set);
		player_a_total_set = (TextView) findViewById(R.id.player_a_total_set);
		player_a_score = (TextView) findViewById(R.id.player_a_score);
		player_x_score = (TextView) findViewById(R.id.player_x_score);
		start_btn.setTag(false);
		start_btn.setOnClickListener(startClick);
		player_x_layout.setOnClickListener(scoreClick);
		player_a_layout.setOnClickListener(scoreClick);

		delete_a_point.setOnClickListener(deleteClick);
		delete_x_point.setOnClickListener(deleteClick);

		player_x_score.setText("" + player_x_score_val);
		player_a_score.setText("" + player_a_score_val);
		player_a_total_set.setText("" + player_a_set_val);
		player_x_total_set.setText("" + player_x_set_val);
		assignValue();

	}

	private void assignValue() {
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		if (bundle != null) {
			Match match = (Match) bundle.getSerializable(EXTRA);
			if (match != null) {
				max_set_limit = match.getSet();
				String team_a = match.getEvent() == SINGLES ? match.getPlayerA() : match.getPlayerA() + "/"
						+ match.getPlayerB();
				String team_x = match.getEvent() == SINGLES ? match.getPlayerX() : match.getPlayerX() + "/"
						+ match.getPlayerY();
				player_x_name.setText("" + team_a);
				player_a_name.setText("" + team_x);
			}
		}

	}
	private View.OnClickListener scoreClick = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.player_x_layout) {
				calculatePlayerXVal();
				player_x_score.setText("" + player_x_score_val);
			} else if (v.getId() == R.id.player_a_layout) {
				calculatePlayerAVal();
				player_a_score.setText("" + player_a_score_val);
			}
		}
	};

	private View.OnClickListener deleteClick = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			int new_score;
			if (v.getId() == R.id.delete_a_point) {
				new_score = player_a_score_val - 1;
				if (new_score >= 0) {
					player_a_score_val = new_score;
					player_a_score.setText("" + player_a_score_val);
				}
			} else if (v.getId() == R.id.delete_x_point) {
				new_score = player_x_score_val - 1;
				if (new_score >= 0) {
					player_x_score_val = new_score;
					player_x_score.setText("" + player_x_score_val);
				}
			}
		}
	};

	private void finishAlert(final int player) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(R.string.finish_text)
				.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						current_set_limit = current_set_limit + 1;
						finishGame(player);
					}
				}).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.dismiss();
					}
				});
		// Create the AlertDialog object and show it
		builder.create().show();
	}

	private void finishGame(int player) {
		if (player == PLAYER_A) {
			player_a_set_val = player_a_set_val + 1;
		} else if (player == PLAYER_X) {
			player_x_set_val = player_x_set_val + 1;
		}
		registerScore();
		player_a_score_val = 0;
		player_x_score_val = 0;
		player_a_total_set.setText("" + player_a_set_val);
		player_x_total_set.setText("" + player_x_set_val);
		player_x_score.setText("" + player_x_score_val);
		player_a_score.setText("" + player_a_score_val);
	}

	private void registerScore() {
		if (current_set_limit >= max_set_limit) {
			Toast.makeText(this, "Game over", Toast.LENGTH_LONG).show();
		}
		float paddingFloat = DisplayUtils.convertPixelsToDp(10, this);
		float marginFloat = DisplayUtils.convertPixelsToDp(8, this);
		int padding = (int) paddingFloat;
		int margin = (int) marginFloat;
		LinearLayout temp = new LinearLayout(this);
		LinearLayout.LayoutParams tempParams = new LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		tempParams.setMargins(margin, margin, margin, margin);
		temp.setOrientation(LinearLayout.VERTICAL);

		TextView temp_a = new TextView(this);
		temp_a.setBackgroundResource(R.drawable.white_broder);
		temp_a.setText(" " + player_a_score_val + " ");
		temp_a.setPadding(padding, padding, padding, padding);
		temp_a.setTextColor(Color.BLACK);
		temp_a.setTypeface(temp_a.getTypeface(), Typeface.BOLD);
		temp.addView(temp_a, tempParams);

		TextView temp_x = new TextView(this);
		temp_x.setText(" " + player_x_score_val + " ");
		temp_x.setPadding(padding, padding, padding, padding);
		temp_x.setTextColor(Color.BLACK);
		temp_x.setBackgroundResource(R.drawable.white_broder);
		temp_x.setTypeface(temp_x.getTypeface(), Typeface.BOLD);

		temp.addView(temp_x, tempParams);

		final_score_board.addView(temp);
	}
	public void calculatePlayerAVal() {
		player_a_score_val = player_a_score_val + 1;
		if (player_a_score_val >= 11 && (player_a_score_val - player_x_score_val >= 2)) {
			finishAlert(PLAYER_A);
		}
	}
	public void calculatePlayerXVal() {
		player_x_score_val = player_x_score_val + 1;
		if (player_x_score_val >= 11 && (player_x_score_val - player_a_score_val >= 2)) {
			finishAlert(PLAYER_X);
		}
	}

	private View.OnClickListener startClick = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			if (!(Boolean) v.getTag()) {
				start_btn.setTag(true);
				start_btn.setText(getResources().getString(R.string.stop_text));
				startTime = SystemClock.uptimeMillis();
				customHandler.post(updateTimerThread);
			} else {
				start_btn.setTag(false);
				start_btn.setText(getResources().getString(R.string.start_text));
				timeSwapBuff += timeInMilliseconds;
				customHandler.removeCallbacks(updateTimerThread);
			}
		}
	};

	private Runnable updateTimerThread = new Runnable() {

		public void run() {
			timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
			updatedTime = timeSwapBuff + timeInMilliseconds;
			int secs = (int) (updatedTime / 1000);
			int mins = secs / 60;
			secs = secs % 60;
			int milliseconds = (int) (updatedTime % 1000);
			timer_text.setText("" + mins + ":" + String.format("%02d", secs) + ":"
					+ String.format("%03d", milliseconds));
			customHandler.postDelayed(this, 0);
		}

	};
}
