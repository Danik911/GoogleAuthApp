package com.example.googleauthapp.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.googleauthapp.domain.model.MessageBarState
import com.example.googleauthapp.ui.theme.EXTRA_LARGE_PADDING
import com.example.googleauthapp.ui.theme.ErrorRed
import com.example.googleauthapp.ui.theme.InfoGreen
import com.example.googleauthapp.ui.theme.MEDIUM_PADDING
import kotlinx.coroutines.delay
import java.net.ConnectException
import java.net.SocketTimeoutException

@Composable
fun MessageBar(messageBarState: MessageBarState) {

    var errorMessage by remember {
        mutableStateOf("")
    }
    var animate by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(key1 = messageBarState) {
        if (messageBarState.error != null) {
            when (messageBarState.error) {
                is ConnectException -> {
                    errorMessage = "No internet connection"
                }
                is SocketTimeoutException -> {
                    errorMessage = "Server is unavailable"
                }
                else -> {
                    "${messageBarState.error.message}"
                }
            }
        }
        animate = true
        delay(3000)
        animate = false
    }
    AnimatedVisibility(
        visible = (messageBarState.error != null && animate) ||
                (messageBarState.message != null && animate),
        enter = expandVertically(
            animationSpec = tween(durationMillis = 300),
            expandFrom = Alignment.Top
        ),
        exit = shrinkVertically(
            animationSpec = tween(durationMillis = 300),
            shrinkTowards = Alignment.Top
        )
    ) {
        Message(messageBarState = messageBarState, errorMessage = errorMessage)
    }
}

@Composable
fun Message(messageBarState: MessageBarState, errorMessage: String) {
    Row(
        modifier = Modifier

            .fillMaxWidth()
            .background(
                if (messageBarState.error != null) ErrorRed else InfoGreen
            )
            .padding(horizontal = EXTRA_LARGE_PADDING)
            .height(40.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = if (messageBarState.error != null) Icons.Default.Warning
            else Icons.Default.Check,
            contentDescription = "Info icon",
            tint = Color.White
        )
        Spacer(modifier = Modifier.width(MEDIUM_PADDING))
        Text(
            text = if (messageBarState.error != null) errorMessage
            else messageBarState.message.toString(),
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = MaterialTheme.typography.button.fontSize,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview
@Composable
fun MessageErrorPreview() {
    Message(
        messageBarState = MessageBarState(error = SocketTimeoutException()),
        errorMessage = "Server is unavailable"
    )
}
@Preview
@Composable
fun MessagePreview() {
    Message(
        messageBarState = MessageBarState(message = "Saved successfully"),
        errorMessage = "Server is unavailable"
    )
}