package zph.zhjx.com.chat.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import zph.zhjx.com.chat.app.App;
import zph.zhjx.com.chat.bean.DetailpeopleBean;
import zph.zhjx.com.chat.dao.People;
import zph.zhjx.com.chat.dao.User;
import zph.zhjx.com.chat.db.DaoSession;

import static android.content.ContentValues.TAG;
import static zph.zhjx.com.chat.app.App.phone;

/**
 * Created by adminZPH on 2017/3/8.
 */

public class DBUtil {
    /**
     * 判断该手机号是否存在数据库中
     * */
    public  static boolean IsInDbByPhone(String phone){
        DaoSession session;
        session  = GreenDaoUtils.getSingleTon().getmDaoSession();
        List<User> users=
                session.getUserDao().loadAll();
        for(int i=0;i<users.size();++i){
            if(users.get(i).getPhone().equals(phone)){
                return true;
            }
        }
        return false;
    }


    /**
     * 更新数据库
     * */
    public static void UpdateDb() {

        DBUtil.DeletAllFriendsList();
        Log.i(TAG,"正在进行数据库清空..");
        Log.i(TAG,"手机号："+ phone);
        SQLiteDatabase db = GreenDaoUtils.getSingleTon().getDb();
        zph.zhjx.com.chat.dao.User u=getUserMessage();
        u.setLogin(false);
        DaoSession seeeon = GreenDaoUtils.getSingleTon().getmDaoSession();
        seeeon.getUserDao().update(u);


    }

    /**
     * 向本地数据库提交数据
     * */
    public static void initDataBase(User user) {
        SQLiteDatabase db = GreenDaoUtils.getSingleTon().getDb();
        DaoSession seeeon = GreenDaoUtils.getSingleTon().getmDaoSession();
        zph.zhjx.com.chat.dao.User u=getUserMessage();
        if(u.getPhone()==null){
            //说明是第一次登录
            u=user;
        }
        else{
            u.setLogin(true);
            u.setToken(user.getToken());
            u.setAddress(user.getAddress());
            u.setBirthday(user.getBirthday());
            u.setImageSrc(user.getImageSrc());
            u.setPosition(false);
            u.setUserName(user.getUserName());
            u.setUserNickname(user.getUserNickname());
            u.setImageBase64(user.getImageBase64());
        }


        //判断是否存在，存在的话就进行更新，不存在的话就进行插入
        if(DBUtil.IsInDbByPhone(user.getPhone())){
            seeeon.getUserDao().update(u);
        }
        else{
            seeeon.getUserDao().insert(u);
        }
    }
    /**
     * 返回当前的用户信息
     * */
    public static User  getUserMessage(){
        User user=new User();
        SQLiteDatabase db = GreenDaoUtils.getSingleTon().getDb();
        DaoSession  session  = GreenDaoUtils.getSingleTon().getmDaoSession();
        List<User>users=session.getUserDao().loadAll();
        for(int i=0;i<users.size();++i){
            if(users.get(i).getPhone().equals(App.phone)){
                return users.get(i);
            }
        }
        return user;
    }


    /**
     * 向本地数据库提交数据
     * */
    public static void UpdateZuJi(String username, boolean flag) {
        SQLiteDatabase db = GreenDaoUtils.getSingleTon().getDb();

        zph.zhjx.com.chat.dao.User u=getUserMessage();
        u.setPosition(flag);
        DaoSession seeeon = GreenDaoUtils.getSingleTon().getmDaoSession();
        //判断是否存在，存在的话就进行更新，不存在的话就进行插入
        seeeon.getUserDao().update(u);
    }

    /**
     * 判断当前用户是否是登录状态
     * */
    public  static boolean UserIsLogin(){
        return getUserMessage().getLogin();
    }

    /**
     * 向数据库中插入联系人的信息
     * */
    public static void InsertIntoDBFriends(List<People> peoples) {
        SQLiteDatabase db = GreenDaoUtils.getSingleTon().getDb();
        DaoSession seeeon = GreenDaoUtils.getSingleTon().getmDaoSession();
        for(int i=0;i<peoples.size();++i){
            if(GetOnePeopeleByPhone(peoples.get(i).getUser_phone())==null){
                Log.i("PersonFragment","本地插入...");
                seeeon.getPeopleDao().insert(peoples.get(i));
            }
            else {
                Log.i("PersonFragment","本地更新...");
                seeeon.getPeopleDao().update(peoples.get(i));
            }
        }
    }
    /**
     * 从数据库中查找当前用户的好友联系人列表
     * */

    public static List<People> GetAllFriendsList(){
        List<People> p=new ArrayList<>();
        if(App.phone==null){
            App.phone=getUserMessage().getPhone();
        }
        DaoSession seeeon = GreenDaoUtils.getSingleTon().getmDaoSession();
        List<People> all=seeeon.getPeopleDao().loadAll();
        for(int i=0;i<all.size();++i){
            if(all.get(i).getId_phone().equals(App.phone))
                p.add(all.get(i));
        }
        return p;
    }
    /**
     *
     * 从数据库中查找当前用户的指定好友联系人
     *
     * */
    public  static People GetOnePeopeleByPhone(String phone){
        List<People> list= GetAllFriendsList();
        for (int i=0;i<list.size();i++){
            if (list.get(i).getUser_phone().equals(phone))
                return list.get(i);
        }
        return null;
    }

    /**
     *
     * 从数据库中查找当前用户的指定好友联系人,通过UID
     *
     * */
    public static People GetOnePeopeleByUID(String uid) {
        List<People> list= GetAllFriendsList();
        for (int i=0;i<list.size();i++){
            if (list.get(i).getUID().equals(uid))
                return list.get(i);
        }
        return null;
    }
    /**
     * 在数据库中删除用户所有的联系人信息
     * */
    public static void DeletAllFriendsList(){
        if(App.phone==null){
            App.phone=getUserMessage().getPhone();
        }
        SQLiteDatabase db = GreenDaoUtils.getSingleTon().getDb();
        DaoSession seeeon = GreenDaoUtils.getSingleTon().getmDaoSession();
        List<People> all=seeeon.getPeopleDao().loadAll();
        for(int i=0;i<all.size();++i){
            if(all.get(i).getId_phone().equals(App.phone))
                seeeon.getPeopleDao().delete(all.get(i));
        }
    }


    /**
     * 在数据库中更新指定联系人的信息
     * */
    public static void UpdateOneFriends(Context context,DetailpeopleBean.DataBean userbean){
        if(App.phone==null){
            App.phone=getUserMessage().getPhone();
        }
        DaoSession seeeon = GreenDaoUtils.getSingleTon().getmDaoSession();
        People p=GetOnePeopeleByPhone(userbean.getPhone());

            p.setImage(userbean.getPhoto());
//        }
        if(userbean.getAddress()==null)
            p.setAddress("未知地区");
        else
            p.setAddress(userbean.getAddress()+"");
        p.setNickname(userbean.getUserNickname()+"");
        p.setName(userbean.getUserName()+"");
        p.setBirthday(userbean.getBirthday()+"");
        p.setGender(userbean.getGender()+"");

        seeeon.getPeopleDao().update(p);
    }
    /**
     * 在数据库中更新指定联系人的备注和拼音名字信息,同时更改备注对应的base64image图片文本
     * */
    public static void UpdateBeiZhu(Context context,String phone,String message) {
        if(App.phone==null){
            App.phone=getUserMessage().getPhone();
        }
        DaoSession seeeon = GreenDaoUtils.getSingleTon().getmDaoSession();
        People p=GetOnePeopeleByPhone(phone);
        p.setPinyinname(PinYinUtil.getPinYin(message));
        p.setBeizhu(message);
        if(p.getImage()==null||p.getImage().equals("")) {
            p.setBitmap_base64(BitmapUtil.BitmapToString(BitmapUtil.GetUserImageByNickName(context,message)));
        }
        seeeon.getPeopleDao().update(p);
    }


}
