package com.vigorchip.juice.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.vigorchip.db.entity.FoodBean;
import com.vigorchip.db.entity.FoodHelpBean;
import com.vigorchip.juice.R;
import com.vigorchip.juice.util.TextFontUtil;
import com.vigorchip.puliblib.base.BaseArrayListAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2017/1/17.
 */

public class CocktailAdapter extends BaseArrayListAdapter {

    private Context context;
    private List<FoodBean> foodBeans;
    private List<FoodHelpBean> foodHelpBeans;

    public CocktailAdapter(Context context, List<FoodBean> foodBeans, List<FoodHelpBean> foodHelpBeans) {
        super(context, foodHelpBeans);
        this.context = context;
        this.foodBeans = foodBeans;
        this.foodHelpBeans = foodHelpBeans;
    }

    @Override
    public int getContentView() {
        return R.layout.item_cocktail;
    }

    @Override
    public void onInitView(View view, int position) {
        TextView step_tv = (TextView) get(view, R.id.step_tv);
        TextView explanation_tv = (TextView) get(view, R.id.explanation_tv);
        ListView listview = (ListView) get(view, R.id.listview);

        step_tv.setText("STEP " + (position + 1));
        FoodHelpBean foodHelpBean = foodHelpBeans.get(position);
        String explanation = foodHelpBean.getExplanation();
        explanation = explanation.replace("\n", "\n•");
        explanation_tv.setText("•" + explanation);
        ArrayList<FoodBean> needfoodBeans = new ArrayList<>();
        for (int i = 0; i < foodBeans.size(); i++) {
            FoodBean foodBean = foodBeans.get(i);
            if (foodBean.getCur_step().equals(foodHelpBean.getCur_step())) {
                needfoodBeans.add(foodBean);
            }
        }
        TextFontUtil.changeRobotoLight(context, step_tv);
        listview.setAdapter(new ItemCocktailAdapter(context, needfoodBeans));
        setListViewHeightBasedOnChildren(listview);
    }


    public void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }
}
