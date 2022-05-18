package com.example.googleauthapp.presentation.screens.login

import androidx.compose.foundation.layout.*
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.googleauthapp.R
import com.example.googleauthapp.domain.model.MessageBarState
import com.example.googleauthapp.presentation.components.GoogleButton
import com.example.googleauthapp.presentation.components.MessageBar
import com.example.googleauthapp.ui.theme.MEDIUM_PADDING
import com.example.googleauthapp.ui.theme.SMALL_PADDING

@Composable
fun LoginContent(
    messageBarState: MessageBarState,
    loginState: Boolean,
    onLoginClicked: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Column(modifier = Modifier.weight(1f)) {
            MessageBar(messageBarState = messageBarState)
        }
        Column(
            modifier = Modifier
                .weight(9f)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            MiddleContent(loginState = loginState, onLoginClicked = onLoginClicked)

        }
    }


}

@Composable
fun MiddleContent(loginState: Boolean, onLoginClicked: () -> Unit) {
    Icon(
        modifier = Modifier
            .size(120.dp)
            .padding(bottom = MEDIUM_PADDING),
        painter = painterResource(id = R.drawable.ic_google_logo),
        contentDescription = "Google logo",
        tint = Color.Unspecified
    )
    Text(
        text = "Sign in to Continue",
        fontWeight = FontWeight.Bold,
        fontSize = MaterialTheme.typography.h5.fontSize
    )
    Text(
        modifier = Modifier
            .padding(vertical = SMALL_PADDING)
            .alpha(ContentAlpha.medium),
        text = stringResource(R.string.login_subtitle),
        fontSize = MaterialTheme.typography.subtitle1.fontSize,
        textAlign = TextAlign.Center
    )
    GoogleButton(onClick = { onLoginClicked() }, loadingState = loginState)
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginContentPreview() {
    LoginContent(
        messageBarState = MessageBarState(),
        loginState = true,
        onLoginClicked = {}
    )
}