package zph.zhjx.com.chat.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;

import zph.zhjx.com.chat.R;

/**
 * Created by adminZPH on 2017/3/6.
 */

public class ForceDownDialog  extends Dialog {
    public ForceDownDialog(Context context, String strMessage, int type) {
        this(context, R.style.ForceDownDialog, strMessage, type);
    }

    public ForceDownDialog(Context context, int theme, String strMessage, int type) {
        super(context, theme);
        this.setContentView(R.layout.forced_downline_layout);
        this.getWindow().getAttributes().gravity = Gravity.CENTER;
        setCanceledOnTouchOutside(false);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
    }
}
