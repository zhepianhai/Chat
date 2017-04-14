package zph.zhjx.com.chat.controller;

import android.content.Context;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.model.LatLng;

import zph.zhjx.com.chat.model.GeoSOTModel;

/**
 * Created by adminZPH on 2017/3/10.
 */

public class GeoSOTController {
    private Context context;
    public GeoSOTController(Context context){
        this.context=context;
    }
    /**
     * @Controller 提交个人信息控制层
     * */
    public void AddLocalNearby(String lat,String lng){
        GeoSOTModel.AddLocalNearby(context,lat,lng);
    }
    /**
     * @Controller 加载附近的人信息控制层
     * */
    public void loadNearByPeople(String lat,String lng,String sex) {
        GeoSOTModel.loadNearByPeople(context,lat,lng,sex);
    }
    /**
     * @Controller 清除附近的人信息控制层
     * */
    public void ClearNearbyMessage() {
        GeoSOTModel.ClearNearbyMessage(context);
    }
    /**
     * @Controller 提交留言信息控制层
     * */
    public void UpdateUserLiuYan(LatLng latLng) {
//        GeoSOTModel.UpdateUserLiuYan(context,latLng);
    }
    /**
     * @Controller 搜索网格中留言信息控制层
     * */
    public void searchGeoSOTLiuYanMessage(AMap aMap, LatLng latlng) {
//        GeoSOTModel.searchGeoSOTLiuYanMessage(context,aMap,latlng);
    }
    /**
     * @Controller 搜索莫个人的轨迹信息控制层
     * */
    public void SearchPeopleTracked(String userphone, String star_time, String end_time, boolean isGeoSOT, boolean isCompare) {
        GeoSOTModel.SearchPeopleTracked(context,userphone,star_time,end_time,isGeoSOT,isCompare);
    }
}
