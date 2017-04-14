package zph.zhjx.com.chat.view;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import zph.zhjx.com.chat.R;

/**
 * Created by adminZPH on 2016/8/26.
 */
public class CustomProgressDialog extends Dialog {
    public CustomProgressDialog(Context context, String strMessage, int type) {
        this(context, R.style.CustomProgressDialog, strMessage, type);
    }

    public CustomProgressDialog(Context context, int theme, String strMessage, int type) {
        super(context, theme);
        this.setContentView(R.layout.progress_dialog);
        this.getWindow().getAttributes().gravity = Gravity.CENTER;
        setCanceledOnTouchOutside(false);
        TextView tvMsg = (TextView) this.findViewById(R.id.id_tv_loadingmsg);
        if (tvMsg != null) {
            tvMsg.setText(strMessage);
        }
        ProgressBar progressBar = (ProgressBar) this.findViewById(R.id.progressbar);
        LinearLayout errorlayout = (LinearLayout) this.findViewById(R.id.error_layout);
        ImageView close = (ImageView) this.findViewById(R.id.close);
        switch (type) {
            case 0:
                //说明是成功的
                progressBar.setVisibility(View.VISIBLE);
                close.setVisibility(View.INVISIBLE);
                errorlayout.setVisibility(View.GONE);
                break;
            case 1:
                //说明是失败的
                progressBar.setVisibility(View.GONE);
                close.setVisibility(View.VISIBLE);
                errorlayout.setVisibility(View.VISIBLE);
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dismiss();
                    }
                });
                break;
        }

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (!hasFocus) {
//            dismiss();
            Log.i("TAG", "点解外部了");
        }
    }


}