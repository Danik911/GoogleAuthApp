package com.example.googleauthapp.presentation.screens.login

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.googleauthapp.domain.model.MessageBarState
import com.example.googleauthapp.domain.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private val _loginState: MutableState<Boolean> = mutableStateOf(false)
    val loginState: State<Boolean> = _loginState


    private val _messageState: MutableState<MessageBarState> = mutableStateOf(MessageBarState())
    val messageState: State<MessageBarState> = _messageState

    init {
        viewModelScope.launch {
            repository.readLoginState().collect { loginState ->
                _loginState.value = loginState
            }
        }
    }

    fun login(loginState: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertLoginState(loginState = loginState)
        }
    }

    fun throwExceptionToMessageState() {
        _messageState.value = MessageBarState(error = MessageBarStateAccountException())
    }
}

class MessageBarStateAccountException(
    override val message: String? = "No accounts were found"
) : Exception()