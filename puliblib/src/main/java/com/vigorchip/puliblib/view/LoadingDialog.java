package com.vigorchip.puliblib.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.zhongjia.puliblib.R;


/**
 * 转圈圈，等待
 */
public class LoadingDialog extends Dialog {
    private ImageView dialog_loading_iv;
    private Animation anim;
    Context context;

    public LoadingDialog(Context context) {
        super(context, R.style.loadingDialogStyle);
        this.context = context;
    }

    private LoadingDialog(Context context, int theme) {
        super(context, theme);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading);
        setCanceledOnTouchOutside(false);
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.height = 415;//45
        lp.width = 415;
//        lp.height = display.getHeight() / 128 * 36;//45
//        lp.width = display.getWidth() / 6 * 5;
        getWindow().setAttributes(lp);
        dialog_loading_iv = (ImageView) this
                .findViewById(R.id.dialog_loading_iv);
        ImageView dialog_close_iv = (ImageView) this.findViewById(R.id.dialog_close_iv);
        dialog_close_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoadingDialog.this.dismiss();
            }
        });
        anim = AnimationUtils.loadAnimation(context, R.anim.loading);
        dialog_loading_iv.startAnimation(anim);
    }

    @Override
    public void show() {
        super.show();
        dialog_loading_iv.startAnimation(anim);
    }

}
