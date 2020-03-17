package com.zj.aptNjavapoet;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.yolo.annotation.ARouter;
import com.yolo.annotation.BindView;
import com.zj.javapoettest.R;

@ARouter(path = "app/MainActivity")
public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";

    @BindView(R.id.btn)
    Button btn;

    @BindView(R.id.btn2)
    Button invokeBtn;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initData() {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick: Success");
                Toast.makeText(MainActivity.this, "aaa", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, SecondActivity.class));
            }
        });

        invokeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick: Success 2");
                Toast.makeText(MainActivity.this, "bbb", Toast.LENGTH_SHORT).show();

            }
        });
    }
}
