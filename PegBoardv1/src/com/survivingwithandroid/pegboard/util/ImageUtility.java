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
package com.survivingwithandroid.pegboard.util;

import java.io.OutputStream;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore.Images;

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
public class ImageUtility {

	public static void saveImage(Bitmap b, String fileName, Context context)
			throws SaveFileException {
		ContentValues values = new ContentValues();

		String path = Environment.getExternalStorageDirectory().toString();

		values.put(Images.Media.TITLE, path + "/DCIM/" + fileName + ".png");
		values.put(Images.Media.DATE_ADDED, System.currentTimeMillis());
		values.put(Images.Media.MIME_TYPE, "image/png");
		Uri uri = context.getContentResolver().insert(
				Images.Media.EXTERNAL_CONTENT_URI, values);

		try {
			OutputStream outStream = context.getContentResolver()
					.openOutputStream(uri);

			b.compress(Bitmap.CompressFormat.PNG, 100, outStream);
			outStream.flush();
			outStream.close();

		} catch (Throwable t) {
			throw new SaveFileException();
		}
	}
}
