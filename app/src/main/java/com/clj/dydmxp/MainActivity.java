package com.clj.dydmxp;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;
import com.clj.dydmxp.httpsocket.WebSocketMessage;
import com.clj.dydmxp.message.MessageContentAdapter;
import com.clj.dydmxp.message.MessageSuccessUserContent;
import com.clj.dydmxp.music.MusicYYY;
import com.clj.dydmxp.musiclist.MusicAdapter;
import com.clj.dydmxp.successuser.SuccessUserAdapter;

import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat ft = new SimpleDateFormat("mm:ss");
    private ListView MessageContentList;
    private Button BunttonClick;
    private SeekBar MusicSeekBar;
    private TextView MusicSeekBarText;
    boolean isFrist = true;
    public MessageContentAdapter messageContentListAdapter;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                MusicSeekBar.setMax(msg.arg1);
            } else if (msg.what == 2) {
                MediaPlayer mediaPlayer = (MediaPlayer) msg.obj;
                MusicSeekBar.setProgress(mediaPlayer.getCurrentPosition());
                String getCurrentPosition = ft.format(new Date(mediaPlayer.getCurrentPosition()));
                String getDuration = ft.format(new Date(mediaPlayer.getDuration()));
                MusicSeekBarText.setText(getCurrentPosition + "/" + getDuration);
            } else {
                try {
                    new JSONObject(String.valueOf(msg.obj));
                    MessageSuccessUserContent messageSuccessUserContent = JSON.parseObject(String.valueOf(msg.obj), MessageSuccessUserContent.class);
                    messageContentListAdapter.add(messageSuccessUserContent);
                    successUserAdapter.changedCountText(messageSuccessUserContent);
                    if (messageSuccessUserContent.getContent().contains("点歌")) {
                        musicAdapter.add(messageSuccessUserContent);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };
    private ListView UserNameList;
    private ListView MusicList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        new WebSocketMessage(handler).start();
        initView();
        setViewData();
    }

    public SuccessUserAdapter successUserAdapter;
    public MusicAdapter musicAdapter;

    private void setViewData() {
        musicAdapter = new MusicAdapter(this);
        MusicList.setAdapter(musicAdapter);
        successUserAdapter = new SuccessUserAdapter(this);
        UserNameList.setAdapter(successUserAdapter);

        messageContentListAdapter = new MessageContentAdapter(this);
        MessageContentList.setAdapter(messageContentListAdapter);
        BunttonClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void run() {
                        long gequid = MusicYYY.geMusicIdArray("真命天子 罗志祥");
                        if (gequid != 0) {
                            try {
                                MusicYYY.getCloundMusicMp3(gequid, handler, MusicSeekBar);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();
            }
        });
    }


    private void initView() {
        MessageContentList = findViewById(R.id.MessageContentList);
        BunttonClick = findViewById(R.id.BunttonClick);
        MusicSeekBar = findViewById(R.id.MusicSeekBar);
        MusicSeekBarText = findViewById(R.id.MusicSeekBarText);
        UserNameList = findViewById(R.id.UserNameList);
        MusicList = findViewById(R.id.MusicList);
    }

}