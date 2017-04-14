package zph.zhjx.com.chat.map.overlay;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.services.core.PoiItem;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import zph.zhjx.com.chat.R;

/**
 * Created by adminZPH on 2017/3/30.
 * 自定义的Overlay,目的是使所有的点，和连线能刚好的显示在当前地图屏幕区域中
 * 使其自动调整地图的图层层级
 * 点的个数是不确定
 * 如：假设共有100个Latlng,其中，50个点在郑州，20个点再北京，30个点在济南，
 * 当前的地图层级应自动调整为刚好显示出这100个点的最小层级，且视角移动到所有点的相对中心经纬度角度
 */

public class MyPointOverlay1 {
    private final String TAG="MyPointOverlay1";
    private List<PoiItem> a;
    private AMap b;
    private ArrayList<Marker> c = new ArrayList();
    private Context context;
    public MyPointOverlay1(Context context,AMap var1, List<PoiItem> var2) {
        this.b = var1;
        this.a = var2;
        this.context=context;
    }

    /**
     * 添加当前的点到地图上
     * */
    public void addToMap() {
        for(int var1 = 0; var1 < this.a.size(); ++var1) {
            Marker var2 = this.b.addMarker(this.a(var1));
            var2.setObject(Integer.valueOf(var1));
            this.c.add(var2);
        }

    }
    /**
     * 移除当前的地图上的mark
     * */
    public void removeFromMap() {
        Iterator var1 = this.c.iterator();

        while(var1.hasNext()) {
            Marker var2 = (Marker)var1.next();
            var2.remove();
        }

    }
    /**
     * 移动当前的地图层级，到合适的层级
     * */
    public void zoomToSpan() {
        if(this.a != null && this.a.size() > 0) {
            if(this.b == null) {
                return;
            }

            LatLngBounds var1 = this.a();
            this.b.moveCamera(CameraUpdateFactory.newLatLngBounds(var1,5));
        }

    }
    /**
     * 在地图上把添加的点进行连线
     * @param type ture代表的是画自己的轨迹，false代表的是画其它人的轨迹
     * */
    public void AddLineToAmap(boolean type){
        int color;
        if(type) {
            color=Color.parseColor("#d4020d");
        }
        else{
            color=Color.parseColor("#fbf800");
        }

        Log.i(TAG,"经纬度点的个数是:"+a.size());
        for (int i =0; i < a.size() - 1; ++i) {
            Log.i(TAG,"第"+i+"个点坐标是："+a.get(i).getLatLonPoint().getLatitude()+"--->"+a.get(i).getLatLonPoint().getLongitude());
            b.addPolyline(Getpolylineoptions().add(
                    new LatLng(a.get(i).getLatLonPoint().getLatitude(), a.get(i).getLatLonPoint().getLongitude()),
                    new LatLng(a.get(i + 1).getLatLonPoint().getLatitude(), a.get(i + 1).getLatLonPoint().getLongitude())
            ));

        }


        b.addMarker(new MarkerOptions().
                icon(BitmapDescriptorFactory.fromBitmap
                        (BitmapFactory.decodeResource
                                (context.getResources(), R.mipmap.point_start))).position(
                new LatLng(a.get(0).getLatLonPoint().getLatitude(), a.get(0).getLatLonPoint().getLongitude()
                )));

        b.addMarker(new MarkerOptions().
                icon(BitmapDescriptorFactory.fromBitmap
                        (BitmapFactory.decodeResource
                                (context.getResources(), R.mipmap.point_end))).position(
                new LatLng(a.get(a.size()-1).getLatLonPoint().getLatitude(), a.get(a.size()-1).getLatLonPoint().getLongitude()
                )));

    }
    /**
     * Created by adminZPH on 2017/4/14.
     * 设置线条中的纹理
     * @return PolylineOptions
     * */
    private PolylineOptions Getpolylineoptions(){
        //添加纹理图片
        List<BitmapDescriptor> textureList = new ArrayList<BitmapDescriptor>();
        BitmapDescriptor mRedTexture = BitmapDescriptorFactory
                .fromAsset("icon_road_red_arrow.png");
        BitmapDescriptor mBlueTexture = BitmapDescriptorFactory
                .fromAsset("icon_road_blue_arrow.png");
        BitmapDescriptor mGreenTexture = BitmapDescriptorFactory
                .fromAsset("icon_road_green_arrow.png");

        textureList.add(mBlueTexture);
        textureList.add(mRedTexture);
        textureList.add(mGreenTexture);
        // 添加纹理图片对应的顺序
        List<Integer> textureIndexs = new ArrayList<Integer>();
        textureIndexs.add(0);
        textureIndexs.add(1);
        textureIndexs.add(2);
        PolylineOptions polylienOptions=new PolylineOptions();
        polylienOptions.setCustomTextureList(textureList);
        polylienOptions.setCustomTextureIndex(textureIndexs);
        polylienOptions.setUseTexture(true);
        polylienOptions.width(10.0f);
        return polylienOptions;
    }

    private LatLngBounds a() {
        LatLngBounds.Builder var1 = LatLngBounds.builder();

        for(int var2 = 0; var2 < this.a.size(); ++var2) {
            var1.include(new LatLng(((PoiItem)this.a.get(var2)).getLatLonPoint().getLatitude(), ((PoiItem)this.a.get(var2)).getLatLonPoint().getLongitude()));
        }

        return var1.build();
    }

    private MarkerOptions a(int var1) {
        return (new MarkerOptions()).position(new LatLng(((PoiItem)this.a.get(var1)).getLatLonPoint().getLatitude(), ((PoiItem)this.a.get(var1)).getLatLonPoint().getLongitude())).title(this.getTitle(var1)).snippet(this.getSnippet(var1)).icon(this.getBitmapDescriptor(var1));
    }

    protected BitmapDescriptor getBitmapDescriptor(int var1) {
        return null;
    }

    protected String getTitle(int var1) {
        return ((PoiItem)this.a.get(var1)).getTitle();
    }

    protected String getSnippet(int var1) {
        return ((PoiItem)this.a.get(var1)).getSnippet();
    }

    public int getPoiIndex(Marker var1) {
        for(int var2 = 0; var2 < this.c.size(); ++var2) {
            if(((Marker)this.c.get(var2)).equals(var1)) {
                return var2;
            }
        }

        return -1;
    }

    public PoiItem getPoiItem(int var1) {
        return var1 >= 0 && var1 < this.a.size()?(PoiItem)this.a.get(var1):null;
    }
}
