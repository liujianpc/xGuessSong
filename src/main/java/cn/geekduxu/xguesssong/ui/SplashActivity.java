package cn.geekduxu.xguesssong.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import cn.geekduxu.xguesssong.R;

public class SplashActivity extends Activity {

    private static final int ANIM_TIME = 1000;

    private TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        init();
    }

    private void init() {
        tvTitle = (TextView) findViewById(R.id.tv_show);

        TranslateAnimation ta = new TranslateAnimation(0, 0, 60, -60);
        ta.setDuration(ANIM_TIME);
        ta.setFillAfter(true);

        AlphaAnimation aa = new AlphaAnimation(0.2f, 1.0f);
        aa.setDuration(ANIM_TIME);
        aa.setFillAfter(true);

        AnimationSet as = new AnimationSet(true);
        as.setFillAfter(true);
        as.setInterpolator(SplashActivity.this, android.R.anim.accelerate_decelerate_interpolator);
        as.setDuration(ANIM_TIME);
        as.addAnimation(aa);
        as.addAnimation(ta);

        as.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) { }
            @Override
            public void onAnimationEnd(Animation animation) {
                TranslateAnimation ta = new TranslateAnimation(0, 0, -60, -180);
                ta.setDuration(ANIM_TIME);
                ta.setFillAfter(true);

                AlphaAnimation aa = new AlphaAnimation(1.0f, 0.0f);
                aa.setDuration(ANIM_TIME);
                aa.setFillAfter(true);

                AnimationSet as = new AnimationSet(true);
                as.setFillAfter(true);
                as.setInterpolator(SplashActivity.this, android.R.anim.accelerate_decelerate_interpolator);
                as.setDuration(ANIM_TIME);
                as.addAnimation(aa);
                as.addAnimation(ta);

                as.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) { }
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        startActivity(new Intent(SplashActivity.this, GuideActivity.class));
                        overridePendingTransition(R.anim.alpha_out, R.anim.alpha_in);
                        finish();
                    }
                    @Override
                    public void onAnimationRepeat(Animation animation) { }
                });

                tvTitle.startAnimation(as);
            }
            @Override
            public void onAnimationRepeat(Animation animation) { }
        });

        tvTitle.startAnimation(as);
    }

    @Override
    public void onBackPressed() {
    }
}
