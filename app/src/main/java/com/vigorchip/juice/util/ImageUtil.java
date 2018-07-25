package com.vigorchip.juice.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.vigorchip.juice.R;
import com.vigorchip.puliblib.utils.Logutil;

/**
 * Created by Administrator on 2017/1/16.
 */

public class ImageUtil {


    public static void showImageByName(Context context, ImageView image_iv, String name) {
        Logutil.e("图片地址=" + name);
        try {
            int resId = context.getResources().getIdentifier(name, "drawable", context.getPackageName());
//            image_iv.setImageResource(resId);
            Glide.with(context).load(resId).into(image_iv);
//            Picasso.with(context).load(resId).into(image_iv);
//            Glide.get(context).clearMemory();
        } catch (Exception e) {
            image_iv.setImageResource(R.drawable.icon_empty);
        }
    }

}
