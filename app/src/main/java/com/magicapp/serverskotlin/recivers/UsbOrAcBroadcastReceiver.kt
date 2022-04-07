package com.magicapp.serverskotlin.recivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.BatteryManager
import android.widget.Toast

class UsbOrAcBroadcastReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        // isCharging if true indicates charging is ongoing and vice-versa
        val status: Int = intent?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1
        val isCharging: Boolean = status == BatteryManager.BATTERY_STATUS_CHARGING
                || status == BatteryManager.BATTERY_STATUS_FULL

        // usbCharge is true when connected to usb port and same with the ac wall charger
        val chargePlug: Int = intent?.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1) ?: -1
        val usbCharge: Boolean = chargePlug == BatteryManager.BATTERY_PLUGGED_USB
        val acCharge: Boolean = chargePlug == BatteryManager.BATTERY_PLUGGED_AC

        // Display whatever the state in the form of a Toast
        when {
            usbCharge -> {
                Toast.makeText(context, "USB Charging", Toast.LENGTH_LONG).show()
            }
            acCharge -> {
                Toast.makeText(context, "AC Charging", Toast.LENGTH_LONG).show()
            }
            else -> {
                Toast.makeText(context, "Not Charging", Toast.LENGTH_LONG).show()
            }
        }
    }
}