package com.sergeyklincevich.androidlivewallpaper;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import com.sergeyklincevich.androidlivewallpaper.data.SettingsFacade_;

import java.util.Random;

/**
 * Created by sergey.klincevich on 19.04.2015.
 */
public class Particle
{
	private static final String TAG = "Particle";

	private Random random = new Random();
	private Paint paint = new Paint();

	public static volatile int radiusMin = SettingsFacade_.getInstance_(LiveWallpaperApplication.getApplication()).getMinRadiusValue();
	public static volatile int radiusMax = SettingsFacade_.getInstance_(LiveWallpaperApplication.getApplication()).getMaxRadiusValue();
	public static volatile float speedMin = SettingsFacade_.getInstance_(LiveWallpaperApplication.getApplication()).getMinSpeedValue();
	public static volatile float speedMax = SettingsFacade_.getInstance_(LiveWallpaperApplication.getApplication()).getMaxSpeedValue();
	public static volatile int lifeTimeMin = SettingsFacade_.getInstance_(LiveWallpaperApplication.getApplication()).getMinLifeTimeValue();
	public static volatile int lifeTimeMax = SettingsFacade_.getInstance_(LiveWallpaperApplication.getApplication()).getMaxLifeTimeValue();

	private int radius;
	private float x;
	private float y;
	private float xStart;
	private float yStart;
	private float speed;
	private long lifeTime;
	private long startingLifeTime;

	public Particle(int width, int height)
	{
		setParametersRandom(width, height);
	}

	private void setParametersRandom(int width, int height)
	{
		int color = Color.argb(random.nextInt(256), random.nextInt(256), random.nextInt(256), random.nextInt(256));
		this.paint.setColor(color);
		this.xStart = (float) random.nextInt(width);
		this.yStart = (float) random.nextInt(10);
		this.radius = random.nextInt(radiusMax - radiusMin) + radiusMin;
		this.speed = (random.nextFloat() * (speedMax - speedMin) + speedMin) / 1000;
		this.lifeTime = (random.nextInt(lifeTimeMax - lifeTimeMin) + lifeTimeMin) * 1000;
		this.startingLifeTime = System.currentTimeMillis();
	}

	private void move()
	{
		float t = ((float) (System.currentTimeMillis() - startingLifeTime)) / 1000;
		x = xStart;
		y = yStart + convertMeterInPxOnY(speed * t);
	}

	public void onDraw(Canvas canvas)
	{
		move();
		canvas.drawCircle(x, y, radius, paint);
	}

	public void onReuse(Canvas canvas, int width, int height)
	{
		setParametersRandom(width, height);
		onDraw(canvas);
	}

	public boolean isVisibleOnScreen(int width, int height)
	{
		return x < width + radius && x > 0 - radius && y < height + radius && y > 0 - radius;
	}

	public boolean isLiveByTime()
	{
		return System.currentTimeMillis() < startingLifeTime + lifeTime;
	}

	private float convertMeterInPxOnY(float m)
	{
		return m * 40 * LiveWallpaperService.getYpxPerInch();
	}
}
