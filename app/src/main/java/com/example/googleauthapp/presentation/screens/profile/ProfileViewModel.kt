package com.example.googleauthapp.presentation.screens.profile


import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.googleauthapp.domain.model.*
import com.example.googleauthapp.domain.repository.Repository
import com.example.googleauthapp.util.RequestState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _user: MutableState<User?> = mutableStateOf(null)
    val user: State<User?> = _user

    private val _firstName: MutableState<String> = mutableStateOf("")
    val firstName: State<String> = _firstName

    private val _lastName: MutableState<String> = mutableStateOf("")
    val lastName: State<String> = _lastName

    private val _apiResponse: MutableState<RequestState<ApiResponse>> =
        mutableStateOf(RequestState.Idle)
    val apiResponse: State<RequestState<ApiResponse>> = _apiResponse

    private val _clearSessionResponse: MutableState<RequestState<ApiResponse>> =
        mutableStateOf(RequestState.Idle)
    val clearSessionResponse: State<RequestState<ApiResponse>> = _clearSessionResponse

    private val _messageBarState: MutableState<MessageBarState> = mutableStateOf(MessageBarState())
    val messageBarState: State<MessageBarState> = _messageBarState

    init {
        getUserInfo()
    }

    private fun getUserInfo() {
        _apiResponse.value = RequestState.Loading
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    repository.getUser()
                }
                _apiResponse.value = RequestState.Success(response)
                _messageBarState.value = MessageBarState(
                    message = response.message,
                    error = response.error
                )
                if (response.user != null) {
                    _user.value = response.user
                    _firstName.value = response.user.name.split(" ").first()
                    _lastName.value = response.user.name.split(" ").last()
                }
            } catch (e: Exception) {
                _apiResponse.value = RequestState.Error(e)
                _messageBarState.value = MessageBarState(error = e)
            }
        }
    }

    fun updateUserInfo() {
        _apiResponse.value = RequestState.Loading
        if (user.value != null) {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val response = repository.getUser()
                    verifyUserInfo(response)

                } catch (e: Exception) {
                    _apiResponse.value = RequestState.Error(e)
                    _messageBarState.value = MessageBarState(error = e)
                }
            }
        }
    }

    private fun verifyUserInfo(currentUser: ApiResponse) {
        val (verified, exception) = if (firstName.value.isEmpty() || lastName.value.isEmpty()) {
            Pair(false, FieldsAreEmptyException())
        } else {
            if (currentUser.user?.name?.split(" ")?.first() == firstName.value &&
                currentUser.user.name.split(" ").last() == lastName.value
            ) {
                Pair(false, ValuesAreEqualException())
            } else {
                Pair(true, null)
            }
        }
        try {
            viewModelScope.launch(Dispatchers.IO) {
                if (verified) {
                    val response = repository.updateUserInfo(
                        userUpdate = UserUpdate(
                            firstName = firstName.value,
                            lastName = lastName.value
                        )
                    )
                    _apiResponse.value = RequestState.Success(data = response)
                    _messageBarState.value =
                        MessageBarState(
                            message = response.message,
                            error = response.error
                        )
                } else {
                    _apiResponse.value =
                        RequestState.Success(data = ApiResponse(success = false, error = exception))
                    _messageBarState.value = MessageBarState(error = exception)
                }
            }

        } catch (e: Exception) {
            _apiResponse.value = RequestState.Error(e)
            _messageBarState.value = MessageBarState(error = e)
        }
    }

    fun clearSession() {
        _apiResponse.value = RequestState.Loading
        _clearSessionResponse.value = RequestState.Loading

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.signOut()
                _apiResponse.value = RequestState.Success(data = response)
                _clearSessionResponse.value = RequestState.Success(data = response)
                _messageBarState.value =
                    MessageBarState(message = response.message, error = response.error)

            } catch (e: Exception) {
                _apiResponse.value = RequestState.Error(e)
                _clearSessionResponse.value = RequestState.Error(e)
                _messageBarState.value = MessageBarState(error = e)
            }
        }
    }

    fun deleteUser() {
        _apiResponse.value = RequestState.Loading
        _clearSessionResponse.value = RequestState.Loading

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.deleteUser()
                _clearSessionResponse.value = RequestState.Success(data = response)
                _apiResponse.value = RequestState.Success(data = response)
                _messageBarState.value =
                    MessageBarState(message = response.message, error = response.error)
            } catch (e: Exception) {
                _apiResponse.value = RequestState.Error(e)
                _messageBarState.value = MessageBarState(error = e)
                _clearSessionResponse.value = RequestState.Error(e)
            }
        }
    }

    fun verifyTokenId(apiRequest: ApiRequest) {
        _apiResponse.value = RequestState.Loading

        try {
            viewModelScope.launch(Dispatchers.IO) {
                val response = repository.tokenVerification(apiRequest = apiRequest)
                _apiResponse.value = RequestState.Success(data = response)
                _messageBarState.value =
                    MessageBarState(message = response.message, error = response.error)
            }
        } catch (e: Exception) {
            _apiResponse.value = RequestState.Error(t = e)
            _messageBarState.value = MessageBarState(error = e)
        }

    }


    fun logIn(loginState: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertLoginState(loginState = loginState)
        }
    }


    fun updateFirstName(newName: String) {
        if (newName.length < 20) {
            _firstName.value = newName
        }
    }

    fun updateLastName(newName: String) {
        if (newName.length < 20) {
            _lastName.value = newName
        }
    }

}

class FieldsAreEmptyException(
    override val message: String? = "Fields are empty"
) : Exception()

class ValuesAreEqualException(
    override val message: String? = "Values have not been changed"
) : Exception()