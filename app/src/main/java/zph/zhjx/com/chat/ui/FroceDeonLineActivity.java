package zph.zhjx.com.chat.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import zph.zhjx.com.chat.R;
import zph.zhjx.com.chat.app.ActivityCollector;
import zph.zhjx.com.chat.base.BaseActivity;

import static zph.zhjx.com.chat.util.DBUtil.UpdateDb;

public class FroceDeonLineActivity extends BaseActivity implements View.OnClickListener {
    Button login,ok;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forced_downline_layout);
        ok= (Button) findViewById(R.id.force_ok_btn);
        login= (Button) findViewById(R.id.force_login_btn);
        ok.setOnClickListener(this);
        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.force_ok_btn:
                UpdateDb();
                ActivityCollector.finishAll();
                break;
            case R.id.force_login_btn:
                UpdateDb();
                Intent intent=new Intent(this,LoginActivity.class);
                startActivity(intent);

                finish();
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UpdateDb();
    }
}
