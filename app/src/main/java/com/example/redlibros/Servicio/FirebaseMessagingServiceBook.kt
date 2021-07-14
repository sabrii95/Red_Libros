package com.example.redlibros.Servicio

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FirebaseMessagingServiceBook : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        Log.d("Token", "Refreshed token: $token")
    }

    override fun onMessageReceived(data: RemoteMessage) {
        Looper.prepare()
        Handler().post{
            Toast.makeText(baseContext,data.notification?.body,Toast.LENGTH_LONG).show()
        }
        Looper.loop()
    }
}