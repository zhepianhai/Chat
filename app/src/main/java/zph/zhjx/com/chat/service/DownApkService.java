///*
//package zph.zhjx.com.chat.service;
//
//import android.annotation.TargetApi;
//import android.app.Notification;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.app.Service;
//import android.content.Context;
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Binder;
//import android.os.Build;
//import android.os.Handler;
//import android.os.IBinder;
//import android.os.Message;
//import android.util.Log;
//import android.widget.RemoteViews;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.HttpURLConnection;
//import java.net.MalformedURLException;
//import java.net.URL;
//
//import zph.zhjx.com.chat.R;
//import zph.zhjx.com.chat.ui.UpdateActivity;
//
//*/
///**
// * 这个是进行APK文件下载得Service
// * 从本地服务器下载
// * *//*
//
//public class DownApkService extends Service {
//    private static final int NOTIFY_ID = 0;
//    private int progress;
//    private NotificationManager mNotificationManager;
//    private boolean canceled;
//    // 返回的安装包url
//    private String apkUrl = "";
//	*/
///* 下载包安装路径 *//*
//
//    private static final String savePath = "/sdcard/updateApkDemo/";
//    private static final String saveFileName = savePath + "landproject.apk";
//    private UpdateActivity.ICallbackResult callback;
//    private DownloadBinder binder;
//    private boolean serviceIsDestroy = false;
//
//    private Context mContext = this;
//    @Override
//    public void onCreate() {
//        // TODO Auto-generated method stub
//        super.onCreate();
//        apkUrl=app.version.getUrl();
//        Log.i("TAG","下载地址是:"+apkUrl);
//        binder = new DownloadBinder();
//        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//    }
//    private Handler mHandler = new Handler() {
//
//        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
//        @Override
//        public void handleMessage(Message msg) {
//            // TODO Auto-generated method stub
//            super.handleMessage(msg);
//
//            switch (msg.what) {
//                case 0:
//                    // 下载完毕
//                    // 取消通知
//                    mNotificationManager.cancel(NOTIFY_ID);
//                    installApk();
//                    break;
//                case 2:
//                    // 这里是用户界面手动取消，所以会经过activity的onDestroy();方法
//                    // 取消通知
//                    mNotificationManager.cancel(NOTIFY_ID);
//                    break;
//                case 1:
//                    int rate = msg.arg1;
//                    if (rate < 100) {
//                        RemoteViews contentview = mNotification.contentView;
//                        contentview.setTextViewText(R.id.tv_progress, rate + "%");
//                        contentview.setProgressBar(R.id.progressbar, 100, rate, false);
//                    } else {
//                        System.out.println("下载完毕!!!!!!!!!!!");
//                        // 下载完毕后变换通知形式
//                        mNotification.flags = Notification.FLAG_AUTO_CANCEL;
//                        mNotification.contentView = null;
//                        Intent intent = new Intent(mContext, JSQActivity.class);
//                        // 告知已完成
//                        intent.putExtra("completed", "yes");
//                        // 更新参数,注意flags要使用FLAG_UPDATE_CURRENT
//                        PendingIntent contentIntent = PendingIntent.getActivity(mContext, 0, intent,
//                                PendingIntent.FLAG_UPDATE_CURRENT);
//                        Notification.Builder builder1 = new Notification.Builder(mContext);
//                        builder1.setSmallIcon(R.mipmap.xuanjilogopng); //设置图标
//                        builder1.setTicker("下载完毕");
//                        builder1.setContentTitle("下载土地流转安装包成功"); //设置标题
//                        builder1.setContentText("app已经更新成功，点击更新"); //消息内容
//                        builder1.setWhen(System.currentTimeMillis()); //发送时间
//                        builder1.setDefaults(Notification.DEFAULT_ALL); //设置默认的提示音，振动方式，灯光
//                        builder1.setAutoCancel(true);//打开程序后图标消失
//                        Intent intent1 =new Intent (mContext, JSQActivity.class);
//                        PendingIntent pendingIntent =PendingIntent.getActivity(mContext, 0, intent, 0);
//                        builder1.setContentIntent(pendingIntent);
//                        Notification notification1 = builder1.build();
//                        mNotificationManager.notify(124, notification1); // 通过通知管理器发送通知
//                        serviceIsDestroy = true;
//                        stopSelf();// 停掉服务自身
//                    }
////                    mNotificationManager.notify(NOTIFY_ID, mNotification);
//                    break;
//            }
//        }
//    };
//
//
//    @Override
//    public IBinder onBind(Intent intent) {
//        // TODO Auto-generated method stub
//        System.out.println("是否执行了 onBind");
//        return binder;
//    }
//
//    @Override
//    public void onDestroy() {
//        // TODO Auto-generated method stub
//        super.onDestroy();
//        System.out.println("downloadservice ondestroy");
//        // 假如被销毁了，无论如何都默认取消了。
//    }
//
//    @Override
//    public boolean onUnbind(Intent intent) {
//        // TODO Auto-generated method stub
//        System.out.println("downloadservice onUnbind");
//        return super.onUnbind(intent);
//    }
//
//    @Override
//    public void onRebind(Intent intent) {
//        // TODO Auto-generated method stub
//
//        super.onRebind(intent);
//        System.out.println("downloadservice onRebind");
//    }
//
//
//
//    public class DownloadBinder extends Binder {
//        public void start() {
//            if (downLoadThread == null || !downLoadThread.isAlive()) {
//
//                progress = 0;
//                setUpNotification();
//                new Thread() {
//                    public void run() {
//                        // 下载
//                        startDownload();
//                    };
//                }.start();
//            }
//        }
//
//        public void cancel() {
//            canceled = true;
//        }
//
//        public int getProgress() {
//            return progress;
//        }
//
//        public boolean isCanceled() {
//            return canceled;
//        }
//
//        public boolean serviceIsDestroy() {
//            return serviceIsDestroy;
//        }
//
//        public void cancelNotification() {
//            mHandler.sendEmptyMessage(2);
//        }
//
//        public void addCallback(UpdateActivity.ICallbackResult callback) {
//            DownApkService.this.callback = callback;
//        }
//
//
//
//    }
//
//    private void startDownload() {
//        // TODO Auto-generated method stub
//        canceled = false;
//        downloadApk();
//    }
//
//    //
//    Notification mNotification;
//
//    // 通知栏
//    */
///**
//     * 创建通知
//     *//*
//
//    private void setUpNotification() {
//        int icon = R.mipmap.xuanjilogopng;
//        CharSequence tickerText = "开始下载";
//        long when = System.currentTimeMillis();
//        mNotification = new Notification(icon, tickerText, when);
//        ;
//        // 放置在"正在运行"栏目中
//        mNotification.flags = Notification.FLAG_ONGOING_EVENT;
//        RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.download_notification_layout);
//        contentView.setTextViewText(R.id.name, "土地流转.apk 正在下载...");
//        // 指定个性化视图
//        mNotification.contentView = contentView;
//
//        Intent intent = new Intent(this, JSQActivity.class);
//        // 下面两句是 在按home后，点击通知栏，返回之前activity 状态;
//        // 有下面两句的话，假如service还在后台下载， 在点击程序图片重新进入程序时，直接到下载界面，相当于把程序MAIN 入口改了 - -
//        // 是这么理解么。。。
//        // intent.setAction(Intent.ACTION_MAIN);
//        // intent.addCategory(Intent.CATEGORY_LAUNCHER);
//        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent,
//                PendingIntent.FLAG_UPDATE_CURRENT);
//        // 指定内容意图
//        mNotification.contentIntent = contentIntent;
//        mNotificationManager.notify(NOTIFY_ID, mNotification);
//    }
//    //
//    */
///**
//     * 下载apk
//     *
//     * @param url
//     *//*
//
//    private Thread downLoadThread;
//
//    private void downloadApk() {
//        downLoadThread = new Thread(mdownApkRunnable);
//        downLoadThread.start();
//    }
//    */
///**
//     * 安装apk
//     * @param
//     *//*
//
//    private void installApk() {
//        File apkfile = new File(saveFileName);
//        if (!apkfile.exists()) {
//            return;
//        }
//        Intent i = new Intent(Intent.ACTION_VIEW);
//        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
//        mContext.startActivity(i);
//        callback.OnBackResult("finish");
//    }
//
//    private int lastRate = 0;
//    private Runnable mdownApkRunnable = new Runnable() {
//        @Override
//        public void run() {
//            try {
//                URL url = new URL(apkUrl);
//                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                conn.connect();
//                int length = conn.getContentLength();
//                InputStream is = conn.getInputStream();
//                File file = new File(savePath);
//                if (!file.exists()) {
//                    file.mkdirs();
//                }
//                String apkFile = saveFileName;
//                File ApkFile = new File(apkFile);
//                FileOutputStream fos = new FileOutputStream(ApkFile);
//                int count = 0;
//                byte buf[] = new byte[1024];
//                do {
//                    int numread = is.read(buf);
//                    count += numread;
//                    progress = (int) (((float) count / length) * 100);
//                    // 更新进度
//                    Message msg = mHandler.obtainMessage();
//                    msg.what = 1;
//                    msg.arg1 = progress;
//                    if (progress >= lastRate + 1) {
//                        mHandler.sendMessage(msg);
//                        lastRate = progress;
//                        if (callback != null)
//                            callback.OnBackResult(progress);
//                    }
//                    if (numread <= 0) {
//                        // 下载完成通知安装
//                        mHandler.sendEmptyMessage(0);
//                        // 下载完了，cancelled也要设置
//                        canceled = true;
//                        break;
//                    }
//                    fos.write(buf, 0, numread);
//                } while (!canceled);// 点击取消就停止下载.
//                fos.close();
//                is.close();
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    };
//
//}
//*/
