package me.relex.recorder.media;

import android.media.MediaRecorder;
import java.io.File;
import me.relex.recorder.tools.RecordListener;

public class MediaRecordHelper implements MediaRecorder.OnErrorListener {

    private MediaRecorder mRecorder;

    private RecordListener mListener;

    public void setMediaRecordListener(RecordListener mediaRecordListener) {
        this.mListener = mediaRecordListener;
    }

    public void startRecord(File outputFile) {
        stopRecord();
        try {
            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setAudioChannels(2); // stereo
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_WB);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_WB);
            mRecorder.setOutputFile(outputFile.getPath());
            mRecorder.prepare();
            mRecorder.start();
            if (mListener != null) {
                mListener.onRecordStart();
            }
        } catch (Exception e) {
            stopRecord();
        }
    }

    public void stopRecord() {
        if (mRecorder != null) {
            try {
                mRecorder.stop();
                mRecorder.release();
                mRecorder = null;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                mRecorder = null;
                if (mListener != null) {
                    mListener.onRecordStop();
                }
            }
        }
    }

    @Override public void onError(MediaRecorder mediaRecorder, int i, int i2) {
        stopRecord();
    }
}

