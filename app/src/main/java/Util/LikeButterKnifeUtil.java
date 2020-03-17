package Util;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import java.lang.reflect.Method;

import static android.content.ContentValues.TAG;

public class LikeButterKnifeUtil {
    public static void bind(Activity activity) {
        Class<? extends Activity> cls = activity.getClass();
        try {
            String clsName = cls.getName() + "_ViewBinding";
            Class<?> bindCls = Class.forName(clsName);
            Method bind = bindCls.getMethod("bind", cls);
            bind.invoke(bindCls.newInstance(), activity);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "bind: " + e.toString());
        }
    }
}
