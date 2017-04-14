package zph.zhjx.com.chat.base;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import zph.zhjx.com.chat.R;
import zph.zhjx.com.chat.contact.Contact;
import zph.zhjx.com.chat.widget.pulltorefresh.PullToRefreshLayout;

public class BaseChatActivity extends BaseActivity {
    public PullToRefreshLayout pullList;
    public EditText mEditTextContent;
    public Button sendBtn;
    private View headview;
    /**
     * 设置标题
     * */
    public void setTitle(String name) {
        setHeaderTitle(headview,name);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_chat);
        setstatusbarcolor();
        initview();
        setBack();
        init();
    }

    private void init() {
        mEditTextContent.setOnKeyListener(onKeyListener);

        sendBtn.setOnClickListener(onClickListener);
    }
    private View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (!mEditTextContent.getText().equals("")){
                sendMessage();
            }
        }
    };


    private View.OnKeyListener onKeyListener = new View.OnKeyListener() {

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_ENTER
                    && event.getAction() == KeyEvent.ACTION_DOWN) {
                sendMessage();
                return true;
            }
            return false;
        }
    };


    private void sendMessage() {
    }




    /**
     * 返回监听事件
     * */
    private void setBack() {
        setHeaderImage(headview, Contact.Position.LEFT, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initview() {
        headview=findViewById(R.id.headview1);
        pullList= (PullToRefreshLayout) findViewById(R.id.content_lv);
        mEditTextContent= (EditText) findViewById(R.id.mess_et);
        sendBtn= (Button) findViewById(R.id.message_send_btn);
    }
}
