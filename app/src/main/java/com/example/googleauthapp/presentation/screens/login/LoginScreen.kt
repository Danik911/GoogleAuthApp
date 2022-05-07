package com.example.googleauthapp.presentation.screens.login

import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.googleauthapp.domain.model.MessageBarState

@Composable
fun LoginScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            LoginTopAppBar()
        },
        content = {
            LoginContent(
                messageBarState = MessageBarState(),
                loginState = true,
                onLoginClicked = {  }
            )

        }
    )
}