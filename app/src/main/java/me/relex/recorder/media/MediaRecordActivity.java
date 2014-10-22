package me.relex.recorder.media;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import java.io.File;
import java.io.IOException;
import me.relex.recorder.R;
import me.relex.recorder.tools.CommonUtil;
import me.relex.recorder.tools.FileUtil;

public class MediaRecordActivity extends ActionBarActivity
        implements MediaRecordListener, MediaPlayListener {

    private Button mStartButton;
    private Button mStopButton;
    private Button mPlayButton;

    private MediaRecordHelper mMediaRecordHelper;
    private MediaPlayHelper mMediaPlayHelper;

    private Handler mHandler;
    private long mStartTime;
    private File mTempRecordFile;

    private Runnable mRecordAutoCountRunnable = new Runnable() {
        @Override public void run() {
            long current = System.currentTimeMillis();
            if (mStartTime == 0) {
                mStartTime = current;
            }
            long time = current - mStartTime;
            mStartButton.setText(CommonUtil.convertMillis(time));
            mHandler.postDelayed(this, 1000L);
        }
    };

    private Runnable mPlayAutoCountRunnable = new Runnable() {
        @Override public void run() {
            long current = System.currentTimeMillis();
            if (mStartTime == 0) {
                mStartTime = current;
            }
            long time = current - mStartTime;
            mPlayButton.setText(CommonUtil.convertMillis(time));
            mHandler.postDelayed(this, 1000L);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_record);
        mHandler = new Handler();
        mMediaRecordHelper = new MediaRecordHelper();
        mMediaRecordHelper.setMediaRecordListener(this);

        mMediaPlayHelper = new MediaPlayHelper();
        mMediaPlayHelper.setMediaPlayListener(this);

        configToolbar();

        mStartButton = (Button) findViewById(R.id.start_record);
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {

                File tempFile = createTempAudioFile();
                if (tempFile == null) {
                    return;
                }

                mTempRecordFile = tempFile;
                mMediaRecordHelper.startRecord(tempFile);
            }
        });

        mStopButton = (Button) findViewById(R.id.stop_record);
        mStopButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                mMediaRecordHelper.stopRecord();
            }
        });

        mPlayButton = (Button) findViewById(R.id.play_record);
        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                if (mTempRecordFile == null || !mTempRecordFile.exists()) {
                    return;
                }
                mMediaPlayHelper.play(mTempRecordFile);
            }
        });

        switchButton(false, true);
    }

    private void configToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                finish();
            }
        });
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.clean_cache, menu);
        return true;
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.action_clean_cache) {
            try {
                FileUtil.deleteDir(FileUtil.getAudioCacheDirectory(MediaRecordActivity.this));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return true;
    }

    // Record
    @Override public void onRecordStart() {
        switchButton(false, false);
        mStartTime = 0L;
        mHandler.post(mRecordAutoCountRunnable);
    }

    @Override public void onRecordStop() {
        mHandler.removeCallbacks(mRecordAutoCountRunnable);
        switchButton(true, true);
    }

    // Play
    @Override public void onPlayPrepare() {
        switchButton(false, false);
        mStopButton.setEnabled(false);
        mPlayButton.setText(R.string.play_preparing);
    }

    @Override public void onPlayStart() {
        mStartTime = 0L;
        mHandler.post(mPlayAutoCountRunnable);
    }

    @Override public void onPlayStop() {
        mHandler.removeCallbacks(mPlayAutoCountRunnable);
        switchButton(false, true);
    }

    private File createTempAudioFile() {
        File tempFile = null;
        try {
            File fileDir = FileUtil.getAudioCacheDirectory(MediaRecordActivity.this);
            tempFile = new File(fileDir, System.currentTimeMillis() + ".amr");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tempFile;
    }

    private void switchButton(boolean canPlay, boolean canRecord) {
        mStartButton.setEnabled(canRecord);
        mStopButton.setEnabled(!canRecord);

        if (canRecord) {
            mStartButton.setText(R.string.start_record);
        }

        mPlayButton.setEnabled(canPlay);
        mPlayButton.setText(R.string.play_record);
    }

    @Override protected void onDestroy() {
        mHandler.removeCallbacks(mRecordAutoCountRunnable);
        mHandler.removeCallbacks(mPlayAutoCountRunnable);

        mMediaRecordHelper.stopRecord();
        mMediaPlayHelper.release();
        super.onDestroy();
    }
}
