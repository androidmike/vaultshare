package com.vaultshare.play;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

/**
 * Created by mchang on 7/4/15.
 */

public class TakePhotoActivity extends Activity {
    private int TAKE_PHOTO_CODE = 0;
    private File   newfile;
    private String dir;

    Uri outputFileUri;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/vs_photos/";
        File newdir = new File(dir);
        newdir.mkdirs();
        String file = dir + ".jpg";
        newfile = new File(file);
        try {
            newfile.createNewFile();
        } catch (IOException e) {
        }
        outputFileUri = Uri.fromFile(newfile);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        startActivityForResult(cameraIntent, TAKE_PHOTO_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TAKE_PHOTO_CODE && resultCode == RESULT_OK) {
            Bitmap b = BitmapFactory.decodeFile(newfile.getAbsolutePath());
            b = processPhoto(b);
            Bus.getInstance().post(new PictureTaken(UIHelper.getImageUri(App.getContext(), b)));
        }
        ;
        finish();
    }


    private Bitmap processPhoto(Bitmap b) {
        b = UIHelper.resize(b, 800, 800);

        try {
            ExifInterface ei = null;
            ei = new ExifInterface(newfile.getAbsolutePath());

            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    b = UIHelper.rotateImage(b, 90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    b = UIHelper.rotateImage(b, 180);
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return b;
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}