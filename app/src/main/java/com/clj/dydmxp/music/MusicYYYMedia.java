package com.clj.dydmxp.music;

import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.widget.SeekBar;

import java.io.IOException;

public class MusicYYYMedia {
    public MediaPlayer mediaPlayer;
    public Handler handler;
    public SeekBar musicSeekBar;

    public MusicYYYMedia(Handler handler, SeekBar musicSeekBar) {
        this.handler = handler;
        this.musicSeekBar = musicSeekBar;
    }

    public void initMedia() {
        mediaPlayer = new MediaPlayer();
        musicSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void setMediaPlayer(String mp3url) {
        try {
            mediaPlayer.setDataSource(mp3url);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer.start();
                    mediaPlayer.getDuration();
                    Message message = handler.obtainMessage();
                    message.what = 1;
                    message.arg1 = mediaPlayer.getDuration();
                    handler.sendMessage(message);
                }
            });
            //缓存进度
            mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                @Override
                public void onBufferingUpdate(MediaPlayer mp, int percent) {

                }
            });
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    while (true) {
                        Message message = handler.obtainMessage();
                        message.what = 2;
                        message.obj = mediaPlayer;
                        handler.sendMessage(message);
                        try {
                            sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startPlay() {
        mediaPlayer.start();
    }

    public int getDuration() {
        return mediaPlayer.getDuration();
    }

    public int getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    public void seekTo(int msec) {
        mediaPlayer.seekTo(msec);
    }

    public void pausePlay() {
        mediaPlayer.pause();
    }

    public void stopPlay() {
        mediaPlayer.stop();
    }
}
