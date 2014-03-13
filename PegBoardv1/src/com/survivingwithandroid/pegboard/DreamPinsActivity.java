package com.survivingwithandroid.pegboard;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.widget.SlidingPaneLayout;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.survivingwithandroid.pegboard.fragment.DreamPinTableFragment;
import com.survivingwithandroid.pegboard.fragment.MenuFragment;

import com.survivingwithandroid.pegboard.util.ImageUtility;
import com.survivingwithandroid.pegboard.util.SaveFileException;

/*
 * Copyright (C) 2014 Francesco Azzola - Surviving with Android (http://www.survivingwithandroid.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

public class DreamPinsActivity extends Activity implements
		MenuFragment.MenuEventListener {

	private SlidingPaneLayout sPaneLy;
	private DreamPinTableFragment pinTableFrag;
	private static final int SELECT_PICTURE = 1410;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_main);

		this.sPaneLy = (SlidingPaneLayout) findViewById(R.id.sp);

		pinTableFrag = (DreamPinTableFragment) getFragmentManager()
				.findFragmentById(R.id.pinTable);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_donate) {
			// Show popup
			// Handle donate here
		}

		return false;
	}

	/**************************************************
	 * 
	 * CallBack methods
	 * 
	 **************************************************/

	@Override
	public void onPinSelected(int pinId) {

		pinTableFrag.setCurrentPin(pinId);
		closeMenu();
	}

	@Override
	public void onClearSelected() {

		closeMenu();

		LayoutInflater inf = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inf.inflate(R.layout.popclear_layout, null, false);

		final PopupWindow pw = new PopupWindow(v);
		pw.setWidth(RelativeLayout.LayoutParams.WRAP_CONTENT);
		pw.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);

		TextView yesTxt = (TextView) v.findViewById(R.id.dlgYes);
		TextView noTxt = (TextView) v.findViewById(R.id.dlgNo);
		noTxt.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				pw.dismiss();
			}
		});

		yesTxt.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				pw.dismiss();
				pinTableFrag.clearBoard();
			}
		});

		pw.showAtLocation(v, Gravity.CENTER, 0, 0);

	}

	@Override
	public void onSaveSelected() {
		closeMenu();

		LayoutInflater inf = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inf.inflate(R.layout.popsave_layout, null, false);

		final PopupWindow pw = new PopupWindow(v);
		pw.setFocusable(true);
		pw.setWidth(RelativeLayout.LayoutParams.WRAP_CONTENT);
		pw.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);

		final EditText edt = (EditText) v.findViewById(R.id.edtFileName);

		TextView saveTxt = (TextView) v.findViewById(R.id.dlgSave);
		TextView cancelTxt = (TextView) v.findViewById(R.id.dlgCancel);
		cancelTxt.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				pw.dismiss();
			}
		});

		saveTxt.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				pw.dismiss();
				Bitmap b = pinTableFrag.createBitmap();
				try {
					ImageUtility.saveImage(b, edt.getEditableText().toString(),
							DreamPinsActivity.this);
				} catch (SaveFileException sfe) {
					Toast.makeText(DreamPinsActivity.this,
							getResources().getText(R.string.msgSaveFileError),
							Toast.LENGTH_LONG).show();
				}
			}
		});

		pw.showAtLocation(v, Gravity.CENTER, 0, 0);

	}

	public void onBackgroundSelected() {
		LayoutInflater inf = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inf.inflate(R.layout.popbkg_layout, null, false);

		final PopupWindow pw = new PopupWindow(v);
		pw.setFocusable(true);
		pw.setWidth(RelativeLayout.LayoutParams.WRAP_CONTENT);
		pw.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);

		TextView changeTxt = (TextView) v.findViewById(R.id.dlgChange);
		TextView resetTxt = (TextView) v.findViewById(R.id.dlgReset);
		TextView cancelTxt = (TextView) v.findViewById(R.id.dlgCancel);
		cancelTxt.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				pw.dismiss();
			}
		});

		resetTxt.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				pw.dismiss();
				pinTableFrag.setBackground(R.drawable.tilebkg);
			}
		});

		changeTxt.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				pw.dismiss();
				// Start a new Intent to get the picture from the Gallery
				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				intent.addCategory(Intent.CATEGORY_OPENABLE);
				startActivityForResult(intent, SELECT_PICTURE);
			}
		});

		pw.showAtLocation(v, Gravity.CENTER, 0, 0);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case SELECT_PICTURE:
			if (resultCode == RESULT_OK) {
				Uri selectedImage = data.getData();
				String[] filePathColumn = { MediaStore.Images.Media.DATA };

				Cursor cursor = getContentResolver().query(selectedImage,
						filePathColumn, null, null, null);
				cursor.moveToFirst();

				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				String filePath = cursor.getString(columnIndex);
				cursor.close();

				Bitmap bkgImg = BitmapFactory.decodeFile(filePath);
				Display disp = getWindowManager().getDefaultDisplay();
				int w = disp.getWidth();
				int h = disp.getHeight();

				double scaleX = (w * 1.0) / bkgImg.getWidth();
				double scaleY = (h * 1.0) / bkgImg.getHeight();

				double scale = Math.min(scaleX, scaleY);

				double sw = w * scaleX;
				double sh = h * scaleY;
				// System.out.println("Scale X ["+scaleX+"] - Scale Y ["+scaleY+"] - Scale ["+scale+"]");

				bkgImg = Bitmap.createBitmap(bkgImg, 0, 0, w, h);
				pinTableFrag.setBackground(bkgImg);

			}
		}

	}

	private void closeMenu() {
		if (sPaneLy.isSlideable()) {
			// We close the left fragment
			sPaneLy.closePane();
		}

	}

}
