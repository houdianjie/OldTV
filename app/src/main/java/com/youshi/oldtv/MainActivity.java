package com.youshi.oldtv;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

/**
 * Created by 典杰 on 2017/7/31.
 */

public class MainActivity extends Activity {
    private VideoView m_video_paly;
private MediaController m_controller;
    private String[] original_paths;
    private List<HashMap<String,String>> path_list;
    private  int  currentPlayNum;
    private TextView tvNumber;
    private TextView tvTitle;
    private Button playPreBtn;
    private Button playNextBtn;
    private long startKeyTime;
    private String chooseShow;
    private TextView chooseTvNumbertv;
    private MyHandler myHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        Intent intent = getIntent();
        boolean isLoadFromNet = intent.getBooleanExtra("isLoadFromNet",false);
        if(isLoadFromNet){
            ListHelper helper= (ListHelper) intent.getBundleExtra("tvitems").getSerializable("tvitems");
            path_list = helper.getList();
            currentPlayNum = 1;
        }
        else {
            original_paths = getResources().getStringArray(R.array.tv_paths);
            path_list = new ArrayList<HashMap<String, String>>();
            currentPlayNum=56;
            for(String str:original_paths) {
                String[] temp = str.split("#");
                HashMap<String, String> map = new HashMap<String,String>();
                map.put("number",temp[0]);
                map.put("title",temp[1]);
                map.put("url",temp[2]);
                path_list.add(map);
            }
        }
        init();
    }

    //程序初始化
    public void init(){
        startKeyTime = 0;
        chooseShow = "";
        myHandler = new MyHandler();
        chooseTvNumbertv = (TextView) findViewById(R.id.chooseTvNumber);
        tvNumber = (TextView) findViewById(R.id.tv_number);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        playPreBtn = (Button) findViewById(R.id.playPreVideoBtn);
        playNextBtn = (Button) findViewById(R.id.playNextVideoBtn);
        playVideo();
        onKeyListener();
    }

    //提高系统音量
    private void setSystemVolumeRaise(){
        AudioManager mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,AudioManager.ADJUST_RAISE,AudioManager.FLAG_SHOW_UI);

    }

    //降低系统音量
    private void setSystemVolumeLower(){
        AudioManager mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,AudioManager.ADJUST_LOWER,AudioManager.FLAG_SHOW_UI);

    }

    //播放按钮
    public void playVideoBtn(View view){
       // playVideo();
    }

    //播放下一个按钮
    public void playNextVideoBtn(View view){
        playNextShow();
    }

    //播放上一个按钮
    public void playPreVideoBtn(View view){
       playPreShow();
    }

    //播放上一个节目
    private void playPreShow(){
        if(m_video_paly.isPlaying()){
            m_video_paly.stopPlayback();

        }
        tvNumber.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.VISIBLE);
        String path="";
        if(currentPlayNum==0){
            currentPlayNum = path_list.size()-1;
            path = path_list.get(currentPlayNum).get("url");
        }else {
            path = path_list.get(--currentPlayNum).get("url");
        }
        //Log.e("上一个当前num",currentPlayNum+"");
        tvNumber.setText(path_list.get(currentPlayNum).get("number"));
        tvTitle.setText(path_list.get(currentPlayNum).get("title"));
        m_video_paly.setVideoPath(path);
        m_video_paly.requestFocus();//取得焦点
        m_video_paly.start();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                myHandler.removeMessages(0x101);
                myHandler.sendEmptyMessage(0x101);
            }
        },2000);
    }

    //播放下一个节目
    private void playNextShow(){
        if(m_video_paly.isPlaying()) {
            m_video_paly.stopPlayback();
        }
        tvNumber.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.VISIBLE);
        String path="";
        if(currentPlayNum==path_list.size()-1){
            currentPlayNum = 0;
            path = path_list.get(currentPlayNum).get("url");
        }else {
            path = path_list.get(++currentPlayNum).get("url");
        }
        //Log.e("下一个当前num",currentPlayNum+"");
        tvNumber.setText(path_list.get(currentPlayNum).get("number"));
        tvTitle.setText(path_list.get(currentPlayNum).get("title"));
        m_video_paly.setVideoPath(path);
        m_video_paly.requestFocus();//取得焦点
        m_video_paly.start();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                myHandler.removeMessages(0x101);
                myHandler.sendEmptyMessage(0x101);
            }
        },2000);
    }

    //播放卫视直播
    private void playVideo(){
        //Log.e("播放","进行中");
        tvNumber.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.VISIBLE);
        String path = path_list.get(currentPlayNum).get("url");
        tvNumber.setText(path_list.get(currentPlayNum).get("number"));
        tvTitle.setText(path_list.get(currentPlayNum).get("title"));
        Vitamio.isInitialized(this);
        initVideoView(path);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                myHandler.removeMessages(0x101);
                myHandler.sendEmptyMessage(0x101);
            }
        },2000);
    }

    private void initVideoView(String path){
        m_video_paly = (VideoView) findViewById(R.id.m_video_paly);
        m_video_paly.setVideoPath(path);//设置播放地址
        m_controller = new MediaController(this);//实例化控制器
        m_controller.show(5000);//控制器显示5s后自动隐藏
        m_video_paly.setVideoLayout(VideoView.VIDEO_LAYOUT_STRETCH,0);
        m_video_paly.setMediaController(m_controller);//绑定控制器
        m_video_paly.setVideoQuality(MediaPlayer.VIDEOQUALITY_HIGH);//设置播放画质 高画质
        m_video_paly.setBufferSize(10240); //设置视频缓冲大小。默认1024KB，单位byte
        m_video_paly.requestFocus();//取得焦点
        m_video_paly.start();
    }

    //跳转节目
    private void chooseShowFunc(int tv_num){
        tvNumber.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.VISIBLE);

        if(tv_num > path_list.size() || tv_num < 1){
            Toast.makeText(MainActivity.this,"节目号无效!",Toast.LENGTH_SHORT).show();
            return;
        }
        if(m_video_paly.isPlaying()){
            m_video_paly.stopPlayback();
        }
        currentPlayNum = tv_num-1;
       String path = path_list.get(currentPlayNum).get("url");
        tvNumber.setText(path_list.get(currentPlayNum).get("number"));
        tvTitle.setText(path_list.get(currentPlayNum).get("title"));
        m_video_paly.setVideoPath(path);
        m_video_paly.requestFocus();//取得焦点
        m_video_paly.start();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                myHandler.removeMessages(0x101);
                myHandler.sendEmptyMessage(0x101);
            }
        },2000);

    }
    //播放器按键监听
    private void onKeyListener() {
        m_video_paly.setOnKeyListener(new MyKeyListener());
    }

    class MyKeyListener implements View.OnKeyListener{

        @Override
        public boolean onKey(View view, int i, KeyEvent keyEvent) {
            if(i==KeyEvent.KEYCODE_DPAD_UP && keyEvent.getAction() == KeyEvent.ACTION_DOWN ){
                //Log.e("上一个按键码",i+","+currentPlayNum);
                playPreShow();
                return true;
            }
            else if(i==KeyEvent.KEYCODE_DPAD_DOWN && keyEvent.getAction() == KeyEvent.ACTION_DOWN ){
                //Log.e("下一个按键码",i+","+currentPlayNum);
                playNextShow();
                return true;
            }else if(i==KeyEvent.KEYCODE_DPAD_LEFT && keyEvent.getAction() == KeyEvent.ACTION_DOWN ){

                setSystemVolumeLower();
                return true;
            }else if(i==KeyEvent.KEYCODE_DPAD_RIGHT && keyEvent.getAction() == KeyEvent.ACTION_DOWN ){
                //Log.e("按键码",i+"");
                setSystemVolumeRaise();
                return true;
            }else if((i>6 && i<17) && keyEvent.getAction() == KeyEvent.ACTION_DOWN ){
                if(startKeyTime==0){
                    startKeyTime = System.currentTimeMillis();
                    chooseTvNumbertv.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            int tv_num=0;
                            try {
                                tv_num = Integer.parseInt(chooseShow);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            Message msg = new Message();
                            msg.what = 0x100;
                            myHandler.sendMessage(msg);
                            chooseShowFunc(tv_num);
                            startKeyTime=0;
                            chooseShow="";
                        }
                    },2000);
                }
                chooseShow += i-7;
                chooseTvNumbertv.setText(chooseShow);
                //Log.e("节目字符",chooseShow);
            }
            return false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            playNextBtn.setVisibility(View.VISIBLE);
            playPreBtn.setVisibility(View.VISIBLE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    myHandler.removeMessages(0x102);
                    myHandler.sendEmptyMessage(0x102);
                }
            },2000);
            return true;
        }
        return super.onTouchEvent(event);
    }

    class MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==0x100){
                chooseTvNumbertv.setVisibility(View.GONE);
            }
            if(msg.what==0x101){
                tvTitle.setVisibility(View.INVISIBLE);
                tvNumber.setVisibility(View.INVISIBLE);
            }
            if(msg.what==0x102){
                playNextBtn.setVisibility(View.INVISIBLE);
                playPreBtn.setVisibility(View.INVISIBLE);
            }
        }
    }

}
