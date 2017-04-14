package zph.zhjx.com.chat.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.PopupWindow;

import java.util.ArrayList;
import java.util.List;

import zph.zhjx.com.chat.R;
import zph.zhjx.com.chat.adapter.SelectAdapter;
import zph.zhjx.com.chat.bean.SaleAttribute;
import zph.zhjx.com.chat.util.PhoneUtil;

/**
 * Created by adminZPH on 2017/3/28.
 * 自定义的筛选popupwindow类
 */

public class SelectPopupwindow  extends PopupWindow{
    private View contentView;
    private  Context context;
    private Activity activity;
    private SelectAdapter selectAdapter1,selectAdapter2,selectAdapter3;
    private List<SaleAttribute> list1,list2,list3,list4;
    private GridView gridView1,gridView2,gridView3;
    private Button reseted,ok;

    public SelectPopupwindow(final Context context, Activity activity, boolean isGeoSOT, boolean isWeeked, boolean isCompare){
        this.context=context;
        this.activity=activity;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        contentView = inflater.inflate(R.layout.item_popuwindow_select, null);
        gridView1= (GridView) contentView.findViewById(R.id.select_gridview_geosot);
        gridView2= (GridView) contentView.findViewById(R.id.select_gridview_date);
        gridView3= (GridView) contentView.findViewById(R.id.select_gridview_compare);
        reseted= (Button) contentView.findViewById(R.id.select_gridview_reset);
        ok= (Button) contentView.findViewById(R.id.select_gridview_sure);
        initDate(isGeoSOT,isWeeked,isCompare);
        selectAdapter1=new SelectAdapter(context,list1);
        gridView1.setAdapter(selectAdapter1);
        gridView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                //设置当前选中的位置的状态为非。
                list1.get(arg2).setChecked(true);
                for (int i = 0; i < list1.size(); i++) {
                    //跳过已设置的选中的位置的状态
                    if (i == arg2) {
                        continue;
                    }
                    list1.get(i).setChecked(false);
                    Log.i("TAG","第"+i+"个"+list1.get(i).isChecked()+"");
                }
                selectAdapter1=new SelectAdapter(context,list1);
                gridView1.setAdapter(selectAdapter1);
            }
        });



        selectAdapter2=new SelectAdapter(context,list2);
        gridView2.setAdapter(selectAdapter2);
        gridView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                //设置当前选中的位置的状态为非。
                list2.get(arg2).setChecked(true);
                for (int i = 0; i < list2.size(); i++) {
                    //跳过已设置的选中的位置的状态
                    if (i == arg2) {
                        continue;
                    }
                    list2.get(i).setChecked(false);
                }
                selectAdapter2=new SelectAdapter(context,list2);
                gridView2.setAdapter(selectAdapter2);
            }
        });


        selectAdapter3=new SelectAdapter(context,list3);
        gridView3.setAdapter(selectAdapter3);
        gridView3.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                //设置当前选中的位置的状态为非。
                list3.get(arg2).setChecked(true);
                for (int i = 0; i < list3.size(); i++) {
                    //跳过已设置的选中的位置的状态
                    if (i == arg2) {
                        continue;
                    }
                    list3.get(i).setChecked(false);
                }
                selectAdapter3=new SelectAdapter(context,list3);
                gridView3.setAdapter(selectAdapter3);
            }
        });


        // 重置的点击监听，将所有选项全设为false
        reseted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < list1.size(); i++) {
                    list1.get(i).setChecked(false);
                }
                for (int i = 0; i < list2.size(); i++) {
                    list2.get(i).setChecked(false);
                }
                for (int i = 0; i < list3.size(); i++) {
                    list3.get(i).setChecked(false);
                }
                selectAdapter1.notifyDataSetChanged();
                selectAdapter2.notifyDataSetChanged();
                selectAdapter3.notifyDataSetChanged();
            }
        });
        // 确定的点击监听，将所有已选中项列出
       ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("TAG","onClick");
                int type_1=0,type_2=0,type_3=0;
                type_1=list1.get(0).isChecked()? 0:1;
                type_2=list2.get(0).isChecked()? 0:1;
                type_3=list3.get(0).isChecked()? 0:1;
                if(onOkClickListener!=null){
                    onOkClickListener.okClick(type_1+"-"+type_2+"-"+type_3);
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
    /**
     * 进行数据的初始化
     *
     * @param isGeoSOT
     * @param isWeeked
     * @param isCompare*/
    private void initDate(boolean isGeoSOT, boolean isWeeked, boolean isCompare) {
        SaleAttribute s=new SaleAttribute("网格查询","1",isGeoSOT);
        SaleAttribute s1=new SaleAttribute("非网格查询","2",!isGeoSOT);
        list1=new ArrayList<>();
        list1.add(s);
        list1.add(s1);

        SaleAttribute s2=new SaleAttribute("按天查询","3",isWeeked);
        SaleAttribute s3=new SaleAttribute("按周查询","4",!isWeeked);
        list2=new ArrayList<>();
        list2.add(s2);
        list2.add(s3);

        SaleAttribute s4=new SaleAttribute("对比查询","1",isCompare);
        SaleAttribute s5=new SaleAttribute("非对比查询","1",!isCompare);
        list3=new ArrayList<>();
        list3.add(s4);
        list3.add(s5);
    }
    public void showFilterPopup(View parent) {
        if (!this.isShowing()) {
            this.showAsDropDown(parent);

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

    OnOkClickListener onOkClickListener;

    public void setOkClickListener(OnOkClickListener mOnItemClickListener) {
        onOkClickListener = mOnItemClickListener;
    }

    public interface OnOkClickListener {
        void okClick(String message);
    }

}
