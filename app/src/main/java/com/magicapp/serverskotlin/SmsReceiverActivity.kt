package com.magicapp.serverskotlin


import android.app.Activity
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.magicapp.serverskotlin.recivers.SmsBroadcastReceiver
import java.util.regex.Pattern


class SmsReceiverActivity : AppCompatActivity() {
    lateinit var smsBroadcastReceiver: SmsBroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_smsbroadcast)

        registerSmsReceiver()
        startSmsUserContent()
    }

    private fun registerSmsReceiver() {
        smsBroadcastReceiver = SmsBroadcastReceiver()
        smsBroadcastReceiver.smsBroadcastReceiverListener = object : SmsBroadcastReceiver.SmsBroadcastReceiverListener {
            override fun onSuccess(intent: Intent) {
                startActivityForResult(intent, 200)
                Toast.makeText(this@SmsReceiverActivity, "Sms", Toast.LENGTH_LONG).show()
            }

            override fun onFailure() {
                Log.d("@@errorR", "Something went wrong")
                Toast.makeText(this@SmsReceiverActivity, "Not Sms", Toast.LENGTH_LONG).show()
            }
        }
        val intentFilter = IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
        registerReceiver(smsBroadcastReceiver, intentFilter)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 200) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                val message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE)
                Log.d("@@message", message.toString())
                message.let {
                    getOtpFromMessage(message)
                }
            }
        }
    }

    private fun startSmsUserContent() {
        val client = SmsRetriever.getClient(this)
        client.startSmsUserConsent(null)
    }

    private fun getOtpFromMessage(message: String?) {
        val matcher = Pattern.compile("(|^)\\d{5}").matcher(message)
        if (matcher.find()) {
            Toast.makeText(this, matcher.group(0), Toast.LENGTH_SHORT).show()
        }
    }


}