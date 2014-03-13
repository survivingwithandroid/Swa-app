package com.survivingwithandroid.pegboard.table;


import com.survivingwithandroid.pegboard.R;
import com.survivingwithandroid.pegboard.TableConfig;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
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
public class PinImageView extends ImageView  {

	private int row;
	private int col;	
	private int xSize;
	private int ySize;	
	private int stato = -1;	
	private Context ctx;
	private int currentPinId;
	
	public PinImageView(Context context, int row, int col) {
		super(context);
		this.ctx = context;
		this.row = row;
		this.col = col;
		//this.parent = parent;		
		
		// Load image
		Drawable d  = getResources().getDrawable(TableConfig.pinBackground);
		setImageDrawable(d);		
		xSize = this.getWidth();
		ySize = this.getHeight();
		this.currentPinId = TableConfig.pinBackground;
		
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}

	public int getxSize() {
		return xSize;
	}

	public void setxSize(int xSize) {
		this.xSize = xSize;
	}

	public int getySize() {
		return ySize;
	}

	public void setySize(int ySize) {
		this.ySize = ySize;
	}

	
	public void setPinColor(int resId) {
		
		this.currentPinId = resId;
		final Drawable d = getResources().getDrawable(resId);
		if (resId != R.drawable.pin40) {
			Animation scaleInAnim = TableConfig.getFadeScaleInAnim(ctx);
			AnimView av = new AnimView(scaleInAnim, d);
			setImageDrawable(d);
			this.setVisibility(View.GONE);
			this.post(av);
		}
		else {			
			Animation fadeOutAnim = TableConfig.getFadeScaleOutAnim(ctx);
			AnimView av = new AnimView(fadeOutAnim, d);
			this.post(av);
		}
		
	}
	
	class AnimView implements Runnable {

		Animation anim;
		Drawable d;
		
		public AnimView(Animation anim, Drawable d) {
			this.anim = anim;
			this.d = d;
		}
		@Override
		public void run() {
			anim.setAnimationListener(new Animation.AnimationListener() {
				
				@Override
				public void onAnimationStart(Animation animation) {
					TableConfig.playSound(PinImageView.this.ctx);
				}
				
				@Override
				public void onAnimationRepeat(Animation animation) {}
				
				@Override
				public void onAnimationEnd(Animation animation) {
					setImageDrawable(d);
					PinImageView.this.setVisibility(View.VISIBLE);
				}
			});
			
			PinImageView.this.startAnimation(anim);	
		}
		
	}
	
	public void reset() {
		//Log.d("Pin", "Reset");
		stato = -1;
		Drawable d = getResources().getDrawable(TableConfig.pinBackground);
		setImageDrawable(d);
	}


	
	
	public int getStato() {
		return stato;
	}

	public void setStato(int stato) {
		this.stato = stato;
	}


}
