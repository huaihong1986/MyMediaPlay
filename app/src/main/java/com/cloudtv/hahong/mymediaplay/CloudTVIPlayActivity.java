package com.cloudtv.hahong.mymediaplay;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.VideoView;

import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

/**
 * <p>
 * The PlayActivity which extends Activity display the view that the user can
 * see and interact with,and play the live Video from Server.
 * </p>
 *
 * @since jdk1.6
 * @version 1.0 2014.11.19
 * @author ygh
 */
public class CloudTVIPlayActivity extends Activity {
    private final String TAG = "CloudTVIPlayActivity";

    private boolean isLoadedSeries;
    public String playName;
    private LinearLayout backFromPlay;
    private boolean stopFlag ;
    private int seekToCurrentMin;
    private Timer timer =null;
    public String playUrl;
    public String startNPT;
    private VideoView videoView;
    private ProgressDialog progressDialog;

    private AudioManager audioManager ;
    private Boolean volumeFlag=false;
    public sendResultHandler handler;

    private String playActivityStr = ".CloudTVIPlayActivity.extras";
    private int touchPostDelayedTime = 3000;
    private int preparedPostDelayedTime = 2000;
    private int intentPostDelayedTime = 6000;
    private MediaControllerLandscape mMediaController;
    private Runnable progressBarRunnable = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub

            if(progressDialog.isShowing())
                progressDialog.dismiss();

        }
    };
    private Runnable volumeShow=new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            volumeFlag=false;



            backFromPlay.setVisibility(View.GONE);

        }
    };
    private Context mContext;

    @SuppressLint("WrongViewCast")
    public void onCreate(Bundle savedInstanceState) {

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_view_play_vod);
        mContext = this;
<<<<<<< HEAD
        progressDialog = new ProgressDialog(this,R.style.dialog);
=======
        progressDialog = new ProgressDialog(this);
>>>>>>> origin/master
        showWaitDialog();
        handler = new sendResultHandler();
        Log.i(TAG, "onCreate");

        videoView = (VideoView) this.findViewById(R.id.VideoView);
        mMediaController = new MediaControllerLandscape(mContext);

        mMediaController.setonFullScreenListener(new MediaControllerLandscape.onFullScreenListener() {

            @Override
            public void fullScreen() {
                // TODO Auto-generated method stub
                Log.i(TAG, "选集");;
            }
        });

        videoView.setMediaController(mMediaController);


        //

        Intent intent = getIntent();

        String[] argArray = intent.getStringArrayExtra(getPackageName()+ playActivityStr);
        playUrl = null;
        if (argArray != null) {
            initLayout();
            Log.i(TAG, "getIntent::");
            playUrl = argArray[0];
            playVideo();
            startNPT = argArray[1];
            int startTime = 0;
            try {
                startTime = Integer.decode(startNPT);
            } catch (NumberFormatException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            videoView.seekTo(startTime);


        }


        audioManager= (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        videoView.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub




                if(event.getAction()== MotionEvent.ACTION_DOWN){


                    if(!backFromPlay.isShown())
                    {

                        audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_SAME,
                                AudioManager.FX_FOCUS_NAVIGATION_UP);
                        volumeFlag=true;
                        handler.removeCallbacks(volumeShow);
                        handler.postDelayed(volumeShow, touchPostDelayedTime);
                        backFromPlay.setVisibility(View.VISIBLE);
                    }
                    else{

                        handler.removeCallbacks(volumeShow);
                        volumeFlag=false;

                        backFromPlay.setVisibility(View.GONE);
                    }
                }


                return false;


            }
        });
        videoView.setOnPreparedListener(new OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {
                // TODO Auto-generated method stub

                    progressBarRunnable.run();



                    mMediaController.setAsstentTVShow(View.GONE);




                if(stopFlag&&seekToCurrentMin!=0){
                    timer = new Timer(); //计时任务开始
                    stopFlag = false;
                    timer.schedule(new videoSeektask(), 100);

                }
            }
        });


        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
        {
            @Override
            public void onCompletion(MediaPlayer mp)
            {

                    Log.i(TAG, "over");

                    finish();



            }
        });


        /**
         * this code is added by hyongbai
         */
        videoView.setOnErrorListener(new OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                isPlayError = true;
                String errTxt = "播放失败！";

                Toast.makeText(CloudTVIPlayActivity.this, errTxt, Toast.LENGTH_SHORT).show();
                if(progressDialog.isShowing())progressDialog.dismiss();
                return true;
            }
        });

        stopFlag = false;

        //new for version 1.2
    }
    private Boolean isPlayError = false;

    private void initLayout(){
        //new for version 1.2
        backFromPlay = (LinearLayout) findViewById(R.id.backfromplay);
        backFromPlay.setVisibility(View.GONE);
        Log.i(TAG, "backFromPlayinitLayout:::initLayout::");


        backFromPlay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });


    }
    private void playVideo(){
        videoView.setVideoPath(playUrl);
        videoView.requestFocus();
        videoView.start();

        Log.i(TAG, "play");
    }

    /**
     * this code is added by hyongbai
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (hasFocus && isPlayError) {
            finish();
        }
        super.onWindowFocusChanged(hasFocus);
    }

    public void showWaitDialog() {

        progressDialog.setMessage(getString(R.string.loadingvideo));
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }




    @Override
    protected void onPause() {
        super.onPause();
        videoView.pause();


            seekToCurrentMin = videoView.getCurrentPosition();

    }

    @Override
    protected void onResume() {
        super.onResume();

        videoView.start();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        isPlayError = false;
        if (!progressDialog.isShowing()) {
            showWaitDialog();
        } else {
            handler.removeCallbacks(progressBarRunnable);
            handler.postDelayed(progressBarRunnable, intentPostDelayedTime);
        }

        this.stopFlag = false;
        String[] argArray = intent.getStringArrayExtra(getPackageName()+ playActivityStr);
        String playUrl = null;
        URL url;
        String resetUrl = null;
        if (argArray != null) {
            playUrl = argArray[0];

            if (playUrl != null && playUrl != "" && playUrl != " ") {

                playVideo();

                startNPT = argArray[1];
                int startTime = 0;
                try {
                    startTime = Integer.decode(startNPT);
                } catch (NumberFormatException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                videoView.seekTo(startTime);


            }




        }
        super.onNewIntent(intent);
    }

    protected void onStart() {

        super.onStart();
    }
    protected void onStop() {

        super.onStop();

    };

    String path;

    public VideoView getPlayViewInstance() {
        return videoView;
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    public class sendResultHandler extends Handler {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 0:
                    String s0 = "赠送成功！";

                    Toast.makeText(mContext, s0, Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    String s1 = "赠送失败！";
                    Toast.makeText(mContext, s1, Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    String s2 = "获取失败！";
                    Toast.makeText(mContext, s2, Toast.LENGTH_SHORT).show();
                    break;



            }

        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // TODO Auto-generated method stub
        super.onConfigurationChanged(newConfig);
        System.out.println("yangguanghui:onConfigurationChanged");
        if (this.getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE) {
            // Nothing need to be done here

        } else {
            // Nothing need to be done here
        }
    }



    private class videoSeektask extends TimerTask {
        @Override
        public void run() {
            timer.cancel();

            videoView.seekTo(seekToCurrentMin);
            seekToCurrentMin = 0;

        }
    }


}
