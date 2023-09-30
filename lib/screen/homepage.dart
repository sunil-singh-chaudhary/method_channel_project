import 'package:flutter/material.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:permission_handler/permission_handler.dart';

import '../methodchannels/platform_channel_support.dart';

class MyHomePage extends StatefulWidget {
  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  int batteryPercentage = -1;
  String wifistatus = "";
  String macAddress = "";
  @override
  void initState() {
    requestLocationPermission();
    super.initState();
  }

  Future<void> requestLocationPermission() async {
    final status = await Permission.location.request();
    if (status.isGranted) {
      Fluttertoast.showToast(msg: 'Location permission granted.');
    } else {
      Fluttertoast.showToast(msg: 'Location permission denied.');
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Platform Channel Demo'),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            ElevatedButton(
              onPressed: () async {
                int bpercentage = await PlatformChannel.getBatteryPercentage();
                setState(() {
                  batteryPercentage = bpercentage;
                });
              },
              child: const Text('Get Battery Percentage'),
            ),
            _buildinfoDisplay('battery Percentage is ', batteryPercentage),
            ElevatedButton(
              onPressed: () async {
                String wifilevel = await PlatformChannel.getWifiStatus();
                debugPrint('wifiStatus -: $wifilevel');

                setState(() {
                  wifistatus = wifilevel;
                });
              },
              child: const Text('Get WiFi Status'),
            ),
            _buildinfoDisplay('wifi is', wifistatus),
            ElevatedButton(
              onPressed: () async {
                String mac = "";
                try {
                  mac = await PlatformChannel.getMacAddress();
                  debugPrint('macAddress: $mac');
                } catch (e) {
                  // Handle the error here, e.g., display an error message or a fallback widget.
                  Text('Error: ${e.toString()}');
                }
                setState(() {
                  macAddress = mac;
                });
              },
              child: const Text('Get MAC Address'),
            ),
            _buildinfoDisplay('macAddress is', macAddress),
          ],
        ),
      ),
    );
  }

  Widget _buildinfoDisplay(String name, dynamic value) {
    return Column(
      children: <Widget>[
        const SizedBox(
          height: 10,
        ),
        Text('$name -> $value'),
        const SizedBox(
          height: 10,
        ),
      ],
    );
  }
}
