package cn.geekduxu.xguesssong.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

public class ViewUtil {

    public static View getView(Context context, int layoutId){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return inflater.inflate(layoutId, null);
    }

}
