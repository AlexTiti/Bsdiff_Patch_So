package com.alex.kotlin.myjni;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

	private File oldFile;
	private File newFile;
	private File patchFile;
	private File destFile;

	@RequiresApi(api = Build.VERSION_CODES.M)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Example of a call to a native method
		TextView tv = findViewById(R.id.sample_text);
		tv.setText(String.valueOf(BsdiffUtils.add(5, 5)));

		requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);

		oldFile = new File(Environment.getExternalStorageDirectory(), "old.apk");
		if (!oldFile.exists()) {
			return;
		}
		Log.i("TAG", oldFile.getAbsolutePath());
		newFile = new File(Environment.getExternalStorageDirectory(), "new.apk");
		if (!newFile.exists()) {
			return;
		}
		Log.i("TAG", newFile.getAbsolutePath());
		patchFile = new File(Environment.getExternalStorageDirectory(), "patch.patch");
		if (!patchFile.exists()) {
			try {
				patchFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		destFile = new File(Environment.getExternalStorageDirectory(), "destPatch.apk");
		if (!destFile.exists()) {
			try {
				destFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		Button button = findViewById(R.id.btnDiff);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						BsdiffUtils.diffApk(oldFile.getAbsolutePath(), newFile.getAbsolutePath(), patchFile.getAbsolutePath());
					}
				}).start();
			}
		});

		Button btnPatch = findViewById(R.id.btnPatch);
		btnPatch.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {


				new Thread(new Runnable() {
					@Override
					public void run() {
						BsdiffUtils.combineNewApk(oldFile.getAbsolutePath(),destFile.getAbsolutePath(), patchFile.getAbsolutePath());
					}
				}).start();
			}
		});


	}


}
