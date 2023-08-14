package com.mohamadjavadx.views.widget

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.glance.appwidget.updateAll
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class ClockUpdateService : Service() {

    private val serviceScope = CoroutineScope(Dispatchers.Default)

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        scheduleNextClockUpdate()
        serviceScope.launch {
            ClockWidget().updateAll(this@ClockUpdateService.applicationContext)
            stopSelf()
        }
        return START_STICKY
    }


    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

}