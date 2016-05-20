package org.nv95.openmanga.utils.imagecontroller;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

import com.nostra13.universalimageloader.core.process.BitmapProcessor;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by admin on 19.05.16.
 */
public class ImageShifter implements BitmapProcessor, SharedPreferences.OnSharedPreferenceChangeListener {
    private static final ImageShifter instance = new ImageShifter();
    private int mSpace;
    private boolean mShift;
    private boolean rtl;

    public static ImageShifter getInstance() {
        return instance;
    }

    private ImageShifter() {
        mShift = false;
        rtl = false;
    }

    public ImageShifter setSpace(int spacePx) {
        mSpace = spacePx;
        return this;
    }

    @Override
    public Bitmap process(Bitmap bitmap) {
        if (!mShift || bitmap.getHeight() <= bitmap.getWidth() * 2.4) {
            return scaleBitmap(bitmap);
        }
        final int count = Math.max((int) Math.sqrt(bitmap.getHeight() / bitmap.getWidth()) - 1, 2);
        final int sectHeight = bitmap.getHeight() / count + 1;
        final Paint paint = new Paint();
        final Bitmap res = Bitmap.createBitmap((bitmap.getWidth() + mSpace) * count - mSpace, sectHeight, bitmap.getConfig());
        final Canvas canvas = new Canvas(res);
        for (int i=0;i<count;i++) {
            canvas.drawBitmap(bitmap,
                    new Rect(0, sectHeight * i, bitmap.getWidth(), sectHeight * (i+1)),     /*source*/
                    !rtl ?                                                                   /*destination*/
                    new RectF((bitmap.getWidth() + mSpace) * i, 0, (bitmap.getWidth() + mSpace) * i + bitmap.getWidth(), sectHeight) :
                            new RectF((bitmap.getWidth() + mSpace) * (count - i - 1), 0, (bitmap.getWidth() + mSpace) * (count - i) - mSpace, sectHeight),
                    paint);
        }
        bitmap.recycle();
        return scaleBitmap(res);
    }

    private Bitmap scaleBitmap (Bitmap bitmap) {
        int width = 0, height = 0;
        float videoAspectRatio = bitmap.getWidth() / (float) bitmap.getHeight();
        if (bitmap.getHeight() > GL10.GL_MAX_TEXTURE_SIZE) {

            height = GL10.GL_MAX_TEXTURE_SIZE;
            if (videoAspectRatio > 1) {
                width = (int) (height / videoAspectRatio);
            } else {
                width = (int) (height * videoAspectRatio);
            }
        }
        if (bitmap.getWidth() > GL10.GL_MAX_TEXTURE_SIZE) {
            width = GL10.GL_MAX_TEXTURE_SIZE;
            if (videoAspectRatio > 1) {
                height = (int) (width / videoAspectRatio);
            } else {
                height = (int) (width * videoAspectRatio);
            }
        }

        if (width == 0 || height == 0)
            return bitmap;

        Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
        bitmap.recycle();
        return newBitmap;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        switch (key) {
            case "shifts":
                mShift = sharedPreferences.getBoolean("shifts", false);
                break;
            case "direction":
                rtl = "2".equals(sharedPreferences.getString("direction", "0"));
        }
    }
}