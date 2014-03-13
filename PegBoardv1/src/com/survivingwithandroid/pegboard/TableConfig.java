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
package com.survivingwithandroid.pegboard;


import android.content.Context;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.util.DisplayMetrics;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

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
public class TableConfig {
	public static int DEFAULT_PIN_SIZE = 30;
	private static MediaPlayer player ;
	
	public static float convertDpToPixel(float dp, Context context){
	    Resources resources = context.getResources();
	    DisplayMetrics metrics = resources.getDisplayMetrics();
	    float px = dp * (metrics.densityDpi / 160f);
	    return px;
	}
	
	public static int[] pinIdList = new int[] {
		R.drawable.black_pin40,
		R.drawable.blue_pin40,
		R.drawable.brown_pin40,
		R.drawable.green_pin40,
		R.drawable.lightblue_pin40,
		R.drawable.orange_pin40,
		R.drawable.pink_pin40,
		R.drawable.red_pin40,
		R.drawable.yellow_pin40,
		R.drawable.violet_pin40,
		R.drawable.pin40
	};
	
	
	public static int pinBackground = R.drawable.pin40;
	
	
	
	
	public static Animation getFadeScaleOutAnim(Context ctx) {
		
		return AnimationUtils.loadAnimation(ctx.getApplicationContext(), R.anim.fadeout_scaleout);	
	
	}
	
	public static Animation getFadeScaleInAnim(Context ctx) {
		
		return  AnimationUtils.loadAnimation(ctx.getApplicationContext(), R.anim.fadein_scalein);
	
	}
	
	public static Animation getRotateAnim(Context ctx) {
				
		return  AnimationUtils.loadAnimation(ctx.getApplicationContext(), R.anim.rotate);
		

	}
	
		
	
	public static void playSound(Context ctx) {
		if (player != null) {
			if (player.isPlaying()) {
				return ;
			}
		}
		else 
		  player = MediaPlayer.create(ctx, R.raw.pinsound);
		
		_play();
	}
	
	private static void _play() {
		Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				player.start();
			}
		});
		t.start();
	}
	
	
}
