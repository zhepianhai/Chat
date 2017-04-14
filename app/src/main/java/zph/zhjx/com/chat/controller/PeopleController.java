package zph.zhjx.com.chat.controller;

import android.content.Context;

import zph.zhjx.com.chat.model.PeopleModel;
import zph.zhjx.com.chat.view.CustomProgressDialog;

/**
 * Created by adminZPH on 2017/3/14.
 */

public class PeopleController {
    private Context context;
    public PeopleController(Context context){
        this.context=context;
    }
    /**
     * @Controller 获取所有联系人的业务控制层
     * @param customProgressDialog*/
    public void getAllPeopleList(CustomProgressDialog customProgressDialog){
        PeopleModel.getAllPeopleList(context,customProgressDialog);
    }
    /**
     * @Controller 获取指定的联系人的业务控制层
     * */
    public void getDeatilPeopleMessage(String uid) {
        PeopleModel.getDeatilPeopleMessage(context,uid);
    }

    /**
     * @Controller 更新指定的联系人的备注业务控制层
     * */
    public void UpdatePeopleBeizhuMessage(String friend_phone,String content) {
        PeopleModel.UpdatePeopleBeizhuMessage(context,friend_phone,content);
    }
}
