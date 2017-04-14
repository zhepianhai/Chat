package zph.zhjx.com.chat.ui;

import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import zph.zhjx.com.chat.R;
import zph.zhjx.com.chat.base.BaseActivity;
import zph.zhjx.com.chat.bean.TrackedBean;
import zph.zhjx.com.chat.controller.GeoSOTController;
import zph.zhjx.com.chat.geosot.DrawGeoSOT2_amap;
import zph.zhjx.com.chat.map.overlay.MyPointOverlay1;
import zph.zhjx.com.chat.util.DateTimePickDialogUtil;
import zph.zhjx.com.chat.util.PhoneUtil;
import zph.zhjx.com.chat.util.TimeUtil;
import zph.zhjx.com.chat.view.SelectPopupwindow;

public class DetailTrackActivity extends BaseActivity implements View.OnClickListener, SelectPopupwindow.OnOkClickListener, AMap.OnCameraChangeListener, AMap.OnMapLongClickListener {
    private final String TAG="DetailTrackActivity";
    private SelectPopupwindow popupWindow;
    private View headview;
    private LinearLayout back;
    private TextView title;
    private ImageView setting;

    private MapView mapview;
    private AMap aMap;
    private DrawGeoSOT2_amap mDrawGeoSOT; // 绘制GeoSOT
    private int mWidth;
    private int mHeight;
    private int fillcolor;
    private int strokecolor;
    private int geosot_layout=0;
    private LatLng check_latlng;
    private String userphone;
    private double lat;
    private double lng;
    private LatLng latlng;
    private String times;
    private Button select_time,select_time2,search;
    private TextView time,time2;
    //条件查询表示
    //默认查询方式是：非网格，按天查询，非对比
    private boolean isGeoSOT,isWeeked,isCompare,flag1,flag2;

    private LayoutInflater inflater = null;
    PopupWindow menuWindow;
    GeoSOTController geoSOTController;
    List<TrackedBean.DataBean.ListBean> listBeen;
    private boolean isSave;
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
        setContentView(R.layout.activity_detail_track);
        setstatusbarcolor();
        initheadview();
        getIntentDate();
        initmap(savedInstanceState);

        geoSOTController=new GeoSOTController(this);
        EventBus.getDefault().register(this);
    }


    //初始化地图
    private void initmap(Bundle savedInstanceState) {
        mapview.onCreate(savedInstanceState);
        if (aMap == null) {
            aMap = mapview.getMap();
            mapview.setSelected(true);
            setMap();
        }
    }

    private void setMap() {
        aMap.setOnCameraChangeListener(this);
        aMap.setOnMapLongClickListener(this);
        aMap.moveCamera(CameraUpdateFactory.zoomTo(17.0f));
        CameraUpdate movecity = CameraUpdateFactory.newLatLngZoom(latlng,12);
        aMap.moveCamera(movecity);
        aMap.addMarker(new MarkerOptions().
                icon(BitmapDescriptorFactory.fromBitmap(
                        BitmapFactory.decodeResource(getResources(), R.mipmap.position))).
                position(latlng));
    }

    //初始化ID
    private void initheadview() {
        headview=findViewById(R.id.headview1);
        back= (LinearLayout) headview.findViewById(R.id.headview_left_layout);
        setting= (ImageView) headview.findViewById(R.id.headview_right);
        back.setOnClickListener(this);
        setting.setOnClickListener(this);
        title= (TextView) headview.findViewById(R.id.headview_left_textview);
        mapview= (MapView) findViewById(R.id.detail_tracked_map);
        select_time= (Button) findViewById(R.id.detail_tracked_time_star_btn);
        select_time.setOnClickListener(this);
        select_time2= (Button) findViewById(R.id.detail_tracked_time_end_btn);
        select_time2.setOnClickListener(this);
        search= (Button) findViewById(R.id.detail_tracked_search_btn);
        search.setOnClickListener(this);
        time= (TextView) findViewById(R.id.detail_tracked_time_star_tv);
        time2= (TextView) findViewById(R.id.detail_tracked_time_end_tv);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.headview_left_layout:
                if(aMap!=null){
                    aMap.clear();
                }
                this.finish();
                break;
            case R.id.headview_right:
                popupWindow = new SelectPopupwindow(DetailTrackActivity.this,DetailTrackActivity.this,isGeoSOT,isWeeked,isCompare);
                popupWindow.setWidth((int)PhoneUtil.getScreenWidth(DetailTrackActivity.this)*4/5);//设置4/5的宽度
                popupWindow.showAtLocation(headview,Gravity.BOTTOM|Gravity.RIGHT,0,0);
                PhoneUtil.setBackgroundAlpha(DetailTrackActivity.this,0.5f);//当前的Activity半透明话设置
                popupWindow.setOkClickListener(this);//设置确定的监听事件
                break;
            case R.id.detail_tracked_time_star_btn:
               /* mTimePickerDialog.showDateAndTimePickerDialog();
                flag1=true;
                flag2=false;*/
                DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(
                        this,times);
                dateTimePicKDialog.onCreateDialog(new DateTimePickDialogUtil.SelectDateTimeCallBack() {
                    @Override
                    public void isConfirm(String edString) {
                        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        Date date= new Date(Long.valueOf(edString));
                        String ttt=sdf.format(date);
                        time.setText(ttt);
                    }
                });


                break;
            case R.id.detail_tracked_time_end_btn:
                DateTimePickDialogUtil dateTimePicKDialog1 = new DateTimePickDialogUtil(
                        this,times);
                dateTimePicKDialog1.onCreateDialog(new DateTimePickDialogUtil.SelectDateTimeCallBack() {
                    @Override
                    public void isConfirm(String edString) {
                        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        Date date= new Date(Long.valueOf(edString));
                        String ttt=sdf.format(date);
                        time2.setText(ttt);
                    }
                });
                break;
            case R.id.detail_tracked_search_btn:
                search();
                break;
        }
    }



    //得到初值
    public void getIntentDate() {
        userphone=getIntent().getStringExtra("phone");
        lat=getIntent().getDoubleExtra("lat",-1);
        lng=getIntent().getDoubleExtra("lng",-1);
        latlng=new LatLng(lat,lng);
        String name=getIntent().getStringExtra("name");
        title.setText(name+"的轨迹");
    }
    /**
     * 用来确定当前的筛选条件的监听事件
     * */
    @Override
    public void okClick(String message) {
        String[] ms=message.split("-");
        isGeoSOT=ms[0].equals("0")?true:false;
        isWeeked=ms[1].equals("0")?true:false;
        isCompare=ms[2].equals("0")?true:false;
        LoadingSelectView();
    }
    /**
     * 显示GeoSOT网格
     */
    private void displayGeoSOT() {
        aMap.clear();
//        aMap.invalidate(); //刷新地图
        aMap.removecache(); //刷新地图
        mWidth  = mapview.getWidth();
        mHeight =mapview.getHeight();
        Log.i("TBQ","屏幕的像素密度为：" + mWidth + ", " + mHeight);
        mDrawGeoSOT = new DrawGeoSOT2_amap(aMap,geosot_layout);
        mDrawGeoSOT.redrawGeoSOT(aMap, mWidth, mHeight);
        aMap.removecache();
    }
    /**
     * 显示当前选中的筛选条件对用的界面
     * */
    private void LoadingSelectView() {
        if(isGeoSOT){
//            check_latlng=null;
            displayGeoSOT();
        }
        else{
            aMap.clear();
            //3.清除GeoSOT网格
            if(mDrawGeoSOT!=null)
                mDrawGeoSOT.clearGeoSOT();
        }
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        if(isGeoSOT){
            mDrawGeoSOT.clearGeoSOT(); // 清除GeoSOT
        }
    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        if(isGeoSOT) {
            if (check_latlng != null){
                mDrawGeoSOT.clearGeoSOT(); // 清除GeoSOT
                aMap.clear();
                aMap.removecache();
                mDrawGeoSOT.redrawGeoSOT(aMap, mWidth, mHeight, check_latlng);
                if(isSave &&list_other!=null){
                    MyPointOverlay1 myPointOverlay = new MyPointOverlay1(DetailTrackActivity.this,aMap,list_other);
                    myPointOverlay.removeFromMap();
                    myPointOverlay.zoomToSpan();
                    myPointOverlay.AddLineToAmap(true);
                }

            }
            else{
                displayGeoSOT();
            }
        }
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        Log.i(TAG,"isGeosot="+isGeoSOT);
        if(isGeoSOT) {
            check_latlng =new LatLng(latLng.latitude,latLng.longitude);
            //TODO 2.显示搜索相关的layout
            //3.清除GeoSOT网格
            mDrawGeoSOT.clearGeoSOT();
            //4.将点所在矩形选中
            Log.i(TAG,"dian:"+latLng.latitude);
            mDrawGeoSOT.redrawGeoSOT(aMap, mWidth, mHeight,check_latlng);
        }
    }




    /**
     * 搜索的业务逻辑方法
     */
    private void search() {
        String star_time=time.getText().toString();
        String end_time=time2.getText().toString();
        if(check_latlng==null &&isGeoSOT){
            toast("请选择要查询网格（长按网格区域）");
            return;
        }
        if(star_time.equals("")||star_time==null){
            toast("起始日期未选择");
            return;
        }
        if(end_time.equals("")||end_time==null){
            toast("结束日期未选择");
            return;
        }
     if(!TimeUtil.CheckStarEndTime(star_time,end_time)){
            toast("起始日期应小于结束日期");
            return;
        }
       if(!TimeUtil.isTimeDaYuSystemTime(end_time)){
            toast("日期不能超过当前时间");
            return;
        }
        geoSOTController.SearchPeopleTracked(userphone,star_time,end_time,isGeoSOT,isCompare);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(aMap!=null){
            aMap.clear();
        }
        if(myPointOverlay!=null){
            myPointOverlay.removeFromMap();
        }
        EventBus.getDefault().unregister(this);
    }


    List<PoiItem> list_other;
    MyPointOverlay1 myPointOverlay;
    //获取指定的人信息的订阅者 
    @Subscribe(threadMode= ThreadMode.MAIN)
    public void onDetailTrackedEventBus(TrackedBean trackedBean) {
        listBeen=trackedBean.getData().getList();
        if(isGeoSOT){
            isSave=true;
        }
        else{
            isSave=false;
        }
        if(listBeen.size()==0||listBeen==null){
            toast("暂无数据");
        }
        else{
            List<PoiItem> list_me=new ArrayList<>();
            list_other=new ArrayList<>();
            for (int i=0;i<listBeen.size();++i) {
                if(!listBeen.get(i).getLat().equals("0.0")) {
                    LatLonPoint point = new LatLonPoint(
                            Double.valueOf(listBeen.get(i).getLat()),
                            Double.valueOf(listBeen.get(i).getLng()));
                    PoiItem p = new PoiItem("1", point, null, null);
                    if (listBeen.get(i).getPhone().equals(userphone)) {
                        list_other.add(p);
                    } else {
                        list_me.add(p);
                    }
                }
            }
            if(myPointOverlay!=null){
                myPointOverlay.removeFromMap();
            }
            if(isGeoSOT){
                check_latlng=null;
            }
            myPointOverlay= new MyPointOverlay1(DetailTrackActivity.this,aMap,list_other);
            myPointOverlay.removeFromMap();
            myPointOverlay.zoomToSpan();
            myPointOverlay.AddLineToAmap(true);
            if(isCompare) {
                MyPointOverlay1 myPointOverlay1 = new MyPointOverlay1(DetailTrackActivity.this,aMap,list_me);
                myPointOverlay.zoomToSpan();
                myPointOverlay.AddLineToAmap(false);
            }
        }
    }
}
