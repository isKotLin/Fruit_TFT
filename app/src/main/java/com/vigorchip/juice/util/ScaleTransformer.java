package com.vigorchip.juice.util;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by  on 2016/8/28.
 */
public class ScaleTransformer implements ViewPager.PageTransformer {
    private Context context;
    private float elevation;

    public ScaleTransformer(Context context) {
        this.context = context;
        elevation = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                20, context.getResources().getDisplayMetrics());
    }

    @Override
    public void transformPage(View page, float position) {
        if (position < -1 || position > 1) {

        } else {
        }
    }
}
