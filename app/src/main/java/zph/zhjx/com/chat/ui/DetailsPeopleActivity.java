package zph.zhjx.com.chat.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import zph.zhjx.com.chat.R;
import zph.zhjx.com.chat.base.BaseActivity;
import zph.zhjx.com.chat.bean.DetailpeopleBean;
import zph.zhjx.com.chat.controller.PeopleController;
import zph.zhjx.com.chat.dao.People;
import zph.zhjx.com.chat.util.BitmapUtil;
import zph.zhjx.com.chat.util.DBUtil;
import zph.zhjx.com.chat.view.CircleImageView;

public class DetailsPeopleActivity extends BaseActivity implements View.OnClickListener {
    private final String TAG="DetailsPeopleActivity";
    private View headview;
    private TextView title,name,nickname,address;
    private CircleImageView photo;
    private ImageView sex;
    private Button beizhu,phone;
    private PeopleController peopleController;
    private String UID;
    private People p;
    private LinearLayout back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_people);
        init();
    }

    private void LoadingDate() {
        UID=getIntent().getStringExtra("uid");
        Log.i(TAG,"uid:"+UID);
        //先加载本地，再加载网络，网络有新的，则更新
        p=DBUtil.GetOnePeopeleByUID(UID);
        if(p!=null){
            showMessage();
        }
        peopleController=new PeopleController(this);
        peopleController.getDeatilPeopleMessage(UID);
    }
    //获取指定的人信息的订阅者 
    @Subscribe(threadMode= ThreadMode.MAIN)
    public void onDetailPeopleEventBus(DetailpeopleBean detailpeopleBean) {
        if(detailpeopleBean.getCode()==0){
            Log.i(TAG,"date:"+detailpeopleBean.getData().toString());
            //写入数据库
            DBUtil.UpdateOneFriends(this,detailpeopleBean.getData());
            p=DBUtil.GetOnePeopeleByPhone(detailpeopleBean.getData().getPhone());
            Log.i(TAG,"p:"+p.toString());
            showMessage();
        }
        else {
            toast(detailpeopleBean.getMessage());
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        LoadingDate();
    }

    private void showMessage() {
        Log.i("TAG","此时的base64:"+p.getBitmap_base64());
        if(p.getImage()==null||p.getImage().equals("")){
            photo.setImageBitmap(BitmapUtil.base64ToBitmap(p.getBitmap_base64()));
        }
        else
            Glide.with(this).load(p.getImage()).into(photo);
        if(p.getGender()!=null &&p.getGender().equals("m")){
            sex.setImageResource(R.mipmap.boy);
        }
        else if(p.getGender()!=null &&p.getGender().equals("f")){
            sex.setImageResource(R.mipmap.gril);
        }
        if(p.getName()!=null)
            name.setText(""+p.getName());
        if(p.getNickname()!=null)
            nickname.setText("昵称:"+p.getNickname());
        if(p.getUser_phone()!=null)
            phone.setText(""+p.getUser_phone());
        if(p.getAddress()!=null)
            address.setText(""+p.getAddress());
        else{
            address.setText("未知地区");
        }
    }

    private void init() {
        setstatusbarcolor();
        EventBus.getDefault().register(this);
        headview=findViewById(R.id.headview1);
        title= (TextView) headview.findViewById(R.id.headview_left_textview);
        title.setText("详细资料"+"");
        back= (LinearLayout) headview.findViewById(R.id.headview_left_layout);
        photo= (CircleImageView) findViewById(R.id.detail_people_image);
        name= (TextView) findViewById(R.id.detail_people_username);
        nickname= (TextView) findViewById(R.id.detail_people_nickname);
        sex= (ImageView) findViewById(R.id.detail_people_sex);
        beizhu= (Button) findViewById(R.id.detail_people_beizhu);
        address= (TextView) findViewById(R.id.detail_people_address);
        phone= (Button) findViewById(R.id.detail_people_phone);
        beizhu.setOnClickListener(this);
        back.setOnClickListener(this);
        phone.setOnClickListener(this);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.detail_people_beizhu:
                if(p.getUser_phone()!=null &&p.getBeizhu()!=null) {
                    Intent intent = new Intent(this, SetBeiZhuActivity.class);
                    intent.putExtra("friend_phone", p.getUser_phone());
                    intent.putExtra("friend_beizhu", p.getBeizhu());
                    Log.i(TAG, "friend_phone:" + p.getUser_phone());
                    Log.i(TAG, "friend_beizhu:" + p.getBeizhu());
                    startActivity(intent);
                }
                break;
            case R.id.headview_left_layout:
                this.finish();
                break;
            case R.id.detail_people_phone:
                Intent intent = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + p.getUser_phone());
                intent.setData(data);
                startActivity(intent);
                break;
        }
    }

}
