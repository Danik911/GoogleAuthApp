package com.example.googleauthapp.presentation.screens.profile

import android.util.Log
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi

@ExperimentalCoilApi
@Composable
fun ProfileScreen(
    navController: NavHostController,
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    val apiResponse by profileViewModel.apiResponse
    val messageBarState by profileViewModel.messageBarState

    val user by profileViewModel.user
    val firstName by profileViewModel.firstName
    val lastName by profileViewModel.lastName
    Log.d("ProfileScreen", "${messageBarState.error}")

    Scaffold(
        topBar = {
            ProfileTopBar(
                onSave = {
                    profileViewModel.updateUserInfo()
                },
                onDeleteAllConfirmed = {

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

                }
            )
        }
    )
}