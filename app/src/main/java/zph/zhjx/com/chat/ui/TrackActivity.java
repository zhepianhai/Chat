package zph.zhjx.com.chat.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps2d.AMapOptions;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import zph.zhjx.com.chat.R;
import zph.zhjx.com.chat.adapter.HorizontalScrollViewAdapter;
import zph.zhjx.com.chat.base.BaseActivity;
import zph.zhjx.com.chat.dao.People;
import zph.zhjx.com.chat.dao.User;
import zph.zhjx.com.chat.util.DBUtil;
import zph.zhjx.com.chat.util.FileUtil;
import zph.zhjx.com.chat.view.MyHorizontalScrollView;

public class TrackActivity extends BaseActivity implements  AMap.OnMapLoadedListener, AMap.OnMapTouchListener, LocationSource, AMapLocationListener, View.OnClickListener, AMap.OnCameraChangeListener {
    private View headview;
    private LinearLayout back;
    private TextView title;
    private MapView mapview;
    private AMap aMap;
    private OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private MyHorizontalScrollView horizontalScrollView;
    private LinearLayout linearLayout;
    private List<People> date;
    private HorizontalScrollViewAdapter adapter;
    private LatLng locallatlng;
    private boolean flag;
    private final int BAIDU_READ_PHONE_STATE=1005;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);
        setstatusbarcolor("#08304B");
        initview();
        mapview.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT>=23){
            showContacts();
        }else{
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
            ActivityCompat.requestPermissions(TrackActivity.this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE}, BAIDU_READ_PHONE_STATE);
        }else{
            initMap();
        }
    }

    /**
     * 初始化地图
     * */
    private void initMap() {

        if (aMap == null) {
            aMap = mapview.getMap();
            Log.i("TAG","11111");
            mapview.setSelected(true);
            setBlueMap();
        }
        aMap.setLocationSource(this);// 设置定位监听
        aMap.setOnMapLoadedListener(this);
        aMap.setOnMapTouchListener(this);
        aMap.setOnCameraChangeListener(this);
    }

    private void setBlueMap() {
        aMap.setLocationSource(this);// 设置定位监听
        InputStreamReader isr = null;
        AMapOptions op=new AMapOptions();
        op.zoomControlsEnabled(false);//隐藏
        UiSettings uiSettings = aMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(false);
        try {
            isr = new InputStreamReader(getAssets().open("style_json/style_json.json"),"UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedReader br = new BufferedReader(isr);
        String line;
        StringBuilder builder = new StringBuilder();
        try {
            while((line = br.readLine()) != null){
                builder.append(line);
            }

            br.close();
            isr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //写入到本地目录中
        FileUtil.writeToSDCardFile("style_json","style.json",builder.toString(),true);
        //加载个性化的地图底图数据
        aMap.setCustomMapStylePath(Environment.getExternalStorageDirectory()
                + File.separator+"style_json/style.json");
        //开启自定义样式
        aMap.setMapCustomEnable(true);
        aMap.getUiSettings().setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
    }

    private void initview() {
        mapview= (MapView) findViewById(R.id.tracked_map);
        headview=findViewById(R.id.headview1);
        back= (LinearLayout) headview.findViewById(R.id.headview_left_layout);
        back.setOnClickListener(this);
        title= (TextView) headview.findViewById(R.id.headview_left_textview);
        title.setText("行为轨迹"+"");
        horizontalScrollView= (MyHorizontalScrollView) findViewById(R.id.tracked_scrollview);
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
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
    public void onMapLoaded() {

    }

    @Override
    public void onTouch(MotionEvent motionEvent) {

    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null && amapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
                //TODO
                //当定位好了以后，拿到经纬度
                double lat = amapLocation.getLatitude();
                double lng = amapLocation.getLongitude();
                LatLng latlng = new LatLng(lat, lng);
                locallatlng=latlng;
                //定位成功的标示
                flag=true;
                //移动到定位处
                CameraUpdate movecity = CameraUpdateFactory.newLatLngZoom(latlng,4);
                aMap.moveCamera(movecity);
                deactivate();
                showListView();


            } else {
                String errText = "定位失败," + amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo();
                Log.i("TAG", errText);
            }
        }
        else{

        }
    }
    /**
     * 这个是用来展示联系人的列表的Listview
     * */
    private void showListView() {
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
                Intent intent=new Intent(TrackActivity.this,DetailTrackActivity.class);
                intent.putExtra("phone",date.get(position).getUser_phone());
                intent.putExtra("name",date.get(position).getBeizhu());
                intent.putExtra("lat",locallatlng.latitude);
                intent.putExtra("lng",locallatlng.longitude);
                startActivity(intent);
           }
        });
    }
    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapview.onResume();
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
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        initMap();
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
    public void onCameraChange(CameraPosition cameraPosition) {
        if(horizontalScrollView.getVisibility()==View.VISIBLE){
            horizontalScrollView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        if(horizontalScrollView.getVisibility()==View.INVISIBLE &&flag){
            horizontalScrollView.setVisibility(View.VISIBLE);
        }
        Log.i("TAG","当前地图的层级:"+aMap.getCameraPosition().zoom);
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
