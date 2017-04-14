package zph.zhjx.com.chat.ui;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import zph.zhjx.com.chat.R;
import zph.zhjx.com.chat.geosot.DrawGeoSOT3;

/**
 * @author adminZPH
 * 自定义地图Overlay
 * 解决经纬度点地图显示适配问题
 * */
public class OverlayTestActivity extends AppCompatActivity implements AMap.OnMapLoadedListener, AMap.OnMapTouchListener, AMap.OnCameraChangeListener {
    private final String TAG="OverlayTestActivity";
    private MapView mapview;
    private AMap aMap;
    public static final LatLng position_ZHENGZHOU = new LatLng(34.7461110548, 113.6589270503);
    private List<LatLng> list;


    private DrawGeoSOT3 mDrawGeoSOT; // 绘制GeoSOT
    private Display mDisplay;
    private int mWidth;
    private int mHeight;
    private int fillcolor;
    private int strokecolor;

    static {
        try {
            System.loadLibrary("geosot");
            Log.i("TAG", "加载成功...");
        } catch (UnsatisfiedLinkError e) {
            Log.i("TAG", "加载失败..."+e.getMessage());
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overlay_test);
        initmap(savedInstanceState);
    }
    /**
     * 随机生成30个数据,经纬度都在中国的经纬度范围内
     * */
    private void initDate() {
        list=new ArrayList<>();
        for (int i=0;i<30;i++) {
            int x = 73 + (int) (Math.random() * 62);
            int y = 15 + (int) (Math.random() * 38);
            int xx = 100000 + (int) (Math.random() * 100000);
            int yy = 100000 + (int) (Math.random() * 100000);
            list.add(new LatLng(Double.valueOf(x+"."+xx),Double.valueOf(y+"."+yy)));
        }
    }

    private void initmap(Bundle savedInstanceState) {
        mapview= (MapView) findViewById(R.id.overlay_map);
        mapview.onCreate(savedInstanceState);
        if (aMap == null) {
            aMap = mapview.getMap();
            mapview.setSelected(true);
            setMap();
        }
        aMap.setOnMapLoadedListener(this);
        aMap.setOnMapTouchListener(this);
        aMap.setOnCameraChangeListener(this);

    }

    private void setMap() {
        aMap.moveCamera(CameraUpdateFactory.zoomTo(19.0f));
        CameraUpdate movecity = CameraUpdateFactory.newLatLngZoom(position_ZHENGZHOU,19);
        aMap.moveCamera(movecity);
        aMap.addMarker(new MarkerOptions().
                icon(BitmapDescriptorFactory.fromBitmap(
                        BitmapFactory.decodeResource(getResources(), R.mipmap.position))).
                position(position_ZHENGZHOU));
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
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapview.onSaveInstanceState(outState);
    }


    /**
     * 显示GeoSOT网格
     */
    private void displayGeoSOT() {
        // aMap.invalidate(); //刷新地图
        mDisplay =getWindowManager().getDefaultDisplay();
        mWidth = mDisplay.getWidth();
        mHeight = mDisplay.getHeight();
        System.out.println("\n屏幕的像素密度为：" + mWidth + ", " + mHeight + "\n");
        fillcolor = Color.argb(10, 1, 1, 255);
        strokecolor = Color.argb(255, 100, 100, 80);
        mDrawGeoSOT = new DrawGeoSOT3(aMap, fillcolor, strokecolor,5,(int)aMap.getCameraPosition().zoom);
        mDrawGeoSOT.redrawGeoSOT(aMap, mWidth, mHeight);
    }


    @Override
    public void onTouch(MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            /**
             */
            case MotionEvent.ACTION_MOVE:


                        mDrawGeoSOT.clearGeoSOT(); // 清除GeoSOT
//                displayGeoSOT();
                        mDrawGeoSOT.redrawGeoSOT(aMap, mWidth, mHeight); // 重绘GeoSOT

                break;
        }
    }


    /**
     * 当地图加载完后
     * */
    @Override
    public void onMapLoaded() {

                displayGeoSOT() ;


      /* initDate();
        List<PoiItem> list1=new ArrayList<>();
        for (int i=0;i<30;++i) {
            LatLonPoint point=new LatLonPoint(list.get(i).latitude,list.get(i).longitude);
            PoiItem p=new PoiItem("1",point,null,null);
            list1.add(p);
        }
        MyPointOverlay myPointOverlay=new MyPointOverlay(aMap,list1);
        myPointOverlay.removeFromMap();
        myPointOverlay.addToMap();
        myPointOverlay.zoomToSpan();
        myPointOverlay.AddLineToAmap(false);*/
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        Log.d(TAG,"当前的地图层级"+aMap.getCameraPosition().zoom);
    }
}
