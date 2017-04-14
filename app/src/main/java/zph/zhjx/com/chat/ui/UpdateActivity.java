package zph.zhjx.com.chat.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import zph.zhjx.com.chat.R;
import zph.zhjx.com.chat.base.BaseActivity;
import zph.zhjx.com.chat.bean.UpdateBean;
import zph.zhjx.com.chat.controller.LoginController;
import zph.zhjx.com.chat.imp.CheckVersion;
import zph.zhjx.com.chat.util.DownLoadApk;
import zph.zhjx.com.chat.util.PhoneUtil;
import zph.zhjx.com.chat.view.CustomProgressDialog;

public class UpdateActivity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout hotfix,update;
    private CustomProgressDialog customProgressDialog;
    private LoginController loginController;
    private View headview;
    private LinearLayout back;
    private TextView title;
    private ImageView logo;


    private boolean isBinded;
    private ProgressBar mProgressBar;
    private boolean isDestroy = true;

    int CurrentVersionCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        setstatusbarcolor();
        loginController=new LoginController(this);
        initview();

    }

    private void initview() {
        logo= (ImageView) findViewById(R.id.update_logo);
        hotfix= (RelativeLayout) findViewById(R.id.hotfix);
        hotfix.setOnClickListener(this);
        update= (RelativeLayout) findViewById(R.id.update);
        update.setOnClickListener(this);
        headview=findViewById(R.id.headview1);
        back= (LinearLayout) headview.findViewById(R.id.headview_left_layout);
        back.setOnClickListener(this);
        title= (TextView) headview.findViewById(R.id.headview_left_textview);
        title.setText("版本更新"+"");

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.headview_left_layout:
                this.finish();
                break;
            case R.id.hotfix:
                toast("暂无补丁");

                break;
            case R.id.update:
                customProgressDialog=new CustomProgressDialog(this,"检查更新...",0);
                customProgressDialog.show();
                Log.i("TAG","目前系统的版本是：");
                loginController.ChackUpdateVersion(new CheckVersion(){
                    @Override
                    public void checkversion(UpdateBean versions) {
                        customProgressDialog.dismiss();
                        Log.i("TAG","获取的版本信息:"+versions.toString());
                        if(!versions.getVersion().equals(PhoneUtil.getVersion(UpdateActivity.this))){
//                            showUpdateDialog(versions);
                        }
                        else{
                            toast("已经是最新版本了...");
                        }
                    }
                });
                break;
        }
    }


    private void showUpdateDialog(final UpdateBean versions){
      /*  new  AlertDialog.Builder(this).setMessage("您当前不是最新版本，是否现在更新?").setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED)) {*/
                   /* Intent intent = new Intent(UpdateActivity.this, UpdateService.class);
                    intent.putExtra("downUrl", versions.getUrl());
                    startService(intent);*/
                   AlertDialog.Builder mDialog;
                    mDialog = new AlertDialog.Builder(UpdateActivity.this);
                    mDialog.setTitle(versions.getName()+"又更新咯！");
                    mDialog.setMessage(versions.getContent());
                    mDialog.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (!canDownloadState()) {
                                showDownloadSetting();
                                return;
                            }
                            // AppInnerDownLoder.downLoadApk(MainActivity.this,downUrl,appName);
                            DownLoadApk.download(UpdateActivity.this,versions.getUrl(),versions.getContent(),versions.getName());
                        }
                    }).setNegativeButton("暂不更新", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).setCancelable(false).create().show();


/*
                } else {
                    Toast.makeText(UpdateActivity.this, "SD卡不可用，请插入SD卡",
                            Toast.LENGTH_SHORT).show();
                }

            }
        }).setNegativeButton("取消",null).show();*/

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    private boolean canDownloadState() {
        try {
            int state = this.getPackageManager().getApplicationEnabledSetting("com.android.providers.downloads");

            if (state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED
                    || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER
                    || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_UNTIL_USED) {
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    private void showDownloadSetting() {
        String packageName = "com.android.providers.downloads";
        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + packageName));
        if (intentAvailable(intent)) {
            startActivity(intent);
        }
    }

    private boolean intentAvailable(Intent intent) {
        PackageManager packageManager = getPackageManager();
        List list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

}
