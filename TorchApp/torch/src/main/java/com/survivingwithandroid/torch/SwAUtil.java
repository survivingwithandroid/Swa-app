package com.survivingwithandroid.torch;

import android.content.Context;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
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

public class SwAUtil {
    public static void showAboutDialog(final Context ctx) {
        LayoutInflater inf = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inf.inflate(R.layout.about_layout, null, false);
        createLink(v, R.id.link, "<a href='https://github.com/survivingwithandroid/Swa-app'> Surviving with Android app </a>");
        createLink(v, R.id.link2, "<a href='http://www.survivingwithandroid.com'> Surviving with Android </a>");

        final PopupWindow pw = new PopupWindow(v);
        pw.setWidth(RelativeLayout.LayoutParams.WRAP_CONTENT);
        pw.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);

        Button canBtn = (Button) v.findViewById(R.id.btnCancel);


        canBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                pw.dismiss();
            }
        });

        pw.showAtLocation(v, Gravity.CENTER, 0, 0);
    }

    private static void createLink(View v, int id, String html) {
        TextView textView =(TextView) v.findViewById(id);
        textView.setClickable(true);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setText(Html.fromHtml(html));
    }
}
