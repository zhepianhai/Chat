package zph.zhjx.com.chat.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.layer.BackgroundLayer;
import gov.nasa.worldwind.layer.BlueMarbleLandsatLayer;
import zph.zhjx.com.chat.R;
import zph.zhjx.com.chat.adapter.NearbyPeopleAdapter;
import zph.zhjx.com.chat.app.App;
import zph.zhjx.com.chat.base.BaseActivity;
import zph.zhjx.com.chat.bean.ClsLocal;
import zph.zhjx.com.chat.bean.NearBayPeopleListBean;
import zph.zhjx.com.chat.bean.NearbyListbean;
import zph.zhjx.com.chat.controller.GeoSOTController;
import zph.zhjx.com.chat.geosot.DrawGeoSOT1;
import zph.zhjx.com.chat.util.DBUtil;
import zph.zhjx.com.chat.util.PhoneUtil;
import zph.zhjx.com.chat.view.CustomProgressDialog;
import zph.zhjx.com.chat.view.PopupDismissListener;

import static zph.zhjx.com.chat.contact.Contact.markers;

public class NearbyPeopleActivity extends BaseActivity implements View.OnClickListener, AMap.OnMapLoadedListener,AMap.OnMapTouchListener, LocationSource, AMapLocationListener {
    private final String TAG="NearbyPeopleActivity";
    private View headview;
    private LinearLayout back;
    private TextView title;
    private ImageView sex,setting;
    private PopupDismissListener popupDismissListener;
    private FrameLayout frameLayout;
    private ListView listView;
    private boolean isSot=false;
    private String type="0";
    private GeoSOTController geoSOTController;
    private NearbyPeopleAdapter mNerNearbyPeopleAdapter;
    private String lat="",lng="";
    private List<NearbyListbean> list;
    private String sexy="all";
    private CustomProgressDialog customProgressDialog,customProgressDialog1;
    private MapView mapview;
    private AMap aMap;
    private DrawGeoSOT1 mDrawGeoSOT; // 绘制GeoSOT
    private Display mDisplay;
    private int mWidth;
    private int mHeight;
    private int fillcolor;
    private int strokecolor;
    private OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private final int BAIDU_READ_PHONE_STATE=1005;
    static {
        try {
            System.loadLibrary("geosot");
            String str=System.getProperty("java.library.path");
            Log.i("TAG", "加载成功...");
        } catch (UnsatisfiedLinkError e) {
            Log.i("TAG", "加载失败..."+e.getMessage());
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_people);
        String str=System.getProperty("java.library.path");
        Log.i("TAG", "加载地址."+str);
        initheadview();
        geoSOTController=new GeoSOTController(this);
        mapview.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
//        UpdateLocal();
        if (Build.VERSION.SDK_INT>=23){
            showContacts();
        }else{
            initMap();
        }

        setstatusbarcolor();
    }
    /**
     * 动态获取权限
     * */
    private void showContacts() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(),"没有权限,请手动开启定位权限",Toast.LENGTH_SHORT).show();
            // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义
            ActivityCompat.requestPermissions(NearbyPeopleActivity.this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE}, BAIDU_READ_PHONE_STATE);
        }else{
            initMap();
        }
    }

    private void initMap() {
        if(aMap==null) {
            aMap = mapview.getMap();
            mapview.setSelected(true);
            setBlueMap();
        }
        aMap.setLocationSource(this);// 设置定位监听
        aMap.setOnMapLoadedListener(this);
        aMap.setOnMapTouchListener(this);

    }
    //加载附近的人信息
    private void LoadNearByPeople() {
        customProgressDialog1=new CustomProgressDialog(this,"正在获取附近的人信息...",0);
        customProgressDialog1.show();
        Log.i(TAG,"---->");
        geoSOTController.loadNearByPeople(lat,lng,sexy);
    }
    //附近的人信息的订阅者 
    @Subscribe(threadMode= ThreadMode.MAIN)
    public void onNearbyPeopleEventBus(NearBayPeopleListBean nearBayPeopleListBean) {
        customProgressDialog1.dismiss();
        if(nearBayPeopleListBean.getCode()==-1){
            toast("服务器连接失败...");
        }
        else if (nearBayPeopleListBean.getCode()==0){
            list=new ArrayList<>();
            Log.i(TAG,"信息是AAA："+nearBayPeopleListBean.getData().getList().toString());
            for(int i=0;i<nearBayPeopleListBean.getData().getList().size();++i){
                NearbyListbean b=new NearbyListbean();
                b.setPhone(nearBayPeopleListBean.getData().getList().get(i).getPhone());
                b.setDistance(nearBayPeopleListBean.getData().getList().get(i).getDistance());
                b.setGender(nearBayPeopleListBean.getData().getList().get(i).getGender());
                b.setName(nearBayPeopleListBean.getData().getList().get(i).getName());
                b.setNickName(nearBayPeopleListBean.getData().getList().get(i).getNickName());
                b.setLat(nearBayPeopleListBean.getData().getList().get(i).getLat());
                b.setLng(nearBayPeopleListBean.getData().getList().get(i).getLng());
                b.setImageSrc(nearBayPeopleListBean.getData().getList().get(i).getPhoto());
                list.add(b);
            }
            Log.i(TAG,"信息是："+list.toString());

            if(list!=null&& list.get(0).getPhone()!=null &&!TextUtils.isEmpty(list.get(0).getPhone())) {
                //对list进行排序
           /*   list.sort(new Comparator<NearbyListbean>() {
                    @Override
                    public int compare(NearbyListbean t, NearbyListbean t1) {
                        return Integer.valueOf(t.getDistance()).compareTo(Integer.valueOf(t1.getDistance()));
                    }
                });*/
                mNerNearbyPeopleAdapter = new NearbyPeopleAdapter(list, this);
                listView.setAdapter(mNerNearbyPeopleAdapter);
                //在地图上添加信息点
                AddPointInMap();
            }
        }
        else{
            toast(nearBayPeopleListBean.getMessage());
        }
    }

    /**
     * 在地图上添加信息点
     * */
    private void AddPointInMap() {
        aMap.clear();
        aMap.addMarker(new MarkerOptions().anchor(0.1f, 0.1f).
                icon(BitmapDescriptorFactory.fromBitmap
                        (BitmapFactory.decodeResource
                                (getResources(),R.mipmap.position))).position(new LatLng(Double.valueOf(lat),Double.valueOf(lng))));
        for(int i=0;i<list.size();++i){
            LatLng latlngNew=new LatLng(Double.valueOf(list.get(i).getLat()),Double.valueOf(list.get(i).getLng()));
            if(i<10)
            aMap.addMarker(new MarkerOptions().anchor(0.1f, 0.1f).
                    icon(BitmapDescriptorFactory.fromBitmap
                            (BitmapFactory.decodeResource
                                    (getResources(),markers[i]))).position(latlngNew));
            else{
                aMap.addMarker(new MarkerOptions().anchor(0.1f, 0.1f).title(list.get(i).getNickName()).
                        icon(BitmapDescriptorFactory.fromBitmap
                                (BitmapFactory.decodeResource
                                        (getResources(), R.mipmap.poi_marker_pressed))).position(latlngNew));
            }
        }
        aMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                marker.showInfoWindow();

                return true;
            }
        });
    }

    //清除附近的人信息的订阅者 
    @Subscribe(threadMode= ThreadMode.MAIN)
    public void onClearPeopleEventBus(ClsLocal localcls) {
        if(customProgressDialog!=null){
            customProgressDialog.dismiss();
        }
        if (localcls.getCode() == -1) {
            toast("服务器连接失败...");
        }
        else if (localcls.getCode() == 0) {
            //说明个人位置上传成功
            Log.i(TAG,"个人位置上传成功");
            //更新本地数据
            DBUtil.UpdateZuJi(App.phone,false);
            this.finish();
        }
    }
    private void initheadview() {
        headview=findViewById(R.id.headview1);
        back= (LinearLayout) headview.findViewById(R.id.headview_left_layout);
        sex= (ImageView) headview.findViewById(R.id.headview_center);
        setting= (ImageView) headview.findViewById(R.id.headview_right);
        frameLayout= (FrameLayout) findViewById(R.id.globe);
        listView= (ListView) findViewById(R.id.nearby_listview);
        back.setOnClickListener(this);
        setting.setOnClickListener(this);
        title= (TextView) headview.findViewById(R.id.headview_left_textview);
        title.setText("附近的人"+"");
        mapview= (MapView) findViewById(R.id.fujin_map);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.headview_left_layout:
                this.finish();
                break;
            case R.id.headview_right:
                popupDismissListener=new PopupDismissListener(this,itemsOnClick);
                popupDismissListener.
                        showAtLocation(this.findViewById(R.id.headview1),
                                Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
                PhoneUtil.setBackgroundAlpha(this,0.5f);
                break;
        }

    }
    /**
     * 显示GeoSOT网格
     */
    private void displayGeoSOT() {
        // aMap.invalidate(); //刷新地图
        mDisplay = getWindowManager().getDefaultDisplay();
        mWidth = mDisplay.getWidth();
        mHeight = mDisplay.getHeight();
        System.out.println("\n屏幕的像素密度为：" + mWidth + ", " + mHeight + "\n");
        fillcolor = Color.argb(10, 1, 1, 255);
        strokecolor = Color.argb(255, 100, 100, 80);
        mDrawGeoSOT = new DrawGeoSOT1(aMap, fillcolor, strokecolor,5);
        mDrawGeoSOT.redrawGeoSOT(aMap, mWidth, mHeight);
    }


    //为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick = new View.OnClickListener(){

        public void onClick(View v) {
            popupDismissListener.dismiss();
            switch (v.getId()) {
                case R.id.buttom_nearby_nv:
                    if(type!="0"){
                        frameLayout.removeAllViews();
                        mapview.setVisibility(View.INVISIBLE);
                        listView.setVisibility(View.VISIBLE);
                        frameLayout.addView(listView);
                        type="0";
                    }
                    sexy="f";
                    LoadNearByPeople();
                    break;
                case R.id.buttom_nearby_nan:
                    if(type!="0"){
                        frameLayout.removeAllViews();
                        mapview.setVisibility(View.INVISIBLE);
                        listView.setVisibility(View.VISIBLE);
                        frameLayout.addView(listView);
                        type="0";
                    }
                    sexy="m";
                    LoadNearByPeople();
                    break;
                case R.id.buttom_nearby_all:
                    if(type!="0"){
                        frameLayout.removeAllViews();
                        mapview.setVisibility(View.INVISIBLE);
                        listView.setVisibility(View.VISIBLE);
                        frameLayout.addView(listView);
                        type="0";
                    }
                    sexy="all";
                    LoadNearByPeople();
                    break;
                case R.id.buttom_nearby_gaode:
                    //type=1;
                    if(type!="1"){
                        frameLayout.removeAllViews();
                        mapview.setVisibility(View.VISIBLE);
                        listView.setVisibility(View.INVISIBLE);
                        frameLayout.addView(mapview);
                        //geoSOT网格的绘制
//                        mDrawGeoSOT.redrawGeoSOT(aMap, mWidth, mHeight);
                        displayGeoSOT();
                        type="1";
                    }
                    break;
                case R.id.buttom_nearby_list:
                    if(type!="0"){
                        frameLayout.removeAllViews();
                        mapview.setVisibility(View.INVISIBLE);
                        listView.setVisibility(View.VISIBLE);
                        frameLayout.addView(listView);
                        geoSOTController.loadNearByPeople(lat,lng,sexy);
                        type="0";
                    }
                    break;
                case R.id.buttom_nearby_sot:
                    if(type!="2") {
                        frameLayout.removeAllViews();
                        WorldWindow wwd = new WorldWindow(getApplicationContext());
                        wwd.getLayers().addLayer(new BackgroundLayer());
                        wwd.getLayers().addLayer(new BlueMarbleLandsatLayer());
                        frameLayout.addView(wwd);
                        type="2";
                    }

                    break;
                case R.id.buttom_nearby_cls:
                    geoSOTController.ClearNearbyMessage();
                    break;
                default:
                    break;
            }
        }

    };

    private void setBlueMap() {
        customProgressDialog=new CustomProgressDialog(this,"正在获取你的位置信息...",0);
        customProgressDialog.show();
        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        mapview.onDestroy();
    }

    @Override
    public void onMapLoaded() {

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
    public void onLocationChanged(AMapLocation amapLocation){

        if (mListener != null && amapLocation != null) {
            customProgressDialog.dismiss();
            if (amapLocation != null && amapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
                //TODO
                //当定位好了以后，拿到经纬度
                double latt = amapLocation.getLatitude();
                double lngg = amapLocation.getLongitude();
                Log.i(TAG, "lat:" + lat + "lng:" + lng);
                LatLng latlng = new LatLng(latt, lngg);
                //移动到定位处
                DBUtil.UpdateZuJi(App.phone,true);
                //加载附近的人信息
                lat=String.valueOf(latt);
                lng=String.valueOf(lngg);
                CameraUpdate movecity = CameraUpdateFactory.newLatLngZoom(latlng, 15);
                aMap.moveCamera(movecity);
                aMap.addMarker(new MarkerOptions().anchor(0.1f, 0.1f).
                        icon(BitmapDescriptorFactory.fromBitmap
                                (BitmapFactory.decodeResource
                                        (getResources(), R.mipmap.position))).position(latlng));
                deactivate();
                LoadNearByPeople();

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


