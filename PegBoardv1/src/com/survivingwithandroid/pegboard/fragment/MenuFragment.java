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
import com.survivingwithandroid.pegboard.R.drawable;
import com.survivingwithandroid.pegboard.R.id;
import com.survivingwithandroid.pegboard.R.layout;
import com.survivingwithandroid.pegboard.TableConfig;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

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
public class MenuFragment extends Fragment {
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View v = inflater.inflate(R.layout.menu_fragment, container, true);
		ImageAdapter ia = new ImageAdapter(getActivity());
		
		GridView gv = (GridView) v.findViewById(R.id.pinGrid);
		gv.setAdapter(ia);
		
		// Save Btn
		final TextView saveTxt = (TextView) v.findViewById(R.id.saveTxt);
		final Animation animSave = TableConfig.getRotateAnim(this.getActivity());
		animSave.setAnimationListener(new Animation.AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
			  ( (MenuEventListener) getActivity()).onSaveSelected();
			}
		});
		
		// Clear btn
		final TextView clearTxt = (TextView) v.findViewById(R.id.clearTxt);
		final Animation animClear = TableConfig.getRotateAnim(this.getActivity());
		animClear.setAnimationListener(new Animation.AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
			  ( (MenuEventListener) getActivity()).onClearSelected();
			}
		});
		
		// Background btn
		final TextView bkgTxt = (TextView) v.findViewById(R.id.bkgTxt);
		final Animation animBkg = TableConfig.getRotateAnim(this.getActivity());

		animBkg.setAnimationListener(new Animation.AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
			  ( (MenuEventListener) getActivity()).onBackgroundSelected();
			}
		});

		
		clearTxt.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				clearTxt.startAnimation(animClear);
				
			}
		});
		
		saveTxt.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				saveTxt.startAnimation(animSave);
			}
		});
		
		bkgTxt.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				bkgTxt.startAnimation(animBkg);
				
			}
		});		
		
		return v;
	}
	
	
	class ImageAdapter extends BaseAdapter {

		private Context ctx;
		
		public ImageAdapter(Context ctx) {
			this.ctx = ctx;
		}
		
		@Override
		public int getCount() {			
			return TableConfig.pinIdList.length;
		}

		
		@Override
		public Object getItem(int arg0) {
			
			return TableConfig.pinIdList[arg0];
		}
		
		@Override
		public long getItemId(int arg0) {			
			return TableConfig.pinIdList[arg0];
		}

		
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			
			ImageView imageView = null;
			if (imageView == null) {
				 imageView = new ImageView(ctx);
		         imageView.setLayoutParams(new GridView.LayoutParams(75, 75));
		         imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		         imageView.setPadding(2, 2, 2, 2);
		         imageView.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Log.d("PIN", "Pin:" + TableConfig.pinIdList[position]);
						( (MenuEventListener) getActivity()).onPinSelected(TableConfig.pinIdList[position]);
						
					}
				});
			}
			else
				imageView = (ImageView) convertView;
			
			imageView.setImageResource(TableConfig.pinIdList[position]);
			
			return imageView;
		}
		
	}
	
	
	public static interface MenuEventListener {
		public void onPinSelected(int pinId);
		public void onClearSelected();
		public void onSaveSelected();
		public void onBackgroundSelected();
	}

}
