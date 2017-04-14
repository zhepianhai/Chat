package zph.zhjx.com.chat.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import zph.zhjx.com.chat.R;
import zph.zhjx.com.chat.bean.NearbyListbean;
import zph.zhjx.com.chat.util.BitmapUtil;

/**
 * Created by adminZPH on 2017/3/13.
 */

public class NearbyPeopleAdapter extends BaseAdapter {
    private List<NearbyListbean> list;
    private Context context;
    public NearbyPeopleAdapter(List<NearbyListbean> list,Context context){
        this.list=list;
        this.context=context;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public NearbyListbean getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        NearViewHolder holder = null;
        if (convertView == null) {
            holder = new NearViewHolder();
            convertView = View.inflate(context, R.layout.item_listview_nearbay, null);
            holder.name = (TextView) convertView.findViewById(R.id.item_listview_nearby_name);
            holder.distance = (TextView) convertView.findViewById(R.id.item_listview_nearby_constance);
            holder.sex = (ImageView) convertView.findViewById(R.id.item_listview_nearby_sex);
            holder.photo = (ImageView) convertView.findViewById(R.id.item_listview_nearby_image);
            convertView.setTag(holder);
        } else {
            holder = (NearViewHolder) convertView.getTag();
        }
        NearbyListbean bean = getItem(i);
        Log.i("TAG","adapter:"+bean.toString());
        if(bean.getGender()!=null &&bean.getGender().equals("m")){
            holder.sex.setImageResource(R.mipmap.boy);
        }
        else if(bean.getGender()!=null &&bean.getGender().equals("f")){
            holder.sex.setImageResource(R.mipmap.gril);
        }
        holder.name.setText(bean.getName()+"");
        if(bean.getDistance()!=null &&!bean.getDistance().equals(""))
            holder.distance.setText(bean.getDistance()+"米以内");
        if(bean.getImageSrc()==null ||bean.getImageSrc().equals("")){
            holder.photo.setImageBitmap(BitmapUtil.GetUserImageByNickName(context,bean.getNickName()));
        }
        else
            Glide.with(context).load(bean.getImageSrc()+"").into(holder.photo);
        return convertView;
    }

    private class NearViewHolder {
        public TextView name;
        public TextView distance;
        public ImageView sex;
        public ImageView photo;
          /*  */
    }
    public void addAll(List<NearbyListbean> listAll){
        list.clear();
        list.addAll(listAll);
        notifyDataSetChanged();
    }
}
