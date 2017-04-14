package zph.zhjx.com.chat.imp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import zph.zhjx.com.chat.bean.ClsLocal;
import zph.zhjx.com.chat.bean.DetailpeopleBean;
import zph.zhjx.com.chat.bean.FriendsList;
import zph.zhjx.com.chat.bean.LiuYanList;
import zph.zhjx.com.chat.bean.LocalAdd;
import zph.zhjx.com.chat.bean.LogoutBeans;
import zph.zhjx.com.chat.bean.Message_01;
import zph.zhjx.com.chat.bean.NearBayPeopleListBean;
import zph.zhjx.com.chat.bean.TrackedBean;
import zph.zhjx.com.chat.bean.User;

/**
 * Created by adminZPH on 2017/3/3.
 */

public interface BeanImp {
//    @Headers("Accept: Application/JSON")
//    @FormUrlEncoded

    //登录接口
    @GET("login.do")
    Call<User> contributorsBySimpleGetCall(
            @Query("phone") String phone,@Query("password") String password,
            @Query("deviceInfo") String deviceInfo,@Query("loginIp") String loginIp);
   /* //日志接口
    @GET("login/log.do")
    Call<LogoutBean> toServeUpadateLog(@Query("token") String token,@Query("phone") String phone,
                                   @Query("logDate") String logDate,@Query("logState") String logState,
                                   @Query("deviceInfo") String deviceInfo,@Query("loginIp") String loginIp);
*/
    //登出接口
    @GET("logout.do")
    Call<LogoutBeans> logout(@Query("token") String token, @Query("phone") String phone,@Query("deviceInfo") String deviceInfo,@Query("loginIp") String loginIp);
    //上传附近的人模块中自己的位置信息
    @GET("add.do")
    Call<LocalAdd> local_add(@Query("phone") String phone, @Query("localState") int localState
                , @Query("lat") String lat, @Query("lng") String lng);
    //清除附近的人模块中自己的位置信息
    @GET("updatestate.do")
    Call <ClsLocal> updatelocal(@Query("phone") String phone, @Query("localState") int localState);
    //加载附近的人的列表信息
    @GET("getusers.do")
    Call<NearBayPeopleListBean> getNearBayPeopleList(@Query("phone") String phone,@Query("lat") String lat,@Query("lng") String lng,@Query("gender") String gender);
    //获取所有联系人列表的接口
    @GET("getlist.do")
    Call<FriendsList> getAllFriendsList(@Query("phone")String phone);
    //获取指定联系人的详情接口
    @GET("getfriend.do?")
    Call<DetailpeopleBean> getDetailFriendsList(@Query("userId")String userId);
    //更改用户的备注信息
    @GET("updatefriendremark.do?")
    Call<LocalAdd> UpdateFriendBeiZhuMessage(@Query("userPhone")String userPhone,@Query("friendPhone")String friendPhone,
                                                     @Query("remark")String remark);
    //上传留言信息
    @GET("addmessage.do?")
    Call<Message_01> AddOneLiuYanMessage(@Query("phone") String phone, @Query("userNickname") String userNickname
                    , @Query("content")String content, @Query("lat")String lat, @Query("lng") String lng);
    //查询指定网格中的留言
    /**
     * luLat:              //String类型，留言查询范围左上角纬度
     * luLng:              //String类型，留言查询范围左上角经度
     * ruLat:              //String类型，留言查询范围右上角纬度
     * ruLng:              //String类型，留言查询范围右上角纬度
     * ldLat:              //String类型，留言查询范围左下角纬度
     * ldLng:              //String类型，留言查询范围左下角经度
     * rdLat:              //String类型，留言查询范围右下角纬度
     * rdLng:              //String类型，留言查询范围右下角纬度
     */
    @GET("getmessages.do?")
    Call<LiuYanList> searchLiuyanByGrid(@Query("luLat") String lulat, @Query("luLng") String luLng, @Query("ruLat")String ruLat,
                                        @Query("ruLng")String ruLng, @Query("ldLat") String ldLat, @Query("ldLng")String ldLng,
                                        @Query("rdLat") String rdLat, @Query("rdLng") String rdLng
    );
    /**
     * 服务，用来上传用户的移动轨迹
     *  phone:              //String类型，运动者手机号
     *  lat:                //String类型，运动位置纬度
     *  lng:                //String类型，运动位置经度
     * */
    @GET("add.do?")
    Call<Message_01> AddMapTrackInToFuWuQi(@Query("phone")String phone,@Query("lat")String lat,@Query("lng")String lng);

    /**
     * 搜索某个用户的详细运动轨迹的业务
     * phone:              //String类型，运动者手机号
     * friendPhone:        //String类型，朋友手机号
     * ulat:                //String类型，运动位置上纬度
     * ulng:                //String类型，运动位置上经度
     * dlat:                //String类型，运动位置下纬度
     * dlng:                //String类型，运动位置下经度
     * startTime:            //String类型，运动开始时间
     * endTime:            //String类型，运动结束时间
     * */
     @GET("getmovemetmessage.do")
     Call<TrackedBean> SearchPeopleTracked(@Query("phone") String phone, @Query("friendPhone") String friendPhone, @Query("ulat") String ulat,
                                           @Query("ulng")String ulng, @Query("dlat") String dlat, @Query("dlng")String dlng,
                                           @Query("startTime") String startTime, @Query("endTime") String endTime);


}
