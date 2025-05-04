package com.example.cstopw

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
import androidx.compose.foundation.layout.aspectRatio
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
fun MainScreenRotate(
    service: StopwatchService?
) {
    val hours = service?.hours?.collectAsState()?.value
    val minutes = service?.minutes?.collectAsState()?.value
    val seconds = service?.seconds?.collectAsState()?.value

    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 100.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Timer Display
        Box(
            modifier = Modifier
                .size(250.dp)
                .clip(CircleShape)
                .border(width = 8.dp, color = MaterialTheme.colorScheme.error, shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Row {
                AnimatedContent(
                    targetState = hours,
                    transitionSpec = {
                        ContentTransform(
                            targetContentEnter = slideInVertically { it / 2 } + fadeIn(),
                            initialContentExit = slideOutVertically { -it / 2 } + fadeOut()
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
                            targetContentEnter = slideInVertically { it / 2 } + fadeIn(),
                            initialContentExit = slideOutVertically { -it / 2 } + fadeOut()
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
                            targetContentEnter = slideInVertically { it / 2 } + fadeIn(),
                            initialContentExit = slideOutVertically { -it / 2 } + fadeOut()
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

        // Stopwatch Controls
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (service?.watchStart?.collectAsState()?.value == false) {
                Button(
                    onClick = {
                        service.startStopwatch()
                    },
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error),
                    modifier = Modifier.fillMaxWidth(0.5f)
                ) {
                    Text("Start")
                }
            } else {
                Button(
                    onClick = {
                        service?.stopStopwatch()
                    },
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error),
                    modifier = Modifier.fillMaxWidth(0.5f)
                ) {
                    Text("Stop")
                }
            }
            Spacer(modifier = Modifier.height(30.dp))
            Button(
                onClick = {
                    service?.resetStopwatch()
                },
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error),
                modifier = Modifier.fillMaxWidth(0.5f)
            ) {
                Text("Clear")
            }
        }
    }
}
