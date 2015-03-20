package com.akg.scoreboard;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akg.scoreboard.Timer.TimerCallBack;

public class CameraRecordActivity extends Activity {
	private Camera myCamera;
	private CameraSurfaceView myCameraSurfaceView;
	private MediaRecorder mediaRecorder;
	private MediaPlayer mPlayer;
	Button myButton, forced_error, unforced_error, winner,view_feedback;
	TextView timerText;
	SurfaceHolder surfaceHolder;
	boolean recording;
	int totalIntrevals = 1000 * 60 * 60 * 3;
	Timer timer;
	GameData data;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		recording = false;
		setContentView(R.layout.activity_camera);
		timer = new Timer(this);
		data = new GameData();
		// Get Camera for preview
		myCamera = getCameraInstance();
		if (myCamera == null) {
			Toast.makeText(CameraRecordActivity.this, "Fail to get Camera",
					Toast.LENGTH_LONG).show();
		}
		myCameraSurfaceView = new CameraSurfaceView(getApplicationContext(),
				myCamera);
		FrameLayout myCameraPreview = (FrameLayout) findViewById(R.id.videoview);
		myCameraPreview.addView(myCameraSurfaceView);
		myButton = (Button) findViewById(R.id.mybutton);
		view_feedback = (Button) findViewById(R.id.view_feedback);
		timerText = (TextView) findViewById(R.id.timerText);
		forced_error = (Button) findViewById(R.id.forced_error);
		unforced_error = (Button) findViewById(R.id.unforced_error);
		winner = (Button) findViewById(R.id.winner);
		myButton.setOnClickListener(myButtonOnClickListener);
		forced_error.setOnClickListener(mClickListener); 
		unforced_error.setOnClickListener(mClickListener);
		winner.setOnClickListener(mClickListener); 
		view_feedback.setOnClickListener(mClickListener);
		view_feedback.setVisibility(View.VISIBLE);
		timer.setTimerCallBack(new TimerCallBack() {
			@Override
			public void setTime(String output) {
				timerText.setText(output);
			}
		});

	}

	View.OnClickListener mClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			Long l = timer.getTotalMilliSecond();
			switch (v.getId()) {

			case R.id.forced_error:
				data.forced_error.add(l);
				break;
			case R.id.unforced_error:
				data.unforced_error.add(l);
				break;
			case R.id.winner:
				data.winner.add(l);
				break;
			case R.id.view_feedback:
				if(recording)stopRecording();
				Intent feedBackIntent = new Intent(CameraRecordActivity.this,FeedBackActivity.class);
				feedBackIntent.putExtra("EXTRA", data);
				startActivity(feedBackIntent);
				break;
			default:
				break;
			}

		}
	};

	Button.OnClickListener myButtonOnClickListener = new Button.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (recording) {
				stopRecording();
			} else {
				startRecording();
			}
		}
	};
	
	private void stopRecording(){
		// stop recording and release camera
		mediaRecorder.stop(); // stop the recording
		releaseMediaRecorder(); // release the MediaRecorder object
		recording = false;
		view_feedback.setVisibility(View.VISIBLE);
		myButton.setText("Start");

	}
	
	private void startRecording(){
		// Release Camera before MediaRecorder start
		releaseCamera();
		if (!prepareMediaRecorder()) {
			Toast.makeText(CameraRecordActivity.this,
					"Fail in prepareMediaRecorder()!\n - Ended -",
					Toast.LENGTH_LONG).show();
			finish();
		}
		if (mediaRecorder != null)
			mediaRecorder.start();
		recording = true;
		timer.start();
		myButton.setText("Stop");

	}

	private Camera getCameraInstance() {
		// TODO Auto-generated method stub
		Camera c = null;
		try {
			c = Camera.open(); // attempt to get a Camera instance
		} catch (Exception e) {
			// Camera is not available (in use or does not exist)
		}
		return c; // returns null if camera is unavailable
	}

	private boolean prepareMediaRecorder() {
		myCamera = getCameraInstance();
		mediaRecorder = new MediaRecorder();
		myCamera.unlock();
		mediaRecorder.setCamera(myCamera);
		mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
		mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
		try {
			mediaRecorder.setProfile(CamcorderProfile
					.get(CamcorderProfile.QUALITY_720P));
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		mediaRecorder.setOutputFile("/sdcard/myvideo.mp4");
		mediaRecorder.setMaxDuration(60000 * 60 * 2); // Set max duration .
		mediaRecorder.setMaxFileSize(5000000 * 10); // Set max file size
		mediaRecorder.setPreviewDisplay(myCameraSurfaceView.getHolder()
				.getSurface());
		try {
			mediaRecorder.prepare();
		} catch (IllegalStateException e) {
			releaseMediaRecorder();
			return false;
		} catch (IOException e) {
			releaseMediaRecorder();
			return false;
		}
		return true;

	}

	@Override
	protected void onPause() {
		super.onPause();
		releaseMediaRecorder(); // if you are using MediaRecorder, release it
								// first
		releaseCamera(); // release the camera immediately on pause event
	}

	private void releaseMediaRecorder() {
		if (mediaRecorder != null) {
			mediaRecorder.reset(); // clear recorder configuration
			mediaRecorder.release(); // release the recorder object
			mediaRecorder = null;
			myCamera.lock(); // lock camera for later use
		}
	}

	private void releaseCamera() {
		if (myCamera != null) {
			myCamera.release(); // release the camera for other applications
			myCamera = null;
		}
	}

	public class CameraSurfaceView extends SurfaceView implements
			SurfaceHolder.Callback {

		private SurfaceHolder mHolder;
		private Camera mCamera;

		public CameraSurfaceView(Context context, Camera camera) {
			super(context);
			mCamera = camera;

			// Install a SurfaceHolder.Callback so we get notified when the
			// underlying surface is created and destroyed.
			mHolder = getHolder();
			mHolder.addCallback(this);
			// deprecated setting, but required on Android versions prior to 3.0
			mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format,
				int weight, int height) {
			// If your preview can change or rotate, take care of those events
			// here.
			// Make sure to stop the preview before resizing or reformatting it.

			if (mHolder.getSurface() == null) {
				// preview surface does not exist
				return;
			}

			// stop preview before making changes
			try {
				mCamera.stopPreview();
			} catch (Exception e) {
				// ignore: tried to stop a non-existent preview
			}

			// make any resize, rotate or reformatting changes here

			// start preview with new settings
			try {
				mCamera.setPreviewDisplay(mHolder);
				mCamera.startPreview();

			} catch (Exception e) {
			}
		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			// TODO Auto-generated method stub
			// The Surface has been created, now tell the camera where to draw
			// the preview.
			try {
				mCamera.setPreviewDisplay(holder);
				mCamera.startPreview();
			} catch (IOException e) {
			} catch (Exception e) {
			}
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			// TODO Auto-generated method stub

		}
	}

}