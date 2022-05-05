package com.example.googleauthapp.navigation

sealed class Screen(val route: String){

    object Login: Screen("login_screen")
    object Profile: Screen("profile_screen")

}
