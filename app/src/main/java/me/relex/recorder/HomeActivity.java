package me.relex.recorder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import me.relex.recorder.media.MediaRecordActivity;

public class HomeActivity extends ActionBarActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        findViewById(R.id.media_record).setOnClickListener(this);
        findViewById(R.id.audio_record).setOnClickListener(this);
    }

    @Override public void onClick(View view) {
        switch (view.getId()) {
            case R.id.media_record:
                startActivity(new Intent(HomeActivity.this, MediaRecordActivity.class));
                break;
            case R.id.audio_record:
                break;
            default:
                break;
        }
    }
}
