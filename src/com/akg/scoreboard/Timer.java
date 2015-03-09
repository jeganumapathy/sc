package com.akg.scoreboard;

import android.content.Context;
import android.os.Handler;
import android.os.SystemClock;

public class Timer {

	Context context;
	private Handler mHandler;
	private long startTime, timeInMilliseconds, updatedTime, timeSwapBuff;
	boolean isStarting;
	private String output;
	private TimerCallBack tc;
	private int secs = 0;
	private int mins = 0;
	private int milliseconds = 0;

	public Timer(Context context) {
		this.context = context;
		this.mHandler = new Handler();
		this.output = new String();
	}

	public static interface TimerCallBack {
		void setTime(String output);
	}

	public void start() {
		isStarting = true;
		startTime = SystemClock.uptimeMillis();
		mHandler.post(mRunnable);
	}

	public void stop() {
		isStarting = false;
		timeSwapBuff += timeInMilliseconds;
		mHandler.removeCallbacks(mRunnable);
	}

	public void reset() {
		isStarting = false;
		startTime = timeInMilliseconds = updatedTime = timeSwapBuff = 0;
	}

	public void setTimerCallBack(TimerCallBack tc) {
		this.tc = tc;
	}

	private Runnable mRunnable = new Runnable() {
		@Override
		public void run() {
			if (isStarting) {
				timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
				updatedTime = timeSwapBuff + timeInMilliseconds;
				secs = (int) (updatedTime / 1000);
				mins = secs / 60;
				secs = secs % 60;
				milliseconds = (int) (updatedTime % 1000);
				output = "" + mins + ":" + String.format("%02d", secs) + ":"
						+ String.format("%03d", milliseconds);
				mHandler.postDelayed(this, 0);
			} else {
				output = "" + mins + ":" + String.format("%02d", secs) + ":"
						+ String.format("%03d", milliseconds);
			}
			if (tc != null) {
				tc.setTime(output);
			}
		}
	};
	
	
	public int getSec(){
		return secs;
	}
	
	public int getMin(){
		return mins;
	}
	
	public long getTotalMilliSecond(){
		return updatedTime;
	}
	

}
