package com.survivingwithandroid.torch;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.survivingwithandroid.torch.util.IabHelper;
import com.survivingwithandroid.torch.util.IabResult;
import com.survivingwithandroid.torch.util.SwABillingUtil;


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
public class MainActivity extends Activity {
    private IabHelper mHelper ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String base64EncodedPublicKey= SwABillingUtil.KEY;
        mHelper = new IabHelper(this, base64EncodedPublicKey);
        mHelper.enableDebugLogging(true);
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                //Log.d("SwA", "Setup finished.");

                if (!result.isSuccess()) {
                    // Oh noes, there was a problem.

                    Toast.makeText(MainActivity.this, "Bwarning continue ["+result.getMessage()+"]", Toast.LENGTH_LONG).show();
                    return;
                }

                // Have we been disposed of in the meantime? If so, quit.
                if (mHelper == null) return;

                // IAB is fully set up. Now, let's get an inventory of stuff we own.
                // Log.d("SwA", "Setup successful.");
            }
        });

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Log.d("SwA", "onActivityResult(" + requestCode + "," + resultCode + "," + data);
        if (mHelper == null) return;

        // Pass on the activity result to the helper for handling
        if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
            // not handled, so handle it ourselves (here's where you'd
            // perform any handling of activity results not related to in-app
            // billing...
            super.onActivityResult(requestCode, resultCode, data);
        }
        else {
            // Log.d("SwA", "onActivityResult handled by IABUtil.");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_share) {
            String playStoreLink = "https://play.google.com/store/apps/details?id=" +
                    getPackageName();

            String msg = getResources().getString(R.string.share_msg) + playStoreLink;
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, msg);
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        }
        else if (id == R.id.action_about) {
            SwAUtil.showAboutDialog(this);
        }
        else if (id == R.id.action_donate) {
            SwABillingUtil.showDonateDialog(this, mHelper, this);
        }

        return super.onOptionsItemSelected(item);
    }

        /**
         * A placeholder fragment containing a simple view.
         */
    public static class PlaceholderFragment extends Fragment {
        private Camera cam;
        private Camera.Parameters camParams;
        private boolean hasCam;
        private int freq;
        private StroboRunner sr;
        private Thread t;
        private boolean isChecked = false;

        public PlaceholderFragment() {
         }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            try {
                //Log.d("TORCH", "Check cam");
                // Get CAM reference
                cam = Camera.open();
                camParams = cam.getParameters();
                cam.startPreview();
                hasCam = true;
                //Log.d("TORCH", "HAS CAM ["+hasCam+"]");
            }
            catch(Throwable t) {
                t.printStackTrace();
            }

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
           // Let's get the reference to the toggle
            final ImageView tBtn = (ImageView) rootView.findViewById(R.id.iconLight);
            tBtn.setImageResource(R.drawable.off);

            tBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isChecked = !isChecked;

                    if (isChecked)
                        tBtn.setImageResource(R.drawable.on);
                    else
                        tBtn.setImageResource(R.drawable.off);

                    turnOnOff(isChecked);
                }
            });


            // Seekbar
            SeekBar skBar = (SeekBar) rootView.findViewById(R.id.seekBar);
            skBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    freq = progress;
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });
            return rootView;
        }

        private void turnOnOff(boolean on) {

            if (on) {
                if (freq != 0) {
                    sr = new StroboRunner();
                    sr.freq = freq;
                    t = new Thread(sr);
                    t.start();
                    return ;
                }
                else
                    camParams.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            }
            if (!on) {
               if (t != null) {
                   sr.stopRunning = true;
                   t = null;
                   return ;
               }
                else
                   camParams.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            }

            cam.setParameters(camParams);
            cam.startPreview();
        }



        private class StroboRunner implements Runnable {

            int freq;
            boolean stopRunning = false;

            @Override
            public void run() {
                Camera.Parameters paramsOn = PlaceholderFragment.this.cam.getParameters();
                Camera.Parameters paramsOff = PlaceholderFragment.this.camParams;
                paramsOn.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                paramsOff.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                try {
                    while (!stopRunning) {
                        PlaceholderFragment.this.cam.setParameters(paramsOn);
                        PlaceholderFragment.this.cam.startPreview();
                        // We make the thread sleeping
                        Thread.sleep(100 - freq);
                        PlaceholderFragment.this.cam.setParameters(paramsOff);
                        PlaceholderFragment.this.cam.startPreview();
                        Thread.sleep(freq);
                     }
                    }
                catch(Throwable t) {}
            }
        }


    }


}
