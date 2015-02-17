package com.akg.scoreboard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class SelectionsActivity extends Activity implements Constants {

	TextView player_a, player_b, player_x, player_y;
	EditText player_a_edit, player_b_edit, player_x_edit, player_y_edit;
	Button three_set, five_set, seven_set;
	int match_data = SINGLES;
	Match match;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	    this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_selection);
		match = new Match();
		initView();
		Intent intent = getIntent();
		match_data = (Integer) intent.getExtras().get(Constants.EXTRA);
		if (match_data == SINGLES) {
			player_b.setVisibility(View.GONE);
			player_y.setVisibility(View.GONE);
			player_b_edit.setVisibility(View.GONE);
			player_y_edit.setVisibility(View.GONE);
		}
	}
	private View.OnClickListener clickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			if (checkMatchData(match)) {
				Intent intent = new Intent(SelectionsActivity.this, ScoreBoardActivity.class);
				intent.putExtra(Constants.EXTRA, match);
				if (v.getId() == R.id.three_set) {
					match.setSet(THREE_SET);
					startActivity(intent);
				} else if (v.getId() == R.id.five_set) {
					match.setSet(FIVE_SET);
					startActivity(intent);
				} else if (v.getId() == R.id.seven_set) {
					match.setSet(SEVEN_SET);
					startActivity(intent);
				}
				finish();
			} else {
				Toast.makeText(SelectionsActivity.this, "Please check the player details", Toast.LENGTH_LONG).show();
			}
		}

		private boolean checkMatchData(Match match) {
			boolean cod, cod1, cod2, cod3;
			match.setEvent(match_data == SINGLES ? SINGLES : DOUBLES);
			cod = match.setPlayerA(player_a_edit.getText().toString());
			cod1 = match.setPlayerB(player_b_edit.getText().toString());
			cod2 = match.setPlayerX(player_x_edit.getText().toString());
			cod3 = match.setPlayerY(player_y_edit.getText().toString());
			return ((cod || cod1) && (cod2 || cod3));
		}
	};
	
	
	public void onRadioButtonClicked(View view) {
	    // Is the button now checked?
	    boolean checked = ((RadioButton) view).isChecked();
	    switch(view.getId()) {
	        case R.id.radio_left:
	            if (checked)
	            break;
	        case R.id.radio_right:
	            if (checked)
	            break;
	        case R.id.radio_left_x:
	            if (checked)
	            break;
	        case R.id.radio_right_x:
	            if (checked)
	            break;
	        case R.id.radio_defense:
	            if (checked)
	            break;
	        case R.id.radio_offense:
	            if (checked)
	            break;
	        case R.id.radio_defense_x:
	            if (checked)
	            break;
	        case R.id.radio_offense_x:
	            if (checked)
	            break;
	        case R.id.radio_shake:
	            if (checked)
	            break;
	        case R.id.radio_penhold:
	            if (checked)
	            break;
	        case R.id.radio_shake_x:
	            if (checked)
	            break;
	        case R.id.radio_penhold_x:
	            if (checked)
	            break;
	    }
	}


	private void initView() {
		player_a = (TextView) findViewById(R.id.player_a_label);
		player_b = (TextView) findViewById(R.id.player_b_label);
		player_x = (TextView) findViewById(R.id.player_x_label);
		player_y = (TextView) findViewById(R.id.player_y_label);
		player_a_edit = (EditText) findViewById(R.id.player_a_edit);
		player_b_edit = (EditText) findViewById(R.id.player_b_edit);
		player_x_edit = (EditText) findViewById(R.id.player_x_edit);
		player_y_edit = (EditText) findViewById(R.id.player_y_edit);
		three_set = (Button) findViewById(R.id.three_set);
		five_set = (Button) findViewById(R.id.five_set);
		seven_set = (Button) findViewById(R.id.seven_set);
		three_set.setOnClickListener(clickListener);
		five_set.setOnClickListener(clickListener);
		seven_set.setOnClickListener(clickListener);

	}

}
