package com.akg.scoreboard;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class CameraRecordActivity extends Activity implements Constants,
		OnClickListener, SurfaceHolder.Callback {
	
	
    private SurfaceHolder surfaceHolder;
    private SurfaceView surfaceView;
    public MediaRecorder mrec = new MediaRecorder();
    private Button startRecording = null,stopRecording = null;
    //private Button stopRecording = null;
    File video;
    private Camera mCamera;
	private int maxDuration = 2 * 60 * 60;
	private String videoStoreLoc = "/sdcard/videocapture_example.mp4";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        Log.i(null , "Video starting");
        startRecording = (Button)findViewById(R.id.button_start);
        stopRecording = (Button)findViewById(R.id.button_stop);
        mCamera = Camera.open();
        surfaceView = (SurfaceView) findViewById(R.id.surface_camera);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        startRecording.setOnClickListener(this);
        stopRecording.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        menu.add(0, 0, 0, "StartRecording");
        menu.add(0, 1, 0, "StopRecording");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
        case 0:
            try {
                startRecording();
            } catch (Exception e) {
            	e.printStackTrace();
                String message = e.getMessage();
                Log.i(null, "Problem Start"+message);
                mrec.release();
            }
            break;

        case 1: //GoToAllNotes
            mrec.stop();
            mrec.release();
            mrec = null;
            break;

        default:
            break;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void startRecording() throws IOException 
    {
        mrec = new MediaRecorder();  // Works well
        mCamera.unlock();

        mrec.setCamera(mCamera);

        mrec.setPreviewDisplay(surfaceHolder.getSurface());
        mrec.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mrec.setAudioSource(MediaRecorder.AudioSource.MIC); 
        mrec.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mrec.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        mrec.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);
        mrec.setOutputFile(videoStoreLoc);
        mrec.setMaxDuration(maxDuration * 1000); // in seconds
        mrec.setMaxFileSize(50 * 1000 * 1000); // Approximately 50 megabytes
        mrec.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_LOW));
        mrec.prepare();
        mrec.start();
    }

    protected void stopRecording() {
        mrec.stop();
        mrec.release();
        mCamera.release();
    }

    private void releaseMediaRecorder(){
        if (mrec != null) {
            mrec.reset();   // clear recorder configuration
            mrec.release(); // release the recorder object
            mrec = null;
            mCamera.lock();           // lock camera for later use
        }
    }

    private void releaseCamera(){
        if (mCamera != null){
            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
            int height) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (mCamera != null){
            Parameters params = mCamera.getParameters();
            mCamera.setParameters(params);
        }
        else {
            Toast.makeText(getApplicationContext(), "Camera not available!", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mCamera.stopPreview();
        mCamera.release();
    }

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.button_start){
			  try {
	                startRecording();
	            } catch (Exception e) {
	                String message = e.getMessage();
	                Log.i(null, "Problem Start"+message);
	                mrec.release();
	            }
		}else if(v.getId() == R.id.button_stop){
			   	mrec.stop();
	            mrec.release();
	            mrec = null;
		}
	}

}
