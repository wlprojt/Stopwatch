package com.example.cstopw

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import java.util.Timer
import kotlin.concurrent.fixedRateTimer


class StopwatchService : Service() {

    private val notificationBuilder: NotificationCompat.Builder by inject()
    private val notificationManager: NotificationManagerCompat by inject { parametersOf(this) }

    private var duration = 0L
    private var timer: Timer? = null

    private val _timeState = MutableStateFlow("00:00:00")
    val timeState: StateFlow<String> = _timeState

    private val _hours = MutableStateFlow("00")
    private val _minutes = MutableStateFlow("00")
    private val _seconds = MutableStateFlow("00")

    val hours: StateFlow<String> = _hours
    val minutes: StateFlow<String> = _minutes
    val seconds: StateFlow<String> = _seconds

    private val _watchStart = MutableStateFlow(false)
    val watchStart: StateFlow<Boolean> = _watchStart

    override fun onBind(intent: Intent?): IBinder = StopwatchBinder()

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    fun startStopwatch() {
        timer?.cancel()
        _watchStart.value = true
        timer = fixedRateTimer("stopwatch", initialDelay = 1000L, period = 1000L) {
            duration++
            val hours = (duration / 3600).toString().padStart(2, '0')
            val minutes = ((duration % 3600) / 60).toString().padStart(2, '0')
            val seconds = (duration % 60).toString().padStart(2, '0')
            _timeState.value = "$hours:$minutes:$seconds"
            _hours.value = hours
            _minutes.value = minutes
            _seconds.value = seconds
            updateNotification()
        }
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    fun stopStopwatch() {
        _watchStart.value = false
        timer?.cancel()
        updateNotification()
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    fun resetStopwatch() {
        _watchStart.value = false
        timer?.cancel()
        duration = 0L
        _timeState.value = "00:00:00"
        _hours.value = "00"
        _minutes.value = "00"
        _seconds.value = "00"
        notificationManager.cancel(1)
    }

    private fun createServiceIntent(action: String): Intent {
        return Intent(this, StopwatchService::class.java).apply { this.action = action }
    }

    private fun createPendingIntent(intent: Intent): PendingIntent {
        return PendingIntent.getService(
            this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    private fun updateNotification() {
        val stopIntent = createServiceIntent("STOP_STOPWATCH")
        val stopPendingIntent = createPendingIntent(stopIntent)

        val startIntent = createServiceIntent("START_STOPWATCH")
        val startPendingIntent = createPendingIntent(startIntent)

        // Intent for the "Clear" button
        val clearIntent = Intent(this, StopwatchService::class.java).apply {
            action = "CLEAR"
        }
        val clearPendingIntent: PendingIntent = PendingIntent.getService(
            this, 0, clearIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val actionTitle = if (_watchStart.value) "Stop" else "Start"
        val actionPendingIntent = if (_watchStart.value) stopPendingIntent else startPendingIntent

        notificationBuilder
            .setContentText(_timeState.value)
            .clearActions()
            .addAction(0, actionTitle, actionPendingIntent)
            .addAction(0, "Clear", clearPendingIntent)

        notificationManager.notify(1, notificationBuilder.build())
    }


    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            "STOP_STOPWATCH" -> {
                if (_watchStart.value) {
                    stopStopwatch()
                    updateNotification()
                }
            }
            "START_STOPWATCH" -> {
                if (!_watchStart.value) {
                    startStopwatch()
                    updateNotification()
                }
            }
            "CLEAR" -> {
              resetStopwatch()
            }
            else -> {
                Log.w("StopwatchService", "Unknown action: ${intent?.action}")
            }
        }
        return START_STICKY
    }



    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "stopwatch_channel",
                "Stopwatch",
                NotificationManager.IMPORTANCE_LOW
            )
            getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
        }
    }

    inner class StopwatchBinder : Binder() {
        fun getService(): StopwatchService = this@StopwatchService
    }
}
