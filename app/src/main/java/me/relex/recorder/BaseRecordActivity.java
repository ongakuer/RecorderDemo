package me.relex.recorder;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import java.io.File;
import me.relex.recorder.tools.CommonUtil;
import me.relex.recorder.tools.FileUtil;

public class BaseRecordActivity extends ActionBarActivity {

    protected Button mStartButton;
    protected Button mStopButton;
    protected Button mPlayButton;

    protected Handler mHandler;
    protected long mStartTime;
    protected File mTempRecordFile;

    protected Runnable mRecordAutoCountRunnable = new Runnable() {
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

    protected Runnable mPlayAutoCountRunnable = new Runnable() {
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

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mHandler = new Handler();
    }

    protected void configToolbar() {
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
                FileUtil.deleteDir(FileUtil.getAudioCacheDirectory(BaseRecordActivity.this));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    protected void switchButton(boolean canPlay, boolean canRecord) {
        mStartButton.setEnabled(canRecord);
        mStopButton.setEnabled(!canRecord);

        if (canRecord) {
            mStartButton.setText(R.string.start_record);
        }

        mPlayButton.setEnabled(canPlay);
        mPlayButton.setText(R.string.play_record);
    }
}

