package com.vaultshare.play;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;

import java.io.File;

/**
 * Created by mchang on 7/6/15.
 */
public class FileHelper {
    public static File getFile(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};

        CursorLoader loader = new CursorLoader(App.getContext(), uri, projection, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return new File(cursor.getString(column_index));
    }
}
