package cn.geekduxu.xguesssong.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import cn.geekduxu.xguesssong.R;
import cn.geekduxu.xguesssong.data.Const;
import cn.geekduxu.xguesssong.model.DialogButtonClickListener;
import cn.geekduxu.xguesssong.util.ViewUtil;

public class GuideActivity extends Activity {

    private TextView tvPercent;
    private TextView tvFooter;

    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        init();
    }

    private void init() {
        sp = getSharedPreferences("config", MODE_PRIVATE);
        tvPercent = (TextView) findViewById(R.id.tv_percent);
        tvFooter = (TextView) findViewById(R.id.tv_footer);

        String text = sp.getString("done", "0") + "/" + Const.SONG_INFO.length;
        tvPercent.setText(text);
        tvFooter.setText(text);
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    public void startGame(View view){
        startActivity(new Intent(GuideActivity.this, MainActivity.class));
    }

    public void about(View view){
        //TODO 关于作者
    }

    public void clearProgress(View view) {
        ViewUtil.showDialog(GuideActivity.this, "确认清除游戏进度吗？", new DialogButtonClickListener() {
            @Override
            public void onClick() {
                SharedPreferences.Editor et = sp.edit();
                et.putString("done", "0");
                et.commit();
                init();
            }
        });
    }

    @Override
    public void onBackPressed() {
        ViewUtil.showDialog(GuideActivity.this, "确认退出游戏吗？", new DialogButtonClickListener() {
            @Override
            public void onClick() {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_guide, menu);
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
