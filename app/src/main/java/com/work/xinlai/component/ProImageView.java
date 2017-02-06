package com.work.xinlai.component;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;


/**
 * Pro ImageView which can load bitmap asynced. And since a LruCache is used for the bitmap, please
 * handle cases when the image is too large (for example, larger than 600X600).
 */
public class ProImageView extends ImageView {

    private int mDefaultImage;
    private String mImageUrl;

    private boolean mSetBackground;

    public ProImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setBackground(String url, int defaultImage) {
        mSetBackground = true;
        mImageUrl = url;
        mDefaultImage = defaultImage;
        setBackgroundResource(mDefaultImage);

    }

    public void setImage(String url, int defaultImage) {
        mImageUrl = url;
        mDefaultImage = defaultImage;
        setImageResource(mDefaultImage);

    }


}
