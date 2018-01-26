package com.example.administrator.myweb;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Process;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.just.library.BaseAgentWebActivity;
import notice.MyNotification;
import runtimepermissions.PermissionsManager;
import runtimepermissions.PermissionsResultAction;
import utils.LocalInfor;

import static android.R.attr.password;

public class MainActivity extends BaseAgentWebActivity {

    private MyNotification myNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //处理权限
        requestPermission();
        //获取异常并通知
        myNotification = new MyNotification(this);
        myNotification.getExceptionPerMin();
        //openBrowser();//京东 "https://www.jd.com" //显示监狱网站"http://demo.aisi365.com/jianyu"
        //mLinearLayout = (LinearLayout) this.findViewById(R.id.main_layout);
        //检查当前网络
        if (!LocalInfor.isNetworkAvailable(this)) {
            new MaterialDialog.Builder(this)
                    .title("提示")
                    .content("当前网络不可用，请检查网络连接")
                    .positiveText("确定")
                    .show();
        }

        //关闭网页缓存
        //每次不再从缓存中读取数据
        mAgentWeb.getAgentWebSettings().getWebSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

        //关闭缓存   无效？？？
//        WebView mWebView = mAgentWeb.getWebCreator().get();
//        WebSettings webSettings = mWebView.getSettings();
//        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //关闭webview中缓存
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //屏蔽back事件
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //跳出退出操作弹窗
            final View view = View.inflate(MainActivity.this,R.layout.password_layout,null);
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("退出");
            builder.setMessage("你想在后台执行此程序吗？");
            builder.setView(view,0,20,20,20);
            builder.setNegativeButton("取消", null);

            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    EditText text = (EditText) view.findViewById(R.id.et_password);
                    String password = text.getText().toString();
                    if (password.equals("uanfun888")){
                        Intent i = new Intent(Intent.ACTION_MAIN);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.addCategory(Intent.CATEGORY_HOME);
                        startActivity(i);
                    }else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("提示");
                        builder.setMessage("密码不正确,请联系管理员");
                        builder.setPositiveButton("确定",null);
                        builder.show();
                    }
                }
            });
            builder.show();
            /*new MaterialDialog.Builder(this)
                    .title("退出")
                    .content("你想在后台执行此程序吗？")
                    .positiveText("是")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            View view = View.inflate(MainActivity.this,R.layout.password_layout,null);
                            EditText text = (EditText) view.findViewById(R.id.et_password);
                            String password = text.getText().toString();
                            if (password.isEmpty()){
                                Toast.makeText(MainActivity.this,"密码不能为空",Toast.LENGTH_SHORT).show();
                            }else {
                                if (password.equals("123456")){
                                    Intent i = new Intent(Intent.ACTION_MAIN);
                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    i.addCategory(Intent.CATEGORY_HOME);
                                    startActivity(i);
                                }else {
                                    return ;
                                }
                            }
                            // TODO
                            //实现Home键效果
                            //super.onBackPressed();这句话一定要注掉,不然又去调用默认的back处理方式了
                           *//* Intent i = new Intent(Intent.ACTION_MAIN);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            i.addCategory(Intent.CATEGORY_HOME);
                            startActivity(i);*//*
                        }
                    })
                    .negativeText("否")
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            // TODO
                            //执行退出操作,并释放资源
                            finish();
                            //Dalvik VM的本地方法完全退出app
                            Process.killProcess(Process.myPid());    //获取PID
                            System.exit(0);   //常规java、c#的标准退出法，返回值为0代表正常退出
                        }
                    })
                    .neutralText("取消")
                    .show();
            return true;*/
        }
        if(keyCode == KeyEvent.KEYCODE_HOME){
            new MaterialDialog.Builder(this)
                    .title("退出").show();
            return true;
        }
        if (mAgentWeb.handleKeyEvent(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @NonNull
    @Override
    protected ViewGroup getAgentWebParent() {
        return (ViewGroup) this.findViewById(R.id.main_layout);
    }

    @Override
    protected void setTitle(WebView view, String title) {
        //mTitleTextView.setText(title);
    }

    //进度条的颜色
    @Override
    protected int getIndicatorColor() {
        return Color.parseColor("#00ff00");
    }

    //进度条的宽度
    @Override
    protected int getIndicatorHeight() {
        return 2;
    }

    @Nullable
    @Override
    protected String getUrl() {
        //京东 "https://www.jd.com"
        // 显示监狱网站"http://demo.aisi365.com/jianyu"
        //"http://www.baidu.com"
        //http://116.62.145.58
        return "http://116.62.145.58";
    }

    /**
     * 适配android6.0以上权限                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         =
     */
    private void requestPermission() {
        /**
         * 请求所有必要的权限
         */
        PermissionsManager.getInstance().requestAllManifestPermissionsIfNecessary(this, new PermissionsResultAction() {
            @Override
            public void onGranted() {
//				Toast.makeText(MainActivity.this, "All permissions have been granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDenied(String permission) {
                //Toast.makeText(MainActivity.this, "Permission " + permission + " has been denied", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
