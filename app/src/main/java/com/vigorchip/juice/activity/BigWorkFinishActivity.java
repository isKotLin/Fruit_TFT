package com.vigorchip.juice.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.vigorchip.juice.MyApplication;
import com.vigorchip.juice.R;
import com.vigorchip.juice.util.FavouriteUtil;
import com.vigorchip.juice.util.ImageUtil;
import com.vigorchip.juice.util.RecentUtil;
import com.vigorchip.juice.util.TextFontUtil;
import com.vigorchip.juice.util.event.BackMenuEvent;
import com.vigorchip.juice.util.event.MainChangeEvent;
import com.vigorchip.puliblib.base.BaseActivity;

import butterknife.InjectView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2017/1/15.
 */

public class BigWorkFinishActivity extends BaseActivity {

    @InjectView(R.id.isCollectShow_iv)
    ImageView isCollectShow_iv;

    @InjectView(R.id.changefont1_tv)
    TextView changefont1_tv;

    @InjectView(R.id.changefont2_tv)
    TextView changefont2_tv;

    @InjectView(R.id.pic_iv)
    ImageView pic_iv;

    private String cocktailID;
    private String cocktailName;
    private String picID;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_bigworkfinish;
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void initData(Bundle bundle) {
        MyApplication.sp.edit().putString("cocktailID", "").putInt("person", 0).putInt("step", 0).putInt("allStep", 0).putLong("cur_time", 0).commit();
        TextFontUtil.changeRobotoBold(this, changefont1_tv);
        TextFontUtil.changeRobotoBold(this, changefont2_tv);

        cocktailID = getIntent().getStringExtra("cocktailID");
        cocktailName = getIntent().getStringExtra("cocktailName");
        picID = getIntent().getStringExtra("picID");
        if (TextUtils.isEmpty(cocktailID)) {
            finish();
            return;
        }
        if (!TextUtils.isEmpty(picID)) {
            ImageUtil.showImageByName(this, pic_iv, picID);
        }
        if (!TextUtils.isEmpty(cocktailName)) {
            changefont1_tv.setText(cocktailName + " IS READY");
        }
        boolean isCollect = FavouriteUtil.isCollect(cocktailID, null);
        if (isCollect) {
            isCollectShow_iv.setImageResource(R.drawable.btn_favorite_3);
        } else {
            isCollectShow_iv.setImageResource(R.drawable.btn_favorite_4);
        }
        RecentUtil.setCollect(cocktailID, new RecentUtil.CollectCallBack() {
            @Override
            public void YesBack() {

            }
        });

    }


    @OnClick(R.id.isCollectShow_iv)
    public void collectClick() {
        if (TextUtils.isEmpty(cocktailID)) {
            return;
        }
        FavouriteUtil.setCollect(cocktailID, new FavouriteUtil.CollectCallBack() {
            @Override
            public void YesBack() {
                isCollectShow_iv.setImageResource(R.drawable.btn_favorite_3);
                FavouriteUtil.showCollectPop(BigWorkFinishActivity.this, layoutView,true);
            }

            @Override
            public void NoBack() {
                isCollectShow_iv.setImageResource(R.drawable.btn_favorite_4);
                FavouriteUtil.showCollectPop(BigWorkFinishActivity.this, layoutView,false);
            }
        });
    }

    public static void startActivity(Context context, String cocktailID, String picID, String cocktailName) {
        context.startActivity(new Intent(context, BigWorkFinishActivity.class).putExtra("cocktailID", cocktailID).putExtra("picID", picID).putExtra("cocktailName", cocktailName));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void backFinishClick(View view) {
        EventBus.getDefault().post(new MainChangeEvent(1, 0, 0));
        EventBus.getDefault().post(new BackMenuEvent());
        finish();
    }

}
