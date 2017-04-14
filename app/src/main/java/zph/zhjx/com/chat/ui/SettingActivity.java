package zph.zhjx.com.chat.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.suke.widget.SwitchButton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import zph.zhjx.com.chat.R;
import zph.zhjx.com.chat.app.ActivityCollector;
import zph.zhjx.com.chat.app.App;
import zph.zhjx.com.chat.base.BaseActivity;
import zph.zhjx.com.chat.bean.LogoutBeans;
import zph.zhjx.com.chat.controller.LoginController;
import zph.zhjx.com.chat.service.LocationService;
import zph.zhjx.com.chat.util.DBUtil;
import zph.zhjx.com.chat.util.ServiceUtil;
import zph.zhjx.com.chat.view.CustomProgressDialog;

import static zph.zhjx.com.chat.util.DBUtil.UpdateDb;

public class SettingActivity extends BaseActivity implements View.OnClickListener {
    SwitchButton sb;
    private final String TAG="SettingActivity";
    private boolean Flag;
    private RelativeLayout logout,update;
    private LoginController loginController;
    private CustomProgressDialog customProgressDialog;
    private View headview;
    private LinearLayout back;
    private TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        setstatusbarcolor();
        initview();
        initTrackend();
        EventBus.getDefault().register(this);
    }

    private void initTrackend() {
        Flag= ServiceUtil.isServiceRunning(this,"zph.zhjx.com.chat.service.LocationService");
        Log.i(TAG,"服务开启的状态"+Flag);
        sb.setChecked(Flag);
        sb.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                Log.i(TAG,"状态："+isChecked);
                if(isChecked){
//                    if(ServiceUtil.isOPen(SettingActivity.this)) {
                        Intent intet1 = new Intent(SettingActivity.this, LocationService.class);
                        startService(intet1);
//                    }

//                    else{
                    if(!ServiceUtil.isOPen(SettingActivity.this)) {
                        toast("开启GPS定位更精确");
                    }
//                        ServiceUtil.openGPS(SettingActivity.this);
//                    }
                }
                else{
                    Intent intet2 = new Intent(SettingActivity.this, LocationService.class);
                    stopService(intet2);
                }
            }
        });
    }

    private void initview() {
        logout= (RelativeLayout) findViewById(R.id.me_logout1);
        logout.setOnClickListener(this);
        update= (RelativeLayout) findViewById(R.id.me_cheack);
        update.setOnClickListener(this);
        sb= (SwitchButton) findViewById(R.id.guiji_switch_button);
        headview=findViewById(R.id.headview1);
        back= (LinearLayout) headview.findViewById(R.id.headview_left_layout);
        back.setOnClickListener(this);
        title= (TextView) headview.findViewById(R.id.headview_left_textview);
        title.setText("设置"+"");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.me_logout1:
                loginController=new LoginController(this);
                customProgressDialog=new CustomProgressDialog(this,"正在退出...",0);
                customProgressDialog.show();
                if(App.phone==null){
                    App.phone= DBUtil.getUserMessage().getPhone();
                    App.token=DBUtil.getUserMessage().getToken();
                }
                loginController.Logout(App.token,App.phone);
                break;
            case R.id.headview_left_layout:
                this.finish();
                break;
            case R.id.me_cheack:
                Intent intent_up=new Intent(this,UpdateActivity.class);
                startActivity(intent_up);
                break;
        }
    }
    //日志的订阅者 
    @Subscribe(threadMode= ThreadMode.ASYNC)
    public void onLogoutEventBus(LogoutBeans message) {

        if(message.getCode()==-1){
            customProgressDialog.dismiss();
            Toast.makeText(this,"服务器未响应...",Toast.LENGTH_SHORT).show();
            return;
        }
        else if (message.getCode()==0){
            boolean Flag= ServiceUtil.isServiceRunning(this,"zph.zhjx.com.chat.service.LocationService");
            if(Flag){
                Intent intet2 = new Intent(this, LocationService.class);
                this.stopService(intet2);
            }
            UpdateDb();
            customProgressDialog.dismiss();
            ActivityCollector.finishAll();
            finish();
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
