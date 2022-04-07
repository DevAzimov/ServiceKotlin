package com.magicapp.serverskotlin.recivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status
import javax.net.ssl.SSLEngineResult

class SmsBroadcastReceiver: BroadcastReceiver() {

    var smsBroadcastReceiverListener: SmsBroadcastReceiverListener? = null

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == SmsRetriever.SMS_RETRIEVED_ACTION){
            val extras = intent.extras
            val smsReceiverStatus = extras!![SmsRetriever.EXTRA_STATUS] as Status
            when(smsReceiverStatus.statusCode) {
                CommonStatusCodes.SUCCESS -> {
                    val messageIntent = extras.getParcelable<Intent>(SmsRetriever.EXTRA_CONSENT_INTENT)
                    smsBroadcastReceiverListener!!.onSuccess(messageIntent!!)
                    Toast.makeText(context, "Sms", Toast.LENGTH_LONG).show()
                }

                CommonStatusCodes.TIMEOUT -> smsBroadcastReceiverListener!!.onFailure()

            }
            Toast.makeText(context, "Not Sms", Toast.LENGTH_LONG).show()
        }
    }

    interface SmsBroadcastReceiverListener{
        fun onSuccess(intent: Intent)
        fun onFailure()
    }
}