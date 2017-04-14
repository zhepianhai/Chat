package zph.zhjx.com.chat.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

import zph.zhjx.com.chat.R;
import zph.zhjx.com.chat.app.App;
import zph.zhjx.com.chat.base.BaseActivity;
import zph.zhjx.com.chat.dao.User;
import zph.zhjx.com.chat.db.DaoSession;
import zph.zhjx.com.chat.util.GreenDaoUtils;

/**
 * @author adminZPH
 * 启动界面，用来判断打开APP后将要跳转的界面
 * 如果用户已经登录，则进行直接的跳转到主界面
 * 如果用户未登录，则进行跳转到登录界面中
 *
 * */
public class WelcomeActivity extends BaseActivity {
    private final String TAG="WelcomeActivity";
    DaoSession session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        session  = GreenDaoUtils.getSingleTon().getmDaoSession();
        List<User>users=
        session.getUserDao().loadAll();

        if(users.size()==0){
            Intent intent=new Intent(this,LoginActivity.class);
            startActivity(intent);
            finish();
        }
        else{
            boolean flag=false;
            for(int i=0;i<users.size();++i){
                Log.i(TAG,"数据库信息是:"+users.get(i).getPhone());
                Log.i(TAG,"数据库信息是:"+users.get(i).getLogin());
                if(users.get(i).getLogin()){
                    flag=true;
                    App.phone=users.get(i).getPhone();
                    App.token=users.get(i).getToken();
                }
            }
            if(flag) {
                Intent intent = new Intent(this, MenuActivity.class);
                startActivity(intent);
                finish();
            }
            else{
                Intent intent=new Intent(this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }
}
