package com.mohamadjavadx.views.widget

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.glance.GlanceId
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class ClockWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        context.scheduleNextClockUpdate()

        provideContent {
            Column {
                Text(
                    text = LocalTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME),
                )
                Text(
                    text = getNextMinuteInMillis().toString(),
                )
                Text(
                    text = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli().toString(),
                )
            }
        }
    }

}