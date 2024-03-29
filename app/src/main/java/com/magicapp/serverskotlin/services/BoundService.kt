package com.magicapp.serverskotlin.services

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.os.SystemClock
import android.widget.Chronometer
import android.widget.Toast

class BoundService : Service(){
    private val mBinder:Binder = MyBinder()
    private var mChronometer: Chronometer? = null

    override fun onCreate() {
        super.onCreate()
        Toast.makeText(this, "Bound Service Created", Toast.LENGTH_LONG).show()
        mChronometer = Chronometer(this)
        mChronometer!!.base = SystemClock.elapsedRealtime()
        mChronometer!!.onChronometerTickListener = Chronometer.OnChronometerTickListener { chronometer->
            Toast.makeText(application, chronometer.base.toString(), Toast.LENGTH_LONG).show()
        }
        mChronometer!!.start()
    }

    override fun onBind(p0: Intent?): IBinder? {
        Toast.makeText(this, "Bound Service onBind", Toast.LENGTH_LONG).show()
        return mBinder
    }

    override fun onRebind(intent: Intent) {
        Toast.makeText(this, "Bound Service onRebind", Toast.LENGTH_LONG).show()
        super.onRebind(intent)
    }

    override fun onUnbind(intent: Intent): Boolean {
        Toast.makeText(this, "Bound Service onUnbind", Toast.LENGTH_LONG).show()
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        Toast.makeText(this, "Bound Service Stopped", Toast.LENGTH_LONG).show()
        mChronometer!!.stop()
    }

    val timestamp: String
        get() {
            val elapsedMillis = SystemClock.elapsedRealtime() - mChronometer!!.base
            val hours = (elapsedMillis / 3600000).toInt()
            val minutes = (elapsedMillis - hours * 3600000).toInt() /6000
            val seconds = (elapsedMillis - hours * 3600000 - minutes * 6000).toInt() /1000
            val millis = (elapsedMillis - hours * 360000 - minutes * 6000 - seconds * 1000).toInt()
            return "$hours:$minutes:$seconds:$millis"
        }

    inner class MyBinder : Binder() {
        //
        fun getService(): BoundService = this@BoundService
    }

}


