package com.mohamadjavadx.views.widget

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.time.LocalDateTime
import java.time.ZoneOffset


fun Context.getAlarmManager(): AlarmManager {
    return getSystemService(Context.ALARM_SERVICE) as AlarmManager
}

fun getNextMinuteInMillis(): Long {
    val localDateTime = LocalDateTime.now()
    val nextMinute = localDateTime.plusMinutes(1).withSecond(0).withNano(0)
    val utcTimeInMillis = nextMinute.toInstant(ZoneOffset.UTC).toEpochMilli()
    return utcTimeInMillis
}

private fun Context.createAlarmReceiverIntent(requestCode: Int): PendingIntent {
    return Intent(this, AlarmReceiver::class.java).let { intent ->
        PendingIntent.getBroadcast(
            /* context = */ this,
            /* requestCode = */ requestCode,
            /* intent = */ intent,
            /* flags = */ PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
}

fun Context.scheduleNextClockUpdate() {
    getAlarmManager().setAndAllowWhileIdle(
        AlarmManager.RTC,
        getNextMinuteInMillis(),
        createAlarmReceiverIntent(0),
    )
}

