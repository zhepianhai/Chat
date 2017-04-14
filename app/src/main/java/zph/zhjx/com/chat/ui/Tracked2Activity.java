package zph.zhjx.com.chat.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import zph.zhjx.com.chat.R;
import zph.zhjx.com.chat.adapter.HorizontalScrollViewAdapter;
import zph.zhjx.com.chat.app.App;
import zph.zhjx.com.chat.base.BaseActivity;
import zph.zhjx.com.chat.bean.TrackedBean;
import zph.zhjx.com.chat.controller.GeoSOTController;
import zph.zhjx.com.chat.dao.People;
import zph.zhjx.com.chat.dao.User;
import zph.zhjx.com.chat.map.overlay.MyPointOverlay;
import zph.zhjx.com.chat.util.DBUtil;
import zph.zhjx.com.chat.util.TimeUtil;
import zph.zhjx.com.chat.view.LoadingDialog;
import zph.zhjx.com.chat.view.MyHorizontalScrollView;

public class Tracked2Activity extends BaseActivity implements AMap.OnMapLoadedListener, View.OnClickListener, LocationSource, AMapLocationListener {
    private final String TAG="Tracked2Activity";
    private MapView mapview;
    private View headview;
    private LinearLayout back;
    private TextView title;
    private AMap aMap;
    private MyHorizontalScrollView horizontalScrollView;
    private LinearLayout linearLayout;
    private List<People> date;
    private HorizontalScrollViewAdapter adapter;
    private final int BAIDU_READ_PHONE_STATE=1005;
    private String lat="",lng="";
    private OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private LatLng locallatlng;
    GeoSOTController geoSOTController;
    List<TrackedBean.DataBean.ListBean> listBeen;
    MyPointOverlay myPointOverlay;
    private LoadingDialog loadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracked2);
        initview();
        mapview.onCreate(savedInstanceState);
        geoSOTController=new GeoSOTController(this);
        EventBus.getDefault().register(this);
        if (Build.VERSION.SDK_INT>=23){
            showContacts();
        }else{
        /*    loadingDialog=new LoadingDialog(this,"加载中");
            loadingDialog.show();*/
            initMap();
        }

    }

    private void showContacts() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(),"没有权限,请手动开启定位权限",Toast.LENGTH_SHORT).show();
            // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义
            ActivityCompat.requestPermissions(Tracked2Activity.this,
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
        aMap.setOnMapLoadedListener(this);
        aMap.setLocationSource(this);// 设置定位监听
    }

    private void setBlueMap() {
        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        AMapOptions op=new AMapOptions();
        op.zoomControlsEnabled(false);//隐藏
        UiSettings uiSettings = aMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(false);
    }


    private void initview() {
        setstatusbarcolor();
        mapview= (MapView) findViewById(R.id.detail_tracked2_map);
        headview=findViewById(R.id.headview1);
        back= (LinearLayout) headview.findViewById(R.id.headview_left_layout);
        back.setOnClickListener(this);
        title= (TextView) headview.findViewById(R.id.headview_left_textview);
        title.setText("行为轨迹"+"");
        horizontalScrollView= (MyHorizontalScrollView) findViewById(R.id.tracked_scrollview);
    }

    @Override
    public void onMapLoaded() {


    }

    private void initDate() {
        if(horizontalScrollView.getVisibility()==View.INVISIBLE){
            horizontalScrollView.setVisibility(View.VISIBLE);
        }
        date= DBUtil.GetAllFriendsList();
        //添加自己
        People p=new People();
        User pp = DBUtil.getUserMessage();
        p.setBeizhu(pp.getUserNickname());
        p.setBitmap_base64(pp.getImageBase64());
        p.setImage(pp.getImageSrc());
        p.setUser_phone(pp.getPhone());
        date.add(0,p);
        adapter=new HorizontalScrollViewAdapter(this,date);
        horizontalScrollView.initDatas(adapter);
        //添加点击回调
        horizontalScrollView.setOnItemClickListener(new MyHorizontalScrollView.OnItemClickListener(){
            @Override
            public void onClick(View view, int position){
                Log.i("TAG","点击处："+date.get(position).getBeizhu());
                Intent intent=new Intent(Tracked2Activity.this,DetailTrackActivity.class);
                intent.putExtra("phone",date.get(position).getUser_phone());
                intent.putExtra("name",date.get(position).getBeizhu());
                intent.putExtra("lat",locallatlng.latitude);
                intent.putExtra("lng",locallatlng.longitude);
                startActivity(intent);
            }
        });
        Log.i("TAG","起始时间"+TimeUtil.getSystemThreeDayTime());
        Log.i("TAG","结束时间"+TimeUtil.getSystemTime());
        geoSOTController.SearchPeopleTracked(DBUtil.getUserMessage().getPhone(),TimeUtil.getSystemThreeDayTime(),TimeUtil.getSystemTime(),false,false);
    }
    //获取指定的人信息的订阅者 
    @Subscribe(threadMode= ThreadMode.MAIN)
    public void onDetailTrackedEventBus(TrackedBean trackedBean) {
        listBeen=trackedBean.getData().getList();
        if(listBeen.size()==0||listBeen==null){
            toast("暂无数据");
            Log.i(TAG,"获得的数据时空：");
        }
        else{

            List<PoiItem> list_me=new ArrayList<>();
            for (int i=0;i<listBeen.size();++i) {
                if(!listBeen.get(i).getLat().equals("0.0")) {
                    LatLonPoint point = new LatLonPoint(
                            Double.valueOf(listBeen.get(i).getLat()),
                            Double.valueOf(listBeen.get(i).getLng()));
                    PoiItem p = new PoiItem("1", point, null, null);
                    list_me.add(p);
                }
            }
            Log.i(TAG,"获得的数据:"+list_me.toString());

            myPointOverlay = new MyPointOverlay(aMap,list_me);
            myPointOverlay.removeFromMap();
            myPointOverlay.zoomToSpan();
            myPointOverlay.AddLineToAmap(true);
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.headview_left_layout:
                this.finish();
                break;

        }
    }

    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置多少秒去刷新位置
            mLocationOption.setInterval(10000*1000);
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
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        mapview.onDestroy();
    }
    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapview.onResume();
        if(myPointOverlay!=null){
            myPointOverlay.removeFromMap();
        }
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapview.onPause();
        deactivate();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapview.onSaveInstanceState(outState);
//        initMap(outState);
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null && amapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
                //TODO
                //当定位好了以后，拿到经纬度
                double latt = amapLocation.getLatitude();
                double lngg = amapLocation.getLongitude();
                LatLng latlng = new LatLng(latt, lngg);
                locallatlng=latlng;
                //移动到定位处
                DBUtil.UpdateZuJi(App.phone,true);
                //加载附近的人信息
                lat=String.valueOf(latt);
                lng=String.valueOf(lngg);
                CameraUpdate movecity = CameraUpdateFactory.newLatLngZoom(latlng, 15);
                aMap.moveCamera(movecity);
               /* aMap.addMarker(new MarkerOptions().anchor(0.1f, 0.1f).
                        icon(BitmapDescriptorFactory.fromBitmap
                                (BitmapFactory.decodeResource
                                        (getResources(), R.mipmap.position))).position(latlng));*/
                deactivate();
                initDate();

            } else {
                String errText = "定位失败," + amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo();
                Log.i("TAG", errText);
            }
        }
        else{}
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
                    Toast.makeText(getApplicationContext(), "获取位置权限失败，请手动开启", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }
}
