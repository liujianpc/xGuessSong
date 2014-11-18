package cn.geekduxu.xguesssong.model;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;

import java.util.ArrayList;

import cn.geekduxu.xguesssong.R;
import cn.geekduxu.xguesssong.util.ViewUtil;

public class WordGridView extends GridView {

    private ArrayList<WordButton> mArrayList = new ArrayList<WordButton>();
    private Context mContext;
    private MyAdapter mMyAdapter;
    private Animation mScaleAnimation;

    private WordButtonClickListener mListener;

    public WordGridView(Context context) {
        this(context, null);
    }

    public WordGridView(Context context, AttributeSet attrs, int defStyle) {
        this(context, attrs);
    }

    public WordGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mMyAdapter = new MyAdapter();
        mContext = context;
        this.setAdapter(mMyAdapter);
    }

    public void updateData(ArrayList<WordButton> list) {
        mArrayList = list;
        setAdapter(mMyAdapter);
        mMyAdapter.notifyDataSetChanged();
    }

    class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mArrayList.size();
        }

        @Override
        public Object getItem(int i) {
            return mArrayList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View v, ViewGroup viewGroup) {
            final WordButton holder;
            View view = ViewUtil.getView(mContext, R.layout.gridview_item);
            holder = mArrayList.get(i);
            holder.mIndex = i;
            holder.mViewButton = (Button) view.findViewById(R.id.item_btn);
            view.setTag(holder);
            mScaleAnimation = AnimationUtils.loadAnimation(mContext, R.anim.scale);
            mScaleAnimation.setStartOffset((int) (i * mScaleAnimation.getDuration() * 0.5));
            holder.mViewButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View vw) {
                    if (mListener != null)
                        mListener.onWordButtonClick(holder);
                }
            });

            holder.mViewButton.setText(holder.mWordString);
            view.startAnimation(mScaleAnimation);
            return view;
        }
    }

    public void setOnWordButtonClickedListener(WordButtonClickListener listener) {
        mListener = listener;
    }
}
