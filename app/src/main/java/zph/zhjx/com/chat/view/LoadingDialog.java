package zph.zhjx.com.chat.view;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.widget.ProgressBar;
import android.widget.TextView;

import zph.zhjx.com.chat.R;

/**
 * Created by adminZPH on 2017/4/13.
 */

public class LoadingDialog extends Dialog {
    public LoadingDialog(Context context, String strMessage) {
        this(context, R.style.CustomProgressDialog, strMessage);
    }

    public LoadingDialog(Context context, int theme, String strMessage) {
        super(context, theme);
        this.setContentView(R.layout.common_dialog_loading_layout);
        this.getWindow().getAttributes().gravity = Gravity.CENTER;
        setCanceledOnTouchOutside(false);
        TextView tvMsg = (TextView) this.findViewById(R.id.id_tv_loadingmsg1);
        if (tvMsg != null) {
            tvMsg.setText(strMessage);
        }
        ProgressBar progressBar = (ProgressBar) this.findViewById(R.id.progressbar1);



    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (!hasFocus) {
//            dismiss();
            Log.i("TAG", "点解外部了");
        }
    }

}
