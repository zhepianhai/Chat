package zph.zhjx.com.chat.model;

import android.app.AlertDialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.amap.api.maps.model.LatLng;
import com.amap.api.maps2d.AMap;
import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import zph.zhjx.com.chat.R;
import zph.zhjx.com.chat.app.App;
import zph.zhjx.com.chat.bean.ClsLocal;
import zph.zhjx.com.chat.bean.LiuYanList;
import zph.zhjx.com.chat.bean.LocalAdd;
import zph.zhjx.com.chat.bean.Message_01;
import zph.zhjx.com.chat.bean.NearBayPeopleListBean;
import zph.zhjx.com.chat.bean.TrackedBean;
import zph.zhjx.com.chat.contact.Contact;
import zph.zhjx.com.chat.dao.User;
import zph.zhjx.com.chat.geosot.DrawGeoSOT2_amap;
import zph.zhjx.com.chat.imp.BeanImp;
import zph.zhjx.com.chat.util.BitmapUtil;
import zph.zhjx.com.chat.util.DBUtil;
import zph.zhjx.com.chat.view.CustomProgressDialog;

/**
 * Created by adminZPH on 2017/3/13.
 * GeoSOT模块业务逻辑代码
 */

public class GeoSOTModel {

    /**
     * @Modle 提交个人业务逻辑层
     * */
    public static void AddLocalNearby(Context context, String lat, String lng) {
        BeanImp repo1;
        Retrofit retrofit1 = new Retrofit.Builder()
                .baseUrl(Contact.Nearbay_Interface)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        repo1=retrofit1.create(BeanImp.class);
        String user_phone= DBUtil.getUserMessage().getPhone();
        Call<LocalAdd> call=repo1.local_add(user_phone,0,lat,lng);
        call.enqueue(new Callback<LocalAdd>() {
            @Override
            public void onResponse(Call<LocalAdd> call, Response<LocalAdd> response) {
                LocalAdd message=response.body();
                EventBus.getDefault().post(message);
            }
            @Override
            public void onFailure(Call<LocalAdd> call, Throwable t) {
                LocalAdd message=new LocalAdd();
                message.setCode(-1);
                EventBus.getDefault().post(message);
            }
        });
    }
    /**
     * @Model 加载附近的人业务逻辑层
     * */
    public static void loadNearByPeople(Context context, String lat, String lng,String sex) {
        BeanImp repo1;
        Retrofit retrofit1 = new Retrofit.Builder()
                .baseUrl(Contact.Nearbay_Interface)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        repo1=retrofit1.create(BeanImp.class);
        String user_phone= DBUtil.getUserMessage().getPhone();
        Call<NearBayPeopleListBean> call=repo1.getNearBayPeopleList(user_phone,lat,lng,sex);
        call.enqueue(new Callback<NearBayPeopleListBean>() {
            @Override
            public void onResponse(Call<NearBayPeopleListBean> call, Response<NearBayPeopleListBean> response) {
                NearBayPeopleListBean message=response.body();
                Log.i("NearbyPeopleActivity","获取附近的人信息:"+message.toString());
                EventBus.getDefault().post(message);
            }
            @Override
            public void onFailure(Call<NearBayPeopleListBean> call, Throwable t ) {
                Log.i("NearbyPeopleActivity","错误信息："+ t.getMessage());
                NearBayPeopleListBean message=new NearBayPeopleListBean();
                message.setCode(-1);
                EventBus.getDefault().post(message);
            }
        });
    }
    /**
     * @Model 清除附近的人业务逻辑层
     * */
    public static void ClearNearbyMessage(Context context) {
        BeanImp repo1;
        Retrofit retrofit1 = new Retrofit.Builder()
                .baseUrl(Contact.Nearbay_Interface)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        repo1=retrofit1.create(BeanImp.class);
        if(App.phone==null){
            App.phone=DBUtil.getUserMessage().getPhone();
        }
        Call<ClsLocal> call=repo1.updatelocal(App.phone,1);
        call.enqueue(new Callback<ClsLocal>() {
            @Override
            public void onResponse(Call<ClsLocal> call, Response<ClsLocal> response) {
                ClsLocal message=response.body();
                Log.i("TAG",message.toString());
                EventBus.getDefault().post(message);
            }
            @Override
            public void onFailure(Call<ClsLocal> call, Throwable t ) {
                Log.i("TAG","错误信息："+ t.getMessage());
                ClsLocal message=new ClsLocal();
                message.setCode(-1);
                EventBus.getDefault().post(message);
            }
        });
    }




    /**
     * @Model 提交留言信息业务逻辑层
     * */
    public static void UpdateUserLiuYan(final Context context,final LatLng latLng) {
        Log.i("TAG","1111111111");
        ImageView imageview;
        Button infowindow_ok, infowindow_del;
        final EditText infowindow_edittext;
        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mview = inflater.inflate(R.layout.item_layout_liuyan, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog dialog = builder.create();
        dialog.show();
        //给弹出框中的EidtText 焦点可以获得
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        Window window = dialog.getWindow();
        window.setContentView(mview);
        infowindow_edittext = (EditText) mview.findViewById(R.id.item_layout_liuyan_editext);
        infowindow_ok = (Button) mview.findViewById(R.id.item_layout_liuyan_button);
        imageview = (ImageView) mview.findViewById(R.id.item_layout_liuyan_image);
        infowindow_edittext.setEnabled(true);
        if(DBUtil.getUserMessage().getImageSrc()==null||DBUtil.getUserMessage().getImageSrc().equals("")){
            imageview.setImageBitmap(BitmapUtil.base64ToBitmap(DBUtil.getUserMessage().getImageBase64()));
        }
        else
            Glide.with(context).load(DBUtil.getUserMessage().getImageSrc()).into(imageview);
        infowindow_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = infowindow_edittext.getText().toString().trim();
                if (TextUtils.isEmpty(content)) {
                    Toast.makeText(context,"输入内容为空！",Toast.LENGTH_SHORT).show();
                    return ;
                }
                UpdateLiuYanMessage(context,latLng,content);
                dialog.dismiss();
            }


        });
    }
    /**
     * 上传留言
     * */
    private static void UpdateLiuYanMessage(Context context, LatLng latLng, String content) {
        Log.i("TAG","2222222");
        final CustomProgressDialog customProgressDialog=new CustomProgressDialog(context,"加载中...",0);
        customProgressDialog.show();
        BeanImp repo1;
        Retrofit retrofit1 = new Retrofit.Builder()
                .baseUrl(Contact.LiuYan_Interface)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        repo1=retrofit1.create(BeanImp.class);
        User user = DBUtil.getUserMessage();
        Call<Message_01> call=repo1.AddOneLiuYanMessage(user.getPhone(),user.getUserNickname(),content,
                String.valueOf(latLng.latitude),String.valueOf(latLng.longitude));
        call.enqueue(new Callback<Message_01>() {
            @Override
            public void onResponse(Call<Message_01> call, Response<Message_01> response) {
                customProgressDialog.dismiss();
                Message_01 message=response.body();
                Log.i("TAG",message.toString());
                EventBus.getDefault().post(message);
            }

            @Override
            public void onFailure(Call<Message_01> call, Throwable t) {
                customProgressDialog.dismiss();
                Log.i("TAG","错误信息："+ t.getMessage());
                Message_01 message=new Message_01();
                message.setCode(-1);
                EventBus.getDefault().post(message);
            }
        });
    }





    /**
     * @Model 搜索网格中留言信息业务逻辑层
     * @param aMap 当前的地图实体
     * @param latlng  当前选中的点
     *  需要根据该点和当前的地图得到当前选中的网格，获取选中网格的四个顶点的经纬度信息
     * */

    public static void searchGeoSOTLiuYanMessage(Context context, AMap aMap, LatLng latlng) {
        //获取选中网格的四个顶点的经纬度坐标，顺序为顺时针：左上，右上，右下，左下
        List<LatLng> list= DrawGeoSOT2_amap.GetCheckGeoSOTLatLngList();
        Log.i("LiuYan","上传的经纬度是:"+list.toString());
        final CustomProgressDialog customProgressDialog=new CustomProgressDialog(context,"加载中...",0);
        customProgressDialog.show();
        BeanImp repo1;
        Retrofit retrofit1 = new Retrofit.Builder()
                .baseUrl(Contact.LiuYan_Interface)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        repo1=retrofit1.create(BeanImp.class);
        if(list.size()==4) {
            Call<LiuYanList> call = repo1.searchLiuyanByGrid(
                    String.valueOf(list.get(0).latitude), String.valueOf(list.get(0).longitude),
                    String.valueOf(list.get(1).latitude), String.valueOf(list.get(1).longitude),
                    String.valueOf(list.get(3).latitude), String.valueOf(list.get(3).longitude),
                    String.valueOf(list.get(2).latitude), String.valueOf(list.get(2).longitude));

            call.enqueue(new Callback<LiuYanList>() {
                @Override
                public void onResponse(Call<LiuYanList> call, Response<LiuYanList> response) {
                    customProgressDialog.dismiss();
                    LiuYanList message = response.body();
                    Log.i("TAG", message.toString());
                    EventBus.getDefault().post(message);
                }

                @Override
                public void onFailure(Call<LiuYanList> call, Throwable t) {
                    customProgressDialog.dismiss();
                    Log.i("TAG", "错误信息：" + t.getMessage());
                    LiuYanList message = new LiuYanList();
                    message.setCode(-1);
                    EventBus.getDefault().post(message);
                }
            });
        }
        else{

        }
    }





    /***
     * @Model  搜索某个联系人的运动轨迹的业务层
     *
     * */

    public static void SearchPeopleTracked(final Context context, String userphone, String star_time, String end_time, boolean isGeoSOT, boolean isCompare) {
        final CustomProgressDialog customProgressDialog=new CustomProgressDialog(context,"加载中...",0);
        customProgressDialog.show();


        BeanImp repo1;
        Retrofit retrofit1 = new Retrofit.Builder()
                .baseUrl(Contact.Track_Interface)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        repo1=retrofit1.create(BeanImp.class);
        User user = DBUtil.getUserMessage();
        String ulat="",ulng="",dlat="",dlng="";
        if(isGeoSOT){
            //获取选中网格的四个顶点的经纬度坐标，顺序为顺时针：左上，右上，右下，左下
            List<LatLng> list= DrawGeoSOT2_amap.GetCheckGeoSOTLatLngList();
            ulat=String.valueOf(list.get(0).latitude);
            ulng=String.valueOf(list.get(1).longitude);
            dlat=String.valueOf(list.get(2).latitude);
            dlng=String.valueOf(list.get(3).longitude);
        }
        Log.i("DetailTrackActivity","上传的ulat："+ulat);
        Log.i("DetailTrackActivity","上传的ulng："+ulng);
        Log.i("DetailTrackActivity","上传的dlat："+dlat);
        Log.i("DetailTrackActivity","上传的dlng："+dlng);


        Log.i("TAGTEST","userphone_me:"+user.getPhone());
        Log.i("TAGTEST","userphone:"+userphone);
        Log.i("TAGTEST","star_time:"+star_time);
        Log.i("TAGTEST","end_time:"+end_time);
        Call<TrackedBean> call = repo1.SearchPeopleTracked(user.getPhone(),userphone,ulat,ulng,dlat,dlng,star_time+":00",end_time+":00");
        call.enqueue(new Callback<TrackedBean>() {
            @Override
            public void onResponse(Call<TrackedBean> call, Response<TrackedBean> response) {
                customProgressDialog.dismiss();
                Log.i("DetailTrackActivity","结果是:"+response.body().toString());
                TrackedBean message = response.body();
                Log.i("TAG", message.toString());
                if(message!=null)
                    EventBus.getDefault().post(message);

            }

            @Override
            public void onFailure(Call<TrackedBean> call, Throwable t) {
                customProgressDialog.dismiss();
                Log.i("DetailTrackActivity","错误是:"+t.getMessage());
                Toast.makeText(context,"服务器未响应",Toast.LENGTH_SHORT).show();

            }
        });

    }
}
