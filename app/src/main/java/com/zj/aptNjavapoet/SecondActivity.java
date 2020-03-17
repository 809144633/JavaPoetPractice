package com.zj.aptNjavapoet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.yolo.annotation.BindView;
import com.zj.javapoettest.R;

public class SecondActivity extends BaseActivity {

    @BindView(R.id.second_btn)
    Button secondButtonInvoke;

    @Override
    public int getLayoutId() {
        return R.layout.activity_second;
    }

    @Override
    public void initData() {
        secondButtonInvoke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SecondActivity.this, "Success Second", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
