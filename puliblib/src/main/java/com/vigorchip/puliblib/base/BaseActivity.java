package com.vigorchip.puliblib.base;

import android.annotation.TargetApi;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.vigorchip.puliblib.utils.Logutil;
import com.vigorchip.puliblib.utils.MachineErrorManager;
import com.vigorchip.puliblib.utils.event.ErrorEvent;
import com.vigorchip.puliblib.utils.event.NullEvent;
import com.vigorchip.puliblib.view.LoadingDialog;

import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;


public abstract class BaseActivity extends FragmentActivity {
    private Toast toast;
    private LoadingDialog loadingDialog;
    public View layoutView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layoutView = LayoutInflater.from(this).inflate(getLayoutId(), null);
        setContentView(layoutView);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        ButterKnife.inject(this);
        loadingDialog = new LoadingDialog(this);

        initView();
        initData(savedInstanceState);
    }

    public void onEventMainThread(NullEvent event) {
    }

    public void onEventMainThread(ErrorEvent event) {
        if (this.hasWindowFocus()) {
            Logutil.e("收到的异常信息" + event.getTag());
            if (event.getTag().equals("E2") && !MachineErrorManager.ErrorMessage.equals("E2")) {
                MachineErrorManager.showE2Dialog(this);
                MachineErrorManager.ErrorMessage = event.getTag();
            } else if (event.getTag().equals("E3") && !MachineErrorManager.ErrorMessage.equals("E3")) {
                MachineErrorManager.showE3Dialog(this);
                MachineErrorManager.ErrorMessage = event.getTag();
            } else if(event.getTag().equals("E0")){
                MachineErrorManager.ErrorMessage = event.getTag();
            }
        }
    }

    /**
     * 传布局id
     *
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * 初始化view
     */
    protected abstract void initView();

    /**
     * 初始化数据
     *
     * @param bundle
     */
    protected abstract void initData(Bundle bundle);

    /**
     * 等待圈圈
     *
     * @param isShow
     */
    public void showProgressBar(boolean isShow) {
        if (isShow) {
            loadingDialog.show();
        } else {
            if (loadingDialog != null) {
                loadingDialog.dismiss();
            }
        }
    }

    public void hideProgressBar() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

    public void loading_again(View view) {

    }


    /**
     * 吐司
     *
     * @param msg
     */

    public void showToast(String msg) {
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        if (null == toast) {
            toast = Toast.makeText(this, msg, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
        } else {
            toast.setText(msg);
        }
        toast.show();
    }

    public void showToast(int msg) {
        if (null == toast) {
            toast = Toast.makeText(this, getResources().getString(msg),
                    Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
        } else {
            toast.setText(getResources().getString(msg));
        }
        toast.show();
    }

    /**
     * Hide the toast, if any.
     */
    private void hideToast() {
        if (null != toast) {
            toast.cancel();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            EventBus.getDefault().unregister(this);
        } catch (Exception e) {

        }
    }

    @Override
    public void onPause() {
        super.onPause();
        hideToast();
    }

    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }

    @TargetApi(19)
    protected void setTranslucentStatus() {
        Window window = getWindow();
        window.setFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

}
