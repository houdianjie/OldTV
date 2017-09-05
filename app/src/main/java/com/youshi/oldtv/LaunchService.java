package com.youshi.oldtv;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by 典杰 on 2017/8/1.
 */

public class LaunchService extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        /*Intent bootsIntent = new Intent(context, MainActivity.class);
        bootsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(bootsIntent);*/
       // Intent bootIntent = new Intent(context, UploadImageService.class);
//为了避免被强制停止后接收不到广播
       // bootIntent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
       // context.startService(bootIntent);
        //包名为要唤醒的应用包名
        Intent bootIntent = context.getPackageManager().getLaunchIntentForPackage("com.youshi.oldtv");
        context.startActivity(bootIntent);
    }
}
