package cn.geekduxu.xguesssong.util;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.util.Log;

import java.io.IOException;

public class SoundPlayUtil {

    private static MediaPlayer mMediaPlayer;

    public static void playMusic(Context context, String fileName) {
        if (mMediaPlayer == null) {
            // new instance
            mMediaPlayer = new MediaPlayer();
        }
        try {
            mMediaPlayer.reset();
            AssetManager assetManager = context.getAssets();
            AssetFileDescriptor descriptor = assetManager.openFd(fileName);
            mMediaPlayer.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
            mMediaPlayer.prepare();
            mMediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void pause(Context context) {
        if (mMediaPlayer != null) {
            mMediaPlayer.pause();
        }
    }

    private static final String[] TONE_NAMES = {"enter.mp3", "cancel.mp3", "coin.mp3"};
    public static final int INDEX_STONE_ENTER = 0;
    public static final int INDEX_STONE_CANCLE = 1;
    public static final int INDEX_STONE_COIN = 2;
    private static MediaPlayer[] toneMediaPlayers = new MediaPlayer[TONE_NAMES.length];

    public static void playTone(Context context, int index) {
        if (index < INDEX_STONE_ENTER || index > INDEX_STONE_COIN) {
            return;
        }
        try {
            AssetManager manager = context.getAssets();
            if (toneMediaPlayers[index] == null) {
                toneMediaPlayers[index] = new MediaPlayer();
                AssetFileDescriptor descriptor = manager.openFd(TONE_NAMES[index]);
                toneMediaPlayers[index].setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
                toneMediaPlayers[index].prepare();
            }
//            toneMediaPlayers[index].reset();

            toneMediaPlayers[index].start();
        } catch (Exception e) {
            Log.i("duxu", "excep ");
        }
    }
}
