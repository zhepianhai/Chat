package zph.zhjx.com.chat.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;

import zph.zhjx.com.chat.R;
import zph.zhjx.com.chat.util.PhoneUtil;

/**
 * Created by adminZPH on 2017/3/10.
 * 附近的人底部弹出自定义样式
 */

public class PopupDismissListener extends PopupWindow {


    private Button nv, nan, all,clear,sot,gaode,list;
    private View mMenuView;
    private Activity context;
    public PopupDismissListener(Activity context, View.OnClickListener itemsOnClick) {
        super(context);
        this.context=context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.buttom_item_nearby, null);
        nv = (Button) mMenuView.findViewById(R.id.buttom_nearby_nv);
        nan = (Button) mMenuView.findViewById(R.id.buttom_nearby_nan);
        all = (Button) mMenuView.findViewById(R.id.buttom_nearby_all);
        clear = (Button) mMenuView.findViewById(R.id.buttom_nearby_cls);
        sot = (Button) mMenuView.findViewById(R.id.buttom_nearby_sot);
        list = (Button) mMenuView.findViewById(R.id.buttom_nearby_list);
        gaode = (Button) mMenuView.findViewById(R.id.buttom_nearby_gaode);

        //设置按钮监听
        nv.setOnClickListener(itemsOnClick);
        nan.setOnClickListener(itemsOnClick);
        all.setOnClickListener(itemsOnClick);
        clear.setOnClickListener(itemsOnClick);
        sot.setOnClickListener(itemsOnClick);
        list.setOnClickListener(itemsOnClick);
        gaode.setOnClickListener(itemsOnClick);
        //设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.FILL_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.PopupAnimation);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mMenuView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = mMenuView.findViewById(R.id.pop_layout).getTop();
                int y=(int) event.getY();
                if(event.getAction()== MotionEvent.ACTION_UP){
                    if(y<height){
                        dismiss();
                    }
                }
                return true;
            }
        });

    }

    @Override
    public void dismiss() {
        super.dismiss();
        PhoneUtil.setBackgroundAlpha(context,1f);
    }
}
