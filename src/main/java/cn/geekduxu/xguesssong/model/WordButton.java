package cn.geekduxu.xguesssong.model;

import android.content.Context;
import android.widget.Button;

/**
 * 文字按钮
 * @author 杜旭
 */
public class WordButton {

    public int mIndex;
    public boolean mIsVisiable;
    public String mWordString;

    public Button mViewButton;

    public WordButton() {
        mIsVisiable = true;
        mWordString = "";
    }
}
