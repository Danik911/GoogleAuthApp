package com.example.googleauthapp.presentation.screens.login

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.googleauthapp.domain.model.ApiRequest
import com.example.googleauthapp.domain.model.ApiResponse
import com.example.googleauthapp.domain.model.MessageBarState
import com.example.googleauthapp.domain.repository.Repository
import com.example.googleauthapp.util.RequestState
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

    private val _apiResponse: MutableState<RequestState<ApiResponse>> =
        mutableStateOf(RequestState.Idle)
    val apiResponse: State<RequestState<ApiResponse>> = _apiResponse


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

    fun verifyTokenId(apiRequest: ApiRequest) {
        _apiResponse.value = RequestState.Loading

        try {
            viewModelScope.launch(Dispatchers.IO) {
                val response = repository.tokenVerification(apiRequest = apiRequest)
                _apiResponse.value = RequestState.Success(data = response)
                _messageState.value =
                    MessageBarState(message = response.message, error = response.error)
            }
        } catch (e: Exception) {
            _apiResponse.value = RequestState.Error(t = e)
            _messageState.value = MessageBarState(error = e)
        }

    }
}

class MessageBarStateAccountException(
    override val message: String? = "No accounts were found"
) : Exception()