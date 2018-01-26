package com.example.administrator.myweb;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Administrator on 2017/12/2 0002.
 */

public class AutoStartBroadcastReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();
        if(action.equals("android.intent.action.BOOT_COMPLETED")){
            Intent sayHelloIntent=new Intent(context,MainActivity.class);
            //Intent intent0 = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
            sayHelloIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(sayHelloIntent);
        }
    }
}
