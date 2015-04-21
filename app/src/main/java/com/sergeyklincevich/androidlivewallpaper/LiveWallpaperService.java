package com.sergeyklincevich.androidlivewallpaper;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.service.wallpaper.WallpaperService;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.WindowManager;

/**
 * Created by sergey.klincevich on 19.04.2015.
 */
public class LiveWallpaperService extends WallpaperService implements SensorEventListener
{

	private static final String TAG = "LiveWallpaperService";

	private SensorManager sensorManager;
	private Sensor accelerometer;

	private static float mYdpi;
	private static float mXdpi;

	private volatile static float[] gravity = new float[3];

	private long lastUpdate;

	public static float getGx()
	{
		return gravity[0];
	}

	public static float getGy()
	{
		return gravity[1];
	}

	@Override
	public void onCreate()
	{
		super.onCreate();
		WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);
		mYdpi = dm.ydpi;
		mXdpi = dm.xdpi;

		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
	}

	public static float getYpxPerInch()
	{
		return mYdpi;
	}

	public static float getXpxPerInch()
	{
		return mXdpi;
	}

	@Override
	public Engine onCreateEngine()
	{
		return new LiveWallpaperEngine();
	}

	@Override
	public void onDestroy()
	{
		sensorManager.unregisterListener(this, accelerometer);
		super.onDestroy();
	}

	@Override
	public void onSensorChanged(SensorEvent event)
	{
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER && System.currentTimeMillis() - lastUpdate > 10)
		{

			final float alpha = 0.7f;

			gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
			gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
			gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

			lastUpdate = System.currentTimeMillis();
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy)
	{

	}

	private class LiveWallpaperEngine extends Engine
	{
		private static final String TAG = "LiveWallpaperEngine";

		private WallpaperPainting mWallPainting;

		@Override
		public void onCreate(SurfaceHolder surfaceHolder)
		{
			super.onCreate(surfaceHolder);
			mWallPainting = new WallpaperPainting(LiveWallpaperService.this, surfaceHolder);
		}

		@Override
		public void onSurfaceCreated(SurfaceHolder holder)
		{
			super.onSurfaceCreated(holder);
			mWallPainting.setSurfaceSize(holder.getSurfaceFrame().width(), holder.getSurfaceFrame().height());
			mWallPainting.startPainting();
		}

		@Override
		public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height)
		{
			super.onSurfaceChanged(holder, format, width, height);
			mWallPainting.setSurfaceSize(width, height);
		}

		@Override
		public void onSurfaceDestroyed(SurfaceHolder holder)
		{
			mWallPainting.stopPainting();
			super.onSurfaceDestroyed(holder);
		}

		@Override
		public void onVisibilityChanged(boolean visible)
		{
			super.onVisibilityChanged(visible);
			if (visible)
			{
				mWallPainting.resumePainting();
			}
			else
			{
				mWallPainting.pausePainting();
			}
		}

	}


}
