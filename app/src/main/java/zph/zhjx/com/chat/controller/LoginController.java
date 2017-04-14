package zph.zhjx.com.chat.controller;

import android.content.Context;

import zph.zhjx.com.chat.imp.CheckVersion;
import zph.zhjx.com.chat.model.LoginModel;

/**
 * Created by adminZPH on 2017/3/10.
 */

public class LoginController {
    private Context context;
    public LoginController(Context context){
        this.context=context;
    }
    /**
     * @Controller
     * 登录的控制
     * */
    public void Login(String username,String password){
        LoginModel.Login(context,username,password);
    }




    /**
     * @Controller
     * 退出系统，登出
     * */
    public void  Logout(String token,String phone){
        LoginModel.Logout(context,token,phone);
    }


    /**
     * @Controller
     * 检查系统版本更新
     * @param checkVersion*/
    public void ChackUpdateVersion(CheckVersion checkVersion) {
        LoginModel.ChackUpdateVersion(checkVersion);
    }
}
