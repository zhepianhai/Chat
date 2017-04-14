package zph.zhjx.com.chat.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import zph.zhjx.com.chat.R;
import zph.zhjx.com.chat.dao.User;
import zph.zhjx.com.chat.ui.LiuYanActivity;
import zph.zhjx.com.chat.ui.NearbyPeopleActivity;
import zph.zhjx.com.chat.ui.Tracked3Activity;
import zph.zhjx.com.chat.util.DBUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class GeoSotFragment extends Fragment implements View.OnClickListener {
/*    public static GeoSotFragment newInstance(String content) {
        Bundle args = new Bundle();
        args.putString("ARGS", content);
        GeoSotFragment fragment = new GeoSotFragment();
        fragment.setArguments(args);
        return fragment;
    }*/

    public GeoSotFragment() {
        // Required empty public constructor
    }
    private final String TAG="GeoSotFragment";
    private View view;
    private RelativeLayout fujin,liuyan,guiji;
    private ImageView zhuji;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_geo_sot, container, false);
        initview();
        setClickListen();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        LoadDBData();
    }

    private void LoadDBData() {
        //设置足迹可见性
        User u=DBUtil.getUserMessage();
        if(u!=null){
            if(u.getPosition()){
                Log.i(TAG,"足迹信息可见");
                zhuji.setVisibility(View.VISIBLE);
            }
            else{
                Log.i(TAG,"足迹信息不可见");
                zhuji.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void setClickListen() {
        fujin.setOnClickListener(this);
        liuyan.setOnClickListener(this);
        guiji.setOnClickListener(this);
    }

    private void initview() {
        fujin= (RelativeLayout) view.findViewById(R.id.fujinderen_layout);
        liuyan= (RelativeLayout) view.findViewById(R.id.liuyan_layout);
        guiji= (RelativeLayout) view.findViewById(R.id.guiji_layout);
        zhuji= (ImageView) view.findViewById(R.id.zhuji);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fujinderen_layout:
                Intent intent=new Intent(getActivity(), NearbyPeopleActivity.class);
                getActivity().startActivity(intent);
                break;
            case R.id.liuyan_layout:
                Intent intent1=new Intent(getActivity(), LiuYanActivity.class);
                getActivity().startActivity(intent1);
                break;
            case R.id.guiji_layout:
                Intent intent2=new Intent(getActivity(), Tracked3Activity.class);
                getActivity().startActivity(intent2);
                break;
        }
    }
}
