package com.work.xinlai.util;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.util.HashMap;

/**
 * Created by Administrator on 2016/12/15.
 */
public class ZxingUtils {
    private static final String TAG_ZxingUtils = "ZxingUtils";
    private static final HashMap<EncodeHintType, Object> QR_HINT = new HashMap<EncodeHintType, Object>();

    public static Bitmap makeQrCodeBitmap(String info, int size) {
        return makeQrCodeBitmap(info, size, size, Color.BLACK, Color.WHITE);
    }

    /**
     * 制作二维码
     **/
    public static Bitmap makeQrCodeBitmap(String info, int width, int height, int colorDark, int colorLight) {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(info, BarcodeFormat.QR_CODE, width, height, QR_HINT);
        } catch (WriterException e) {
            Logger.e(TAG_ZxingUtils, e.getMessage());
            return null;
        } catch (NoSuchMethodError e) {
            Logger.e(TAG_ZxingUtils, e.getMessage());
            return null;
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                bitmap.setPixel(x, y, bitMatrix.get(x, y) ? colorDark : colorLight);
            }
        }

        return bitmap;
    }
}
