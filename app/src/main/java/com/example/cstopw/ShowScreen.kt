package com.example.cstopw

import android.Manifest
import android.content.res.Configuration
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
@Composable
fun ShowScreen(service: StopwatchService?) {
    val configuration = LocalConfiguration.current
    when(configuration.orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> {
            MainScreenRotate(service = service)
        }

        else -> {
            MainScreen(service = service)
        }
    }
}