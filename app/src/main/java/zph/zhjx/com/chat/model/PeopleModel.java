package zph.zhjx.com.chat.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import zph.zhjx.com.chat.app.App;
import zph.zhjx.com.chat.bean.DetailpeopleBean;
import zph.zhjx.com.chat.bean.FriendsList;
import zph.zhjx.com.chat.bean.LocalAdd;
import zph.zhjx.com.chat.contact.Contact;
import zph.zhjx.com.chat.dao.People;
import zph.zhjx.com.chat.imp.BeanImp;
import zph.zhjx.com.chat.util.BitmapUtil;
import zph.zhjx.com.chat.util.DBUtil;
import zph.zhjx.com.chat.util.PinYinUtil;
import zph.zhjx.com.chat.view.CustomProgressDialog;

/**
 * Created by adminZPH on 2017/3/14.
 */

public class PeopleModel {
    /**
     * @Model 获取所有联系人的业务逻辑层
     * */
    public static void getAllPeopleList(final Context context,final CustomProgressDialog customProgressDialog) {
        BeanImp repo1;
        Retrofit retrofit1 = new Retrofit.Builder()
                .baseUrl(Contact.People_Interface)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        repo1=retrofit1.create(BeanImp.class);
        String user_phone= DBUtil.getUserMessage().getPhone();
        Call<FriendsList> call=repo1.getAllFriendsList(user_phone);
        call.enqueue(new Callback<FriendsList>() {
            @Override
            public void onResponse(Call<FriendsList> call, Response<FriendsList> response) {
                customProgressDialog.dismiss();
                FriendsList message=response.body();
                Log.i("TAG",message.toString());
                List<People> peoples=new ArrayList<People>();
                if(message.getCode()==0) {
                    for (int i = 0; i < message.getData().getList().size(); ++i){
                        People p=new People();
                        p.setBeizhu(message.getData().getList().get(i).getNickName());
                        p.setId_phone(App.phone);
                        if(message.getData().getList().get(i).getPhoto()==null ||message.getData().getList().get(i).getPhoto().equals("")){
                            Bitmap b=BitmapUtil.GetUserImageByNickName(context,message.getData().getList().get(i).getNickName());
                            p.setBitmap_base64(BitmapUtil.BitmapToString(b));
                        }
                        else {
                            p.setImage(message.getData().getList().get(i).getPhoto());
                        }
                        p.setUser_phone(message.getData().getList().get(i).getPhone());
                        p.setUID(""+message.getData().getList().get(i).getUserId());
                        p.setPinyinname(PinYinUtil.getPinYin(message.getData().getList().get(i).getNickName()));
                        peoples.add(p);
                    }
                }
                EventBus.getDefault().post(peoples);
            }
            @Override
            public void onFailure(Call<FriendsList> call, Throwable t ) {
                customProgressDialog.dismiss();
                Toast.makeText(context,"服务器未响应...",Toast.LENGTH_SHORT).show();
            }
        });
    }



    /**
     * @Model 获取指定联系人的业务逻辑层
     * */
    public static void getDeatilPeopleMessage(final  Context context, String uid) {
        BeanImp repo1;
        Retrofit retrofit1 = new Retrofit.Builder()
                .baseUrl(Contact.People_Interface)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        repo1=retrofit1.create(BeanImp.class);
        String user_phone= DBUtil.getUserMessage().getPhone();
        Call<DetailpeopleBean> call=repo1.getDetailFriendsList(uid);
        call.enqueue(new Callback<DetailpeopleBean>() {
            @Override
            public void onResponse(Call<DetailpeopleBean> call, Response<DetailpeopleBean> response) {
                DetailpeopleBean message=response.body();
                Log.i("TAG",message.toString());
                EventBus.getDefault().post(message);
            }

            @Override
            public void onFailure(Call<DetailpeopleBean> call, Throwable t) {
                Toast.makeText(context,"服务器未响应...",Toast.LENGTH_SHORT).show();
            }
        });
    }


    /**
     * @Model 更新指定的联系人的备注业务控制层
     * */
    public static void UpdatePeopleBeizhuMessage(final Context context, String friend_phone, String content) {
        BeanImp repo1;
        Retrofit retrofit1 = new Retrofit.Builder()
                .baseUrl(Contact.People_Interface)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        repo1=retrofit1.create(BeanImp.class);
        String user_phone= DBUtil.getUserMessage().getPhone();
        Call<LocalAdd> call=repo1.UpdateFriendBeiZhuMessage(user_phone,friend_phone,content);
        call.enqueue(new Callback<LocalAdd>() {
            @Override
            public void onResponse(Call<LocalAdd> call, Response<LocalAdd> response) {
                LocalAdd message=response.body();
                Log.i("TAG",message.toString());
                EventBus.getDefault().post(message);
            }

            @Override
            public void onFailure(Call<LocalAdd> call, Throwable t) {
                Toast.makeText(context,"服务器未响应...",Toast.LENGTH_SHORT).show();
            }
        });

    }
}
