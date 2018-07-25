package com.vigorchip.juice.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.vigorchip.juice.MyApplication;
import com.vigorchip.juice.R;
import com.vigorchip.juice.util.ImageUtil;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/7/27.
 */

public class UltraPagerAdapter extends PagerAdapter {
    ArrayList<String> beans;
    private Context context;
    private LayoutInflater inflater;

    public UltraPagerAdapter(Context context, ArrayList<String> beans) {
        this.context = context;
        this.beans = beans;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return beans.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        FrameLayout linearLayout = (FrameLayout) inflater.inflate(R.layout.item_category_vp_child, null);
        ImageView image_iv = (ImageView) linearLayout.findViewById(R.id.image_iv);
        linearLayout.setId(R.id.item_id);


        image_iv.setScaleType(ImageView.ScaleType.FIT_XY);
        FrameLayout.LayoutParams iv_params = new FrameLayout.LayoutParams(MyApplication.sWidthPix, MyApplication.sHeightPix);
        image_iv.setLayoutParams(iv_params);
        image_iv.setScaleType(ImageView.ScaleType.FIT_XY);
        ImageUtil.showImageByName(context, image_iv, beans.get(position));
        container.addView(linearLayout);
        image_iv.setOnTouchListener(new View.OnTouchListener() {
            private int downX = 0;
            private long downTime = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //获取按下的x坐标
                        downX = (int) v.getX();
                        downTime = System.currentTimeMillis();
                        break;
                    case MotionEvent.ACTION_UP:
                        int moveX = (int) v.getX();
                        long moveTime = System.currentTimeMillis();
                        if (downX == moveX && (moveTime - downTime < 500)) {//点击的条件
                        }
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        break;
                }
                return true;
            }
        });
        return linearLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        FrameLayout view = (FrameLayout) object;
        container.removeView(view);
    }
}