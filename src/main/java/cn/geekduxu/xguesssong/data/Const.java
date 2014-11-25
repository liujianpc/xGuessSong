package cn.geekduxu.xguesssong.data;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import cn.geekduxu.xguesssong.model.Music;

public class Const {

    public static final int TOTAL_COINS = 1000;

    public static final String SONG_INFO[][] = {
            {"__00000.m4a", "1", "征服"},
            {"__00001.m4a", "1", "童话"},
            {"__00002.m4a", "1", "同桌的你"},
            {"__00003.m4a", "1", "七里香"},
            {"__00004.m4a", "1", "传奇"},
            {"__00005.m4a", "1", "大海"},
            {"__00006.m4a", "1", "后来"},
            {"__00007.m4a", "1", "你的背包"},
            {"__00008.m4a", "1", "再见"},
            {"__00009.m4a", "1", "老男孩"},
            {"__00010.m4a", "1", "龙的传人"},
    };

    private static List<Integer> MUSICS_LIST;
    static {
        MUSICS_LIST = new LinkedList<Integer>();
        for(int i = 0; i < SONG_INFO.length; i++){
            MUSICS_LIST.add(i);
        }
    }

    public static Music loadNextMusic(){
        Music music = new Music();
        int idx = new Random().nextInt(MUSICS_LIST.size());
        int pos = MUSICS_LIST.remove(idx);
        music.setFilename(SONG_INFO[pos][0]);
        music.setMode(Integer.parseInt(SONG_INFO[pos][1]));
        music.setMusicName(SONG_INFO[pos][2]);
        return music;
    }

    public static boolean hasMoreMusic(){
        return  MUSICS_LIST.size() > 0;
    }

}
