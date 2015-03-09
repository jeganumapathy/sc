package com.akg.scoreboard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class MainActivity extends Activity implements Constants {

	private Button singles, doubles,record;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	    this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);
		singles = (Button) findViewById(R.id.singles);
		doubles = (Button) findViewById(R.id.doubles);
		record = (Button) findViewById(R.id.record);
		singles.setOnClickListener(clickListener);
		doubles.setOnClickListener(clickListener);
		record.setOnClickListener(clickListener);
	}

	private View.OnClickListener clickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(MainActivity.this, SelectionsActivity.class);
			if (v.getId() == R.id.singles) {
				intent.putExtra(Constants.EXTRA, SINGLES);
				startActivity(intent);
			} else if (v.getId() == R.id.doubles) {
				intent.putExtra(Constants.EXTRA, DOUBLES);
				startActivity(intent);
			}
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		return super.onOptionsItemSelected(item);
	}
}
