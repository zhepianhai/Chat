package zph.zhjx.com.chat.fragment;


import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
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
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import zph.zhjx.com.chat.R;
import zph.zhjx.com.chat.bean.LiuYanList;
import zph.zhjx.com.chat.controller.GeoSOTController;
import zph.zhjx.com.chat.geosot.DrawGeoSOT2;
import zph.zhjx.com.chat.view.CustomProgressDialog;

import static zph.zhjx.com.chat.contact.Contact.markers;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchLiuYanFragment extends Fragment implements  LocationSource, AMap.OnMapLoadedListener, AMap.OnMapTouchListener, AMapLocationListener, AMap.OnCameraChangeListener, AMap.OnMapLongClickListener, AMap.OnMapClickListener {
    private final String TAG="SearchLiuYanFragment";
    private View view;
    private MapView mapview;
    private AMap aMap;
    private DrawGeoSOT2 mDrawGeoSOT; // 绘制GeoSOT
    private Display mDisplay;
    private int mWidth;
    private int mHeight;
    private LatLng check_latlng;
    private int geosot_layout=0;
    private CustomProgressDialog customProgressDialog,customProgressDialog1;
    private LocationSource.OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private GeoSOTController geoSOTController;
    private ScrollView scrollView;
    private TextView name,time,content;
    private List<LatLng> search_latlng_list;
    private List<LiuYanList.DataBean.ListBean> listbean;
    private final int BAIDU_READ_PHONE_STATE=1005;
    public SearchLiuYanFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_search_liu_yan, container, false);
        initview();

        mapview.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT>=23){
            showContacts();
        }else{
            initMap();
        }
        geoSOTController=new GeoSOTController(getActivity());
        EventBus.getDefault().register(SearchLiuYanFragment.this);
        Toast.makeText(getActivity(),"长按网格区域可进行查询",Toast.LENGTH_LONG).show();
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

    private void initview() {
        mapview= (MapView) view.findViewById(R.id.search_liuyan_map);
        scrollView= (ScrollView) view.findViewById(R.id.search_liuyan_srcollview);
        name= (TextView) view.findViewById(R.id.search_liuyan_name);
        time= (TextView) view.findViewById(R.id.search_liuyan_time);
        content= (TextView) view.findViewById(R.id.search_liuyan_content);
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
        aMap.setOnCameraChangeListener(this);
        aMap.setOnMapLongClickListener(this);
        aMap.setOnMapClickListener(this);

        customProgressDialog=new CustomProgressDialog(getActivity(),"正在获取你的位置信息...",0);
        customProgressDialog.show();
    }

    private void setBlueMap() {
        aMap.setLocationSource(this);// 设置定位监听
        AMapOptions op=new AMapOptions();
        op.zoomControlsEnabled(false);//隐藏
        UiSettings uiSettings = aMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(false);
       /* UiSettings uiSettings = aMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(false);*/
        aMap.getUiSettings().setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
    }


    @Override
    public void onMapLoaded() {

    }
    /**
     * 长按事件的对应功能：
     * @author adminZPH
     * 长按后，根据所在的position的latlng，选中该点所在的矩形区域，然后显示搜索相关的layout
     * */
    @Override
    public void onMapLongClick(LatLng latlng) {
        Log.i(TAG,"onMapLongClick");
        //1.先赋值，防止屏幕移动的时候会把该区域给清除
        check_latlng=latlng;
        //TODO 2.显示搜索相关的layout
        //3.清除GeoSOT网格
        mDrawGeoSOT.clearGeoSOT();
        //4.将点所在矩形选中
        mDrawGeoSOT.redrawGeoSOT(aMap, mWidth, mHeight, latlng);

        //5.将该区域移动到屏幕的中心位置，其中心点是latlng所在区域的矩形左上和右下的中心
/*
        CameraUpdate camera=CameraUpdateFactory.newLatLngZoom(DrawGeoSOT2.centerLatlng,15);
        aMap.moveCamera(camera);*/
        //搜索网格中的留言数据
        SearchGeoSOTLiuYanMessage(aMap,latlng);


    }
    /**
     * 搜索网格中的留言数据
     * */
    private void SearchGeoSOTLiuYanMessage(AMap aMap,LatLng latlng) {
        geoSOTController.searchGeoSOTLiuYanMessage(aMap,latlng);
    }
    //获取搜索结果的订阅者 
    @Subscribe(threadMode= ThreadMode.MAIN)
    public void onSubmitLiuYanEventBus(LiuYanList message) {
        if(message.getCode()==-1){
            Toast.makeText(getActivity(),"服务器异常",Toast.LENGTH_SHORT).show();
        }
        else{
//            aMap.clear();
            Log.i("LiuYan","获取的结果是："+message.getData().getList().toString());
            if(message.getData().getList().size()>0){
                search_latlng_list=new ArrayList<>();
                listbean=message.getData().getList();
                for(int i=0;i<message.getData().getList().size();++i){
                    search_latlng_list.add(new LatLng(Double.valueOf(message.getData().getList().get(i).getLat()),Double.valueOf(message.getData().getList().get(i).getLng())));
                }
                AddMarks();
            }
            else{
                Toast.makeText(getActivity(),"暂无该网格中的留言信息",Toast.LENGTH_SHORT).show();
            }
        }
    }
    /**
     * 在地图上添加搜索到的marks
     * */
    private void AddMarks() {
        if(search_latlng_list==null){
            return;
        }
        for (int i=0;i<search_latlng_list.size();++i){
            if(i<10)
                aMap.addMarker(new MarkerOptions().anchor(0.1f, 0.1f).title(listbean.get(i).getNickName()).snippet(listbean.get(i).getContent()).
                        icon(BitmapDescriptorFactory.fromBitmap
                                (BitmapFactory.decodeResource
                                        (getResources(),markers[i]))).position(search_latlng_list.get(i)));
            else{
                aMap.addMarker(new MarkerOptions().anchor(0.1f, 0.1f).title(listbean.get(i).getContent()).
                        icon(BitmapDescriptorFactory.fromBitmap
                                (BitmapFactory.decodeResource
                                        (getResources(), R.mipmap.poi_marker_pressed))).position(search_latlng_list.get(i)));
            }
        }
        aMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Log.i("TAG","fdas");
                setMarkLayoutVisible(true);
                name.setText(marker.getTitle()+"");
                content.setText(marker.getSnippet()+"");
                return true;
            }
        });
    }

    /**
     * 设置mark点击的可见性
     * */
    public void setMarkLayoutVisible(boolean flag){
        if(flag)
            scrollView.setVisibility(View.VISIBLE);
        else{
            scrollView.setVisibility(View.INVISIBLE);
        }

    }

    /**
     * 显示GeoSOT网格
     */
    private void displayGeoSOT() {
        Log.i(TAG,"displayGeosot");
        aMap.invalidate(); //刷新地图
        mDisplay = getActivity().getWindowManager().getDefaultDisplay();
        mWidth = mDisplay.getWidth();
        mHeight = mDisplay.getHeight();
        Log.i("TBQ","屏幕的像素密度为：" + mWidth + ", " + mHeight);
        mDrawGeoSOT = new DrawGeoSOT2(aMap,geosot_layout);
        mDrawGeoSOT.redrawGeoSOT(aMap, mWidth, mHeight);
        aMap.invalidate();
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
                Log.i(TAG,"onTouch");
                mDrawGeoSOT.clearGeoSOT(); // 清除GeoSOT
                aMap.invalidate();
                mDrawGeoSOT.redrawGeoSOT(aMap, mWidth, mHeight,check_latlng); // 重绘GeoSOT
                AddMarks();
                break;
        }
    }
    @Override
    public void onCameraChange(CameraPosition arg0) {
        Log.i(TAG,"onCameraChange");
        mDrawGeoSOT.clearGeoSOT(); // 清除GeoSOT
        setMarkLayoutVisible(false);
    }
    @Override
    public void onCameraChangeFinish(CameraPosition arg0) {
        Log.i(TAG,"onCameraChangeFinish");
        //TODO  这里有点问题
        mDrawGeoSOT.clearGeoSOT(); // 清除GeoSOT
        aMap.clear();
        aMap.invalidate();
        mDisplay = getActivity().getWindowManager().getDefaultDisplay();
        mWidth = mDisplay.getWidth();
        mHeight = mDisplay.getHeight();
        mDrawGeoSOT.redrawGeoSOT(aMap, mWidth, mHeight,check_latlng);
        AddMarks();
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
                displayGeoSOT();

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
        EventBus.getDefault().unregister(SearchLiuYanFragment.this);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        Log.i(TAG,"onMapClick");
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
