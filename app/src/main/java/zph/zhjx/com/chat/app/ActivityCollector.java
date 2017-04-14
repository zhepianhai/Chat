package zph.zhjx.com.chat.app;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adminZPH on 2017/3/6.
 * 活动管理容器
 */

public class ActivityCollector {
    public static List<Activity> activities = new ArrayList<Activity>();
    public static void addActivity(Activity activity) {
        activities.add(activity);
    }
    public static void removeActivity(Activity activity) {
        activities.remove(activity);
    }
    public static void finishAll() {
        for (Activity activity : activities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }
}
