package com.vigorchip.juice.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vigorchip.db.entity.FoodBean;
import com.vigorchip.juice.R;
import com.vigorchip.juice.util.TextFontUtil;
import com.vigorchip.puliblib.utils.Logutil;

import java.util.List;

/**
 * Created by Administrator on 2017/6/19.
 */

public class ItemCocktailAdapter extends BaseAdapter {
    private List<FoodBean> beans;
    private Context context;
    private LayoutInflater inflater;

    public ItemCocktailAdapter(Context context, List<FoodBean> beans) {
        this.context = context;
        this.beans = beans;
        Logutil.e("食材个数=" + beans.size());
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return beans.size();
    }

    @Override
    public Object getItem(int position) {
        return beans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.item_item_cocktail, null);
        FoodBean bean = beans.get(position);
        Logutil.e("AAAAAAAAAAAAAAAAAA" + bean.toString());
        TextView count_tv = (TextView) convertView.findViewById(R.id.count_tv);
        TextView unit_tv = (TextView) convertView.findViewById(R.id.unit_tv);
        TextView content_tv = (TextView) convertView.findViewById(R.id.content_tv);
        unit_tv.setText(bean.getUnit());
        count_tv.setText(bean.getCount());
        content_tv.setText(bean.getName());


        TextFontUtil.changeRobotoBold(context, count_tv);
        TextFontUtil.changeRobotoBold(context, unit_tv);
        TextFontUtil.changeRobotoLight(context, content_tv);
        return convertView;
    }
}
