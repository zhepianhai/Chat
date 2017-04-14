package zph.zhjx.com.chat.base;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import zph.zhjx.com.chat.R;
import zph.zhjx.com.chat.app.ActivityCollector;
import zph.zhjx.com.chat.contact.Contact;


public class BaseActivity extends FragmentActivity {
    Toast toast;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
        ActivityCollector.addActivity(this);
    }

    public void setHeaderTitle(View headerView,String left_text, String title, Contact.Position position) {
        TextView tv = (TextView) headerView.findViewById(R.id.headview_center);
        TextView tvv= (TextView) headerView.findViewById(R.id.headview_left_textview);
        if (title == null) {
            tv.setText("TITLE");
        } else {
            tv.setText(title);
        }
        if (left_text == null) {
            tvv.setText("返回");
        } else {
            tvv.setText(left_text);
        }
        switch (position) {
            case LEFT:
                tv.setGravity(Gravity.LEFT);
                break;

            default:
                tv.setGravity(Gravity.CENTER);
                break;
        }

    }
    public void setHeaderTitle(View headerView,String title) {
        setHeaderTitle(headerView,null, title, Contact.Position.CENTER);
    }
    public void setHeaderImage(View headerView,  Contact.Position position, OnClickListener listener) {
        ImageView iv = null;
        switch (position) {
            case LEFT:
                iv = (ImageView) headerView.findViewById(R.id.headview_left);
                break;

            default:
                iv = (ImageView) headerView.findViewById(R.id.headview_right);
                break;
        }

//        iv.setImageResource(resId);

        if (listener != null) {
            iv.setOnClickListener(listener);
        }
    }



    public void setstatusbackground() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    public void setstatusbarcolor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            // 激活状态栏
            tintManager.setStatusBarTintEnabled(true);
            // enable navigation bar tint 激活导航栏
            tintManager.setNavigationBarTintEnabled(true);
            //设置系统栏设置颜色
            tintManager.setTintColor(R.color.colorhome);
            //给状态栏设置颜色
            tintManager.setStatusBarTintResource(R.color.colorhome);
            //给导航栏设置资源
            tintManager.setNavigationBarTintResource(R.color.colorhome);
        }
    }




    public void setstatusbarcolor(String color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            // 激活状态栏
            tintManager.setStatusBarTintEnabled(true);
            // enable navigation bar tint 激活导航栏
            tintManager.setNavigationBarTintEnabled(true);
            //设置系统栏设置颜色
            tintManager.setTintColor(Color.parseColor(color));
            //给状态栏设置颜色
//            tintManager.setStatusBarTintResource(Color.parseColor(color));
            //给导航栏设置资源
            tintManager.setNavigationBarTintResource(Color.parseColor(color));
        }
    }

    //写一些打印“吐司”的方法

    public void toast(String text){

        if(TextUtils.isEmpty(text)){
            return;
        }
        toast.setText(text);
        toast.show();
    }


    public boolean CheckIsEmpty(String... strings) {
        for(String s:strings){
            if (s.isEmpty()){
                return false;
            }
        }
        return  true;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
