package com.soundcloud.android.crop;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class BitmapUtil {
    private BitmapUtil() {

    }

    public static Bitmap getBitmapFromFile(File file, int w) {
        FileInputStream fileInputStream = null;
        try {
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            if (file == null || !file.exists()) {
                return null;
            } else if (file.length() == 0) {
                file.delete();
                return null;
            }
            fileInputStream = new FileInputStream(file);
            BitmapFactory.decodeFile(file.getAbsolutePath(), opts);
            int be =getSampleSize(opts.outWidth, w);
            opts.inSampleSize = be;
            opts.inJustDecodeBounds = false;
            opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
            return BitmapFactory.decodeStream(fileInputStream, null, opts);


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static int getSampleSize(float size, float outSize) {
        int be = (int) (size / outSize);
        if (be <= 0) {
            be = 1;
        }
        return be;

    }

    public static Bitmap getMatrixBitmap(Bitmap bm, int w, int h, boolean needRecycle) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        boolean isCompress = (width > w && height > h) && (w != 0 && h != 0);
        if (isCompress) {
            float scaleWidth = ((float) w) / width;
            float scaleHeight = ((float) h) / height;
            float scale = Math.max(scaleWidth, scaleHeight);
            Matrix matrix = new Matrix();
            matrix.postScale(scale, scale);
            Bitmap bitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
            if (needRecycle && bm != null && bm != bitmap) {
                bm.recycle();
            }
            return bitmap;
        } else {
            return bm;
        }

    }

}
