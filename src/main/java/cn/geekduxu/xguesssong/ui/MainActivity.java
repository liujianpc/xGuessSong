package cn.geekduxu.xguesssong.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;

import cn.geekduxu.xguesssong.R;

public class MainActivity extends Activity {

    /** 盘片动画 */
    private Animation mPanAnim;
    private LinearInterpolator mPanLin;

    /** 拨杆进入动画 */
    private Animation mBarInAnim;
    private LinearInterpolator mBarInLin;

    /** 拨杆出去动画 */
    private Animation mBarOutAnim;
    private LinearInterpolator mBarOutLin;

    //play button
    private ImageButton mBtnPlayStart;

    private ImageView mViewPan;
    private ImageView mViewPanBar;

    /** 动画是否处于运行状态 */
    private boolean mIsRunning;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mViewPan = (ImageView) findViewById(R.id.iv_pan);
        mViewPanBar = (ImageView) findViewById(R.id.iv_pan_bar);

        //anim init here.
        mPanAnim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.rotate);
        mPanLin = new LinearInterpolator();
        mPanAnim.setInterpolator(mPanLin);
        mPanAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                System.out.println("pan rotate");
                mViewPanBar.startAnimation(mBarOutAnim);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        mBarInAnim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.rotate_45);
        mBarInLin = new LinearInterpolator();
        mBarInAnim.setFillAfter(true);
        mBarInAnim.setInterpolator(mBarInLin);
        mBarInAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                System.out.println("in end");
                mViewPan.startAnimation(mPanAnim);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        mBarOutAnim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.rotate_d_45);
        mBarOutLin = new LinearInterpolator();
        mBarOutAnim.setFillAfter(true);
        mBarOutAnim.setInterpolator(mBarOutLin);
        mBarOutAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                System.out.println("out end");
                mIsRunning = false;
                mBtnPlayStart.setVisibility(View.VISIBLE);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        mBtnPlayStart = (ImageButton) findViewById(R.id.btn_play_start);
        mBtnPlayStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handlePlay();
            }
        });

    }

    private void handlePlay(){
        if(mIsRunning){
            return;
        }
        System.out.println("handlePlay : " + mIsRunning);
        mIsRunning = true;
        mViewPanBar.startAnimation(mBarInAnim);
        mBtnPlayStart.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onPause() {
        mViewPan.clearAnimation();
    }

    // AUTO GEN CODES .
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
