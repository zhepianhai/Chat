package zph.zhjx.com.chat.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import zph.zhjx.com.chat.R;
import zph.zhjx.com.chat.dao.User;
import zph.zhjx.com.chat.ui.SettingActivity;
import zph.zhjx.com.chat.util.BitmapUtil;
import zph.zhjx.com.chat.util.DBUtil;
import zph.zhjx.com.chat.view.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class MeFragment extends Fragment implements View.OnClickListener {
 /*   public static MeFragment newInstance(String content) {
        Bundle args = new Bundle();
        args.putString("ARGS", content);
        MeFragment fragment = new MeFragment();
        fragment.setArguments(args);
        return fragment;
    }
*/
    public MeFragment() {
        // Required empty public constructor
    }
    private  View view;
    private RelativeLayout logout;
    private final String TAG="MeFragment";

    private CircleImageView imageView;
    private ImageView sex;
    private TextView name,nickname;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_me, container, false);
        initview();
        setOnClick();
        LoadingDate();
        return view;
    }

    private void LoadingDate() {
        User message = DBUtil.getUserMessage();
        if(message.getImageSrc()!=null &&!"".equals(message.getImageSrc())) {
            Glide.with(getActivity()).load(message.getImageSrc()).into(imageView);
        }
       else{
            imageView.setImageBitmap(BitmapUtil.base64ToBitmap(message.getImageBase64()));
        }
        if("m".equals(message.getGender()+"")){
            sex.setImageResource(R.mipmap.boy);
        }
        else{
            sex.setImageResource(R.mipmap.gril);
        }
        name.setText(""+message.getUserName());
        nickname.setText("昵称:"+message.getUserNickname());
    }

    private void setOnClick() {
        logout.setOnClickListener(this);
    }

    private void initview() {
        logout= (RelativeLayout) view.findViewById(R.id.me_logout);
        imageView= (CircleImageView) view.findViewById(R.id.me_message_image);
        sex= (ImageView) view.findViewById(R.id.me_message_sex);
        name= (TextView) view.findViewById(R.id.me_message_username);
        nickname= (TextView) view.findViewById(R.id.me_message_nickname);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.me_logout:
                Intent intent=new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
            break;

        }
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
