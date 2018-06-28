package elite.driver.adsmove.com.settingpermission;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.admin.DevicePolicyManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;
import android.provider.SyncStateContract;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.security.Permission;

public class MainActivity extends AppCompatActivity {

    String TAG = "MainActivity";
    public static MainActivity mInstance;
    private static final int ADMIN_REQUEST = 0;
    private static final int BATTERY_REQUEST = 1;
    private static int MY_PERMISSIONS_REQUEST_LOCATION = 2;
    private BroadcastReceiver adminBroadcastReceiver;

    private ImageView mImgCheck_ReadFile;
    private ImageView mImgCheck_Bluetooth;
    private ImageView mImgCheck_Location;
    private ImageView mImgCheck_Camera;
    private ImageView mImgCheck_Administrator;
    private ImageView mImgCheck_Battery;
    private ImageView mImgCheck_AutoStart;
    private ImageView mImgCheck_EnergySaver;

    private LinearLayout btnReadFile;
    private LinearLayout btnBluetooth;
    private LinearLayout btnFineLocation;
    private LinearLayout btnCamera;
    private LinearLayout btnAdministrator;
    private LinearLayout btnBattery;
    private LinearLayout btnAutoStart;
    private LinearLayout btnEnergySaver;
    private LinearLayout btnLowPowerMode;

    private static final int READ_WRITE_REQUEST = 3;
    private static final int BLUETOOTH_REQUEST = 4;
    private static final int LOCATION_REQUEST = 5;
    private static final int CAMERA_REQUEST = 6;
    private static final int AUTOSTART_REQUEST = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mInstance = this;

        mImgCheck_ReadFile = (ImageView) findViewById(R.id.icCheck_ReadFile);
        mImgCheck_Bluetooth = (ImageView) findViewById(R.id.icCheck_Bluetooth);
        mImgCheck_Location = (ImageView) findViewById(R.id.icCheck_Location);
        mImgCheck_Camera = (ImageView) findViewById(R.id.icCheck_Camera);
        mImgCheck_Administrator = (ImageView) findViewById(R.id.icCheck_Administrator);
        mImgCheck_Battery = (ImageView) findViewById(R.id.icCheck_Battery);
        mImgCheck_AutoStart = (ImageView) findViewById(R.id.icCheck_AutoStart);


        btnReadFile = (LinearLayout) findViewById(R.id.lnReadFile);
        btnBluetooth = (LinearLayout) findViewById(R.id.lnBluetooth);
        btnFineLocation = (LinearLayout) findViewById(R.id.lnLocation);
        btnCamera = (LinearLayout) findViewById(R.id.lnCamera);
        btnAdministrator = (LinearLayout) findViewById(R.id.lnAdministrator);
        btnBattery = (LinearLayout) findViewById(R.id.lnBattery);
        btnAutoStart = (LinearLayout) findViewById(R.id.lnAutoStart);
        btnEnergySaver = (LinearLayout) findViewById(R.id.lnEnergySaver);
        btnLowPowerMode = (LinearLayout) findViewById(R.id.lnLowPowerMode);

        clickButtonPermission();

        detectAdminStatus();
        requestAllPermission();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            showRequestIgnoreBatteryOptimization();
        }

         showPopupRequestAdminPermission();

        //   requestAutoStartPermission();
        //   requestLowPowerModePermission();
        //   requestEnergySaverPermission();

        checkAllPermission();
    }

    private void clickButtonPermission() {
        btnReadFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] permissions = {
                        "android.permission.READ_EXTERNAL_STORAGE",
                        "android.permission.WRITE_EXTERNAL_STORAGE"};

                ActivityCompat.requestPermissions(MainActivity.this, permissions, READ_WRITE_REQUEST);


            }
        });

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] permissions = {
                        "android.permission.CAMERA"};

                ActivityCompat.requestPermissions(MainActivity.this, permissions, CAMERA_REQUEST);

            }
        });
        btnBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] permissions = {
                        "android.permission.BLUETOOTH_ADMIN",
                        "android.permission.BLUETOOTH"};
                ActivityCompat.requestPermissions(MainActivity.this, permissions, BLUETOOTH_REQUEST);
            }
        });

        btnFineLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] permissions = {
                        "android.permission.ACCESS_FINE_LOCATION",
                        "android.permission.ACCESS_COARSE_LOCATION"};
                ActivityCompat.requestPermissions(MainActivity.this, permissions, LOCATION_REQUEST);

            }
        });
        btnAdministrator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestAdminPermission();
            }
        });

        btnBattery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestIgnoreBatteryOptimization();
                }
            }
        });

        btnAutoStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                requestAutoStartPermission();
            }
        });
        btnEnergySaver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestEnergySaverPermission();
            }
        });
        btnLowPowerMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestLowPowerModePermission();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADMIN_REQUEST) {
            if (resultCode == RESULT_CANCELED) {
                Log.d(TAG, "ADMIN_REQUEST1 - resultCode: " + RESULT_CANCELED);
                startActivity(new Intent().setComponent(new ComponentName("com.android.settings", "com.android.settings.DeviceAdminSettings")));
            }
        } else if (requestCode == BATTERY_REQUEST) {
            if (resultCode == RESULT_OK) {
                mImgCheck_Battery.setImageResource(R.drawable.ic_checkwarring);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void checkRequestPermission(ImageView imageView, String[] permission) {
        try {
            for (int i = 0; i < permission.length; i++) {
                if (ActivityCompat.checkSelfPermission(this, permission[i])
                        == PackageManager.PERMISSION_GRANTED) {
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permission[i])) {
                        Log.d(TAG, permission + "NEVER ASK AGAIN");
                        imageView.setImageResource(R.drawable.ic_checktrue);
                    } else {
                        Log.d(TAG, permission + "ASK AGAIN");
                        imageView.setImageResource(R.drawable.ic_checkwarring);
                    }
                } else {
                    Log.d(TAG, permission + "NOT ALLOWS");
                    imageView.setImageResource(R.drawable.ic_checkfalse);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        checkAllPermission();
    }


    private void checkAllPermission() {

        checkRequestPermission(mImgCheck_ReadFile, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE});
        checkRequestPermission(mImgCheck_Location, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION});
        checkRequestPermission(mImgCheck_Bluetooth, new String[]{Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.BLUETOOTH});
        checkRequestPermission(mImgCheck_Camera, new String[]{Manifest.permission.CAMERA});
    }

    private void requestAllPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN)
                    != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                String[] permissions = {
                        "android.permission.READ_EXTERNAL_STORAGE",
                        "android.permission.WRITE_EXTERNAL_STORAGE",
                        "android.permission.ACCESS_FINE_LOCATION",
                        "android.permission.ACCESS_COARSE_LOCATION",
                        "android.permission.BLUETOOTH_ADMIN",
                        "android.permission.BLUETOOTH",
                        Manifest.permission.CAMERA
                };
                mImgCheck_ReadFile.setImageResource(R.drawable.ic_checkfalse);
                mImgCheck_Location.setImageResource(R.drawable.ic_checkfalse);
                mImgCheck_Bluetooth.setImageResource(R.drawable.ic_checkfalse);
                mImgCheck_Camera.setImageResource(R.drawable.ic_checkfalse);

                requestPermissions(permissions, MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                //do something
            }
        }
    }

    private void requestLowPowerModePermission() {
        if (Build.MANUFACTURER.equals("OPPO")) {
            try {
                startActivity(new Intent().setComponent(new ComponentName("com.coloros.oppoguardelf", "com.coloros.powermanager.fuelgaue.PowerUsageModelActivity")));
            } catch (Exception e) {
                try {
                    startActivity(new Intent().setComponent(new ComponentName("com.coloros.oppoguardelf", "com.coloros.powermanager.fuelgaue.PowerSaverModeActivity")));
                } catch (Exception e1) {
                    try {
                        startActivity(new Intent().setComponent(new ComponentName("com.coloros.oppoguardelf", "com.coloros.powermanager.fuelgaue.PowerConsumptionActivity")));
                    } catch (Exception e2) {

                    }
                }
            }

        }
    }

    private void requestEnergySaverPermission() {
        if (Build.MANUFACTURER.equals("OPPO")) {
            try {
                startActivity(new Intent().setComponent(new ComponentName("com.coloros.oppoguardelf", "com.coloros.powermanager.fuelgaue.PowerUsageModelActivity")));
            } catch (Exception e) {
                try {
                    startActivity(new Intent().setComponent(new ComponentName("com.coloros.oppoguardelf", "com.coloros.powermanager.fuelgaue.PowerSaverModeActivity")));
                } catch (Exception e1) {
                    try {
                        startActivity(new Intent().setComponent(new ComponentName("com.coloros.oppoguardelf", "com.coloros.powermanager.fuelgaue.PowerConsumptionActivity")));
                    } catch (Exception e2) {

                    }
                }
            }

        }

    }

    private void requestAutoStartPermission() {
        //com.coloros.safecenter.permission.singlepage.PermissionSinglePageActivity     listpermissions
        //com.coloros.privacypermissionsentry.PermissionTopActivity                     privacypermissions
        // getPackageManager().getLaunchIntentForPackage("com.coloros.safecenter");
        if (Build.MANUFACTURER.equals("OPPO")) {
            try {
                startActivity(new Intent().setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.startup.FakeActivity")));
            } catch (Exception e) {
                try {
                    startActivity(new Intent().setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.startupapp.StartupAppListActivity")));
                } catch (Exception e1) {
                    try {
                        startActivity(new Intent().setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.startupmanager.StartupAppListActivity")));
                    } catch (Exception e2) {
                        try {
                            startActivity(new Intent().setComponent(new ComponentName("com.coloros.safe", "com.coloros.safe.permission.startup.StartupAppListActivity")));
                        } catch (Exception e3) {
                            try {
                                startActivity(new Intent().setComponent(new ComponentName("com.coloros.safe", "com.coloros.safe.permission.startupapp.StartupAppListActivity")));
                            } catch (Exception e4) {
                                try {
                                    startActivity(new Intent().setComponent(new ComponentName("com.coloros.safe", "com.coloros.safe.permission.startupmanager.StartupAppListActivity")));
                                } catch (Exception e5) {
                                    try {
                                        startActivity(new Intent().setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.startsettings")));
                                    } catch (Exception e6) {
                                        try {
                                            startActivity(new Intent().setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.startupapp.startupmanager")));
                                        } catch (Exception e7) {
                                            try {
                                                startActivity(new Intent().setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.startupmanager.startupActivity")));
                                            } catch (Exception e8) {
                                                try {
                                                    startActivity(new Intent().setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.startup.startupapp.startupmanager")));
                                                } catch (Exception e9) {
                                                    try {
                                                        startActivity(new Intent().setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.privacypermissionsentry.PermissionTopActivity.Startupmanager")));
                                                    } catch (Exception e10) {
                                                        try {
                                                            startActivity(new Intent().setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.privacypermissionsentry.PermissionTopActivity")));
                                                        } catch (Exception e11) {
                                                            try {
                                                                startActivity(new Intent().setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.FakeActivity")));
                                                            } catch (Exception e12) {
                                                                e12.printStackTrace();
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void showPopupRequestAdminPermission() {
        try {
            DevicePolicyManager mDPM = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
            ComponentName mAdminName = new ComponentName(getPackageName(), DeviceAdminControl.class.getName());
            if (mDPM != null) {
                if (!mDPM.isAdminActive(mAdminName)) {
                    requestAdminPermission();
                } else {
                    mImgCheck_Administrator.setImageResource(R.drawable.ic_checkwarring);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void requestAdminPermission() {
        // try to become active
        ComponentName mAdminName = new ComponentName(getPackageName(), DeviceAdminControl.class.getName());
//        ComponentName mAdminName = new ComponentName(this, DeviceAdminControl.class);
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mAdminName);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Click on Activate button to secure your application.");
        startActivityForResult(intent, ADMIN_REQUEST);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void showRequestIgnoreBatteryOptimization() {
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        if (pm != null && !pm.isIgnoringBatteryOptimizations(getPackageName())) {
            requestIgnoreBatteryOptimization();
        } else {
            Log.d(TAG, "IgnoringBatteryOptimizations not show");

            checkRequestPermission(mImgCheck_Battery, new String[]{Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS});
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestIgnoreBatteryOptimization() {
        try {
            String packageName = getPackageName();
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse("package:" + packageName));
            startActivityForResult(intent, BATTERY_REQUEST);
        } catch (ActivityNotFoundException e) {
            Log.d(TAG, "android.content.ActivityNotFoundException " + e.getLocalizedMessage());
        }
    }

    private void detectAdminStatus() {
        adminBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action != null) {
                    switch (action) {
                        case DeviceAdminControl.ENABLE_ACTION:
                            Log.d(TAG, "ENABLE_ACTION");
                            mImgCheck_Administrator.setImageResource(R.drawable.ic_checkwarring);
                            break;
                        case DeviceAdminControl.DISABLE_ACTION:
                            Log.d(TAG, "DISABLE_ACTION");
                            mImgCheck_Administrator.setImageResource(R.drawable.ic_checkfalse);
                            break;
                    }
                    Log.d(TAG, "detectAdminStatus " + action);
                    //    NotificationUtil.updateNotification();
                }
            }
        };

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(DeviceAdminControl.DISABLE_ACTION);
        intentFilter.addAction(DeviceAdminControl.ENABLE_ACTION);

        LocalBroadcastManager.getInstance(this).registerReceiver(adminBroadcastReceiver, intentFilter);
    }
}

