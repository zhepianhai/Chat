package zph.zhjx.com.chat.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;

import java.util.List;

import zph.zhjx.com.chat.R;
import zph.zhjx.com.chat.adapter.ListViewPopupwindowAdapter;
import zph.zhjx.com.chat.dao.People;
import zph.zhjx.com.chat.dao.User;
import zph.zhjx.com.chat.util.DBUtil;
import zph.zhjx.com.chat.util.PhoneUtil;

/**
 * Created by adminZPH on 2017/4/13.
 * 这个是显示联系人列表的Popupwindow
 */

public class ListViewPopupwindow extends PopupWindow {
    private View contentView;
    private Context context;
    private Activity activity;
    private ListView listview;
    private ListViewPopupwindowAdapter adapter;
    private List<People>list;
    public ListViewPopupwindow(Context context,Activity activity){
        this.activity=activity;
        this.context=context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        contentView = inflater.inflate(R.layout.item_popuwindow_listview, null);
        listview= (ListView) contentView.findViewById(R.id.popupwindow_listview);
        list= DBUtil.GetAllFriendsList();
        People p=new People();
        User pp = DBUtil.getUserMessage();
        p.setBeizhu(pp.getUserNickname());
        p.setBitmap_base64(pp.getImageBase64());
        p.setImage(pp.getImageSrc());
        p.setUser_phone(pp.getPhone());
        list.add(0,p);
        adapter=new ListViewPopupwindowAdapter(context,list);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(onOkClickListener!=null){
                    onOkClickListener.okClick(adapter.getItem(i));
                    dismiss();
                }
            }
        });



        this.setContentView(contentView);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        ColorDrawable dw = new ColorDrawable(00000000);
        this.setBackgroundDrawable(dw);
        this.setFocusable(true);
        this.setOutsideTouchable(false);
        this.update();
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void showFilterPopup(View parent) {
        if (!this.isShowing()) {
//            this.showAsDropDown(parent);
            this.showAtLocation(parent,Gravity.BOTTOM|Gravity.RIGHT,0,0);

        } else {
            this.dismiss();
        }
    }
    @Override
    public void dismiss() {
        super.dismiss();
        PhoneUtil.setBackgroundAlpha(activity,1f);
    }

    /**
     *  通过接口进行选择的类型信息回调
     *
     * */

    OnItemClickListener onOkClickListener;

    public void setOkClickListener(OnItemClickListener mOnItemClickListener) {
        onOkClickListener = mOnItemClickListener;
    }

    public interface OnItemClickListener {
        void okClick(People message);
    }
}
