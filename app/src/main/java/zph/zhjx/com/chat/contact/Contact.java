package zph.zhjx.com.chat.contact;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import zph.zhjx.com.chat.R;

/**
 * Created by adminZPH on 2017/2/9.
 */

public class Contact {
    public static enum Position {LEFT, CENTER, RIGHT}
//    private static final String  HOST="http://192.168.56.1:8080/";
    private static final String  HOST="http://192.168.3.1:8080/";
//    private static final String  HOST="http://zhjxdns.imwork.net:8088/";
//    private static final String  HOST="http://localhost:8080/";
    public static final String  IMAGE_HOST="http://192.168.3.1:8080/gridapp";
//    public static final String Login_Interface="http://192.168.3.2:8080/gridapp/client/";
    public static final String Login_Interface=HOST+"gridapp/client/";
    public static final String Log_Interface=HOST+"gridapp/client/login/";
    public static final String Nearbay_Interface=HOST+"gridapp/client/local/";
    public static final String People_Interface=HOST+"gridapp/client/friends/";
    public static final String LiuYan_Interface=HOST+"gridapp/client/leave/";
    public static final String Track_Interface=HOST+"/gridapp/client/user/movement/";
    public static final String APP_ID="";
    public static  final int[] markers = {
            R.mipmap.poi_marker_1,
            R.mipmap.poi_marker_2,
            R.mipmap.poi_marker_3,
            R.mipmap.poi_marker_4,
            R.mipmap.poi_marker_5,
            R.mipmap.poi_marker_6,
            R.mipmap.poi_marker_7,
            R.mipmap.poi_marker_8,
            R.mipmap.poi_marker_9,
            R.mipmap.poi_marker_10,
            R.mipmap.poi_marker_pressed
    };

    public static final String[] ImageBgColor={
            "#568AAD",
            "#17c295",
            "#4DA9EB",
            "#F2725E",
            "#B38979",
            "#568AAD"
    };
    public static final  String UpdateUrl="http://192.168.56.1:8080/gridapp/update_version.txt";


    public static final String LISTVIEW_DATABASE_NAME = "listview.db";
    public static final String RECYCLER_DATABASE_NAME = "recycler.db";
    public static final int SENDING = 0;
    public static final int COMPLETED = 1;
    public static final int SENDERROR = 2;

    @IntDef({SENDING, COMPLETED, SENDERROR})
    @Retention(RetentionPolicy.SOURCE)
    public @interface SendState {
    }


}
