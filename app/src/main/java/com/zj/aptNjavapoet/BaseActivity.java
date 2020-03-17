package com.zj.aptNjavapoet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import Util.LikeButterKnifeUtil;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        LikeButterKnifeUtil.bind(this);
        initData();
    }

    public abstract void initData();

    public int getLayoutId() {
        return 0;
    }
}
