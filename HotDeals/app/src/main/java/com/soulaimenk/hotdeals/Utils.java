package com.soulaimenk.hotdeals;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by Soulaimen on 20/07/2016.
 */
public class Utils {

    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality) {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    public static int GetColorPerPercentage(int percentage) {

        if (percentage < 0 || percentage > 100) {
            return R.color.colorWhite;
        } else if (percentage > 0 && percentage < 20) {
            return R.color.colorBlue_500;
        } else if (percentage >= 20 && percentage < 40) {
            return R.color.colorGreen_500;
        } else if (percentage >= 40 && percentage < 60) {
            return R.color.colorOrange_500;
        } else if (percentage >= 60 && percentage <= 100) {
            return R.color.colorRed_500;
        }
        return R.color.colorWhite;
    }

    public static long GetRemainigTimeInMillis(Date articleCreationDate, int hours) {
        long futurDateInMillis = articleCreationDate.getTime() + (TimeUnit.HOURS.toMillis(hours));
        long nowDateInMillis = (new Date().getTime());
        return futurDateInMillis - nowDateInMillis;
    }

    public static String FormatDate(Date date) {
        SimpleDateFormat fmtOut = new SimpleDateFormat("dd-MM-yyyy");
        return fmtOut.format(date);
    }
}
