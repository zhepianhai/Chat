package zph.zhjx.com.chat.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import zph.zhjx.com.chat.R;
import zph.zhjx.com.chat.base.BaseActivity;
import zph.zhjx.com.chat.bean.User;
import zph.zhjx.com.chat.controller.LoginController;
import zph.zhjx.com.chat.util.BitmapUtil;
import zph.zhjx.com.chat.util.PhoneUtil;
import zph.zhjx.com.chat.view.CustomProgressDialog;

import static zph.zhjx.com.chat.app.App.phone;
import static zph.zhjx.com.chat.app.App.token;
import static zph.zhjx.com.chat.util.DBUtil.initDataBase;

//import butterknife.ButterKnife;

public class LoginActivity extends BaseActivity {
    private RelativeLayout welcomeBg;
    private String username,password;
    private CustomProgressDialog customProgressDialog;
    private EditText username_edit,password_edit;
    private Button login;
    private LoginController loginController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username_edit= (EditText) findViewById(R.id.edit_username);
        password_edit= (EditText) findViewById(R.id.edit_userpassword);
        login= (Button) findViewById(R.id.button_login);
        setStatus();
        loginController=new LoginController(this);
        EventBus.getDefault().register(this);
       /* String num=PhoneUtil.getNativePhoneNumber(this);
        if(num!=null){
            username_edit.setText(num);
        }*/
    }
    public void login(View view){
        username=username_edit.getText().toString().trim();
        password=password_edit.getText().toString().trim();
        if(!CheckIsEmpty(username,password)){
            toast("不能为空！");
            return;
        }
        customProgressDialog=new CustomProgressDialog(this,"登录中...",0);
        customProgressDialog.show();
        loginController.Login(username,password);

    }
    private void setStatus() {
        setstatusbackground();
    }
    //登录的订阅者 
    @Subscribe(threadMode= ThreadMode.MAIN)
    public void onLoginEventBus(User user){

        if (user.getCode()==-1){
            toast("服务器连接失败...");
            customProgressDialog.dismiss();
        }
        else if(user.getCode()==0){
            customProgressDialog.dismiss();
            JPushInterface.setAlias(this,username+ PhoneUtil.getIMEI(this), new TagAliasCallback() {
                @Override
                public void gotResult(int i, String s, Set<String> set) {
                }
            });

            String tokenn=user.getData().getToken();
            phone=username;
            token=tokenn;

            zph.zhjx.com.chat.dao.User myuser=new zph.zhjx.com.chat.dao.User(true,username,user.getData().getPhoto(),user.getData().getToken(),user.getData().getUserName(),user.getData().getUserNickname(),
                    user.getData().getAddress(),user.getData().getBirthday(),user.getData().getGender(),false,
                    BitmapUtil.BitmapToString(BitmapUtil.GetUserImageByNickName(LoginActivity.this,user.getData().getUserNickname())));
            Log.i("TAG","base64格式:"+BitmapUtil.BitmapToString(BitmapUtil.GetUserImageByNickName(LoginActivity.this,user.getData().getUserNickname())));
            initDataBase(myuser);
            Intent intent=new Intent(this,MenuActivity.class);
            startActivity(intent);
            this.finish();
        }
        else {
            toast(user.getMessage()+"");
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}

