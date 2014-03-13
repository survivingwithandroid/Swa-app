package com.survivingwithandroid.pegboard.table;

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

public class Pin {

	public static int[] createDotArray(int dotSize, boolean isFull) {
		int[] dotColors = null;
		
		if (!isFull)
			dotColors = new int[4];
		else
			dotColors = new int[10];
		

		
		return dotColors;
	}
	
	
	public static int getDotIdBkg(int dotSize) {
		
		return 0;
	}
	
	public static int getDotIdDis(int dotSize) {
		
		return 0;
	}
	
	public static int getDotFileCmd(int dotSize) {
		
		return 0;
	}	

	
	public static int getDotDelCmd(int dotSize) {
		
		return 0;
	}	

}
