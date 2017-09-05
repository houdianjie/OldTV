package com.youshi.oldtv;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

/**
 * Created by 典杰 on 2017/7/31.
 */

public class LogoActivity extends Activity {
    String tvlist;
    List<HashMap<String,String>> tvListItems;
    private MyHandler myHandler;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logo_activity);
        myHandler = new MyHandler();
        //downloadFile();
new Thread(new Runnable() {
    @Override
    public void run() {
        boolean isOk =    httpDownload();
        if(isOk){
         myHandler.sendEmptyMessage(0x100);
        }
        else{
           myHandler.sendEmptyMessage(0x101);
        }
    }
}).start();
    }

    private boolean downloadFile(){
        String http = "http://wanvip.picp.net/app/tvm3u8list.txt";
        //创建下载任务,downloadUrl就是下载链接
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(http));
//指定下载路径和下载文件名
        request.setDestinationInExternalPublicDir("/Download/", "tvm3u8list.txt");
//获取下载管理器
        DownloadManager downloadManager= (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
//将下载任务加入下载队列，否则不会进行下载
        downloadManager.enqueue(request);

        return true;
    }

    private boolean httpDownload(){
        ParseXml parseXml = new ParseXml();
        String http = "http://wanvip.picp.net/app/tvm3u8list.xml";
        try{
            URL url = new URL(http);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            //connection.setDoInput(true);
           // connection.setDoOutput(true);
            connection.setRequestMethod("GET");
            connection.connect();
            if(connection.getResponseCode()==200){
                InputStream is = connection.getInputStream();
               tvListItems =  parseXml.parseTvitems(is);
                is.close();
                return true;
            }
        }catch (Exception e){
            Log.e("ERROR","获取失败");
            e.printStackTrace();
        }
        return false;
    }
    class MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            final Bundle bundle = new Bundle();
            boolean isLoadFromNet=false;
            if(msg.what==0x100){
                ListHelper helper = new ListHelper(tvListItems);
                bundle.putSerializable("tvitems",helper);
                isLoadFromNet = true;
            }
            if(msg.what==0x101){
                isLoadFromNet = false;
                bundle.putString("ERROR","网络获取出错");
            }
            Handler handler = new Handler();
            final boolean finalIsLoadFromNet = isLoadFromNet;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(LogoActivity.this,MainActivity.class);
                    intent.putExtra("isLoadFromNet", finalIsLoadFromNet);
                    intent.putExtra("tvitems",bundle);
                    LogoActivity.this.startActivity(intent);
                    LogoActivity.this.finish();
                }
            },3000);
        }
    }
}
