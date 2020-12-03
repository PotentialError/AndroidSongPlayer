package michaelrodyushkin.honorsmobileapps.songplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.content.Context;
import android.media.AudioManager;

import java.util.ArrayList;

import static android.media.MediaPlayer.create;

public class MainActivity extends AppCompatActivity {
    ArrayList<Song> songSelection;
    int index;
    MediaPlayer mPlayer;
    TextView songTitle;
    ImageView image;
    ImageView playPause;
    SeekBar seekBar;
    Handler mHandler;
    //SeekBar volumeBar;
    //AudioManager audioManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        index = 0;
        songSelection = new ArrayList<>();
        songSelection.add(new Song("Summer Vibes", R.drawable.merkel, R.raw.summer));
        songSelection.add(new Song("Creative Vibes", R.drawable.donald, R.raw.creative));
        songSelection.add(new Song("Smooth Vibes", R.drawable.macron, R.raw.smooth_one));
        songTitle = findViewById(R.id.songTitle);
        image = findViewById(R.id.image);
        playPause = findViewById(R.id.playPause);
        seekBar = findViewById(R.id.seek);
        seekBar.setEnabled(false);
        mHandler = new Handler();
        //volumeBar = findViewById(R.id.volume);
        //volumeBar.setProgress(5);
        //audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mPlayer != null){
                    int mCurrentPosition = mPlayer.getCurrentPosition() / 1000;
                    seekBar.setProgress(mCurrentPosition);
                }
                mHandler.postDelayed(this, 50);
            }
        });
        SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
            int progressValue;
            @Override
            public void onProgressChanged(SeekBar seekBar, int newProgressValue, boolean fromUser) {
                progressValue = newProgressValue;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                    mPlayer.seekTo(progressValue*1000);
            }
        };
        seekBar.setOnSeekBarChangeListener(seekBarChangeListener);
    }
    public void setUpNewSong() {
        seekBar.setEnabled(true);
        mPlayer = create(this, songSelection.get(index).getSong());
        songTitle.setText(songSelection.get(index).getTitle());
        image.setImageResource(songSelection.get(index).getArt());
        seekBar.setMax(mPlayer.getDuration()/1000);
        startSong();
    }
    public void startSong() {
            mPlayer.start();
            playPause.setImageResource(R.drawable.pause);
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer player) {
                releaseMediaPlayer();
                setUpNewSong();
                pauseSong(null);
            }
        });
    }
    public void pauseSong(View v) {
        mPlayer.pause();
        playPause.setImageResource(R.drawable.play_arrow);
    }
    public void fastForward(View v) {
        if(mPlayer.getCurrentPosition() != 0 && mPlayer != null) {
            mPlayer.seekTo(mPlayer.getCurrentPosition() + 10000);
        }
    }
    public void rewind(View v) {
        if(mPlayer != null)
            mPlayer.seekTo(mPlayer.getCurrentPosition() - 10000);
    }
    public void skipForward(View v) {
        if(mPlayer != null) {
            releaseMediaPlayer();
            index++;
            if(index > songSelection.size() - 1) {
                index = 0;
            }
            setUpNewSong();
        }
    }
    public void onPausePlay(View v) {
        if(mPlayer != null) {
            if(mPlayer.isPlaying()) {
                pauseSong(null);
            }
            else if(mPlayer.getCurrentPosition() != mPlayer.getDuration()) {
                startSong();
            }
            else {
                mPlayer.seekTo(0);
                startSong();
            }
        }
        else {
            setUpNewSong();
        }
    }
    public void skipPrevious(View v) {
        if(mPlayer != null) {
            releaseMediaPlayer();
            index--;
            if (index < 0) {
                index = songSelection.size() - 1;
            }
            setUpNewSong();
        }
    }
    public void releaseMediaPlayer() {
        if(mPlayer != null) {
            mPlayer.release();
        }
        mPlayer = null;
    }
    @Override
    public void onPause() {
        super.onPause();
        releaseMediaPlayer();
    }
}
