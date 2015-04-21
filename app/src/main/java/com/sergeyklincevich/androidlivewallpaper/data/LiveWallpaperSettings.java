package com.sergeyklincevich.androidlivewallpaper.data;

import org.androidannotations.annotations.sharedpreferences.DefaultFloat;
import org.androidannotations.annotations.sharedpreferences.DefaultInt;
import org.androidannotations.annotations.sharedpreferences.SharedPref;

/**
 * Created by sergey.klincevich on 19.04.2015.
 */
@SharedPref(SharedPref.Scope.UNIQUE)
interface LiveWallpaperSettings
{
	@DefaultInt(100)
	int quantity();

	@DefaultFloat(1f)
	float minSpeed();

	@DefaultFloat(10f)
	float maxSpeed();

	@DefaultInt(5)
	int minRadius();

	@DefaultInt(20)
	int maxRadius();

	@DefaultInt(1)
	int minLifeTime();

	@DefaultInt(20)
	int maxLifeTime();
}
