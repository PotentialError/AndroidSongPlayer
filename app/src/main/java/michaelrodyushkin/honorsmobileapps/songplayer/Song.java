package michaelrodyushkin.honorsmobileapps.songplayer;

public class Song {
    private String mTitle;
    private int mArt;
    private int mSong;
    public Song(String title, int art, int song) {
        mTitle = title;
        mArt = art;
        mSong = song;
    }
    public String getTitle() {
        return mTitle;
    }
    public int getArt() {
        return mArt;
    }
    public int getSong() {
        return mSong;
    }
}
