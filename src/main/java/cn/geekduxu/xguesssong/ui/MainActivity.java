package cn.geekduxu.xguesssong.ui;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import java.util.ArrayList;

import cn.geekduxu.xguesssong.R;
import cn.geekduxu.xguesssong.model.WordButton;
import cn.geekduxu.xguesssong.model.WordGridView;
import cn.geekduxu.xguesssong.util.ViewUtil;

public class MainActivity extends Activity {

    private static final int WORDS_COUNT = 24;

    /**
     * 盘片动画
     */
    private Animation mPanAnim;
    private LinearInterpolator mPanLin;

    /**
     * 拨杆进入动画
     */
    private Animation mBarInAnim;
    private LinearInterpolator mBarInLin;

    /**
     * 拨杆出去动画
     */
    private Animation mBarOutAnim;
    private LinearInterpolator mBarOutLin;

    //play button
    private ImageButton mBtnPlayStart;

    private ImageView mViewPan;
    private ImageView mViewPanBar;

    /**
     * 动画是否处于运行状态
     */
    private boolean mIsRunning;

    /**
     * 文字框容器
     */
    private ArrayList<WordButton> mAllWords;
    private WordGridView mGridView;

    private ArrayList<WordButton> mSelectedWords;
    private LinearLayout mViewWordsContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mViewPan = (ImageView) findViewById(R.id.iv_pan);
        mViewPanBar = (ImageView) findViewById(R.id.iv_pan_bar);

        mGridView = (WordGridView) findViewById(R.id.gridview);
        mViewWordsContainer = (LinearLayout) findViewById(R.id.word_select_container);

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

        initCurrentStageData();
    }

    private void initCurrentStageData() {
        mSelectedWords = initSelectedWords();
        LayoutParams params = new LayoutParams(60, 60);
        for (WordButton btn : mSelectedWords) {
            mViewWordsContainer.addView(btn.mViewButton, params);
        }

        mAllWords = initAllWords();
        mGridView.updateData(mAllWords);


    }

    /**
     * 初始化已选择文字框
     *
     * @return
     */
    private ArrayList<WordButton> initSelectedWords() {
        ArrayList<WordButton> data = new ArrayList<WordButton>();
        for (int i = 0; i < 4; i++) {
            View v = ViewUtil.getView(MainActivity.this, R.layout.gridview_item);
            WordButton holder = new WordButton();
            holder.mViewButton = (android.widget.Button) v.findViewById(R.id.item_btn);
            holder.mViewButton.setTextColor(Color.WHITE);
            holder.mViewButton.setText("");
            holder.mIsVisiable = false;
            holder.mViewButton.setBackgroundResource(R.drawable.game_wordblank);
            data.add(holder);
        }
        return data;
    }

    /**
     * 初始化待选择文字框
     */
    private ArrayList<WordButton> initAllWords() {
        ArrayList<WordButton> data = new ArrayList<WordButton>(WORDS_COUNT);
        for (int i = 0; i < WORDS_COUNT; i++) {
            WordButton button = new WordButton();
            button.mWordString = "好";
            data.add(button);
        }
        return data;
    }

    private void handlePlay() {
        if (mIsRunning) {
            return;
        }
        System.out.println("handlePlay : " + mIsRunning);
        mIsRunning = true;
        mViewPanBar.startAnimation(mBarInAnim);
        mBtnPlayStart.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onPause() {
        try {
            mViewPan.clearAnimation();
        } catch (Exception e) {
        }
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
