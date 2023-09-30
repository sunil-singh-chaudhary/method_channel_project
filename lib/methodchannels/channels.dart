// Get battery level.

import 'package:flutter/services.dart';

class PlatformChannel {
  static const batteryChannel = MethodChannel('battery');
  static const wifiChannel = MethodChannel('wifi');
  static const macAddressChannel = MethodChannel('mac_address');

  static Future<int> getBatteryPercentage() async {
    final int batteryPercentage =
        await batteryChannel.invokeMethod('getBatteryPercentage');
    return batteryPercentage;
  }

  static Future<String> getWifiStatus() async {
    final String wifiStatus = await wifiChannel.invokeMethod('getWifiStatus');
    return wifiStatus;
  }

  static Future<String> getMacAddress() async {
    final String macAddress =
        await macAddressChannel.invokeMethod('getMacAddress');
    return macAddress;
  }
}




















// import 'package:flutter/services.dart';

// class PlatformInfo {
//   static const platformbattery = MethodChannel('samples.flutter.dev/battery');
//   static const platformwifiStatus = MethodChannel('samples.flutter.dev/wifi');
//   static const platformMacStatus = MethodChannel('samples.flutter.dev/mac');

//   Future<void> _getBatteryLevel() async {
//     String batteryLevel;
//     try {
//       final int result = await platformbattery.invokeMethod('getBatteryLevel');
//       batteryLevel = 'Battery level at $result % .';
//     } on PlatformException catch (e) {
//       batteryLevel = "Failed to get battery level: '${e.message}'.";
//     }

//     setState(() {
//       widget._batteryLevel = batteryLevel;
//     });
//   }
// }
