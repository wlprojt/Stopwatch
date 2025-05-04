package com.example.cstopw

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.androidx.compose.koinViewModel

@androidx.annotation.RequiresPermission(android.Manifest.permission.POST_NOTIFICATIONS)
@Composable
fun MainScreen(
    service: StopwatchService?
) {
    // Collect the current stopwatch time from the service
    val hours = service?.hours?.collectAsState()?.value
    val minutes = service?.minutes?.collectAsState()?.value
    val seconds = service?.seconds?.collectAsState()?.value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(250.dp) // Circle size
                .clip(CircleShape)
                .border(width = 8.dp, color = MaterialTheme.colorScheme.error, shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Row {
                AnimatedContent(
                    targetState = hours,
                    transitionSpec = {
                        ContentTransform(
                            targetContentEnter = slideInVertically{it} + fadeIn(),
                            initialContentExit = slideOutVertically{-it} + fadeOut()
                        )
                    }
                ) {
                    Text(
                        text = it.toString(),
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = ":",
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.error
                )
                AnimatedContent(
                    targetState = minutes,
                    transitionSpec = {
                        ContentTransform(
                            targetContentEnter = slideInVertically{it} + fadeIn(),
                            initialContentExit = slideOutVertically{-it} + fadeOut()
                        )
                    }
                ) {
                    Text(
                        text = it.toString(),
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = ":",
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.error
                )
                AnimatedContent(
                    targetState = seconds,
                    transitionSpec = {
                        ContentTransform(
                            targetContentEnter = slideInVertically{it} + fadeIn(),
                            initialContentExit = slideOutVertically{-it} + fadeOut()
                        )
                    }
                ) {
                    Text(
                        text = it.toString(),
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Stopwatch control buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            if (service?.watchStart?.collectAsState()?.value == false) {
                Button(
                    onClick = {
                        service.startStopwatch()
                    },
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error)
                ) {
                    Text("Start")
                }
            } else {
                Button(
                    onClick = {
                        service?.stopStopwatch()
                    },
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error)
                ) {
                    Text("Stop")
                }
            }
            Button(
                onClick = {
                    service?.resetStopwatch()
                },
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error)
            ) {
                Text("Clear")
            }
        }
    }
}

