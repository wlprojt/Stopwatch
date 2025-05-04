package com.example.cstopw

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single {
        val context: Context = get()

        // Intent for tapping the notification body
        val contentIntent = Intent(context, MainActivity::class.java)
        val contentPendingIntent: PendingIntent = PendingIntent.getActivity(
            context, 0, contentIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )


        // Build the notification
        NotificationCompat.Builder(context, "stopwatch_channel")
            .setSmallIcon(R.drawable.baseline_watch_later_24)
            .setContentTitle("Stopwatch Running")
            .setContentText("00:00:00")
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true) // Keep the notification persistent
            .setContentIntent(contentPendingIntent)
    }

    single { (context: Context) ->
        NotificationManagerCompat.from(context)
    }
}

