package campus.m2dl.ane.campus.service;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.net.URL;
import java.net.URLConnection;


public class BitmapManagement {

    public static Bitmap loadImage(String url) {
        try {
            URLConnection connection = new URL(url).openConnection();
            connection.connect();

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4;

            BitmapFactory.Options opts = new BitmapFactory.Options();
            // Get bitmap dimensions before reading...
            opts.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(url, opts);
            int width = opts.outWidth;
            int height = opts.outHeight;
            int largerSide = Math.max(width, height);
            opts.inJustDecodeBounds = false; // This time it's for real!
            int sampleSize = calculateInSampleSize(opts,width,height) ; // Calculate your sampleSize here
            opts.inSampleSize = sampleSize;

            return BitmapFactory.decodeStream(connection.getInputStream(),null, opts);
        }
        catch(Exception e) {
            Log.d("Error loading image","Error loading image : "+e.getMessage());
        }
        return null;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }
}
