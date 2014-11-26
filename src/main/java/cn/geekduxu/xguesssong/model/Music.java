package cn.geekduxu.xguesssong.model;

public class Music {

    private String mMusicName;
    private String mFilename;
    private int mNameLength;
    private int mMode;

    public char[] getNameArray() {
        return mMusicName.toCharArray();
    }

    public String getFilename() {
        return mFilename;
    }

    public void setFilename(String filename) {
        this.mFilename = filename;
    }

    public int getNameLength() {
        return mNameLength;
    }

    public String getMusicName() {
        return mMusicName;
    }

    public void setMusicName(String musicName) {
        this.mMusicName = musicName;
        this.mNameLength = musicName.length();
    }

    public int getMode() {
        return mMode;
    }

    public void setMode(int mode) {
        this.mMode = mode;
    }

    public static String getModeString(int mode) {
        switch (mode) {
            case 1:
                return "猜歌名";
            case 2:
                return "猜歌手";
            case 3:
                return "猜下一句";
        }
        return "";
    }
}
