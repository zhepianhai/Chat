package zph.zhjx.com.chat.util;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by adminZPH on 2017/3/30.
 */

public class TimeUtil {

    //判断结束日期是否大于起始日期

    public static boolean CheckStarEndTime(String star_time, String end_time) {
        boolean flag=false;
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date data1,data2;
        int type;
        try {
             data1 = sdf.parse(star_time);
             data2 = sdf.parse(end_time);
             type=data1.compareTo(data2);
             Log.i("TAG","结果是："+type);
            //-1 代表大于，0代表等于，1代表小于
            if(type==-1){
                return  true;
            }
            else return false;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }



    //判定给定的时间是否大于当前的系统时间
    public static boolean isTimeDaYuSystemTime(String time){
        boolean flag=false;
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String data3=sdf.format(new Date());
        Date data1,data2;
        int type;
        try {
            data1 = sdf.parse(time);
            data2=sdf.parse(data3);
            type=data1.compareTo(data2);
            Log.i("TAG","结果是："+type);
            //-1 代表大于，0代表等于，1代表小于
            if(type==-1 ||type==0){
                return  true;
            }
            else return false;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  flag;
    }

    /**
     * 返回当前的时间，格式是：yyyy-MM-dd HH:mm
     *
     * */
    public static String getSystemTime(){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Calendar now = Calendar.getInstance();
        now.setTime(new Date());
        now.set(Calendar.DATE, now.get(Calendar.DATE));
        return sdf.format(now.getTime());
    }
    /***
     * 返回当前时间前三天的时间，格式是：yyyy-MM-dd HH:mm
     * */
    public static String getSystemThreeDayTime(){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Calendar now = Calendar.getInstance();
        now.setTime(new Date());
        now.set(Calendar.DATE, now.get(Calendar.DATE) - 15);
        return sdf.format(now.getTime());
    }
}
