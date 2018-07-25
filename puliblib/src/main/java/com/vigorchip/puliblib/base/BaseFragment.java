package com.vigorchip.puliblib.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.vigorchip.puliblib.utils.event.NullEvent;
import com.vigorchip.puliblib.view.LoadingDialog;

import butterknife.ButterKnife;

public abstract class BaseFragment extends Fragment {
    private Toast toast;
    private LoadingDialog loadingDialog;
    public FragmentActivity myActivity;
    public View layoutView;
    public Context mContext;

    @Override
    public void onAttach(Context context) {
        mContext = context;
        super.onAttach(context);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layoutView = inflater.inflate(getLayoutId(), container, false);
        myActivity = getActivity();
        loadingDialog = new LoadingDialog(myActivity);
        loadingDialog.setCancelable(true);
        return layoutView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        ViewX.inject(this);
        ButterKnife.inject(this, layoutView);
        initView(layoutView);
        initData(savedInstanceState);
    }

    protected abstract int getLayoutId();

    protected abstract void initView(View view);

    protected abstract void initData(Bundle savedInstanceState);

    protected void showProgressBar(boolean show) {
        showProgressBar(show, "");
    }

    protected void setProgressBarProgress() {
        if (loadingDialog == null) {
            return;
        }
    }

    protected void showProgressBar(boolean show, String message) {
        if (loadingDialog == null) {
            return;
        }
        if (show) {
            loadingDialog.show();
        } else {
            loadingDialog.hide();
        }
    }

    protected boolean isProgressbarShow() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            return true;
        }
        return false;
    }


    protected void showProgressBar(int messageId) {
        String message = getString(messageId);
        showProgressBar(true, message);
    }

    public void onEventMainThread(NullEvent event) {
    }

    public void showToast(String msg) {
        if (null == toast) {
            toast = Toast.makeText(myActivity, msg, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
        } else {
            toast.setText(msg);
        }

        toast.show();
    }


    public void showToast(int msg) {
        if (null == toast) {
            toast = Toast.makeText(myActivity,
                    getResources().getString(msg), Toast.LENGTH_LONG);
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

    public void onPause() {
        super.onPause();
        hideToast();
    }


    public void onDestroy() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
            loadingDialog = null;
        }
        super.onDestroy();
    }
}
