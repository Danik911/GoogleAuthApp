package com.example.googleauthapp.presentation.screens.profile

import android.app.Activity
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import com.example.googleauthapp.domain.model.ApiRequest
import com.example.googleauthapp.domain.model.ApiResponse
import com.example.googleauthapp.navigation.Screen
import com.example.googleauthapp.presentation.common.StartActivityForResult
import com.example.googleauthapp.presentation.common.signIn
import com.example.googleauthapp.util.RequestState
import com.google.android.gms.auth.api.identity.Identity
import retrofit2.HttpException

@ExperimentalCoilApi
@Composable
fun ProfileScreen(
    navController: NavHostController,
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    val apiResponse by profileViewModel.apiResponse
    val messageBarState by profileViewModel.messageBarState
    val clearSessionResponse by profileViewModel.clearSessionResponse
    val user by profileViewModel.user
    val firstName by profileViewModel.firstName
    val lastName by profileViewModel.lastName

    val activity = LocalContext.current as Activity

    Scaffold(
        topBar = {
            ProfileTopBar(
                onSave = {
                    profileViewModel.updateUserInfo()
                },
                onDeleteAllConfirmed = {
                    profileViewModel.deleteUser()
                }
            )
        },
        content = {
            ProfileContent(
                apiResponse = apiResponse,
                messageBarState = messageBarState,
                firstName = firstName,
                onFirstNameChanged = {
                    profileViewModel.updateFirstName(it)
                },
                lastName = lastName,
                onLastNameChanged = {
                    profileViewModel.updateLastName(it)
                },
                emailAddress = user?.email,
                profilePhoto = user?.profilePicture,
                onSignOutClicked = {
                    profileViewModel.clearSession()
                }
            )
        }
    )

    StartActivityForResult(
        key = apiResponse,
        onResultReceived = { tokenId ->
            profileViewModel.verifyTokenId(
                apiRequest = ApiRequest(tokenId = tokenId)
            )
        },
        onDialogDismissed = {
            profileViewModel.logIn(loginState = false)
            navigateToLoginScreen(navController = navController)
        }
    ) { activityLauncher ->
        if (apiResponse is RequestState.Success) {
            val response = (apiResponse as RequestState.Success<ApiResponse>).data
            if (response.error is HttpException && response.error.code() == 401) {
                signIn(
                    activity = activity,
                    accountNotFound = {
                        profileViewModel.logIn(false)
                        navigateToLoginScreen(navController = navController)
                    },
                    launchActivityResult = {
                        activityLauncher.launch(it)
                    }
                )
            }
        } else if (apiResponse is RequestState.Error) {
            profileViewModel.logIn(loginState = false)
            navigateToLoginScreen(navController = navController)
        }

    }

    LaunchedEffect(key1 = clearSessionResponse) {
        if (clearSessionResponse is RequestState.Success &&
            (clearSessionResponse as RequestState.Success<ApiResponse>).data.success
        ) {
            val oneTapClient = Identity.getSignInClient(activity)
            oneTapClient.signOut()
            profileViewModel.logIn(false)
            navigateToLoginScreen(navController = navController)
        }
    }
}


private fun navigateToLoginScreen(navController: NavHostController) {
    navController.navigate(Screen.Login.route) {
        popUpTo(Screen.Profile.route) {
            inclusive = true
        }
    }
}