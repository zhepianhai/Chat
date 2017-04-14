package zph.zhjx.com.chat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import zph.zhjx.com.chat.R;
import zph.zhjx.com.chat.dao.People;
import zph.zhjx.com.chat.util.BitmapUtil;

/**
 * Created by adminZPH on 2017/3/27.
 * 轨迹中界面的底部水平滚动列表
 */

public class HorizontalScrollViewAdapter
{

    private Context mContext;
    private LayoutInflater mInflater;
    private List<People> mDatas;

    public HorizontalScrollViewAdapter(Context context, List<People> mDatas)
    {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        this.mDatas = mDatas;
    }

    public int getCount()
    {
        return mDatas.size();
    }

    public People getItem(int position)
    {
        return mDatas.get(position);
    }

    public long getItemId(int position)
    {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder viewHolder = null;
        if (convertView == null)
        {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(
                    R.layout.item_listview_tracked, parent, false);
            viewHolder.mImg = (ImageView) convertView
                    .findViewById(R.id.item_listview_tracked_image);
            viewHolder.mText = (TextView) convertView
                    .findViewById(R.id.item_listview_tracked_name);

            convertView.setTag(viewHolder);
        } else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        People person = mDatas.get(position);
        if(person.getImage()==null ||person.getImage().equals("")){
            viewHolder.mImg.setImageBitmap(BitmapUtil.base64ToBitmap(person.getBitmap_base64()));
        }
        else{
            Glide.with(mContext).load(person.getImage()).into(viewHolder.mImg);
        }
        if(person.getBeizhu()==null ||person.getBeizhu().equals("")){
            viewHolder.mText.setText(" ");
        }
        else{
            viewHolder.mText.setText(person.getBeizhu());
        }
        return convertView;
    }

    private class ViewHolder
    {
        ImageView mImg;
        TextView mText;
    }

}

