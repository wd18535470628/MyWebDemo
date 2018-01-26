package utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017/4/28 0028.
 * 用以获取本机信息的类
 */

public class LocalInfor {

    /**
     * 获取系统当前的时间
     */
    //根据传入的格式获取String类型时间
    public static String getCurrentTime(String dataFormat) {
        SimpleDateFormat format = new SimpleDateFormat(dataFormat);//"yyyy-MM-dd HH:mm:ss:SSS"
        Date curDate = new Date();
        String str = format.format(curDate);
        return str;
    }
    /**
     * 检查当前网络是否可用
     *
     * @param activity
     * @return
     */
    public static boolean isNetworkAvailable(Activity activity)
    {
        Context context = activity.getApplicationContext();
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null)
        {
            return false;
        }
        else
        {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null && networkInfo.length > 0)
            {
                for (int i = 0; i < networkInfo.length; i++)
                {
                    System.out.println(i + "===状态===" + networkInfo[i].getState());
                    System.out.println(i + "===类型===" + networkInfo[i].getTypeName());
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}

