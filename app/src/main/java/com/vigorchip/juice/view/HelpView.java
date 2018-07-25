package com.vigorchip.juice.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.vigorchip.db.entity.FoodHelpBean;
import com.vigorchip.juice.R;
import com.vigorchip.juice.util.TextFontUtil;

/**
 * Created by Administrator on 2017/6/19.
 */

public class HelpView extends LinearLayout {


    public HelpView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public HelpView(Context context) {
        super(context);
    }


    public HelpView(final Context context, final FoodHelpBean bean) {
        super(context);
        View myview = LayoutInflater.from(context).inflate(R.layout.view_help, null);
        TextView step_tv = (TextView) myview.findViewById(R.id.step_tv);
        TextView explanation_tv = (TextView) myview.findViewById(R.id.explanation_tv);
        ListView listview = (ListView) myview.findViewById(R.id.listview);

        step_tv.setText("STEP " + bean.getCur_step());
        String explanation = bean.getExplanation();
        explanation = explanation.replace("\n", "\n•");
        explanation_tv.setText("•" + explanation);
        TextFontUtil.changeRobotoLight(context, step_tv);
        addView(myview);
    }

}
