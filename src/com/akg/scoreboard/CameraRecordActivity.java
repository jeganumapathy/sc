package com.akg.scoreboard;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

public class CameraRecordActivity extends Activity{
    private Camera myCamera;
    private MyCameraSurfaceView myCameraSurfaceView;
    private MediaRecorder mediaRecorder;
    private MediaPlayer mPlayer;
    Button myButton,forced_error,unforced_error,winner;
    SurfaceHolder surfaceHolder;
    boolean recording;
    int totalIntrevals= 1000 * 60  * 60 * 3;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        recording = false;

        setContentView(R.layout.activity_camera);

        //Get Camera for preview
        myCamera = getCameraInstance();
        if(myCamera == null){
            Toast.makeText(CameraRecordActivity.this,
                    "Fail to get Camera",
                    Toast.LENGTH_LONG).show();
        }
        myCameraSurfaceView = new MyCameraSurfaceView(getApplicationContext(), myCamera);
        FrameLayout myCameraPreview = (FrameLayout)findViewById(R.id.videoview);
        myCameraPreview.addView(myCameraSurfaceView);
        myButton = (Button)findViewById(R.id.mybutton);
        forced_error= (Button)findViewById(R.id.forced_error);
        unforced_error= (Button)findViewById(R.id.unforced_error);
        winner= (Button)findViewById(R.id.winner);
        myButton.setOnClickListener(myButtonOnClickListener);
       

    }
    
    View.OnClickListener mClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.forced_error:
				break;
			case R.id.unforced_error:
				break;
			case R.id.winner:
				break;
			default:
				break;
			}
			
		}
	};

    Button.OnClickListener myButtonOnClickListener
            = new Button.OnClickListener(){

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            if(recording){
                // stop recording and release camera
                mediaRecorder.stop();  // stop the recording
                releaseMediaRecorder(); // release the MediaRecorder object

                //Exit after saved
                finish();
            }else{

                //Release Camera before MediaRecorder start
                releaseCamera();

                if(!prepareMediaRecorder()){
                    Toast.makeText(CameraRecordActivity.this,
                            "Fail in prepareMediaRecorder()!\n - Ended -",
                            Toast.LENGTH_LONG).show();
                    finish();
                }

                mediaRecorder.start();
                recording = true;
                myButton.setText("STOP");
            }
        }};

    private Camera getCameraInstance(){
        // TODO Auto-generated method stub
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }
    


    private boolean prepareMediaRecorder(){
        myCamera = getCameraInstance();
        mediaRecorder = new MediaRecorder();
        myCamera.unlock();
        mediaRecorder.setCamera(myCamera);
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_720P));
        mediaRecorder.setOutputFile("/sdcard/myvideo.mp4");
        mediaRecorder.setMaxDuration(60000 * 60 * 2); // Set max duration 60 sec.
        mediaRecorder.setMaxFileSize(5000000 * 10); // Set max file size 5M
        mediaRecorder.setPreviewDisplay(myCameraSurfaceView.getHolder().getSurface());
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
        releaseMediaRecorder();       // if you are using MediaRecorder, release it first
        releaseCamera();              // release the camera immediately on pause event
    }

    private void releaseMediaRecorder(){
        if (mediaRecorder != null) {
            mediaRecorder.reset();   // clear recorder configuration
            mediaRecorder.release(); // release the recorder object
            mediaRecorder = null;
            myCamera.lock();           // lock camera for later use
        }
    }

    private void releaseCamera(){
        if (myCamera != null){
            myCamera.release();        // release the camera for other applications
            myCamera = null;
        }
    }

    public class MyCameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback{

        private SurfaceHolder mHolder;
        private Camera mCamera;

        public MyCameraSurfaceView(Context context, Camera camera) {
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
        public void surfaceChanged(SurfaceHolder holder, int format, int weight,
                                   int height) {
            // If your preview can change or rotate, take care of those events here.
            // Make sure to stop the preview before resizing or reformatting it.

            if (mHolder.getSurface() == null){
                // preview surface does not exist
                return;
            }

            // stop preview before making changes
            try {
                mCamera.stopPreview();
            } catch (Exception e){
                // ignore: tried to stop a non-existent preview
            }

            // make any resize, rotate or reformatting changes here

            // start preview with new settings
            try {
                mCamera.setPreviewDisplay(mHolder);
                mCamera.startPreview();

            } catch (Exception e){
            }
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            // TODO Auto-generated method stub
            // The Surface has been created, now tell the camera where to draw the preview.
            try {
                mCamera.setPreviewDisplay(holder);
                mCamera.startPreview();
            } catch (IOException e) {
            }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            // TODO Auto-generated method stub

        }
    }

}