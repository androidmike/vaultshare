package com.vaultshare.play;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;

/**
 * Created by mchang on 7/4/15.
 */
public class UIHelper {
    public static void setGrayScale(ImageView v) {
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);  //0 means grayscale
        ColorMatrixColorFilter cf = new ColorMatrixColorFilter(matrix);
        v.setColorFilter(cf);
    }

    public static void resetColor(ImageView v) {
        v.setColorFilter(null);
    }

    public static Bitmap resize(Bitmap image, int maxWidth, int maxHeight) {
        if (maxHeight > 0 && maxWidth > 0) {
            int width = image.getWidth();
            int height = image.getHeight();
            float ratioBitmap = (float) width / (float) height;
            float ratioMax = (float) maxWidth / (float) maxHeight;

            int finalWidth = maxWidth;
            int finalHeight = maxHeight;
            if (ratioMax > 1) {
                finalWidth = (int) ((float) maxHeight * ratioBitmap);
            } else {
                finalHeight = (int) ((float) maxWidth / ratioBitmap);
            }
            image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);
            return image;
        } else {
            return image;
        }
    }

        private static int THIRD_COLOR = Color.RED;
        private static int SECOND_COLOR = Color.parseColor("#b103af");
        private static int FIRST_COLOR = Color.parseColor("#160088");

        public static int getColor(float p) {
            int c0;
            int c1;
            if (p <= 0.5f) {
                p *= 2;
                c0 = FIRST_COLOR;
                c1 = SECOND_COLOR;
            } else {
                p = (p - 0.5f) * 2;
                c0 = SECOND_COLOR;
                c1 = THIRD_COLOR;
            }
            int a = ave(Color.alpha(c0), Color.alpha(c1), p);
            int r = ave(Color.red(c0), Color.red(c1), p);
            int g = ave(Color.green(c0), Color.green(c1), p);
            int b = ave(Color.blue(c0), Color.blue(c1), p);
            return Color.argb(a, r, g, b);
        }

        private static int ave(int src, int dst, float p) {
            return src + java.lang.Math.round(p * (dst - src));
        }


    public static Bitmap rotateImage(Bitmap img, int rotation) {
        if (rotation != 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(rotation);
            Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
            img.recycle();
            return rotatedImg;
        } else {
            return img;
        }
    }

    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(App.getContext().getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    private static float interpolate(float a, float b, float proportion) {
        return (a + ((b - a) * proportion));
    }

    /** Returns an interpoloated color, between <code>a</code> and <code>b</code> */
    public static int interpolateColor(int a, int b, float proportion) {
        float[] hsva = new float[3];
        float[] hsvb = new float[3];
        Color.colorToHSV(a, hsva);
        Color.colorToHSV(b, hsvb);
        for (int i = 0; i < 3; i++) {
            hsvb[i] = interpolate(hsva[i], hsvb[i], proportion);
        }
        return Color.HSVToColor(hsvb);
    }
}
