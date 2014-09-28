package com.skdck.antreplacechannel;

import android.app.Activity;
import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		((TextView) findViewById(R.id.tv)).setText("CHANNEL:"
				+ getMetaData("CHANNEL") + ",NAME:" + getMetaData("NAME"));
	}

	private String getMetaData(String name) {
		try {
			return getPackageManager()
					.getApplicationInfo(
							new ComponentName(this, MainActivity.class)
									.getPackageName(),
							PackageManager.GET_META_DATA).metaData
					.getString(name);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
}
