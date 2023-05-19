package com.example.tenant.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageUtil {
    public static boolean saveBitmapToFile(Bitmap bitmap, String filePath, Context context) {
        File file = new File(filePath);
        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput(filePath, Context.MODE_PRIVATE);
            bitmap.compress(CompressFormat.PNG, 100, fos); // Или CompressFormat.JPEG для JPEG-изображений
            fos.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String generateFilePath(String fileName) {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/my_images");
        if (!myDir.exists()) {
            myDir.mkdirs();
        }
        return myDir.getAbsolutePath() + "/" + fileName;
    }
}

