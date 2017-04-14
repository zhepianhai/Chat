package zph.zhjx.com.chat.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import zph.zhjx.com.chat.R;


/**
 * Created by adminZPH on 2016/8/16.
 * 这里是用来滑动页面效果的
 *
 */
public class TopIndicator extends LinearLayout {
    private List<CheckedTextView> mCheckedList = new ArrayList<CheckedTextView>();
    private List<View> mViewList = new ArrayList<View>();
    // 顶部菜单的文字数组
    private CharSequence[] mLabels = new CharSequence[3];

    public void setTopText(Context context,String... s){
        mLabels[0]=s[0];
        mLabels[1]=s[1];
        mLabels[2]=s[2];
        init(context);

    }
    public void setTopText(Context context,String s1,String s2,String s3){
        mLabels = new CharSequence[3];
        mLabels[0]=s1;
        mLabels[1]=s2;
        mLabels[2]=s3;
        init(context);
    }
    public void setTopText(Context context,String s1,String s2){
        mLabels = new CharSequence[2];
        mLabels[0]=s1;
        mLabels[1]=s2;
        init(context);
    }
    private int mScreenWidth;
    private int mUnderLineWidth;
    private View mUnderLine;
    // 底部线条移动初始位置
    private int mUnderLineFromX = 0;

    public TopIndicator(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public TopIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TopIndicator(Context context) {
        super(context);
    }

    private void init(Context context) {

        setOrientation(LinearLayout.VERTICAL);
        this.setBackgroundColor(Color.WHITE);

        mScreenWidth = context.getResources().getDisplayMetrics().widthPixels;
        mUnderLineWidth = mScreenWidth / mLabels.length;

        mUnderLine = new View(context);
        mUnderLine.setBackgroundColor(Color.parseColor("#22759f"));
        LayoutParams underLineParams = new LayoutParams(
                mUnderLineWidth, 3);

        // 标题layout
        LinearLayout topLayout = new LinearLayout(context);
        LayoutParams topLayoutParams = new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        topLayout.setOrientation(LinearLayout.HORIZONTAL);

        LayoutInflater inflater = LayoutInflater.from(context);

        LayoutParams params = new LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        params.weight = 1.0f;
        params.gravity = Gravity.CENTER;

        int size = mLabels.length;
        for (int i = 0; i < size; i++) {
            Log.i("TAG","看看size是几？:"+size);
            final int index = i;
            final View view = inflater.inflate(R.layout.top_indicator_item,
                    null);

            final CheckedTextView itemName = (CheckedTextView) view
                    .findViewById(R.id.item_name);

            itemName.setCompoundDrawablePadding(10);
            itemName.setText(mLabels[i]);

            topLayout.addView(view, params);
            Log.i("TAG","卡看看这里添加了几次");
            itemName.setTag(index);

            mCheckedList.add(itemName);
            mViewList.add(view);

            view.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (null != mTabListener) {
                        mTabListener.onIndicatorSelected(index);
                    }
                }
            });

           // 初始化 底部菜单选中状态,默认第一个选中
            if (i==0) {
                itemName.setChecked(true);
                itemName.setTextColor(Color.parseColor("#22759f"));

            } else {
                itemName.setChecked(false);
                itemName.setTextColor(Color.BLACK);
            }

        }
//        mCheckedList.clear();
        mViewList.clear();
        this.addView(topLayout, topLayoutParams);
        this.addView(mUnderLine, underLineParams);
    }

    /**
     * 设置底部导航中图片显示状态和字体颜色
     */
    public void setTabsDisplay(Context context, int index) {
        int size = mCheckedList.size();
        for (int i = 0; i < size; i++) {
            CheckedTextView checkedTextView = mCheckedList.get(i);
            if ((Integer) (checkedTextView.getTag()) == index) {
                checkedTextView.setChecked(true);
                checkedTextView.setTextColor(Color.parseColor("#22759f"));
            } else {
                checkedTextView.setChecked(false);
                checkedTextView.setTextColor(Color.BLACK);
            }
        }
        // 下划线动画
        doUnderLineAnimation(index);
    }

    private void doUnderLineAnimation(int index) {
        TranslateAnimation animation = new TranslateAnimation(mUnderLineFromX,
                index * mUnderLineWidth, 0, 0);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        animation.setFillAfter(true);
        animation.setDuration(150);
        mUnderLine.startAnimation(animation);
        mUnderLineFromX = index * mUnderLineWidth;

    }

    // 回调接口
    private OnTopIndicatorListener mTabListener;

    public interface OnTopIndicatorListener {
        void onIndicatorSelected(int index);
    }

    public void setOnTopIndicatorListener(OnTopIndicatorListener listener) {
        this.mTabListener = listener;
    }

}
