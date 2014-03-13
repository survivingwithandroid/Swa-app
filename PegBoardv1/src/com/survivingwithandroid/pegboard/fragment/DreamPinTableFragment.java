/**
 * This is a tutorial source code 
 * provided "as is" and without warranties.
 *
 * For any question please visit the web site
 * http://www.survivingwithandroid.com
 *
 * or write an email to
 * survivingwithandroid@gmail.com
 *
 */
package com.survivingwithandroid.pegboard.fragment;

import com.survivingwithandroid.pegboard.R;
import com.survivingwithandroid.pegboard.TableConfig;
import com.survivingwithandroid.pegboard.table.PinsTableView;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;

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
public class DreamPinTableFragment extends Fragment implements OnTouchListener {

	private PinsTableView pinsTable;
	private int currentPinDraw = R.drawable.blue_pin40;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View v = inflater.inflate(R.layout.table_fragment, container, true);
		pinsTable = (PinsTableView) v.findViewById(R.id.pinsTable);
		pinsTable.setOnTouchListener(this);
		createBoard();
		
		pinsTable.setCurrentColor(currentPinDraw);
		this.setRetainInstance(true);
		return v;
	}

	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
				
		int x = (int) event.getX() ;
		int y = (int) event.getY() ;

		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			pinsTable.changePinColor(x, y);			
			return true;
		}
		else if (event.getAction() == MotionEvent.ACTION_MOVE) {
			pinsTable.changePinColor(x, y);	
			return true;
		}
		
		return false;

	}
	
	public void setCurrentPin(int id) {
		this.currentPinDraw = id;
		pinsTable.setCurrentColor(currentPinDraw);
	}

	public void clearBoard() {
		pinsTable.removeAllViews();
		pinsTable.invalidate();
		createBoard();
	}
	
	public Bitmap createBitmap() {
		return pinsTable.createBitmap();
	}
	
	
	
	public void setBackground(int resourceId) {
		pinsTable.setBackgroundResource(resourceId);
	}
	
	
	@SuppressWarnings("deprecation")
	public void setBackground(Bitmap b) {
		pinsTable.setBackgroundDrawable(new BitmapDrawable(this.getResources(), b ) );
	}	
	
	private void createBoard() {
		// We start defining the table grid
		Display d = getActivity().getWindowManager().getDefaultDisplay();
		
		int pinSize = (int) (TableConfig.convertDpToPixel(TableConfig.DEFAULT_PIN_SIZE, getActivity()));
		
		int[] vals = pinsTable.disposePins(d.getWidth(), d.getHeight(), pinSize);

	}


	
	
	
}
