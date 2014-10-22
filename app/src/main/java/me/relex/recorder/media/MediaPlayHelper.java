package me.relex.recorder.media;

import android.media.AudioManager;
import android.media.MediaPlayer;
import java.io.File;

public class MediaPlayHelper implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener {

    private MediaPlayer mMediaPlayer;

    private MediaPlayListener mListener;

    public void setMediaPlayListener(MediaPlayListener listener) {
        this.mListener = listener;
    }

    public void play(File file) {
        release();
        mMediaPlayer = new MediaPlayer();
        try {
            mMediaPlayer.setDataSource(file.getPath());
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setOnErrorListener(this);
            mMediaPlayer.setOnCompletionListener(this);
            mMediaPlayer.setOnPreparedListener(this);
            if (mListener != null) {
                mListener.onPlayPrepare();
            }
            mMediaPlayer.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
            release();
        }
    }

    public void release() {
        release(mMediaPlayer);
    }

    public void release(MediaPlayer mediaPlayer) {
        if (mediaPlayer != null) {
            try {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
                mediaPlayer.release();
                mediaPlayer = null;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                mediaPlayer = null;
                mMediaPlayer = null;
                if (mListener != null) {
                    mListener.onPlayStop();
                }
            }
        }
    }

    @Override public void onPrepared(MediaPlayer mediaPlayer) {
        try {
            mediaPlayer.start();
            if (mListener != null) {
                mListener.onPlayStart();
            }
        } catch (Exception e) {
            e.printStackTrace();
            release(mediaPlayer);
        }
    }

    @Override public void onCompletion(MediaPlayer mediaPlayer) {
        release(mediaPlayer);
    }

    @Override public boolean onError(MediaPlayer mediaPlayer, int what, int extra) {

        release(mediaPlayer);
        return false;
    }
}

