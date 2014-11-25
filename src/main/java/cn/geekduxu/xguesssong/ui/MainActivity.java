package cn.geekduxu.xguesssong.ui;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import cn.geekduxu.xguesssong.R;
import cn.geekduxu.xguesssong.data.Const;
import cn.geekduxu.xguesssong.model.Music;
import cn.geekduxu.xguesssong.model.WordButton;
import cn.geekduxu.xguesssong.model.WordButtonClickListener;
import cn.geekduxu.xguesssong.model.WordGridView;
import cn.geekduxu.xguesssong.util.ViewUtil;

public class MainActivity extends Activity implements WordButtonClickListener {

    private static final int WORDS_COUNT = 24;
    private static final int SPASH_TIMES = 7;

    private static final int STATUS_ANSWER_RIGHT = 1;
    private static final int STATUS_ANSWER_WRONG = 2;
    private static final int STATUS_ANSWER_LACK = 3;

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
     * 过关界面
     */
    private LinearLayout mPassView;

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

    private Music mCurrentMusic;
    private int mCurrentIndex = 0;

    /**
     * 文字框容器
     */
    private ArrayList<WordButton> mAllWords;
    private WordGridView mGridView;

    private ArrayList<WordButton> mSelectedWords;
    private LinearLayout mViewWordsContainer;

    /**
     * 当前金币数量
     */
    private int mCurrentCoins = Const.TOTAL_COINS;

    private TextView mViewCoins;
    private TextView mViewCurrentStage;
    private TextView mViewCurrentMusicName;

    private ImageView mViewDelete;
    private ImageView mViewTip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mViewPan = (ImageView) findViewById(R.id.iv_pan);
        mViewPanBar = (ImageView) findViewById(R.id.iv_pan_bar);

        mGridView = (WordGridView) findViewById(R.id.gridview);
        mViewWordsContainer = (LinearLayout) findViewById(R.id.word_select_container);

        mViewCoins = (TextView) findViewById(R.id.txt_bar_icon);
        mViewCoins.setText(mCurrentCoins + "");

        mViewDelete = (ImageView) findViewById(R.id.btn_delete_word);
        mViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteWord();
            }
        });
        mViewTip = (ImageView) findViewById(R.id.btn_tip_answer);
        mViewTip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tipAnswer();
            }
        });

        mGridView.setOnWordButtonClickedListener(this);

        //anim init here.
        initAnims();

        mBtnPlayStart = (ImageButton) findViewById(R.id.btn_play_start);
        mBtnPlayStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handlePlay();
            }
        });

        initCurrentStageData();


        for (int i = 0; i < WORDS_COUNT; i++) {
            toRand.add(i);
        }
    }

    private void deleteWord() {
        if (!handleCoins(-getResources().getInteger(R.integer.pay_delete_answer))) {
            return;
        }
        WordButton btn = findNotAnswerButton();
        if (btn != null) {
            setButtonVisiable(btn, View.INVISIBLE);
        }
    }

    private void tipAnswer(){
        if (!handleCoins(-getResources().getInteger(R.integer.pay_tip_answer))) {
            return;
        }
        char[] name = mCurrentMusic.getNameArray();
        int pos = new Random().nextInt(name.length);
        Toast.makeText(MainActivity.this, "答案中的一个字是：" + name[pos], Toast.LENGTH_SHORT).show();
    }

    private List<Integer> toRand = new LinkedList<Integer>();
    private WordButton findNotAnswerButton() {
        Random r = new Random();
        WordButton btn;
        while (true) {
            try {
                int idx = toRand.remove(r.nextInt(toRand.size()));
                btn = mAllWords.get(idx);
                Log.i("duxu", "while loop, rand : " + idx + ", music name:" + mCurrentMusic.getMusicName() + ", btn string:" + btn.mWordString);
                if (btn.mIsVisiable && !mCurrentMusic.getMusicName().contains(btn.mWordString)) {
                    return btn;
                }
            } catch (Exception e) {
                return null;
            }
        }
    }

    private boolean handleCoins(int data) {
        if (mCurrentCoins + data >= 0) {
            mCurrentCoins += data;
            mViewCoins.setText(mCurrentCoins + "");
            return true;
        }
        return false;
    }

    private void initAnims() {
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
    }

    @Override
    public void onWordButtonClick(WordButton btn) {
        setSelectedWord(btn);
        switch (checkAnswer()) {
            case STATUS_ANSWER_RIGHT:
                handlePassEvent();
                break;
            case STATUS_ANSWER_WRONG:
                sparkWords();
                Toast.makeText(this, "答案错误", Toast.LENGTH_SHORT).show();
                break;
            case STATUS_ANSWER_LACK:
                for (int i = 0; i < mSelectedWords.size(); i++)
                    mSelectedWords.get(i).mViewButton.setTextColor(Color.WHITE);
                break;
        }
    }

    private void handlePassEvent() {
        mPassView = (LinearLayout) findViewById(R.id.pass_view);
        mPassView.setVisibility(View.VISIBLE);

        mViewPan.clearAnimation();

        mViewCurrentStage = (TextView) findViewById(R.id.text_current_stage_pass);
        mViewCurrentStage.setText((mCurrentIndex + 1) + "");

        mViewCurrentMusicName = (TextView) findViewById(R.id.text_currnt_song_name);
        mViewCurrentMusicName.setText(mCurrentMusic.getMusicName());
    }

    /**
     * 文字闪烁
     */
    private void sparkWords() {
        TimerTask task = new TimerTask() {
            boolean mChange;
            int mSpardTimes;

            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mSpardTimes++ > SPASH_TIMES)
                            return;
                        for (int i = 0; i < mSelectedWords.size(); i++) {
                            mSelectedWords.get(i).mViewButton.setTextColor(mChange ? Color.RED : Color.WHITE);
                        }
                        mChange = !mChange;
                    }
                });
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 1, 150);
    }

    private int checkAnswer() {
        StringBuilder answer = new StringBuilder();
        for (int i = 0; i < mSelectedWords.size(); i++) {
            WordButton btn = mSelectedWords.get(i);
            if (TextUtils.isEmpty(btn.mWordString))
                return STATUS_ANSWER_LACK;
            answer.append(btn.mWordString);
        }
        return answer.toString().equals(mCurrentMusic.getMusicName())
                ? STATUS_ANSWER_RIGHT : STATUS_ANSWER_WRONG;
    }

    private void clearAnswer(WordButton button) {
        button.mViewButton.setText("");
        button.mWordString = "";
        setButtonVisiable(mAllWords.get(button.mIndex), View.VISIBLE);
        for (int i = 0; i < mSelectedWords.size(); i++)
            mSelectedWords.get(i).mViewButton.setTextColor(Color.WHITE);
    }

    private void setSelectedWord(WordButton button) {

        for (int i = 0; i < mSelectedWords.size(); i++) {
            if (TextUtils.isEmpty(mSelectedWords.get(i).mWordString)) {
                mSelectedWords.get(i).mViewButton.setText(button.mWordString + "");
                mSelectedWords.get(i).mIsVisiable = true;
                mSelectedWords.get(i).mWordString = button.mWordString;
                mSelectedWords.get(i).mIndex = button.mIndex;

                setButtonVisiable(button, View.INVISIBLE);
                break;
            }
        }
    }

    private void setButtonVisiable(WordButton button, int visibility) {
        button.mViewButton.setVisibility(visibility);
        button.mIsVisiable = (visibility == View.VISIBLE);
    }

    //TODO override it, random generate music info
    private Music loadStageMusicInfo(int index) {
        Music music = new Music();
        String[] infos = Const.SONG_INFO[index];
        music.setFilename(infos[0]);
        music.setMode(Integer.parseInt(infos[1]));
        music.setMusicName(infos[2]);
        return music;
    }

    private void initCurrentStageData() {
        mCurrentMusic = loadStageMusicInfo(mCurrentIndex++);
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
     */
    private ArrayList<WordButton> initSelectedWords() {
        ArrayList<WordButton> data = new ArrayList<WordButton>();
        for (int i = 0; i < mCurrentMusic.getNameLength(); i++) {
            View v = ViewUtil.getView(MainActivity.this, R.layout.gridview_item);
            final WordButton holder = new WordButton();
            holder.mViewButton = (android.widget.Button) v.findViewById(R.id.item_btn);
            holder.mViewButton.setTextColor(Color.WHITE);
            holder.mViewButton.setText("");
            holder.mIsVisiable = false;
            holder.mIndex = -1;
            holder.mViewButton.setBackgroundResource(R.drawable.game_wordblank);

            holder.mViewButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clearAnswer(holder);
                }
            });
            data.add(holder);
        }
        return data;
    }

    /**
     * 初始化待选择文字框
     */
    private ArrayList<WordButton> initAllWords() {
        ArrayList<WordButton> data = new ArrayList<WordButton>(WORDS_COUNT);
        char[] words = generateWords();
        for (int i = 0; i < WORDS_COUNT; i++) {
            WordButton button = new WordButton();
            button.mWordString = words[i] + "";
            data.add(button);
        }
        return data;
    }

    private void handlePlay() {
        if (mIsRunning) {
            return;
        }
        mIsRunning = true;
        mViewPanBar.startAnimation(mBarInAnim);
        mBtnPlayStart.setVisibility(View.INVISIBLE);
    }

    private char[] generateWords() {
        char[] words = new char[WORDS_COUNT];
        for (int i = 0; i < mCurrentMusic.getNameLength(); i++) {
            words[i] = mCurrentMusic.getNameArray()[i];
        }
        for (int i = mCurrentMusic.getNameLength(); i < WORDS_COUNT; i++) {
            words[i] = getRandomChar();
        }
        Random r = new Random();
        for (int i = WORDS_COUNT - 1; i >= 0; i--) {
            int index = r.nextInt(i + 1);
            char buf = words[index];
            words[index] = words[i];
            words[i] = buf;
        }
        return words;
    }

    private char getRandomChar() {
        int high, low;
        Random r = new Random();
        high = (176 + Math.abs(r.nextInt(39)));
        low = (161 + Math.abs(r.nextInt(93)));
        byte[] b = new byte[2];
        b[0] = Integer.valueOf(high).byteValue();
        b[1] = Integer.valueOf(low).byteValue();
        try {
            return new String(b, "GBK").charAt(0);
        } catch (Exception e) {
        }
        return ' ';
    }

    @Override
    protected void onPause() {
        super.onPause();
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
