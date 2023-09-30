package com.example.battery_info_flutter
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.os.BatteryManager
import android.util.Log
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import java.net.NetworkInterface
import java.util.Collections

class MainActivity : FlutterActivity() {
    companion object {
        private const val BATTERY_CHANNEL = "battery"
        private const val WIFI_CHANNEL = "wifi"
        private const val MAC_ADDRESS_CHANNEL = "mac_address"
    }
     override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, BATTERY_CHANNEL)
            .setMethodCallHandler { call: MethodCall, result: MethodChannel.Result ->
                if (call.method == "getBatteryPercentage") {
                    val batteryPercentage = batteryPercentage
                    Log.e("android battery_info->", batteryPercentage.toString());
                    result.success(batteryPercentage)
                } else {
                    result.notImplemented()
                }
            }
        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, WIFI_CHANNEL)
            .setMethodCallHandler { call: MethodCall, result: MethodChannel.Result ->
                if (call.method == "getWifiStatus") {
                    val wifiStatus = wifiStatus
                    Log.e("android wifiStatus->", wifiStatus);

                    result.success(wifiStatus)
                } else {
                    result.notImplemented()
                }
            }
        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, MAC_ADDRESS_CHANNEL)
            .setMethodCallHandler { call: MethodCall, result: MethodChannel.Result ->
                if (call.method == "getMacAddress") {
                    val macAddress = macAddress
                    Log.e("android mac->", macAddress);

                    result.success(macAddress)
                } else {
                    result.notImplemented()
                }
            }
    }

    private val batteryPercentage: Int
        private get() {
            val ifilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
            val batteryStatus = registerReceiver(null, ifilter)
            val level = batteryStatus!!.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            val scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
            return if (level == -1 || scale == -1) {
                -1
            } else (level / scale.toFloat() * 100).toInt()
        }
    private val wifiStatus: String
        private get() {
            val connManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connManager.activeNetworkInfo
            return if (networkInfo != null && networkInfo.isConnected &&
                networkInfo.type == ConnectivityManager.TYPE_WIFI) {
                "Connected to Wi-Fi"
            } else {
                "Not connected to Wi-Fi"
            }
        }

//FIRST WAY TO GET MAC ADDRESS NOT WORKING ABOVE 29 ANDROID OS CAN GET ANDROID ID only SECURITY REASONS

//    private val macAddress: String
//        private get() {
//            val wifiManager =
//                getApplicationContext().getSystemService(WIFI_SERVICE) as WifiManager
//            return wifiManager.connectionInfo.macAddress
//        }


    //TRYING SECOND METHOD TO GET MAC ADDRESS TODO: Security related Problem above 29 for getting MAC Address
   // The code Below I provided attempts to retrieve the MAC address of the device
    // by iterating through the network interfaces and checking for the "wlan0" interface.
    // While this approach might work on some Android devices, it's important to
    // understand that accessing the MAC address directly is restricted on Android starting
    // from Android 10 (API level 29) for privacy and security reasons.


    private val macAddress: String
        private get() {
            try {
                val all = Collections.list(NetworkInterface.getNetworkInterfaces())
                for (nif in all) {
                    if (!nif.getName().equals("wlan0", ignoreCase=true)) continue

                    val macBytes = nif.getHardwareAddress() ?: return ""

                    val res1 = StringBuilder()
                    for (b in macBytes) {
                        //res1.append(Integer.toHexString(b & 0xFF) + ":");
                        res1.append(String.format("%02X:", b))
                    }

                    if (res1.length > 0) {
                        res1.deleteCharAt(res1.length - 1)
                    }
                    return res1.toString()
                }
            } catch (ex: Exception)
            {
                ex.printStackTrace()
            }

            return "02:00:00:00:00:00"
        }


}