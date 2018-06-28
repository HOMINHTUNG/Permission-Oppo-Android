package elite.driver.adsmove.com.settingpermission;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

/**
 * Created by HOMINHTUNG-PC on 4/10/2018.
 */

public class DeviceAdminControl extends DeviceAdminReceiver {

    public static final String ENABLE_ACTION = "admin_enable_action";
    public static final String DISABLE_ACTION = "admin_disable_action";

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }

    @Override
    public void onEnabled(Context context, Intent intent) {
        super.onEnabled(context, intent);
        if (context != null) {
            Intent homeIntent = new Intent(context, MainActivity.class);
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(homeIntent);
        }
        Intent enableIntent = new Intent(ENABLE_ACTION);
        LocalBroadcastManager.getInstance(context).sendBroadcast(enableIntent);
    }

    @Override
    public void onDisabled(Context context, Intent intent) {
        super.onDisabled(context, intent);
        Intent enableIntent = new Intent(DISABLE_ACTION);
        LocalBroadcastManager.getInstance(context).sendBroadcast(enableIntent);
    }
}