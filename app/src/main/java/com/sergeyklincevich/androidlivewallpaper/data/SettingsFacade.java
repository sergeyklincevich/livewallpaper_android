package com.sergeyklincevich.androidlivewallpaper.data;

import com.sergeyklincevich.androidlivewallpaper.Particle;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.sharedpreferences.Pref;

/**
 * Created by sergey.klincevich on 19.04.2015.
 */
@EBean(scope = EBean.Scope.Singleton)
public class SettingsFacade
{
	@Pref
	public LiveWallpaperSettings_ preferences;

	public int getQuantityValue()
	{
		return preferences.quantity().get();
	}


	public void saveQuantityValue(int value)
	{
		preferences.edit().quantity().put(value).apply();
	}

	public float getMinSpeedValue()
	{
		return preferences.minSpeed().get();
	}


	public void saveMinSpeedValue(float value)
	{
		preferences.edit().minSpeed().put(value).apply();
	}

	public float getMaxSpeedValue()
	{
		return preferences.maxSpeed().get();
	}


	public void saveMaxSpeedValue(float value)
	{
		preferences.edit().maxSpeed().put(value).apply();
	}

	public int getMinRadiusValue()
	{
		return preferences.minRadius().get();
	}


	public void saveMinRadiusValue(int value)
	{
		preferences.edit().minRadius().put(value).apply();
	}

	public int getMaxRadiusValue()
	{
		return preferences.maxRadius().get();
	}


	public void saveMaxRadiusValue(int value)
	{
		preferences.edit().maxRadius().put(value).apply();
	}

	public int getMinLifeTimeValue()
	{
		return preferences.minLifeTime().get();
	}


	public void saveMinLifeTimeValue(int value)
	{
		preferences.edit().minLifeTime().put(value).apply();
	}

	public int getMaxLifeTimeValue()
	{
		return preferences.maxLifeTime().get();
	}


	public void saveMaxLifeTimeValue(int value)
	{
		preferences.edit().maxLifeTime().put(value).apply();
	}

	public void saveAllSettings(int quantity, float minSpeed, float maxSpeed, int minRadius, int maxRadius, int minLifeTime, int maxLifeTime)
	{
		preferences.edit()
				.quantity().put(quantity)
				.minSpeed().put(minSpeed)
				.maxSpeed().put(maxSpeed)
				.minRadius().put(minRadius)
				.maxRadius().put(maxRadius)
				.minLifeTime().put(minLifeTime)
				.maxLifeTime().put(maxLifeTime)
				.apply();
	}

	public void changePreferencesByKey(String key)
	{

		switch (key)
		{

			case "minSpeed":
				Particle.speedMin = getMinSpeedValue();
				break;

			case "maxSpeed":
				Particle.speedMax = getMaxSpeedValue();
				break;

			case "minRadius":
				Particle.radiusMin = getMinRadiusValue();
				break;

			case "maxRadius":
				Particle.radiusMax = getMaxRadiusValue();
				break;

			case "minLifeTime":
				Particle.lifeTimeMin = getMinLifeTimeValue();
				break;

			case "maxLifeTime":
				Particle.lifeTimeMax = getMaxLifeTimeValue();
				break;
		}

	}
}
