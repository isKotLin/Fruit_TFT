package com.vigorchip.juice.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by Administrator on 2017/2/28.
 */

public class ViewPageContainer extends FrameLayout {
    private ViewPager mViewPager;

    public ViewPageContainer(Context context) {
        this(context, null);
    }

    public ViewPageContainer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewPageContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initData();
    }

    private void initData() {
        // 允许子View在其区域内进行绘制
        setClipChildren(false);

        // child clip功能在Android3.x之后的版本会因为硬件加速而不起作用，所以这里需要关闭硬件加速功能
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        try {
            mViewPager = (ViewPager) getChildAt(0);
        } catch (Exception e) {
            throw new IllegalStateException("The root child of PagerContainer must be a ViewPager");
        }
    }

    public ViewPager getViewPager() {
        return mViewPager;
    }
}