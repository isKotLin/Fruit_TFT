package com.vigorchip.juice.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vigorchip.db.entity.FoodBean;
import com.vigorchip.juice.R;
import com.vigorchip.juice.util.TextFontUtil;

/**
 * Created by Administrator on 2017/6/19.
 */

public class FoodView extends LinearLayout {


    public FoodView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public FoodView(Context context) {
        super(context);
    }


    public FoodView(final Context context, final FoodBean bean) {
        super(context);
        View myview = LayoutInflater.from(context).inflate(R.layout.view_cocktail, null);
        TextView count_tv = (TextView) myview.findViewById(R.id.count_tv);
        TextView unit_tv = (TextView) myview.findViewById(R.id.unit_tv);
        TextView content_tv = (TextView) myview.findViewById(R.id.content_tv);
        unit_tv.setText(bean.getUnit());
        count_tv.setText(bean.getCount());
        content_tv.setText(bean.getName());


        TextFontUtil.changeRobotoBold(context, count_tv);
        TextFontUtil.changeRobotoBold(context, unit_tv);
        TextFontUtil.changeRobotoLight(context, content_tv);

        addView(myview);
    }

}
