package com.magicapp.serverskotlin

import android.content.*
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.widget.Button
import android.widget.TextView
import com.magicapp.serverskotlin.recivers.NetworkBroadcastReceiver
import com.magicapp.serverskotlin.recivers.PluginBroadcastReceiver
import com.magicapp.serverskotlin.recivers.SmsBroadcastReceiver
import com.magicapp.serverskotlin.recivers.UsbOrAcBroadcastReceiver
import com.magicapp.serverskotlin.services.BoundService
import com.magicapp.serverskotlin.services.StartedService

class MainActivity : AppCompatActivity() {

    lateinit var bt_callSmsRecActivity: Button
    lateinit var bt_start1:Button
    lateinit var bt_start2:Button
    lateinit var bt_stop1:Button
    lateinit var bt_stop2:Button
    lateinit var bt_timestamp:Button
    lateinit var bt_receiver:Button
    lateinit var tv_timestamp:TextView
    var isBound:Boolean = false
    var boundService: BoundService? = null
    lateinit var receiver: NetworkBroadcastReceiver
    lateinit var pluginreceiver: PluginBroadcastReceiver
    lateinit var usbReceiver: UsbOrAcBroadcastReceiver
    lateinit var smsReceiverActivity: SmsReceiverActivity
    lateinit var smsReceiver: SmsBroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()

    }

    private fun initViews() {
        bt_start1 = findViewById(R.id.bt_start1)
        bt_start2 = findViewById(R.id.bt_start2)
        bt_stop1 = findViewById(R.id.bt_stop1)
        bt_stop2 = findViewById(R.id.bt_stop2)
        bt_timestamp = findViewById(R.id.bt_timestamp)
        tv_timestamp = findViewById(R.id.tv_timestamp)

//        bt_callSmsRecActivity.setOnClickListener { callSmsReceiverActivity() }
        bt_start1.setOnClickListener { startStartedService() }
        bt_stop1.setOnClickListener { stopStartedService() }
        bt_start2.setOnClickListener { startBoundService() }
        bt_stop2.setOnClickListener { stopBoundService() }
        bt_timestamp.setOnClickListener {
            if (isBound) {
                tv_timestamp.text = boundService!!.timestamp
            }
        }
        receiver = NetworkBroadcastReceiver()
        pluginreceiver = PluginBroadcastReceiver()
        usbReceiver = UsbOrAcBroadcastReceiver()
        smsReceiverActivity = SmsReceiverActivity()
        smsReceiver = SmsBroadcastReceiver()

    }

    override fun onStart() {
        super.onStart()
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(receiver, filter)
        val usbFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        registerReceiver(usbReceiver, usbFilter)
        val pluginFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        registerReceiver(pluginreceiver, pluginFilter)

    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
        unregisterReceiver(usbReceiver)
        unregisterReceiver(pluginreceiver)

    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(usbReceiver)
    }

    private fun stopBoundService() {
        if (isBound) {
            unbindService(mServiceConnection)
            isBound = false
        }
    }

    private fun startBoundService() {
        val intent = Intent(this, BoundService::class.java)
        bindService(intent, mServiceConnection, BIND_AUTO_CREATE)
    }

    private val mServiceConnection: ServiceConnection = object :ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, service: IBinder?) {
            val myBinder: BoundService.MyBinder = service as BoundService.MyBinder
            boundService = myBinder.getService()
            isBound = true
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            isBound = false
        }

    }

    private fun stopStartedService() {
        val intent = Intent(this, StartedService::class.java)
        stopService(intent)
    }

    private fun startStartedService() {
        val intent = Intent(this, StartedService::class.java)
        startService(intent)
    }

//    private fun callSmsReceiverActivity() {
//        val intent = Intent(this, SmsReceiverActivity::class.java)
//        startActivity(intent)
//    }

}