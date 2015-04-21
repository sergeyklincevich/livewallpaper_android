package com.sergeyklincevich.androidlivewallpaper;

import android.app.Application;
import org.androidannotations.annotations.EApplication;

/**
 * Created by sergey.klincevich on 19.04.2015.
 */
@EApplication
public class LiveWallpaperApplication extends Application
{
	private static LiveWallpaperApplication application;

	@Override
	public void onCreate()
	{
		super.onCreate();
		application = this;
	}

	public static LiveWallpaperApplication getApplication()
	{
		return application;
	}
}
