package org.nv95.openmanga.components;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.TextView;

import org.nv95.openmanga.R;

/**
 * Created by nv95 on 05.11.16.
 */

public class RatingView extends TextView {

    private static final int[] mColors = new int[] {
            Color.parseColor("#808080"),
            Color.parseColor("#5ab2ff"),
            Color.parseColor("#ab4ee5"),
            Color.parseColor("#e5483a")
    };

    private Drawable mStarDrawable;

    public RatingView(Context context) {
        super(context);
        init(context);
    }

    public RatingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RatingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public RatingView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        setLines(1);
        mStarDrawable = ContextCompat.getDrawable(context, R.drawable.ic_star);
        mStarDrawable.setAlpha(200);
        setCompoundDrawablesWithIntrinsicBounds(mStarDrawable, null, null, null);
    }

    @SuppressLint("DefaultLocale")
    public void setRating(byte value) {
        int a = value / 10;
        int b = value % 10;
        setText(String.format("%d.%d", a, b));
        int color;
        if (a < 4) {
            color = mColors[0];
        } else if (a < 7) {
            color = mColors[1];
        } else if (a < 9) {
            color = mColors[2];
        } else {
            color = mColors[3];
        }
        mStarDrawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
    }
}
