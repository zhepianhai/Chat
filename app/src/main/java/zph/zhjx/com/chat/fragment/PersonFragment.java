package zph.zhjx.com.chat.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Collections;
import java.util.List;

import zph.zhjx.com.chat.R;
import zph.zhjx.com.chat.adapter.SortAdapter;
import zph.zhjx.com.chat.controller.PeopleController;
import zph.zhjx.com.chat.dao.People;
import zph.zhjx.com.chat.ui.DetailsPeopleActivity;
import zph.zhjx.com.chat.util.DBUtil;
import zph.zhjx.com.chat.util.PinyinComparator;
import zph.zhjx.com.chat.view.CustomProgressDialog;
import zph.zhjx.com.chat.view.SlideBar;


/**
 * A simple {@link Fragment} subclass.
 */
public class PersonFragment extends Fragment implements AdapterView.OnItemClickListener {
   /* public static PersonFragment newInstance(String content) {
        Bundle args = new Bundle();
        args.putString("ARGS", content);
        PersonFragment fragment = new PersonFragment();
        fragment.setArguments(args);
        return fragment;
    }*/

    public PersonFragment() {
        // Required empty public constructor
    }
    private final String TAG="PersonFragment";
    private SlideBar mSlideBar;
    private View view;
    private PeopleController mPeopleController;
    private ListView listView;
    private SortAdapter mSortAdapter;
    private List<People> data;
    private TextView dialog;
    private CustomProgressDialog customProgressDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_person, container, false);
        mPeopleController=new PeopleController(getActivity());
        EventBus.getDefault().register(PersonFragment.this);
        initview();
        initslidebar();
        if(data!=null && savedInstanceState!=null){
            listView.setTop(savedInstanceState.getInt("position"));

        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        LoadingDate();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("position",listView.getFirstVisiblePosition());

    }

    /**
     * 加载数据
     * */
    private void LoadingDate() {
        if(DBUtil.GetAllFriendsList().size()==0) {
            Log.i(TAG,"去服务器加载...");
            customProgressDialog=new CustomProgressDialog(getActivity(),"加载中...",0);
            customProgressDialog.show();
            mPeopleController.getAllPeopleList(customProgressDialog);
        }
        else{
            Log.i(TAG,"去本地数据加载..."+DBUtil.GetAllFriendsList().size());
            data=DBUtil.GetAllFriendsList();
            ShowDate();
        }
    }
    //联系人信息列表的订阅者 
    @Subscribe(threadMode= ThreadMode.MAIN)
    public void onLogEventBus(List<People> peoples) {
       if(peoples.size()>0){
           //写入数据库
           DBUtil.InsertIntoDBFriends(peoples);
           data=peoples;

           ShowDate();
       }
    }
    private void ShowDate(){

        // 数据在放在adapter之前需要排序
        Collections.sort(data, new PinyinComparator());
        Log.i("TAG","看看这个的base64:"+data.get(0).getBitmap_base64());
        mSortAdapter = new SortAdapter(getActivity(),data);
        listView.setAdapter(mSortAdapter);
        listView.setOnItemClickListener(this);
    }
    private void initslidebar() {
//        mSlideBar=new SlideBar(getActivity());
        mSlideBar.setTextView(dialog);
        // 设置字母导航触摸监听
        mSlideBar.setOnTouchingLetterChangedListener(new SlideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                // TODO Auto-generated method stub
                // 该字母首次出现的位置
                if(s!=null &&mSortAdapter!=null) {
                    int position = mSortAdapter.getPositionForSelection(s.charAt(0));
                    if (position != -1) {
                        listView.setSelection(position);
                    }
                }
            }
        });
    }

    private void initview() {
        mSlideBar= (SlideBar) view.findViewById(R.id.slidebar);
        listView= (ListView) view.findViewById(R.id.people_listview);
        dialog= (TextView) view.findViewById(R.id.dialog);
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(PersonFragment.this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(PersonFragment.this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent=new Intent(getActivity(), DetailsPeopleActivity.class);
        intent.putExtra("uid",data.get(i).getUID());
        startActivity(intent);
    }
}







