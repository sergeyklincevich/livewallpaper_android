package com.sergeyklincevich.androidlivewallpaper;

import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TextView;
import com.sergeyklincevich.androidlivewallpaper.data.SettingsFacade;
import org.androidannotations.annotations.*;

/**
 * Created by sergey.klincevich on 19.04.2015.
 */
@EActivity(R.layout.activity_settings)
public class ConfigurationsActivity extends ActionBarActivity
{
	private static final String TAG = "ConfigurationsActivity";

	private static final String TAG_TAB_1 = "tab1";
	private static final String TAG_TAB_2 = "tab2";
	private EditText speedMinView, speedMaxView, radiusMinView, radiusMaxView, lifeTimeMinView, lifeTimeMaxView;

	@Bean
	protected SettingsFacade settingsFacade;

	@ViewById(R.id.tabHost)
	protected TabHost tabHostView;
	@ViewById(R.id.edt_quantity)
	protected EditText quantityView;

	@AfterViews
	void init()
	{
		tabHostView.setup();

		TabHost.TabSpec spec = tabHostView.newTabSpec(TAG_TAB_1);
		spec.setIndicator(getString(R.string.particle_setup));
		spec.setContent(R.id.tab1);
		tabHostView.addTab(spec);

		spec = tabHostView.newTabSpec(TAG_TAB_2);
		spec.setIndicator(getString(R.string.particle_behaviour));
		spec.setContent(R.id.tab2);
		tabHostView.addTab(spec);

		tabHostView.setCurrentTabByTag(TAG_TAB_1);

		quantityView.setText(String.valueOf(settingsFacade.getQuantityValue()));

		View view = findViewById(R.id.view_speed);
		TextView tvTitle = (TextView) view.findViewById(R.id.title);
		tvTitle.setText(R.string.partiсle_speed);

		speedMinView = (EditText) view.findViewById(R.id.min_value_view);
		speedMinView.setText(String.valueOf(settingsFacade.getMinSpeedValue()));
		speedMaxView = (EditText) view.findViewById(R.id.max_value_view);
		speedMaxView.setText(String.valueOf(settingsFacade.getMaxSpeedValue()));

		view = findViewById(R.id.view_radius);
		tvTitle = (TextView) view.findViewById(R.id.title);
		tvTitle.setText(R.string.particle_size);
		radiusMinView = (EditText) view.findViewById(R.id.min_value_view);
		radiusMinView.setText(String.valueOf(settingsFacade.getMinRadiusValue()));
		radiusMaxView = (EditText) view.findViewById(R.id.max_value_view);
		radiusMaxView.setText(String.valueOf(settingsFacade.getMaxRadiusValue()));

		view = findViewById(R.id.view_lifetime);
		tvTitle = (TextView) view.findViewById(R.id.title);
		tvTitle.setText(R.string.partiсle_lifetime);
		lifeTimeMinView = (EditText) view.findViewById(R.id.min_value_view);
		lifeTimeMinView.setText(String.valueOf(settingsFacade.getMinLifeTimeValue()));
		lifeTimeMaxView = (EditText) view.findViewById(R.id.max_value_view);
		lifeTimeMaxView.setText(String.valueOf(settingsFacade.getMaxLifeTimeValue()));
	}

	@Click(R.id.button_apply)
	protected void onButtonApplyClicked()
	{
		onBackPressed();
	}

	private void saveSettings()
	{
		settingsFacade.saveAllSettings(Integer.parseInt(quantityView.getText().toString()),
				Float.parseFloat(speedMinView.getText().toString()), Float.parseFloat(speedMaxView.getText().toString()),
				Integer.parseInt(radiusMinView.getText().toString()), Integer.parseInt(radiusMaxView.getText().toString()),
				Integer.parseInt(lifeTimeMinView.getText().toString()), Integer.parseInt(lifeTimeMaxView.getText().toString()));
	}

	@Override
	public void onBackPressed()
	{
		if (checkValidateSettings())
		{
			saveSettings();
			super.onBackPressed();
		}
	}

	private boolean checkValidateSettings()
	{
		if (getValueFromView(speedMinView) >= getValueFromView(speedMaxView))
		{
			speedMaxView.setError("Error");
			return false;
		}

		if (getValueFromView(lifeTimeMinView) >= getValueFromView(lifeTimeMaxView))
		{
			lifeTimeMaxView.setError("Error");
			return false;
		}

		if (getValueFromView(radiusMinView) >= getValueFromView(radiusMaxView))
		{
			radiusMaxView.setError("Error");
			return false;
		}

		return true;
	}

	private float getValueFromView(EditText view)
	{
		return Float.parseFloat(view.getText().toString());
	}
}
