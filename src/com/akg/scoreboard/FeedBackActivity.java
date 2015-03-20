package com.akg.scoreboard;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class FeedBackActivity extends Activity {

	TableLayout feedback_table;
	LinearLayout errorReport;
	GameData data;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback);
		feedback_table = (TableLayout) findViewById(R.id.feedback_table);
		errorReport = (LinearLayout) findViewById(R.id.error_score);
		Bundle extra = getIntent().getExtras();
		if (extra != null) {
			data = (GameData) extra.get("EXTRA");
		}
		populateData();
	}

	private void populateData() {
		if (data == null)
			return;
		TableRowBuilder rowBuilder = new TableRowBuilder(this);
		TableRow headerRow = (TableRow) rowBuilder.buildRow();
		rowBuilder.buildSingleColumn(headerRow, "Feed Back");
		feedback_table.addView(headerRow);
		TableRow winnerTableRow = (TableRow) rowBuilder.buildRow();
		rowBuilder.buildSingleColumn(winnerTableRow, "Winner");
		rowBuilder.buildColumn(data.winner, winnerTableRow, TableRow.HORIZONTAL);
		feedback_table.addView(winnerTableRow);
		TableRow forcedErrorTableRow = (TableRow) rowBuilder.buildRow();
		rowBuilder.buildSingleColumn(forcedErrorTableRow, "Forced Error");
		rowBuilder.buildColumn(data.forced_error, forcedErrorTableRow, TableRow.HORIZONTAL);
		feedback_table.addView(forcedErrorTableRow);
		TableRow unForcedErrorTableRow = (TableRow) rowBuilder.buildRow();
		rowBuilder.buildSingleColumn(unForcedErrorTableRow, "UnForced Error");
		rowBuilder.buildColumn(data.unforced_error, unForcedErrorTableRow, TableRow.HORIZONTAL);
		feedback_table.addView(unForcedErrorTableRow);
		TextView win = rowBuilder.createTextView();
		win.setText("Total win " + data.winner.size());
		TextView error = rowBuilder.createTextView();
		error.setText("Total Forced error " + data.forced_error.size());
		TextView un_error = rowBuilder.createTextView();
		un_error.setText("Total UnForced Error " + data.unforced_error.size());
		errorReport.addView(win);
		errorReport.addView(error);
		errorReport.addView(un_error);

	}
	


	public interface RowBuilder {
		int LEFT = 10;
		int TOP = 10;
		int RIGHT = 10;
		int BOTTOM = 10;

		Object buildRow();

		void buildColumn(ArrayList<Object> lists, TableRow tableRow, int orientation);

		void buildSingleColumn(TableRow tableRow, String tableName);
	}

	public class TableRowBuilder implements RowBuilder {

		Context context;

		public TableRowBuilder(Context context) {
			this.context = context;
		}

		@Override
		public Object buildRow() {
			TableRow tableRow = new TableRow(context);
			tableRow.setPadding(LEFT, TOP, RIGHT, BOTTOM);
			return tableRow;
		}

		@Override
		public void buildColumn(ArrayList<Object> lists, TableRow tableRow, int orientation) {
			LinearLayout fixedColumn = new LinearLayout(context);
			fixedColumn.setOrientation(orientation);
			for (Object object : lists) {
				TextView textView = createTextView();
				textView.setText("" + object);
				textView.setTag(object);
				fixedColumn.addView(textView);
			}
			tableRow.addView(fixedColumn);
		}

		public TextView createTextView() {
			TextView textView = new TextView(context);
			textView.setPadding(LEFT, TOP, RIGHT, BOTTOM);
			return textView;
		}

		@Override
		public void buildSingleColumn(TableRow tableRow, String name) {
			TextView textView = createTextView();
			textView.setTag(name);
			textView.setText("" + name);
			tableRow.addView(textView);
		}
	}

}
