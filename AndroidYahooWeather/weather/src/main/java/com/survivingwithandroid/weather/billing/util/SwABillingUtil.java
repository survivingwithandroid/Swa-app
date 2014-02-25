package com.survivingwithandroid.weather.billing.util;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.survivingwithandroid.weather.R;

/**
 * Created by Francesco on 21/02/14.
 */
public class SwABillingUtil {

    public static String KEY = "Your_key_here";
    private static String SKU_DONATE_W2 = "SKU_IN_APP";
    //private static String SKU_DONATE_W2 = "android.test.purchased";


    public static void showDonateDialog(final Context ctx, final IabHelper mHelper, final Activity act) {
        LayoutInflater inf = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inf.inflate(R.layout.popdonate_layout, null, false);

        final PopupWindow pw = new PopupWindow(v);
        pw.setWidth(RelativeLayout.LayoutParams.WRAP_CONTENT);
        pw.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);

        Button canBtn = (Button) v.findViewById(R.id.btnCancelDonate);


        canBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                pw.dismiss();
            }
        });

        final Button donBtn = (Button) v.findViewById(R.id.btnDonate);
        donBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String payload="test";

                mHelper.launchPurchaseFlow(act, SKU_DONATE_W2, 2000,
                        new IabHelper.OnIabPurchaseFinishedListener() {
                            @Override
                            public void onIabPurchaseFinished(IabResult result, Purchase info) {
                                if (result.isFailure()) {
                                    Log.i("SwA", "Failure = " + result);
                                    //Toast.makeText(getActivity(), "Error purchasing: " + result, Toast.LENGTH_LONG).show();
                                    return;
                                } else if (info.getSku().equals(SKU_DONATE_W2)) {
                                    donBtn.setClickable(false);
                                    Toast.makeText(ctx, "Thank you!", Toast.LENGTH_LONG).show();
                                }
                            }
                        }, payload);
                pw.dismiss();

            }
        });

        pw.showAtLocation(v, Gravity.CENTER, 0, 0);
    }
}
