package zph.zhjx.com.chat.fragment;


import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMapOptions;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.LatLng;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import zph.zhjx.com.chat.R;
import zph.zhjx.com.chat.bean.Message_01;
import zph.zhjx.com.chat.contact.Contact;
import zph.zhjx.com.chat.controller.GeoSOTController;
import zph.zhjx.com.chat.geosot.DrawGeoSOT1;
import zph.zhjx.com.chat.util.BitmapUtil;
import zph.zhjx.com.chat.view.CustomProgressDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class LiuYanFragment extends Fragment implements  LocationSource, AMap.OnMapLoadedListener, AMap.OnMapTouchListener, AMapLocationListener {
    private View view;
    private MapView mapview;
    private AMap aMap;
    private DrawGeoSOT1 mDrawGeoSOT; // 绘制GeoSOT
    private Display mDisplay;
    private int mWidth;
    private int mHeight;
    private int fillcolor;
    private int strokecolor;
    private CustomProgressDialog customProgressDialog,customProgressDialog1;
    private LocationSource.OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private GeoSOTController geoSOTController;
    private final int BAIDU_READ_PHONE_STATE=1005;
    //第三方API和微信通信的接口
    private IWXAPI api;
    public LiuYanFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_liu_yan, container, false);
        initview();
        mapview.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT>=23){
            showContacts();
        }else{
            initMap();
        }
        geoSOTController=new GeoSOTController(getActivity());
        EventBus.getDefault().register(LiuYanFragment.this);
        Toast.makeText(getActivity(),"长按可进行留言",Toast.LENGTH_LONG).show();
        return view;
    }

    private void showContacts() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getActivity(),"没有权限,请手动开启定位权限",Toast.LENGTH_SHORT).show();
            // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE}, BAIDU_READ_PHONE_STATE);
        }else{
            initMap();
        }
    }

    private void initMap() {

        if (aMap == null) {
            aMap = mapview.getMap();
            mapview.setSelected(true);
            setBlueMap();
        }
        aMap.setLocationSource(this);// 设置定位监听
        aMap.setOnMapLoadedListener(this);
        aMap.setOnMapTouchListener(this);
        customProgressDialog=new CustomProgressDialog(getActivity(),"正在获取你的位置信息...",0);
        customProgressDialog.show();
    }
    /**
     * 显示GeoSOT网格
     */
    private void displayGeoSOT() {
        // aMap.invalidate(); //刷新地图
        mDisplay = getActivity().getWindowManager().getDefaultDisplay();
        mWidth = mDisplay.getWidth();
        mHeight = mDisplay.getHeight();
        System.out.println("\n屏幕的像素密度为：" + mWidth + ", " + mHeight + "\n");
        fillcolor = Color.argb(10, 1, 1, 255);
        strokecolor = Color.argb(255, 100, 100, 80);
        mDrawGeoSOT = new DrawGeoSOT1(aMap, fillcolor, strokecolor,5);
        mDrawGeoSOT.redrawGeoSOT(aMap, mWidth, mHeight);
    }
    private void setBlueMap() {

        aMap.setLocationSource(this);// 设置定位监听
        AMapOptions op=new AMapOptions();
        op.zoomControlsEnabled(false);//隐藏
        UiSettings uiSettings = aMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(false);
        aMap.getUiSettings().setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
    }
    private void initview() {

        mapview= (MapView) view.findViewById(R.id.liuyan_map);
    }



    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(getActivity());
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置多少秒去刷新位置
            mLocationOption.setInterval(10*1000);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();
        }
    }

    @Override
    public void deactivate() {
        //停止定位
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    @Override
    public void onMapLoaded() {
        displayGeoSOT();
        /**
         * 地图长按事件
         * */

    }
    /**
     * 用来显示加载的弹出画面和进行留言上传的界面
     *
     * */
    private void LoadingUI(LatLng latLng) {
        // aMap的长按事件
        geoSOTController.UpdateUserLiuYan(latLng);
    }
    //获取指定的人信息的订阅者 
    @Subscribe(threadMode= ThreadMode.MAIN)
    public void onSubmitLiuYanEventBus(Message_01 message) {
        if(message.getCode()==-1){
            Toast.makeText(getActivity(),"服务器异常",Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getActivity(),message.getMessage(),Toast.LENGTH_SHORT).show();
            if(message.getCode()==0){
                ShareToWeiChat();
            }
        }
    }
    //选择是否分享到微信平台
    private void ShareToWeiChat() {
        new AlertDialog.Builder(getActivity()).setTitle("留言分享").setMessage("微信分享审核中...")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                        //TODO 没做完
                        registerApp();
                        fenxiangNexwork();
                        getActivity().finish();
                    }
                }).show();
    }
    /**将第三方API注册到微信中去
	 * */
    protected void registerApp() {
        // 获取API的实例
        api= WXAPIFactory.createWXAPI(getActivity(),Contact.APP_ID , true);
        //注册
        api.registerApp(Contact.APP_ID);
    }
    /**
     * 网站分享功能
     * **/
    protected void fenxiangNexwork() {
        //初始化对象
        WXWebpageObject webobj=new WXWebpageObject();
        webobj.webpageUrl="http://www.watertek.com/";
        WXMediaMessage mess=new WXMediaMessage(webobj);
        mess.title="旋极科技—网格聊天";
        mess.description="这是一个微信分享的描述测试数据";
        //进行图片缩略
        Bitmap bitmap= BitmapFactory.decodeResource(getResources(),R.mipmap.icoon_main);
        Bitmap sbitmap=Bitmap.createScaledBitmap(bitmap, 100, 100, true);
        mess.thumbData= BitmapUtil.bmpToByteArray(sbitmap, true);

        //设置req
        SendMessageToWX.Req req=new SendMessageToWX.Req();
        req.message=mess;
        //发送
        api.sendReq(req);


    }
    /**
     * 方法必须重写
     */
    @Override
    public void onResume() {
        super.onResume();
        mapview.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onPause() {
        super.onPause();
        mapview.onPause();
        deactivate();
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapview.onSaveInstanceState(outState);
//        initMap(outState);
    }


    @Override
    public void onTouch(MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            /**
             */
            case MotionEvent.ACTION_MOVE:
                mDrawGeoSOT.clearGeoSOT(); // 清除GeoSOT
                mDrawGeoSOT.redrawGeoSOT(aMap, mWidth, mHeight); // 重绘GeoSOT
                break;
        }
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation){
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null && amapLocation.getErrorCode() == 0) {
                customProgressDialog.dismiss();
                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
                //TODO
                //当定位好了以后，拿到经纬度
                double lat = amapLocation.getLatitude();
                double lng = amapLocation.getLongitude();
                LatLng latlng = new LatLng(lat, lng);
                //移动到定位处
                CameraUpdate movecity = CameraUpdateFactory.newLatLngZoom(latlng, 15);
                aMap.moveCamera(movecity);
                aMap.setOnMapLongClickListener(new AMap.OnMapLongClickListener() {
                    @Override
                    public void onMapLongClick(LatLng latLng) {
                        LoadingUI(latLng);
                    }
                });

              /*  aMap.addMarker(new MarkerOptions().anchor(0.1f, 0.1f).
                        icon(BitmapDescriptorFactory.fromBitmap
                                (BitmapFactory.decodeResource
                                        (getResources(), R.mipmap.position))).position(latlng));*/
                deactivate();
            } else {
                if(customProgressDialog!=null){
                    customProgressDialog.dismiss();
                }

                String errText = "定位失败," + amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo();
                Log.i("TAG", errText);
            }
        }
        else{
            if(customProgressDialog!=null){
                customProgressDialog.dismiss();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(LiuYanFragment.this);
    }


    //Android6.0申请权限的回调方法
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            // requestCode即所声明的权限获取码，在checkSelfPermission时传入
            case BAIDU_READ_PHONE_STATE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 获取到权限，作相应处理（调用定位SDK应当确保相关权限均被授权，否则可能引起定位失败）
                    initMap();
                } else {
                    // 没有获取到权限，做特殊处理
                    Toast.makeText(getActivity(), "获取位置权限失败，请手动开启", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }



}
