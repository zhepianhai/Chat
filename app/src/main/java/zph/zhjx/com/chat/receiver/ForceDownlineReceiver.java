package zph.zhjx.com.chat.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import cn.jpush.android.api.JPushInterface;
import zph.zhjx.com.chat.app.ActivityCollector;
import zph.zhjx.com.chat.ui.FroceDeonLineActivity;
import zph.zhjx.com.chat.util.DBUtil;
import zph.zhjx.com.chat.view.ForceDownDialog;

/**
 * Created by adminZPH on 2017/3/6.
 * 这里是用来进行用户强制下线的
 */

public class ForceDownlineReceiver extends BroadcastReceiver {
    ForceDownDialog downDialog;
    @Override
    public void onReceive(Context context, Intent intent) {
        JPushInterface.init(context);
        Bundle bundle = intent.getExtras();
        if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.d("TAG", "[MyReceiver] 接收到推送下来的自定义消息: "+bundle.getString(JPushInterface.EXTRA_MESSAGE));
            processCustomMessage(context, bundle);
        }
    }
    private void processCustomMessage(Context context, Bundle bundle) {
        String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        Log.i("TAGG", "自定义消息:" + message.toString());
        Log.i("TAGG", "自定义消息:" + extras.toString());
       /* downDialog=new ForceDownDialog(App.getIntance(),"",0);
        downDialog.show();*/
       Log.i("TAGG","登录状态:"+DBUtil.UserIsLogin());
       if(DBUtil.UserIsLogin()) {
           ActivityCollector.finishAll();
           Intent intent = new Intent(context, FroceDeonLineActivity.class);
           intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
           context.startActivity(intent);
       }
    }
}
