package com.wallpapers_manager.cyril;

import java.io.File;

public final class WallpaperManagerConstants {
	public final static File registrationFilesDir = new File("/sdcard/WallpaperManager/");
	public final static boolean makeRegistrationFilesDir() {
		return registrationFilesDir.mkdir();
	}

}