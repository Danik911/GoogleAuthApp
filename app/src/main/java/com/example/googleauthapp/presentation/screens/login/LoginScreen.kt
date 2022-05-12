package com.example.googleauthapp.presentation.screens.login

import android.app.Activity
import android.util.Log
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.googleauthapp.domain.model.ApiRequest
import com.example.googleauthapp.domain.model.ApiResponse
import com.example.googleauthapp.domain.model.MessageBarState
import com.example.googleauthapp.navigation.Screen
import com.example.googleauthapp.presentation.common.StartActivityForResult
import com.example.googleauthapp.presentation.common.signIn
import com.example.googleauthapp.util.RequestState

@Composable
fun LoginScreen(
    navController: NavHostController,
    viewModel: LoginViewModel = hiltViewModel()
) {

    val messageBarState by viewModel.messageState
    val loginState by viewModel.loginState
    val apiResponse by viewModel.apiResponse


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
    val activity = LocalContext.current as Activity
    StartActivityForResult(
        key = loginState,
        onResultReceived = { tokenId ->
            viewModel.verifyTokenId(apiRequest = ApiRequest(tokenId = tokenId))
        },
        onDialogDismissed = {
            viewModel.login(loginState = false)
        },
        launcher = { activityLauncher ->
            if (loginState) {
                signIn(
                    activity = activity,
                    launchActivityResult = { intentSenderRequest ->
                        activityLauncher.launch(intentSenderRequest)

                    },
                    accountNotFound = {
                        viewModel.login(loginState = false)
                        viewModel.throwExceptionToMessageState()
                        Log.d("LoginScreen", "${messageBarState.error}")
                    }
                )
            }

        }
    )
    LaunchedEffect(key1 = apiResponse) {
        when (apiResponse) {
            is RequestState.Success -> {
                val result = (apiResponse as RequestState.Success<ApiResponse>).data.success
                if (result) {
                    navigateToDetailsScreen(navController = navController)
                } else {
                    viewModel.login(false)
                }
            }
            else -> {}
        }
    }
}

fun navigateToDetailsScreen(navController: NavHostController) {
    navController.navigate(Screen.Profile.route) {
        popUpTo(Screen.Login.route) {
            inclusive = true
        }
    }
}