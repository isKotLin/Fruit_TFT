package com.vigorchip.juice.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.vigorchip.juice.R;
import com.vigorchip.juice.util.PublicUtil;
import com.vigorchip.puliblib.base.BaseActivity;

import butterknife.InjectView;

/**
 * Created by Administrator on 2017/2/22.
 */

public class SetWiFiPswdActivity extends BaseActivity {

    @InjectView(R.id.wifiname_tv)
    TextView wifiname_tv;

    @InjectView(R.id.password_et)
    EditText password_et;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_setwifipswd;
    }

    @Override
    protected void initView() {
        wifiname_tv.setText(getIntent().getStringExtra("wifiName"));
        password_et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    PublicUtil.hideSoftInput(SetWiFiPswdActivity.this);
                    String password = password_et.getText().toString();
                    Intent intent = new Intent();
                    intent.putExtra("password", password);
                    setResult(RESULT_OK, intent);
                    finish();
                }
                return false;
            }
        });
    }

    public void backFinishClick(View view) {
        Intent intent = new Intent();
        intent.putExtra("password", "");
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void initData(Bundle bundle) {

    }

    public static void startActivity(Activity context, String wifiName, int requestCode) {
        context.startActivityForResult(new Intent(context, SetWiFiPswdActivity.class).putExtra("wifiName", wifiName), requestCode);
    }
}
