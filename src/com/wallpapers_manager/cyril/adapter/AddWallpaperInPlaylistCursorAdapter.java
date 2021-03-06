package com.wallpapers_manager.cyril.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wallpapers_manager.cyril.R;
import com.wallpapers_manager.cyril.bdd.WallpapersPlaylistDBAdapter;
import com.wallpapers_manager.cyril.data.Playlist;
import com.wallpapers_manager.cyril.data.Wallpaper;
import com.wallpapers_manager.cyril.data.WallpaperPlaylist;
import static com.wallpapers_manager.cyril.WallpaperManagerConstants.*;

public class AddWallpaperInPlaylistCursorAdapter extends CursorAdapter {
	protected final LayoutInflater 	mInflater;
	protected final Context 		mContext;
	
	private Wallpaper 	mWallpaper;
	private Dialog 		mDialog;
	
	public AddWallpaperInPlaylistCursorAdapter(Context context, Cursor cursor, Wallpaper wpp, Dialog dg) {
		super(context, cursor);
		mInflater = LayoutInflater.from(context);
		mContext = context;
		mWallpaper = wpp;
		mDialog = dg;
	}
	
	@Override
	public void bindView(final View view, Context context, final Cursor cursor) {
		final Playlist playlist = new Playlist(cursor.getInt(0), cursor.getString(1), cursor.getInt(2));

		ImageView imageView = (ImageView) view.findViewById(R.id.image);
		imageView.setImageResource(playlist.isSelected() ? R.drawable.selected_playlist : R.drawable.playlist);

		TextView nameTextView = (TextView) view.findViewById(R.id.name);
		nameTextView.setText(cursor.getString(1));

		view.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				WallpapersPlaylistDBAdapter wallpapersPlaylistDBAdapter = new WallpapersPlaylistDBAdapter(mContext);
				wallpapersPlaylistDBAdapter.open();
					WallpaperPlaylist wallpaperPlaylist = new WallpaperPlaylist(mWallpaper.getId(), playlist.getId());
					wallpapersPlaylistDBAdapter.insertWallpaperPlaylist(wallpaperPlaylist);
				wallpapersPlaylistDBAdapter.close();
				Intent intentBroadcast = new Intent(BROADCAST_UPDATE_WPP_PL);
				mContext.sendBroadcast(intentBroadcast);
				mDialog.dismiss();
			}
		});
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		return mInflater.inflate(R.layout.playlist, parent, false);
	}
}
