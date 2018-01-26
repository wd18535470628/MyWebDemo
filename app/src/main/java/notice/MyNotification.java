package notice;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.NotificationCompat;

import com.alibaba.fastjson.JSON;
import com.example.administrator.myweb.MainActivity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * 获取异常信息，并显示在通知栏
 * Created by Administrator on 2017/8/7 0007.
 */

public class MyNotification {
    private Activity mActivity;

    //获取之前存储的id
    private int oldId;
    //异常获取地址
    private static String urlStr = "http://prison.uanfun.com/index/index/getexception";

    public MyNotification(Activity activity) {
        mActivity = activity;
        //取之前存的id
        SharedPreferences pref = activity.getSharedPreferences("data", activity.MODE_PRIVATE);
        oldId = pref.getInt("oldId", -1);
    }


    //
    public void getExceptionPerMin() {
        //获取异常
        new GetThread().start();
    }

    //以下是获取异常的方法
    //GET
    //网络线程，因为不能在主线程访问Intent
    class GetThread extends Thread {
        @Override
        public void run() {
            while (true) {
                HttpURLConnection conn = null;//声明连接对象
                InputStream is = null;
                String resultData = "";
                try {
                    URL url = new URL(urlStr); //URL对象
                    conn = (HttpURLConnection) url.openConnection(); //使用URL打开一个链接,下面设置这个连接
                    conn.setRequestMethod("GET"); //使用get请求
                    // 不要用cache，用了也没有什么用，因为我们不会经常对一个链接频繁访问。（针对程序）
                    conn.setUseCaches(false);
                    conn.setConnectTimeout(6 * 1000);
                    conn.setReadTimeout(6 * 1000);
                    conn.setRequestProperty("Charset", "utf-8");

                    if (conn.getResponseCode() == 200) {//返回200表示连接成功
                        is = conn.getInputStream(); //获取输入流
                        InputStreamReader isr = new InputStreamReader(is);
                        BufferedReader bufferReader = new BufferedReader(isr);
                        String inputLine = "";
                        while ((inputLine = bufferReader.readLine()) != null) {
                            resultData += inputLine + "\n";
                            //resultData += inputLine;
                        }
                        //System.out.println("get方法取回内容：" + resultData);
                    }
                    //resultData为获取的异常信息
                    //json格式的字符串 若是解析出错 会抓取到异常
                    MyException myException = JSON.parseObject(resultData, MyException.class);
                    int id = myException.getId();
                    if (id > oldId) {   //是应该不等于判断  还是 大于判断
                        String description = myException.getDescription();
                        //查找本地是否已有此id的信息
                        //存此id
                        SharedPreferences.Editor editor = mActivity.getSharedPreferences("data", mActivity.MODE_PRIVATE).edit();
                        editor.putInt("oldId", id);
                        editor.apply();
                        oldId = id;
                        //把description推送到控制台
                        showNotification(description);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    //一分钟后再重新获取
                    //改为20s获取一次
                    try {
                        Thread.sleep(20 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void showNotification(String content) {
        //实例化通知管理器
        NotificationManager notificationManager = (NotificationManager) mActivity.getSystemService(Context.NOTIFICATION_SERVICE);
        //实例化通知
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mActivity);

        builder.setContentTitle("通知");//设置通知标题
        builder.setContentText(content);//设置通知内容
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);//设置通知的方式，震动、LED灯、音乐等
        builder.setAutoCancel(true);//点击通知后，状态栏自动删除通知
        builder.setSmallIcon(android.R.drawable.ic_media_play);//设置小图标
        builder.setContentIntent(PendingIntent.getActivity(mActivity, 0x102, new Intent(mActivity, MainActivity.class), 0));//设置点击通知后将要启动的程序组件对应的PendingIntent
        Notification notification = builder.build();

        //发送通知
        notificationManager.notify(0x101, notification);
    }


}
