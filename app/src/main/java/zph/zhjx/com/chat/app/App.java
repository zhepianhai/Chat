package zph.zhjx.com.chat.app;
import android.app.Application;
import android.content.Intent;

import cn.jpush.android.api.JPushInterface;
import zph.zhjx.com.chat.receiver.ForceDownlineReceiver;
public class App extends Application {
    private static App myApplaction;
    public static String phone;
    public static String token;
    public static String appVersion;

    @Override
    public void onCreate() {
        super.onCreate();
        myApplaction=this;
      /* CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());*/
        JPushInterface.setDebugMode(true);    // 设置开启日志,发布时请关闭日志
        JPushInterface.init(myApplaction);     // 初始化 JPush
        Intent intet = new Intent(myApplaction,ForceDownlineReceiver.class);
        startService(intet);
//        initApp();//初始化APP 
//        initHotfix();//初始化Hotfix 
    }

  /*  private void initHotfix() {
        SophixManager.getInstance().setContext(this)
                .setAppVersion(appVersion)
                .setAesKey(null)
                .setEnableDebug(true)
                .setPatchLoadStatusStub(new PatchLoadStatusListener() {
                    @Override
                    public void onload(final int mode, final int code, final String info, final int handlePatchVersion) {
                        // 补丁加载回调通知
                        if (code == PatchStatus.CODE_LOAD_SUCCESS) {
                            // 表明补丁加载成功
                        } else if (code == PatchStatus.CODE_LOAD_RELAUNCH) {
                            // 表明新补丁生效需要重启. 开发者可提示用户或者强制重启;
                            // 建议: 用户可以监听进入后台事件, 然后应用自杀
                        } else if (code == PatchStatus.CODE_LOAD_FAIL) {
                            // 内部引擎异常, 推荐此时清空本地补丁, 防止失败补丁重复加载
                            // SophixManager.getInstance().cleanPatches();
                        } else {
                            // 其它错误信息, 查看PatchStatus类说明
                        }
                    }
                }).initialize();
        SophixManager.getInstance().queryAndLoadNewPatch();
    }

    private void initApp() {
        this.appVersion="1.0";

    }*/

    /**
     * 用来检查数据库是否存在
     * */

   public static App getIntance(){
        return myApplaction;
   }
}
