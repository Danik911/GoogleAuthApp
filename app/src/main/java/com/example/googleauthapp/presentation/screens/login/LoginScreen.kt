package com.example.googleauthapp.presentation.screens.login

import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.googleauthapp.domain.model.MessageBarState

@Composable
fun LoginScreen(
    navController: NavHostController,
    viewModel: LoginViewModel = hiltViewModel()
) {

    val messageBarState by viewModel.messageState
    val loginState by viewModel.loginState


    Scaffold(
        topBar = {
            LoginTopAppBar()
        },
        content = {
            LoginContent(
                messageBarState = messageBarState,
                loginState = loginState,
                onLoginClicked = {
                    viewModel.login(true)
                }
            )

        }
    )
}