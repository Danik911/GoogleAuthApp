package com.example.googleauthapp.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import com.example.googleauthapp.domain.repository.DataStoreOperations
import com.example.googleauthapp.util.Constants.LOGIN_PREFERENCE_KEY
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class DataStoreOperationsImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : DataStoreOperations {

    private object PreferencesKeys {
        val loginKey = booleanPreferencesKey(LOGIN_PREFERENCE_KEY)
    }

    override suspend fun insertLoginState(loginState: Boolean) {
        dataStore.edit { preference ->
            preference[PreferencesKeys.loginKey] = loginState
        }
    }

    override fun readLoginState(): Flow<Boolean> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preference ->
                val loginState = preference[PreferencesKeys.loginKey] ?: false
                loginState
            }
    }
}