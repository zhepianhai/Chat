package zph.zhjx.com.chat.geosot;

import android.graphics.Point;
import android.util.Log;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Polygon;
import com.amap.api.maps2d.model.PolygonOptions;
import com.watertek.geosot.GCode1D;
import com.watertek.geosot.GCode1DList;
import com.watertek.geosot.GCode1DNode;
import com.watertek.geosot.GeoRange;
import com.watertek.geosot.GeoSOT;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
 * Created by adminZPH on 2017/4/11.
 * 根据地图的层级的改变去动态改变网格的层级
 * 地图层级为3到19层级 层级越高，越详细
 * 网格刨分层级为1-32层级
 * 设定网格的刨分层级变化为15-18范围的变化
 * 这是对DrawGeoSOT1的改进，可以动态的更变网格的刨分层级
 **/



public class DrawGeoSOT3 {
    public static final String TAG="GeoSOT";
    public static final String TAGG="GeoSOTT";
    private LatLng mLeftUp; // 屏幕左上角（0 * 0）对应的地图的经纬度
    private LatLng mRightDown; // 屏幕右下角（1080 * 1920）对应的地图的经纬度
    private AMap mAMap;
    private GCode1DList mGCode1DList;
    private GCode1DNode mGCode1DNode;
    private GCode1D mGCode1D;
    private GeoRange mGeoRange;
    private Polygon mPolygon;
    private PolygonOptions mPolygonOptions;
    private List<Polygon> polygonListgon = new ArrayList<Polygon>();
    private int strokecolor;
    private int geosotwidth;
    private int fillcolor;
    private int map_layout;//当前地图的层级
    public DrawGeoSOT3(AMap mAMap,int fillcolor,int strokecolor,int geosotwidth,int map_layout) {
        this.mAMap=mAMap;
        this.fillcolor=fillcolor;
        this.strokecolor=strokecolor;
        this.geosotwidth=geosotwidth;
        this.map_layout=map_layout;
    }
    /*
     *绘制GeoSOT网格
     *@param leftUp 屏幕左上角对应的经纬度
     *@param rightDown 屏幕右下角对应的经纬度
     **/


    public void drawGeoSOT(LatLng leftUp,LatLng rightDown) {
		 /* 根据传入的屏幕的左上角和右下角的经纬度坐标值进行当前屏幕可视区域的剖分
		 * GeoSOT.getGCode1DOfRectangle() 计算矩阵的一维编码
		 * */
    Log.i("OverlayTestActivity","map_layout: "+map_layout);

    mGCode1DList= GeoSOT.getGCode1DOfRectangle(
                leftUp.longitude,leftUp.latitude,rightDown.longitude,rightDown.latitude,
            getMapZoomSize(map_layout));
    Log.i("OverlayTestActivity","刨分的地图层级是:"+getMapZoomSize(map_layout));
       //根据当前的地图层级去判断和得到对应的GeoSOT刨分显示的层级
       /* mGCode1DList= GeoSOT.getGCode1DOfRectangle(
                leftUp.longitude,leftUp.latitude,rightDown.longitude,rightDown.latitude,
                getMapZoomSize((int)mAMap.getCameraPosition().zoom));*/
    Log.i(TAGG,"传进去的值是:"+mAMap.getCameraPosition().zoom+2);
    Log.i("OverlayTestActivity","剖分出的矩形数量:"+mGCode1DList.getCount());

		 /*
		 * 得到根据当前屏幕可视区域的坐标值进行剖分后的所有编码节点
		 * */

        mGCode1DNode=mGCode1DList.getList();

		/* 循环遍历出所有的
		 **/

        for(int i=0;i<mGCode1DList.getCount();i++)
        {
            mPolygonOptions=new PolygonOptions();
            //得到当前矩阵的编码
            mGCode1D = mGCode1DNode.getCode();
            Log.i(TAG,"当前矩阵的一维编码是:"+mGCode1D.getCode());

            //将当前的编码，转换为地理范围：经纬度
            mGeoRange=GeoSOT.toGeoRangeFromGCode1D(mGCode1D);

            Log.i(TAG,"当前矩阵左上角的经纬度值:"+mGeoRange.getMaxLat()+", "+mGeoRange.getMinLon()+
                    "; 当前地图的层级:"+mGCode1D.getLayer());
            Log.i(TAG,"当前矩阵右下角的经纬度值:"+mGeoRange.getMinLat()+", "+mGeoRange.getMaxLon()+
                    "; 当前矩阵的层级:"+mGCode1D.getLayer());
			 /* 添加当前矩形 调用createRectangle()函数，将转换后的地理范围mGeoRange(经纬度)作为参数传进去
			 * 得到该矩形范围的四个角点的经纬度值，根据返回的经纬度值绘制矩形
			 * PolygonOptions.addAll(java.lang.Iterable<LatLng> paramOterable) 添加多个多变性边框的顶点
			 * PolygonOptions.fillColor()   设置多边形的填充颜色，32位ARGB格式
			 * PolygonOptions.strokeColor() 设置多边形的边框颜色，32位ARGB格式，默认为黑色
			 * PolygonOptions.strokeWidth() 设置多边形边框宽度，单位：像素
			 * */


            mPolygon=mAMap.addPolygon(mPolygonOptions.addAll(createRectangle(mGeoRange)).fillColor(fillcolor)
                    .strokeColor(strokecolor).strokeWidth(geosotwidth));

            //向容器中添加一个矩形实例
            polygonListgon.add(mPolygon);

            //每添加完一个矩形后需要清空，防止绘错
            mPolygonOptions=null;

            //得到下一个矩形
            mGCode1DNode=mGCode1DNode.getNext();
        }
        Log.i(TAGG, "层级是:"+mGCode1D.getLayer());


    }

    /*
     * 根据当前的地图的层级去进行判断和返回需要刨分的GeoSOT层级
     * GeoSOT刨分层级不能够太大和太小，应该控制在一定的范围内
     * 根据地图的层级变换，选择等级跨度进行刨分
     * */
    private int getMapZoomSize(int zoom) {
        Log.i("TAG","当前的地图层级是:"+zoom);
        if(zoom>=3 && zoom<=8)
            return 17;
        else if(zoom>8 && zoom<14)
            return 18;
        else
            return 19;
    }

    /*
     * 重绘GeoSOT网格
     *
     * @param width
     *            屏幕横轴像素数
     * @param height
     *            屏幕纵轴像素数
     */

    public void redrawGeoSOT(AMap aMap, int width, int height) {
        //先获取屏幕左上角的坐标，转换为当前地图的经纬度坐标
        mLeftUp=mAMap.getProjection().fromScreenLocation(new Point(0, 0));

        //获取屏幕右下角的坐标，转换为当前地图的经纬度坐标
        mRightDown=mAMap.getProjection().fromScreenLocation(new Point(width, height));
        mAMap=aMap;
        this.map_layout=(int)aMap.getCameraPosition().zoom+1;
        drawGeoSOT(mLeftUp, mRightDown);
    }
    /**
     * 生成一个矩形的四个坐标点
     * @return 矩形的四个角点
     */
    private List<LatLng> createRectangle(GeoRange geoRang){
        return Arrays.asList(
                new LatLng(geoRang.getMaxLat(),geoRang.getMinLon()),//左上角
                new LatLng(geoRang.getMaxLat(),geoRang.getMaxLon()),//右上角
                new LatLng(geoRang.getMinLat(),geoRang.getMaxLon()),//右下角
                new LatLng(geoRang.getMinLat(),geoRang.getMinLon()) //左下角
        );
    }

    /**
     * 关闭GeoSOT
     */
    public void clearGeoSOT(){
        Log.i(TAGG,"容器中的数量为: "+polygonListgon.size());
        for(int i=0;i<polygonListgon.size();++i){
            polygonListgon.get(i).remove();
            mAMap.invalidate();
        }
        polygonListgon.clear();
    }

}
