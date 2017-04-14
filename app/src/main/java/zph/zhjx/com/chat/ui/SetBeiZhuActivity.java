package zph.zhjx.com.chat.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import zph.zhjx.com.chat.R;
import zph.zhjx.com.chat.base.BaseActivity;
import zph.zhjx.com.chat.bean.LocalAdd;
import zph.zhjx.com.chat.controller.PeopleController;
import zph.zhjx.com.chat.util.DBUtil;
import zph.zhjx.com.chat.view.CustomProgressDialog;

public class SetBeiZhuActivity extends BaseActivity implements View.OnClickListener {
    private final String TAG="SetBeiZhuActivity";
    private View headview;
    private TextView title;
    private EditText editText;
    private Button button;
    private LinearLayout back;
    private PeopleController peopleController;
    private String phone,beizhul,message;
    private CustomProgressDialog customProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_bei_zhu);
        peopleController=new PeopleController(this);
        EventBus.getDefault().register(this);
        init();
        getIntnet();
        setDate();
    }

    private void setDate() {
        editText.setText(beizhul+"");
    }

    private void init() {
        setstatusbarcolor();
        headview=findViewById(R.id.headview1);
        title= (TextView) headview.findViewById(R.id.headview_left_textview);
        title.setText("备注信息"+"");
        back= (LinearLayout) headview.findViewById(R.id.headview_left_layout);
        button= (Button) findViewById(R.id.update_beizhu);
        back.setOnClickListener(this);
        button.setOnClickListener(this);
        editText= (EditText) findViewById(R.id.edit_beizhu);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.update_beizhu:
                message=editText.getText().toString();
                if(message.equals(beizhul)) {
                    toast("备注未改变");
                    return;
                }
                if (TextUtils.isEmpty(message)){
                    toast("不能为空");
                    return;
                }
                customProgressDialog=new CustomProgressDialog(this,"上传中...",0);
                customProgressDialog.show();
                peopleController.UpdatePeopleBeizhuMessage(phone,message);
                break;
            case R.id.headview_left_layout:
                this.finish();
                break;
        }
    }

    //获取指定的人信息的订阅者 
    @Subscribe(threadMode= ThreadMode.MAIN)
    public void onDetailPeopleEventBus(LocalAdd updatebeizhu) {
        customProgressDialog.dismiss();
        if(updatebeizhu.getCode()==0){
            //更新数据库
            DBUtil.UpdateBeiZhu(this,phone,message);
            this.finish();
        }
        else {
            toast(updatebeizhu.getMessage());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void getIntnet() {
        phone=getIntent().getStringExtra("friend_phone");
        beizhul=getIntent().getStringExtra("friend_beizhu");
        Log.i(TAG,"phone:"+phone);
        Log.i(TAG,"beizhul:"+beizhul);
    }
}
