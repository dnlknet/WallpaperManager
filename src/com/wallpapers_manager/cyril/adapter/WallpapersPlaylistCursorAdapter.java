package com.wallpapers_manager.cyril.adapter;

import java.io.File;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;

import com.wallpapers_manager.cyril.R;
import com.wallpapers_manager.cyril.WallpaperManagerConstants;
import com.wallpapers_manager.cyril.bdd.WallpapersDBAdapter;
import com.wallpapers_manager.cyril.bdd.WallpapersPlaylistDBAdapter;
import com.wallpapers_manager.cyril.data.Wallpaper;
import com.wallpapers_manager.cyril.data.WallpaperPlaylist;

public class WallpapersPlaylistCursorAdapter extends CursorAdapter {
	protected final LayoutInflater 	mInflater;
	protected final Context 		mContext;
	private Resources				mResources;
	
	public WallpapersPlaylistCursorAdapter(Context context, Cursor cursor) {
		super(context, cursor);
		mInflater = LayoutInflater.from(context);
		mContext = context;
		mResources = mContext.getResources();
	}
	
	@Override
	public void bindView(final View view, Context context, final Cursor cursor) {
		final WallpaperPlaylist wallpaperPlaylist = new WallpaperPlaylist(cursor);
		WallpapersDBAdapter wallpapersDBAdapter = new WallpapersDBAdapter(mContext);
        wallpapersDBAdapter.open();
        	final Wallpaper wallpaper = wallpapersDBAdapter.getWallpaper(wallpaperPlaylist.getWallpaperId());
        wallpapersDBAdapter.close();

		File wallpaperFile = new File(WallpaperManagerConstants._registrationFilesDir, wallpaper.getAddress());
		Bitmap wallpaperBitmap = BitmapFactory.decodeFile(wallpaperFile.getAbsolutePath());
		
		final ImageView wallpaperImageView = (ImageView) view.findViewById(R.id.wallpaper);
		wallpaperImageView.setImageBitmap(wallpaperBitmap);
		
		final CursorAdapter cursorAdapter = this;

		view.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				final CharSequence[] items = mResources.getTextArray(R.array.playlist_wallpaper_menu);

				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
				alertDialogBuilder.setTitle(mResources.getText(R.string.actions));
				alertDialogBuilder.setItems(items, new DialogInterface.OnClickListener() {
				    public void onClick(DialogInterface dialogInterface, int item) {
				    	switch(item){
				    	case 0:
				    		WallpapersPlaylistDBAdapter wallpapersPlaylistDBAdapter = new WallpapersPlaylistDBAdapter(mContext);
				            wallpapersPlaylistDBAdapter.open();
					        	wallpapersPlaylistDBAdapter.remove(wallpaperPlaylist);
					            Cursor curs = wallpapersPlaylistDBAdapter.getCursor(wallpaperPlaylist.getPlaylistId());
					            cursorAdapter.changeCursor(curs);
				            wallpapersPlaylistDBAdapter.close();
				    		break;
				    	}
				    	
				    }
				});
				
				alertDialogBuilder.show();
			}
		});
	}
	
	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		return mInflater.inflate(R.layout.wallpaper, parent, false);
	}
}
