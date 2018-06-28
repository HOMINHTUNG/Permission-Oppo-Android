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
    private static int MY_PERMISSIONS_REQUEST_LOCATION;
    private BroadcastReceiver adminBroadcastReceiver;

    private ImageView mImgCheck_ReadFile;
    private ImageView mImgCheck_WriteFile;
    private ImageView mImgCheck_Bluetooth;
    private ImageView mImgCheck_BluetoothAdmin;
    private ImageView mImgCheck_FineLocation;
    private ImageView mImgCheck_CoarseLocation;
    private ImageView mImgCheck_Camera;
    private ImageView mImgCheck_Administrator;
    private ImageView mImgCheck_Battery;
    private ImageView mImgCheck_AutoStart;

    private LinearLayout btnReadFile;
    private LinearLayout btnWriteFile;
    private LinearLayout btnBluetooth;
    private LinearLayout btnBluetoothAdmin;
    private LinearLayout btnFineLocation;
    private LinearLayout btnCoarseLocation;
    private LinearLayout btnCamera;
    private LinearLayout btnAdministrator;
    private LinearLayout btnBattery;
    private LinearLayout btnAutoStart;


    private static final int READ_WRITE_REQUEST = 10;
    private static final int BLUETOOTH_REQUEST = 11;
    private static final int LOCATION_REQUEST = 12;
    private static final int CAMERA_REQUEST = 13;
    private static final int AUTOSTART_REQUEST = 13;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mInstance = this;

        initPalatte();
        clickButtonPermission();

        detectAdminStatus();
        requestAllPermission();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            showRequestIgnoreBatteryOptimization();
        }

        showPopupRequestAdminPermission();

        requestAutoStartPermission();

        checkAllPermission();
    }

    private void initPalatte() {
        mImgCheck_ReadFile = (ImageView) findViewById(R.id.icCheck_ReadFile);
        mImgCheck_WriteFile = (ImageView) findViewById(R.id.icCheck_WriteFile);
        mImgCheck_Bluetooth = (ImageView) findViewById(R.id.icCheck_Bluetooth);
        mImgCheck_BluetoothAdmin = (ImageView) findViewById(R.id.icCheck_BluetoothAdmin);
        mImgCheck_FineLocation = (ImageView) findViewById(R.id.icCheck_FineLocation);
        mImgCheck_CoarseLocation = (ImageView) findViewById(R.id.icCheck_CoarseLocation);
        mImgCheck_Camera = (ImageView) findViewById(R.id.icCheck_Camera);
        mImgCheck_Administrator = (ImageView) findViewById(R.id.icCheck_Administrator);
        mImgCheck_Battery = (ImageView) findViewById(R.id.icCheck_Battery);
        mImgCheck_AutoStart = (ImageView) findViewById(R.id.icCheck_AutoStart);

        btnReadFile = (LinearLayout) findViewById(R.id.lnReadFile);
        btnWriteFile = (LinearLayout) findViewById(R.id.lnWriteFile);
        btnBluetooth = (LinearLayout) findViewById(R.id.lnBluetooth);
        btnBluetoothAdmin = (LinearLayout) findViewById(R.id.lnBluetoothAdmin);
        btnFineLocation = (LinearLayout) findViewById(R.id.lnFineLocation);
        btnCoarseLocation = (LinearLayout) findViewById(R.id.lnCoarseLocation);
        btnCamera = (LinearLayout) findViewById(R.id.lnCamera);
        btnAdministrator = (LinearLayout) findViewById(R.id.lnAdministrator);
        btnBattery = (LinearLayout) findViewById(R.id.lnBattery);
        btnAutoStart = (LinearLayout) findViewById(R.id.lnAutoStart);
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
        btnWriteFile.setOnClickListener(new View.OnClickListener() {
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
        btnBluetoothAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] permissions = {
                        "android.permission.BLUETOOTH_ADMIN",
                        "android.permission.BLUETOOTH"};
                ActivityCompat.requestPermissions(MainActivity.this, permissions, BLUETOOTH_REQUEST);
            }
        });
        btnCoarseLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] permissions = {
                        "android.permission.ACCESS_FINE_LOCATION",
                        "android.permission.ACCESS_COARSE_LOCATION"};
                ActivityCompat.requestPermissions(MainActivity.this, permissions, LOCATION_REQUEST);
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADMIN_REQUEST) {
            if (resultCode == RESULT_CANCELED) {
                Log.d(TAG, "ADMIN_REQUEST1 - resultCode: " + RESULT_CANCELED);
                startActivity(new Intent().setComponent(new ComponentName("com.android.settings", "com.android.settings.DeviceAdminSettings")));
            }
        }
        if (requestCode == BATTERY_REQUEST) {
            if (resultCode == RESULT_OK) {
                mImgCheck_Battery.setImageResource(R.drawable.ic_checktrue);
            }
        }

        if(requestCode == AUTOSTART_REQUEST) {
            if (resultCode == RESULT_OK) {
                mImgCheck_AutoStart.setImageResource(R.drawable.ic_checktrue);
            }
        }


        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
            Log.d(TAG, "onRequestPermissionsResult");

            checkAllPermission();
        } else {
            //do something
        }
    }


    private void checkAllPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            mImgCheck_ReadFile.setImageResource(R.drawable.ic_checktrue);
            mImgCheck_WriteFile.setImageResource(R.drawable.ic_checktrue);
        } else {
            mImgCheck_ReadFile.setImageResource(R.drawable.ic_checkfalse);
            mImgCheck_WriteFile.setImageResource(R.drawable.ic_checkfalse);
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mImgCheck_FineLocation.setImageResource(R.drawable.ic_checktrue);
        } else {
            mImgCheck_FineLocation.setImageResource(R.drawable.ic_checkfalse);
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mImgCheck_CoarseLocation.setImageResource(R.drawable.ic_checktrue);
        } else {
            mImgCheck_CoarseLocation.setImageResource(R.drawable.ic_checkfalse);

        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN)
                == PackageManager.PERMISSION_GRANTED) {
            mImgCheck_BluetoothAdmin.setImageResource(R.drawable.ic_checktrue);
        } else {
            mImgCheck_BluetoothAdmin.setImageResource(R.drawable.ic_checkfalse);

        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH)
                == PackageManager.PERMISSION_GRANTED) {
            mImgCheck_Bluetooth.setImageResource(R.drawable.ic_checktrue);
        } else {
            mImgCheck_Bluetooth.setImageResource(R.drawable.ic_checkfalse);

        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            mImgCheck_Camera.setImageResource(R.drawable.ic_checktrue);
        } else {
            mImgCheck_Camera.setImageResource(R.drawable.ic_checkfalse);
        }


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
                    || ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH)
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
                mImgCheck_WriteFile.setImageResource(R.drawable.ic_checkfalse);
                mImgCheck_FineLocation.setImageResource(R.drawable.ic_checkfalse);
                mImgCheck_CoarseLocation.setImageResource(R.drawable.ic_checkfalse);
                mImgCheck_BluetoothAdmin.setImageResource(R.drawable.ic_checkfalse);
                mImgCheck_Bluetooth.setImageResource(R.drawable.ic_checkfalse);
                mImgCheck_Camera.setImageResource(R.drawable.ic_checkfalse);

                requestPermissions(permissions, MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                checkAllPermission();
            }
        }
    }

    private void requestAutoStartPermission() {
        try {
            if ("asus".equalsIgnoreCase(Build.MANUFACTURER)) {
                try {
                    Intent intent = new Intent();
                    intent.setClassName("com.asus.mobilemanager", "com.asus.mobilemanager.MainActivity");
                    startActivityForResult(intent, AUTOSTART_REQUEST);
                } catch (ActivityNotFoundException e) {
                    Log.d(TAG, "requestAutoStartPermission: " + e.getMessage());
                }
            } else if ("xiaomi".equalsIgnoreCase(Build.MANUFACTURER)) {
                try {
                    Intent intent = new Intent();
                    intent.setComponent(new ComponentName(
                            "com.miui.securitycenter",
                            "com.miui.permcenter.autostart.AutoStartManagementActivity"));
                    startActivityForResult(intent, AUTOSTART_REQUEST);
                } catch (ActivityNotFoundException e) {
                    Log.d(TAG, "requestAutoStartPermission: " + e.getMessage());
                }
            } else if ("oppo".equalsIgnoreCase(Build.MANUFACTURER)) {
                try {
                    Intent intent = new Intent();
                    intent.setClassName("com.coloros.safecenter",
                            "com.coloros.safecenter.permission.startup.StartupAppListActivity");
                    startActivityForResult(intent, AUTOSTART_REQUEST);
                } catch (Exception e) {
                    try {
                        Intent intent = new Intent();
                        intent.setClassName("com.oppo.safe",
                                "com.oppo.safe.permission.startup.StartupAppListActivity");
                        startActivityForResult(intent, AUTOSTART_REQUEST);
                    } catch (Exception ex) {
                        try {
                            Intent intent = new Intent();
                            intent.setClassName("com.coloros.safecenter",
                                    "com.coloros.safecenter.startupapp.StartupAppListActivity");
                            startActivityForResult(intent, AUTOSTART_REQUEST);
                        } catch (Exception exx) {
                            Log.d(TAG, "requestAutoStartPermission: " + e.getMessage());
                        }
                    }
                }
            } else if ("Letv".equalsIgnoreCase(Build.MANUFACTURER)) {
                try {
                    Intent intent = new Intent();
                    intent.setComponent(new ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.AutobootManageActivity"));
                    startActivityForResult(intent, AUTOSTART_REQUEST);
                } catch (ActivityNotFoundException e) {
                    Log.d(TAG, "requestAutoStartPermission: " + e.getMessage());
                }
            } else if ("Huawei".equalsIgnoreCase(Build.MANUFACTURER)) {
                try {
                    Intent intent = new Intent();
                    intent.setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity"));
                    startActivityForResult(intent, AUTOSTART_REQUEST);
                } catch (ActivityNotFoundException e) {
                    Log.d(TAG, "requestAutoStartPermission: " + e.getMessage());
                }
            } else if ("vivo".equalsIgnoreCase(Build.MANUFACTURER)) {
                try {
                    Intent intent = new Intent();
                    intent.setComponent(new ComponentName("com.iqoo.secure",
                            "com.iqoo.secure.ui.phoneoptimize.AddWhiteListActivity"));
                    startActivityForResult(intent, AUTOSTART_REQUEST);
                } catch (Exception e) {
                    try {
                        Intent intent = new Intent();
                        intent.setComponent(new ComponentName("com.vivo.permissionmanager",
                                "com.vivo.permissionmanager.activity.BgStartUpManagerActivity"));
                        startActivityForResult(intent, AUTOSTART_REQUEST);
                    } catch (Exception ex) {
                        try {
                            Intent intent = new Intent();
                            intent.setClassName("com.iqoo.secure",
                                    "com.iqoo.secure.ui.phoneoptimize.BgStartUpManager");
                            startActivityForResult(intent, AUTOSTART_REQUEST);
                        } catch (Exception exx) {
                            ex.printStackTrace();
                        }
                    }
                }
            }else if ("samsung".equalsIgnoreCase(Build.MANUFACTURER)) {
               btnAutoStart.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
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
                    mImgCheck_Administrator.setImageResource(R.drawable.ic_checktrue);
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

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS) == PackageManager.PERMISSION_GRANTED) {
                mImgCheck_Battery.setImageResource(R.drawable.ic_checktrue);
            } else {
                mImgCheck_Battery.setImageResource(R.drawable.ic_checkfalse);
            }
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
                            mImgCheck_Administrator.setImageResource(R.drawable.ic_checktrue);
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

