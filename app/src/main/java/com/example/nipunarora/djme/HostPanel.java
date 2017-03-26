package com.example.nipunarora.djme;

import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class HostPanel extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_panel);
        Uri playlistsUri = Uri.parse("content://com.google.android.music.MusicContent/playlists");
        Cursor playlists = getApplicationContext().getContentResolver().query(playlistsUri, new String[]{"_id", "playlist_name"}, null, null, null);
        playlists.moveToFirst();
        Log.d("playlist investigation",playlists.getString(1));
        getMusic();

    }
    private String [] getMusic()
    {
        final Cursor mCursor = managedQuery(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[] { MediaStore.Audio.Media.DISPLAY_NAME }, null, null,
                "LOWER(" + MediaStore.Audio.Media.TITLE + ") ASC");

        int count = mCursor.getCount();

        String[] songs = new String[count];
        int i = 0;
        if (mCursor.moveToFirst()) {
            do {
                songs[i] = mCursor.getString(0);
                Log.d("Song Name",songs[i]);
                i++;
            } while (mCursor.moveToNext());
        }

        mCursor.close();

        return songs;
    }
}
