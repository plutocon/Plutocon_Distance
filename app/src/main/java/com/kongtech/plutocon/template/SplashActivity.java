package com.kongtech.plutocon.template;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.kongtech.plutocon.template.distance.R;
import com.kongtech.plutocon.template.distance.TemplateActivity;

public class SplashActivity extends AppCompatActivity {

    private final static int SPLASH_DELAY = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, TemplateActivity.class));
                finish();
            }
        }, SPLASH_DELAY);
    }
}
