package zph.zhjx.com.chat.model;

import android.content.Context;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import zph.zhjx.com.chat.bean.LogoutBeans;
import zph.zhjx.com.chat.bean.UpdateBean;
import zph.zhjx.com.chat.bean.User;
import zph.zhjx.com.chat.contact.Contact;
import zph.zhjx.com.chat.imp.BeanImp;
import zph.zhjx.com.chat.imp.CheckVersion;
import zph.zhjx.com.chat.util.MD5Util;
import zph.zhjx.com.chat.util.PhoneUtil;

/**
 * Created by adminZPH on 2017/3/10.
 */

public class LoginModel {

    public static void Login(final Context context,final String username, String password) {
        BeanImp repo;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Contact.Login_Interface)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        repo=retrofit.create(BeanImp.class);
        Call<User> call=repo.contributorsBySimpleGetCall(username, MD5Util.getMD5(password),PhoneUtil.getIMEI(context),"192.168.65.1");
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User us=response.body();
                Log.i("TAG",""+response.message().toString());
                if(us!=null)
                    EventBus.getDefault().post(us);
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                User us=new User();
                us.setCode(-1);
                EventBus.getDefault().post(us);
            }
        });

    }





   /* *//**
     * 向服务器提交日志信息
     * *//*
    public static void Log(Context context, String token, String phone,String state) {
        BeanImp repo1;
        Retrofit retrofit1 = new Retrofit.Builder()
                .baseUrl(Contact.Login_Interface)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        repo1=retrofit1.create(BeanImp.class);
        Call<LogoutBean> call=repo1.toServeUpadateLog(token,phone,token,state, PhoneUtil.getIMEI(context),"192.168.65.1");
        call.enqueue(new Callback<LogoutBean>() {
            @Override
            public void onResponse(Call<LogoutBean> call, Response<LogoutBean> response) {
                LogoutBean message=response.body();
                EventBus.getDefault().post(message);
            }
            @Override
            public void onFailure(Call<LogoutBean> call, Throwable t) {
                LogoutBean message=new LogoutBean();
                message.setCode(-1);
                EventBus.getDefault().post(message);
            }
        });
    }
*/


    /**
     * 登出系统
     *
     * */
    public static void Logout(Context context, String token, String phone) {
        BeanImp repo1;
        Retrofit retrofit1 = new Retrofit.Builder()
                .baseUrl(Contact.Login_Interface)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        repo1=retrofit1.create(BeanImp.class);
        Call<LogoutBeans> call=repo1.logout(token,phone,PhoneUtil.getIMEI(context),"192.168.65.1");
        call.enqueue(new Callback<LogoutBeans>() {
            @Override
            public void onResponse(Call<LogoutBeans> call, Response<LogoutBeans> response) {
                LogoutBeans message=response.body();
                if(message!=null)
                    EventBus.getDefault().post(message);
            }
            @Override
            public void onFailure(Call<LogoutBeans> call, Throwable t) {
                LogoutBeans message=new LogoutBeans();
                message.setCode(-1);
                EventBus.getDefault().post(message);
            }
        });
    }


    /**
     * @MODel
     * 检查版本更新的业务逻辑类
     * @param checkVersion*/

    public static void ChackUpdateVersion(final CheckVersion checkVersion) {
        new Thread(){
            @Override
            public void run() {
                Log.i("TAG","1");
                String path = Contact.UpdateUrl;
                Log.i("TAG","2");
                StringBuffer sb = new StringBuffer();
                Log.i("TAG","3");
                String line = null;
                Log.i("TAG","4");
                BufferedReader reader = null;
                Log.i("TAG","5");
                try {
                    // 创建一个url对象
                    URL url = new URL(path);
                    Log.i("TAG","6");
                    // 通過url对象，创建一个HttpURLConnection对象（连接）
                    HttpURLConnection urlConnection = (HttpURLConnection) url
                            .openConnection();
                    // 通过HttpURLConnection对象，得到InputStream
                    reader = new BufferedReader(new InputStreamReader(
                            urlConnection.getInputStream(),"gbk"));
                    // 使用io流读取文件
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                     checkVersion.checkversion(fengedate(sb.toString()));
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (reader != null) {
                            reader.close();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    private static UpdateBean fengedate(String s) {
        UpdateBean version=new UpdateBean();
        String[] aa = s.split("&");
        version.setVersion(aa[0]);
        version.setContent(aa[1]);
        version.setUrl(aa[2]);
        version.setName(aa[3]);
        return  version;
    }
}
