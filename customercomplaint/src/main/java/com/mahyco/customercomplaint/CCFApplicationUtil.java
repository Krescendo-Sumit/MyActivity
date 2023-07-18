package com.mahyco.customercomplaint;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class CCFApplicationUtil {
    public static String getImageDatadetail(String path) {
        String myTable = "Table1";//Set name of your table
        String str = "";
        try {
            if (path != null || path.length() > 0) {
                str = path;//Base64.encodeToString(cursor.getBlob(cursor.getColumnIndex(colname)),Base64.DEFAULT);
                // rowObject.put(cursor.getColumnName(i), Base64.encodeToString(cursor.getBlob(i),Base64.DEFAULT));
                File f = new File(str);
                Bitmap b = BitmapFactory.decodeFile(f.getAbsolutePath());
                // original measurements
                int origWidth = b.getWidth();
                int origHeight = b.getHeight();
                final int destWidth = 200;//or the width you need
                if (origWidth > destWidth) {
                    // picture is wider than we want it, we calculate its target height
                    int destHeight = origHeight / (origWidth / destWidth);
                    // we create an scaled bitmap so it reduces the image, not just trim it
                    // Bitmap b2 = Bitmap.createScaledBitmap(b, 400, 350, false);
                    Bitmap b2 = compressImage(str);//scaleBitmap(b,400,400);


                    // 70 is the 0-100 quality percentage
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    b2.compress(Bitmap.CompressFormat.JPEG, 60, byteArrayOutputStream);
                    byte[] byteArray = byteArrayOutputStream.toByteArray();
                    str = Base64.encodeToString(byteArray, Base64.DEFAULT);
                } else {
                    // 70 is the 0-100 quality percentage
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    b.compress(Bitmap.CompressFormat.JPEG, 60, byteArrayOutputStream);
                    byte[] byteArray = byteArrayOutputStream.toByteArray();
                    str = Base64.encodeToString(byteArray, Base64.DEFAULT);
                }
            }


        } catch (Exception e) {
            Log.d("TAG_NAME", e.getMessage());
            //Balnk image 64 data
            str = "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";

        }
        //  }


        return str;
    }
    //Start
    public static Bitmap compressImage(String str) {
        Bitmap scaledBitmap = null;
        try {
            File f = new File(str);


            BitmapFactory.Options options = new BitmapFactory.Options();
//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
            options.inJustDecodeBounds = true;
            Bitmap bmp = BitmapFactory.decodeFile(f.getAbsolutePath(), options);
            int actualHeight = options.outHeight;
            int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612
            // float maxHeight = 816.0f;
            //float maxWidth = 612.0f;
            float maxHeight = 516.0f;
            float maxWidth = 412.0f;
            float imgRatio = actualWidth / actualHeight;
            float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image
            if (actualHeight > maxHeight || actualWidth > maxWidth) {
                if (imgRatio < maxRatio) {
                    imgRatio = maxHeight / actualHeight;
                    actualWidth = (int) (imgRatio * actualWidth);
                    actualHeight = (int) maxHeight;
                } else if (imgRatio > maxRatio) {
                    imgRatio = maxWidth / actualWidth;
                    actualHeight = (int) (imgRatio * actualHeight);
                    actualWidth = (int) maxWidth;
                } else {
                    actualHeight = (int) maxHeight;
                    actualWidth = (int) maxWidth;
                }
            }

//      setting inSampleSize value allows to load a scaled down version of the original image
            options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
            options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
            options.inPurgeable = true;
            options.inInputShareable = true;
            options.inTempStorage = new byte[16 * 1024];

            try {
//          load the bitmap from its path
                //bmp = BitmapFactory.decodeFile(filePath, options);
                bmp = BitmapFactory.decodeFile(f.getAbsolutePath(), options);
            } catch (OutOfMemoryError exception) {
                exception.printStackTrace();

            }
            try {
                scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
            } catch (OutOfMemoryError exception) {
                exception.printStackTrace();
            }

            float ratioX = actualWidth / (float) options.outWidth;
            float ratioY = actualHeight / (float) options.outHeight;
            float middleX = actualWidth / 2.0f;
            float middleY = actualHeight / 2.0f;

            Matrix scaleMatrix = new Matrix();
            scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

            Canvas canvas = new Canvas(scaledBitmap);
            canvas.setMatrix(scaleMatrix);
            canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
            ExifInterface exif = null;
            try {
                try {
                    exif = new ExifInterface(f.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                int orientation = exif.getAttributeInt(
                        ExifInterface.TAG_ORIENTATION, 0);
                Log.d("EXIF", "Exif: " + orientation);
                Matrix matrix = new Matrix();
                if (orientation == 6) {
                    matrix.postRotate(90);
                    Log.d("EXIF", "Exif: " + orientation);
                } else if (orientation == 3) {
                    matrix.postRotate(180);
                    Log.d("EXIF", "Exif: " + orientation);
                } else if (orientation == 8) {
                    matrix.postRotate(270);
                    Log.d("EXIF", "Exif: " + orientation);
                }
                scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                        scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                        true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return scaledBitmap;
    }
    //end
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        String debugTag = "MemoryInformation";
        // Image nin islenmeden onceki genislik ve yuksekligi
        final int height = options.outHeight;
        final int width = options.outWidth;
        Log.d(debugTag, "image height: " + height + "---image width: " + width);
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        Log.d(debugTag, "inSampleSize: " + inSampleSize);
        return inSampleSize;
    }
}
