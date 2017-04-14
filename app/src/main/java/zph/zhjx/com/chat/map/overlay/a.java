package zph.zhjx.com.chat.map.overlay;

import android.graphics.Bitmap;

import com.amap.api.maps2d.model.LatLng;
import com.amap.api.services.core.LatLonPoint;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/*
 * Created by adminZPH on 2017/3/30.
 *
 * 自定义图层Overlay
 * */

class a {
    private static int a = 2048;

    public static LatLng a(LatLonPoint var0) {
        return new LatLng(var0.getLatitude(), var0.getLongitude());
    }

    public static ArrayList<LatLng> a(List<LatLonPoint> var0) {
        ArrayList var1 = new ArrayList();
        Iterator var2 = var0.iterator();

        while(var2.hasNext()) {
            LatLonPoint var3 = (LatLonPoint)var2.next();
            LatLng var4 = a(var3);
            var1.add(var4);
        }

        return var1;
    }

    public static Bitmap a(Bitmap var0, float var1) {
        if(var0 == null) {
            return null;
        } else {
            int var2 = (int)((float)var0.getWidth() * var1);
            int var3 = (int)((float)var0.getHeight() * var1);
            Bitmap var4 = Bitmap.createScaledBitmap(var0, var2, var3, true);
            return var4;
        }
    }
}
