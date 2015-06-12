package meme5.c4q.nyc.meme_project;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;

/**
 * Created by sufeizhao on 6/9/15.
 */
public class Decode {

    // Method used to resize, rotate and set up bitmap
    public static Bitmap decodeFile(Bitmap image, String filePath, boolean demo) {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, o);

        // The new size we want to scale to
        // Log actual size of image for reference
        final int REQUIRED_SIZE = 2048;
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        Log.d("width", String.valueOf(width_tmp)); //1456
        Log.d("height", String.valueOf(height_tmp)); //2592

        // Find the correct scale value. It should be the power of 2.
        int scale = 1;
        while (true) {
            if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        Bitmap b1 = BitmapFactory.decodeFile(filePath, o2);
        image = ExifUtils.rotateBitmap(filePath, b1);

        // if in demotivational layout, add black border to image
        if (demo) {
            image = addBorder(image, Color.BLACK, 10);
            image = addBorder(image, Color.WHITE, 10);
            image = addBorder(image, Color.BLACK, 60, 60, 60, 270);
        }

        return image;
    }

    // addBorder only used for demotivational poster
    private static Bitmap addBorder(Bitmap bmp, int color, int borderSize) {
        return addBorder(bmp, color, borderSize, borderSize, borderSize, borderSize);
    }

    private static Bitmap addBorder(Bitmap bmp, int color, int left, int top, int right, int bottom) {
        Bitmap bmpWithBorder = Bitmap.createBitmap(bmp.getWidth() + left + right, bmp.getHeight() + top + bottom, bmp.getConfig());
        Canvas canvas = new Canvas(bmpWithBorder);
        canvas.drawColor(color);
        canvas.drawBitmap(bmp, left, top, null);

        return bmpWithBorder;
    }
}
