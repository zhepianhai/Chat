package zph.zhjx.com.chat.geosot;

import android.graphics.Color;
import android.graphics.Point;
import android.util.Log;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Polygon;
import com.amap.api.maps.model.PolygonOptions;
import com.watertek.geosot.GCode1D;
import com.watertek.geosot.GCode1DList;
import com.watertek.geosot.GCode1DNode;
import com.watertek.geosot.GeoRange;
import com.watertek.geosot.GeoSOT;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author adminZPH
 * 网格选中的主要功能
 * */
public class DrawGeoSOT2_amap {
	private final String TAG="DrawGeoSOT2_amap";
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


	private int fillcolor = Color.argb(1, 1, 1, 255);
	private int fillcolor1 = Color.argb(100, 1, 1, 255);
	private int strokecolor = Color.argb(255, 0, 0, 0);
	private int geosotwidth = 4;
	private int geosotlayout;

	public static LatLng centerLatlng;//选中矩形的中心点
	public static LatLng leftDownlatlng;	//选中矩形的左下角的latlng
	public static LatLng rightUplatlng;//选中矩形的右上角的latlng


	private int maplayout_zoom;

	public DrawGeoSOT2_amap(AMap aMap, int geosotlayout) {
		mAMap = aMap;
		this.geosotlayout = geosotlayout;
		this.maplayout_zoom=(int )aMap.getCameraPosition().zoom;
	}

	/**
	 * 绘制GeoSOT网格
	 * 
	 * @param leftUp
	 *            对应屏幕左上角的经纬度坐标
	 * @param rightDown
	 *            对应屏幕右上角的经纬度坐标
	 */
	public void drawGeoSOT(LatLng leftUp, LatLng rightDown,LatLng point) {

		/**
		 * 根据传入的屏幕的左上角与右下角的经纬度坐标值进行当前屏幕可视区域的剖分
		 * GeoSOT.getGCode1DOfRectangle()计算矩形的一维编码
		 */
		//这个是以当前的地图层级作为GeoSOT的刨分层级的，就是在放大和缩小的时候会出现之前的地图无法从节点列表中移除
		//获取当前的地图刨分层级：(int) mAMap.getCameraPosition().zoom
		Log.i(TAG,"当前的地图层级是:"+maplayout_zoom);
		Log.i(TAG,"当前的地图刨分层级是:"+getMapZoomSize(maplayout_zoom));

		mGCode1DList = GeoSOT.getGCode1DOfRectangle(leftUp.longitude, leftUp.latitude, rightDown.longitude,
				rightDown.latitude,maplayout_zoom+2);
		//这个是固定的GeoSOT刨分层级，以16层为例，越大越密集
		/*mGCode1DList = GeoSOT.getGCode1DOfRectangle(leftUp.longitude, leftUp.latitude, rightDown.longitude,
				rightDown.latitude, 16);*/
		//经过试验测试，上面的说明好像是反了,，第一个层级不会变，而第二个测越缩小层级越多，最终会直接卡死


		//其中：ZOOM的输出范围是:3.0---19.0,即：mAMap.getCameraPosition().zoom
		Log.i("ZOOM",":"+mAMap.getCameraPosition().zoom);
		Log.i(TAG, "\n剖分出的编码(矩形)数量为：" + mGCode1DList.getCount() + "\n");
		
		/**
		 * 得到根据当前屏幕可视区域的坐标值进行剖分后的所有编码节点
		 */
		mGCode1DNode = mGCode1DList.getList();

		/**
		 * 循环遍历所有
		 */
		for (int i = 0; i < mGCode1DList.getCount(); i++) {
			mPolygonOptions = new PolygonOptions();

			mGCode1D = mGCode1DNode.getCode(); // 得到当前矩形的编码

			Log.i("TBQ1", "\n当前矩形的一维编码：" + mGCode1D.getCode() + "\n");

			mGeoRange = GeoSOT.toGeoRangeFromGCode1D(mGCode1D); // 将当前矩形的一维编码转换为地理范围（经纬度）

			Log.i("TBQ2", "\n当前矩形左上角的经纬度值：" + mGeoRange.getMaxLat() + ", " + mGeoRange.getMinLon() + "; 当前地图的层级："
					+ mGCode1D.getLayer() + "\n");

			Log.i("TBQ3", "\n当前矩形右下角的经纬度值：" + mGeoRange.getMinLat() + ", " + mGeoRange.getMaxLon() + ", 当前地图的层级："
					+ mGCode1D.getLayer() + "\n");

			/**
			 * 添加当前矩形 调用createRectangle()函数，将转换后的地理范围 mGeoRange（经纬度）作为参数传进去
			 * 得到该矩形范围的四个角点的经纬度值，根据返回的经纬度值绘制矩形
			 * PolygonOptions.addAll(java.lang.Iterable
			 * <LatLng> paramIterable)添加多个多边形边框的顶点
			 * PolygonOptions.fillColor()设置多边形的填充颜色，32位ARGB格式。
			 * PolygonOptions.strokeColor()设置多边形边框颜色，32位 ARGB格式，默认为黑色
			 * PolygonOptions.strokeWidth()设置多边形边框宽度，单位：像素
			 */

			mPolygon = mAMap.addPolygon(mPolygonOptions.addAll(createRectangle(mGeoRange)).fillColor(fillcolor)
					.strokeColor(strokecolor).strokeWidth(geosotwidth));
			//找到当前选中的编码网格区域
			if (point!=null && WhatIsThePointRectangle(point, mGeoRange)) {
				Log.i("HHH", "aaaa");
				mPolygon.setStrokeColor(strokecolor);
				mPolygon.setFillColor(fillcolor1);
				mPolygon.setStrokeWidth(geosotwidth);
				//找到中心点，然后赋值给   centerlatlng
				FindCenterFromLatLngInRectangle(mGeoRange);
			} 
			else
				mPolygon = mAMap.addPolygon(mPolygonOptions.addAll(createRectangle(mGeoRange)).fillColor(fillcolor)
						.strokeColor(strokecolor).strokeWidth(geosotwidth));

			polygonListgon.add(mPolygon); // 向容器中添加一个矩形实例
			mPolygonOptions = null; // 每次添加完一个矩形后需要清空，防止绘错。
			// mPolygon.setVisible(true);//可见
			mGCode1DNode = mGCode1DNode.getNext(); // 得到下一个矩形
		}
	}

	/**
	 * 重绘GeoSOT网格
	 * 
	 * @param width
	 *            屏幕横轴像素数
	 * @param height
	 *            屏幕纵轴像素数
	 */
	public void redrawGeoSOT(AMap aMap, int width, int height) {

		// 获取屏幕左上角坐标，转换成当前地图的经纬度坐标
		mLeftUp = mAMap.getProjection().fromScreenLocation(new Point((int) 0, (int) 0));
		// 获取屏幕右下角坐标，转换成当前地图的经纬度坐标
		mRightDown = mAMap.getProjection().fromScreenLocation(new Point((int) width, (int) height));
		mAMap = aMap;
		drawGeoSOT(mLeftUp, mRightDown,null);
	}
	/**
	 * 重绘GeoSOT网格
	 @param width
	 *            屏幕横轴像素数
	 * @param height
	 *            屏幕纵轴像素数
	 * @param latlng 
	 * 			点击的点
	 * 
	 * */
	public void redrawGeoSOT(AMap aMap, int width, int height,LatLng latlng) {
		
		// 获取屏幕左上角坐标，转换成当前地图的经纬度坐标
		mLeftUp = mAMap.getProjection().fromScreenLocation(new Point((int) 0, (int) 0));
		// 获取屏幕右下角坐标，转换成当前地图的经纬度坐标
		mRightDown = mAMap.getProjection().fromScreenLocation(new Point((int) width, (int) height));
		mAMap = aMap;
		this.maplayout_zoom=(int)aMap.getCameraPosition().zoom;
		clearGeoSOT();
		drawGeoSOT(mLeftUp, mRightDown,latlng);
	}
	/**
	 * 关闭GeoSOT
	 */
	public void clearGeoSOT() {
		if(polygonListgon!=null) {
			Log.i("TBQ", "容器中的数量为：" + polygonListgon.size());
			for (int i = 0; i < polygonListgon.size(); i++) {
				polygonListgon.get(i).remove();
				mAMap.removecache();
			}
			polygonListgon.clear();
		}
	}

	/**
	 * 生成一个矩形的四个坐标点
	 * 
	 * @param geoRange
	 *            矩形范围
	 * @return 矩形的四个角点
	 */
	private List<LatLng> createRectangle(GeoRange geoRange) {
		return Arrays.asList(new LatLng(geoRange.getMaxLat(), geoRange.getMinLon()), // 左上角
				new LatLng(geoRange.getMaxLat(), geoRange.getMaxLon()), // 右上角
				new LatLng(geoRange.getMinLat(), geoRange.getMaxLon()), // 右下角
				new LatLng(geoRange.getMinLat(), geoRange.getMinLon())); // 左下角
	}

	/**
	 * 用来判断当前矩形中是否包含定位的位置所在的经纬度的点
	 * @author adminZPH
	 * @param latlng
	 *            当前点的经纬度信息
	 * @param mGeoRange
	 *            当前屏幕的矩形实例
	 * @return true false
	 */
	public boolean WhatIsThePointRectangle(LatLng latlng, GeoRange mGeoRange) {
		if (latlng.latitude <= mGeoRange.getMaxLat() && latlng.longitude >= mGeoRange.getMinLon()
				&& latlng.latitude >= mGeoRange.getMinLat() && latlng.longitude <= mGeoRange.getMaxLon()) {
			return true;
		}
		return false;
	}
	/**
	 * 用来查询该latlng点对应得矩形中的中心点,和左上右下点的经纬度，为接下来的搜索做赋值操作
	 * 即：左上和右下角和他俩的连线的中点
	 * @param mGeoRange2 该矩形
	 * */
	public static void FindCenterFromLatLngInRectangle(GeoRange mGeoRange2){
		leftDownlatlng=new LatLng(mGeoRange2.getMinLat(), mGeoRange2.getMinLon());
		rightUplatlng=new LatLng(mGeoRange2.getMaxLat(), mGeoRange2.getMaxLon());
		centerLatlng=new LatLng((mGeoRange2.getMaxLat()+mGeoRange2.getMinLat())/2, 
				(mGeoRange2.getMinLon()+mGeoRange2.getMaxLon())/2
				);	
	}

	/**
	 * 用来返回当前选中编码中的一块矩形的对角线对应的顶点经纬度
	 *
	 * */
	public static List<LatLng> GetCheckGeoSOTLatLngList(){
//		leftDownlatlng;	//选中矩形的左下角的latlng
//		rightUplatlng;//选中矩形的右上角的latlng
		//根据左下角和右上角的经度分析得出左上角和右下角的经纬度
		List<LatLng> list=new LinkedList<>();//保证有序返回
		LatLng leftUpLatlng=new LatLng(rightUplatlng.latitude,leftDownlatlng.longitude);
		LatLng rightDownLalng=new LatLng(leftDownlatlng.latitude,rightUplatlng.longitude);
		//添加顺序为顺时针添加：左上，右上，右下，左下
		Log.i("latlnglist","左下和右上经纬度"+leftDownlatlng.toString()+"...."+rightUplatlng.toString());
		list.add(leftUpLatlng);
		list.add(rightUplatlng);
		list.add(rightDownLalng);
		list.add(leftDownlatlng);
		Log.i("latlnglist","四点的经纬度："+list.toString());
		return  list;
	}



	/*
   * 根据当前的地图的层级去进行判断和返回需要刨分的GeoSOT层级
   * GeoSOT刨分层级不能够太大和太小，应该控制在一定的范围内
   * 根据地图的层级变换，选择等级跨度进行刨分
   * 如果是返回18的话，根据屏幕经纬度和尺寸会刨分2000多个，所以要降低
   * 返回15的话，会刨分出56个，是可以的
   * */
	private int getMapZoomSize(int zoom) {
		Log.i("TAG","当前的地图层级是:"+zoom);
		if(zoom>=3 && zoom<=8)
			return 14;
		else if(zoom>8 && zoom<14)
			return 15;
		else
			return 16;
	}


}
