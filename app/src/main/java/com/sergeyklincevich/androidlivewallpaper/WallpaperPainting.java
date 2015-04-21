package com.sergeyklincevich.androidlivewallpaper;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.SurfaceHolder;
import com.sergeyklincevich.androidlivewallpaper.data.SettingsFacade_;

import java.util.ArrayList;

/**
 * Created by sergey.klincevich on 19.04.2015.
 */
public class WallpaperPainting extends Thread
{
	private static final String TAG = "WallpaperPainting";

	private static final int TIME_FOR_UPDATE = 100;//ms
	private volatile int mQuantity = SettingsFacade_.getInstance_(LiveWallpaperApplication.getApplication()).getQuantityValue();

	private int widthSurface, heightSurface;

	private final SurfaceHolder surfaceHolder;
	private ArrayList<Particle> particleList = new ArrayList<>(mQuantity);
	private Context context;

	private volatile boolean wait;
	private volatile boolean run;

	public WallpaperPainting(Context context, SurfaceHolder surfaceHolder)
	{
		this.surfaceHolder = surfaceHolder;
		this.context = context;
	}

	public void setSurfaceSize(int width, int height)
	{
		widthSurface = width;
		heightSurface = height;
	}

	public void startPainting()
	{
		SettingsFacade_.getInstance_(context).preferences.getSharedPreferences().registerOnSharedPreferenceChangeListener(mPrefListener);
		run = true;
		this.start();
	}

	public void stopPainting()
	{
		run = false;
		synchronized (this)
		{
			this.notify();
		}
		SettingsFacade_.getInstance_(context).preferences.getSharedPreferences().unregisterOnSharedPreferenceChangeListener(mPrefListener);
	}

	public void pausePainting()
	{
		wait = true;
		synchronized (this)
		{
			this.notify();
		}
	}

	public void resumePainting()
	{
		wait = false;
		synchronized (this)
		{
			this.notify();
		}
	}

	@Override
	public void run()
	{

		Canvas canvas = null;
		while (run)
		{
			try
			{
				canvas = surfaceHolder.lockCanvas();
				if (canvas == null)
				{
					continue;
				}
				synchronized (surfaceHolder)
				{
					Thread.sleep(TIME_FOR_UPDATE);
					if (particleList.size() < mQuantity)
					{
						particleList.add(new Particle(widthSurface, heightSurface));
					}
					draw(canvas);
				}
			}
			catch (InterruptedException e)
			{
				Log.e(TAG, e.getMessage(), e);
			}
			finally
			{
				if (canvas != null)
				{
					surfaceHolder.unlockCanvasAndPost(canvas);
				}
			}
			if (wait)
			{
				synchronized (this)
				{
					try
					{
						wait();
					}
					catch (InterruptedException e)
					{
						Log.e(TAG, e.getMessage(), e);
					}
				}
			}
		}
	}

	private void draw(Canvas canvas)
	{
		canvas.drawColor(Color.BLACK);
		for (Particle p : particleList)
		{
			if (isVisible(p))
			{
				p.onDraw(canvas);
			}
			else
			{
				p.onReuse(canvas, widthSurface, heightSurface);
			}
		}
	}

	private boolean isVisible(Particle p)
	{
		return p.isVisibleOnScreen(widthSurface, heightSurface) && p.isLiveByTime();
	}

	public SharedPreferences.OnSharedPreferenceChangeListener mPrefListener = new SharedPreferences.OnSharedPreferenceChangeListener()
	{
		@Override
		public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
		{
			if (key.equals("quantity")) {
                mQuantity = SettingsFacade_.getInstance_(context).getQuantityValue();
                if (mQuantity < particleList.size()) {
                    particleList.subList(mQuantity, particleList.size()).clear();
                    particleList.trimToSize();
                }
                particleList.ensureCapacity(mQuantity);
            } else {
                SettingsFacade_.getInstance_(context).changePreferencesByKey(key);
            }
		}
	};

}
