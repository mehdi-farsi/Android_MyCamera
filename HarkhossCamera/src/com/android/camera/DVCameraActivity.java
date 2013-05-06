
package com.android.camera;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;

public class DVCameraActivity extends Activity 
{    
    private Camera mCamera;
    private CameraPreview mCameraPreview;
    private Boolean isCamera = true;
    private Parameters param;
	private String mSuffix = "jpeg";
	private int zoomPart=0;
	private int currentZoom=0;
    
    private View.OnClickListener mTakePicture = new OnClickListener() 
    {
		@Override
		public void onClick(View v) 
		{
			if (isCamera == true)
				mCamera.takePicture(null, null, mPicture);
		}
	};
    PictureCallback mPicture = new PictureCallback() 
    { 
        @Override
        public void onPictureTaken(byte[] data, Camera camera) 
        {
        	new SavePhotoTask().execute(data);
        	camera.startPreview();
        }
         
    };

    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         
        mCamera = getCameraInstance();
        param = mCamera.getParameters();

        mCameraPreview = new CameraPreview(this, mCamera);
         
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mCameraPreview);
        ImageButton b = (ImageButton)findViewById(R.id.button_capture);
        b.setOnClickListener(mTakePicture);
        zoomPart = 1;
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
      MenuInflater inflater = getMenuInflater();
      inflater.inflate(R.menu.main, menu);
      return true;
    } 

    private Camera getCameraInstance() 
    {
        Camera camera = null;
 
        try 
        {
            camera = Camera.open();
        } 
        catch (Exception e) 
        {
        }
        return camera;
    }
    
    class SavePhotoTask extends AsyncTask<byte[], String, String> 
    {
        @Override
        protected String doInBackground(byte[]... jpeg)
        {
        	File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "HarkhossCamera");

        	if (! mediaStorageDir.exists())
        	{
        		if (! mediaStorageDir.mkdirs()){
        			Log.d("MyCameraApp", "failed to create directory");
        			return null;
        		}
          }
        	
          String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
          File mediaFile;
          mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_"+ timeStamp + "." + mSuffix);
          try 
          {
              FileOutputStream fos = new FileOutputStream(mediaFile);
              fos.write(jpeg[0]);
              fos.close();
          } 
          catch (FileNotFoundException e) 
          {

          } 
          catch (IOException e) 
          { 
          }
              
          return mediaFile.toString();
        }	
      }
    
    private boolean	otherOptions(MenuItem item)
    {
    	switch (item.getItemId()) 
    	{
    	case R.id.flash:
    		if (item.isChecked())
    		{
    			item.setChecked(false);
    			param.setFlashMode(Parameters.FLASH_MODE_OFF);
    			mCamera.setParameters(param);
    		}
    		else 
    		{
    			item.setChecked(true);
    			param.setFlashMode(Parameters.FLASH_MODE_ON);
    			mCamera.setParameters(param);
    		}
    		return true;
    	case R.id.antibanding:
    		if (item.isChecked())
    		{
    			item.setChecked(false);
    			param.setAntibanding(Parameters.ANTIBANDING_OFF);
    			mCamera.setParameters(param);
    		}
    		else
    		{
    			item.setChecked(true);
    			param.setAntibanding(Parameters.ANTIBANDING_AUTO);
    			mCamera.setParameters(param);
    		}
    		return true;
    	default:
    		return true;
    	}
    }
    
    private boolean formatsOptions(MenuItem item)
    {
    	switch (item.getItemId()) 
    	{
    	case R.id.jpg:
    		item.setChecked(true);
    		mSuffix = "jpeg";
    		return true;
    	case R.id.png:
    		item.setChecked(true);
    		mSuffix = "png";
    		return true;
    	default:
    		return true;
    	}
 
    }
    
    private boolean colorEffects(MenuItem item)
    {
    	switch (item.getItemId()) 
    	{

    	case R.id.none:
    		item.setChecked(true);
    		param.setColorEffect(Parameters.EFFECT_NONE);
    		mCamera.setParameters(param);
    		return true;
    	case R.id.blackwhite:
    		item.setChecked(true);
    		param.setColorEffect(Parameters.EFFECT_MONO);
    		mCamera.setParameters(param);
    		return true; 
    	case R.id.sepia:
    		item.setChecked(true);
    		param.setColorEffect(Parameters.EFFECT_SEPIA);
    		mCamera.setParameters(param);
    		return true; 
    	case R.id.negative:
    		item.setChecked(true);
    		param.setColorEffect(Parameters.EFFECT_NEGATIVE);
    		mCamera.setParameters(param);
    		return true;
    	case R.id.aqua:
    		item.setChecked(true);
    		param.setColorEffect(Parameters.EFFECT_AQUA);
    		mCamera.setParameters(param);
    		return true;
    	default:
    		return true;
    	}
    }
   
    private boolean setSize(MenuItem item)
    {
    	switch (item.getItemId()) 
    	{
    	case R.id.size640_480:
    		item.setChecked(true);
    		param.setPictureSize(640, 480);
    		mCamera.setParameters(param);
    		return true;
    	case R.id.size800_480:
    		item.setChecked(true);
    		param.setPictureSize(800, 480);
    		mCamera.setParameters(param);
    		return true;
    	case R.id.size2048_1232:
    		item.setChecked(true);
    		param.setPictureSize(2048, 1232);
    		mCamera.setParameters(param);
    		return true;
    	case R.id.size2048_1536:
    		item.setChecked(true);
    		param.setPictureSize(2048, 1536);
    		mCamera.setParameters(param);
    		return true;
    	default:
    		return true;
    	}
    }
    
    private boolean whiteBalance(MenuItem item)
    {
    	switch (item.getItemId()) 
    	{
    	case R.id.auto:
    		item.setChecked(true);
    		param.setWhiteBalance(Parameters.WHITE_BALANCE_AUTO);
    		mCamera.setParameters(param);
    		return true;
    	case R.id.daylight:
    		item.setChecked(true);
    		param.setWhiteBalance(Parameters.WHITE_BALANCE_DAYLIGHT);
    		mCamera.setParameters(param);
    		return true;
    	case R.id.cloudy:
    		item.setChecked(true);
    		param.setWhiteBalance(Parameters.WHITE_BALANCE_CLOUDY_DAYLIGHT);
    		mCamera.setParameters(param);
    		return true;
    	case R.id.incandescent:
    		item.setChecked(true);
    		param.setWhiteBalance(Parameters.WHITE_BALANCE_INCANDESCENT);
    		mCamera.setParameters(param);
    		return true;
    	case R.id.fluorescent:
    		item.setChecked(true);
    		param.setWhiteBalance(Parameters.WHITE_BALANCE_FLUORESCENT);
    		mCamera.setParameters(param);
    		return true;
    	default:
    		return true;
    	}
    }
    
    private boolean setScene(MenuItem item)
    {
    	switch (item.getItemId()) 
    	{
    	case R.id.none_scene:
    		item.setChecked(true);
    		param.setSceneMode(Parameters.SCENE_MODE_AUTO);
    		mCamera.setParameters(param);
    		return true;
    	case R.id.night:
    		item.setChecked(true);
    		param.setSceneMode(Parameters.SCENE_MODE_NIGHT);
    		mCamera.setParameters(param);
    		return true;
    	case R.id.snow:
    		item.setChecked(true);
    		param.setSceneMode(Parameters.SCENE_MODE_SNOW);
    		mCamera.setParameters(param);
    		return true;
    	case R.id.beach:
    		item.setChecked(true);
    		param.setSceneMode(Parameters.SCENE_MODE_BEACH);
    		mCamera.setParameters(param);
    		return true;
    	case R.id.candlelight:
    		item.setChecked(true);
    		param.setSceneMode(Parameters.SCENE_MODE_CANDLELIGHT);
    		mCamera.setParameters(param);
    		return true;
    	default:
    		return true;
    	}
    }
    
    private boolean setFocus(MenuItem item)
    {
    	switch (item.getItemId()) 
    	{
    	case R.id.focus_auto:
    		item.setChecked(true);
    		param.setFocusMode(Parameters.FOCUS_MODE_AUTO);
    		mCamera.setParameters(param);
    		return true;
    	case R.id.fixed:
    		item.setChecked(true);
    		param.setFocusMode(Parameters.FOCUS_MODE_FIXED);
    		mCamera.setParameters(param);
    		return true;
    	case R.id.macro:
    		item.setChecked(true);
    		param.setFocusMode(Parameters.FOCUS_MODE_MACRO);
    		mCamera.setParameters(param);
    		return true;
    	case R.id.infinity:
    		item.setChecked(true);
    		param.setFocusMode(Parameters.FOCUS_MODE_INFINITY);
    		mCamera.setParameters(param);
    		return true;
    	default:
    		return true;
    	}
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) 
    {
    	otherOptions(item);
    	formatsOptions(item);
    	colorEffects(item);
    	setSize(item);
    	whiteBalance(item);
    	setScene(item);
    	setFocus(item);
    	return super.onOptionsItemSelected(item);

    }
    
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) 
    {
        int action = event.getAction();
        int keyCode = event.getKeyCode();
        int maxZoom = param.getMaxZoom();
            switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
            	if (action == KeyEvent.ACTION_DOWN)
            	{
            		
            		if (param.isZoomSupported())
            		{
            			currentZoom += zoomPart;
            			if (currentZoom > maxZoom)
            			{		
            				currentZoom = maxZoom - 1;
            			}
        				param.setZoom(currentZoom);
        				mCamera.setParameters(param);
            		}
                    return true;
            	}
            	return false;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
            	if (action == KeyEvent.ACTION_DOWN)
            	{
            		if (param.isZoomSupported())
            		{
            			currentZoom -= zoomPart;
            			if (currentZoom < 0)
            			{
            				currentZoom = 0;
            			}
            			param.setZoom(currentZoom);
        				mCamera.setParameters(param);
            		}
                    return true;
            	}
            	return false;
            default:
                return super.dispatchKeyEvent(event);
            }
        }
    
}