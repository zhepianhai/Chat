package zph.zhjx.com.chat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import zph.zhjx.com.chat.R;
import zph.zhjx.com.chat.dao.People;
import zph.zhjx.com.chat.util.BitmapUtil;
import zph.zhjx.com.chat.view.CircleImageView;

/**
 * Created by adminZPH on 2017/3/14.
 */

public class SortAdapter extends BaseAdapter {
    private Context context;
    private List<People> persons;
    private LayoutInflater inflater;

    public SortAdapter(Context context, List<People> persons) {
        this.context = context;
        this.persons = persons;
        this.inflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return persons.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return persons.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewholder = null;
        People person = persons.get(position);
        if (convertView == null) {
            viewholder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_listview_people,null);
            viewholder.imageView = (CircleImageView) convertView
                    .findViewById(R.id.item_listview_people_image);
            viewholder.tv_tag= (TextView) convertView.findViewById(R.id.item_listview_people_tag);
            viewholder.tv_name = (TextView) convertView
                    .findViewById(R.id.item_listview_people_name);
            convertView.setTag(viewholder);
        } else {
            viewholder = (ViewHolder) convertView.getTag();
        }
        // 获取首字母的assii值
        int selection = person.getPinyinname().charAt(0);
        // 通过首字母的assii值来判断是否显示字母
        int positionForSelection = getPositionForSelection(selection);
        if (position == positionForSelection) {// 相等说明需要显示字母
            viewholder.tv_tag.setVisibility(View.VISIBLE);
            viewholder.tv_tag.setText(person.getPinyinname().charAt(0)+"");
        } else {
            viewholder.tv_tag.setVisibility(View.GONE);

        }
        viewholder.tv_name.setText(person.getBeizhu());
        if(person.getImage()==null ||person.getImage().equals("")){
            viewholder.imageView.setImageBitmap(BitmapUtil.base64ToBitmap(person.getBitmap_base64()));
        }
        else
            Glide.with(context).load(person.getImage()).into(viewholder.imageView);
        return convertView;
    }

    public int getPositionForSelection(int selection) {
        for (int i = 0; i < persons.size(); i++) {
            String Fpinyin = persons.get(i).getPinyinname();
            char first = Fpinyin.toUpperCase().charAt(0);
            if (first == selection) {
                return i;
            }
        }
        return -1;
    }

    class ViewHolder {
        TextView tv_tag;
        TextView tv_name;
        CircleImageView imageView;
    }

}
