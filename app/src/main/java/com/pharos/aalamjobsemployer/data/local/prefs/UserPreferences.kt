package com.pharos.aalamjobsemployer.data.local.prefs

import android.content.Context
import androidx.datastore.DataStore
import androidx.datastore.preferences.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferences (
    context: Context
        ){
    private val applicationContext = context.applicationContext
    private val dataStore: DataStore<Preferences> = applicationContext.createDataStore(
        name = "my_data_store"
    )

    val tokenAccess: Flow<String?>
    get() = dataStore.data.map { preferences ->
        preferences[KEY_AUTH]
    }

    val tokenRefresh: Flow<String?>
        get() = dataStore.data.map { preferences ->
            preferences[KEY_AUTH_REFRESH]
        }

    val email: Flow<String?>
    get() = dataStore.data.map { preferences ->
        preferences[KEY_AUTH_EMAIL]
    }

    val id: Flow<Int?>
        get() = dataStore.data.map { preferences ->
            preferences[KEY_AUTH_ID]
        }

    val username: Flow<String?>
        get() = dataStore.data.map { preferences ->
            preferences[KEY_AUTH_USERNAME]
        }

    val language: Flow<String?>
    get() = dataStore.data.map { preferences ->
        preferences[KEY_LANGUAGE]
    }

    val photo: Flow<String?>
    get() = dataStore.data.map { preferences ->
        preferences[KEY_PHOTO]
    }

    val city: Flow<String?>
    get() = dataStore.data.map { preferences ->
        preferences[KEY_CITY]
    }

    val country: Flow<String?>
    get() = dataStore.data.map { preferences ->
        preferences[KEY_COUNTRY]
    }

    val position: Flow<String?>
    get() = dataStore.data.map { preferences ->
        preferences[KEY_POSITION]
    }

    val fullname: Flow<String?>
    get() = dataStore.data.map { preferences ->
        preferences[KEY_FULLNAME]
    }
    val created: Flow<String?>
    get() = dataStore.data.map { preferences ->
        preferences[KEY_COMPANY]
    }
    
    suspend fun saveAuthToken(tokenAccess: String, tokenRefresh: String){
        dataStore.edit { preferences ->
            preferences[KEY_AUTH] = tokenAccess
            preferences[KEY_AUTH_REFRESH] = tokenRefresh

        }
    }
    suspend fun saveAccessToken(tokenAccess: String) {
        dataStore.edit { preferences ->
            preferences[KEY_AUTH] = tokenAccess
        }
    }
    suspend fun companyCreated(created: String){
        dataStore.edit { preferences ->
            preferences[KEY_COMPANY] = created
        }
    }


    suspend fun saveUser(email: String, id: Int, username: String, photo: String,
    city: String, country: String, position: String, fullname: String){
        dataStore.edit { preferences ->
            preferences[KEY_AUTH_USERNAME] = username
            preferences[KEY_AUTH_ID] = id
            preferences[KEY_AUTH_EMAIL] = email
            preferences[KEY_PHOTO] = photo
            preferences[KEY_CITY] = city
            preferences[KEY_COUNTRY] = country
            preferences[KEY_POSITION] = position
            preferences[KEY_FULLNAME] = fullname
        }
    }

    suspend fun saveLang(lang: String){
        dataStore.edit { preferences ->
            preferences[KEY_LANGUAGE] = lang
        }
    }

    suspend fun clear(){
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        private val KEY_AUTH = preferencesKey<String>("key_auth")
        private val KEY_AUTH_REFRESH = preferencesKey<String>("key_auth_refresh")
        private val KEY_AUTH_EMAIL = preferencesKey<String>("key_auth_email")
        private val KEY_AUTH_ID = preferencesKey<Int>("key_auth_id")
        private val KEY_AUTH_USERNAME = preferencesKey<String>("key_auth_username")
        private val KEY_LANGUAGE = preferencesKey<String>("key_language")
        private val KEY_PHOTO = preferencesKey<String>("key_photo")
        private val KEY_FULLNAME = preferencesKey<String>("key_fullname")
        private val KEY_CITY = preferencesKey<String>("key_city")
        private val KEY_COUNTRY = preferencesKey<String>("key_country")
        private val KEY_POSITION = preferencesKey<String>("key_position")
        private val KEY_COMPANY = preferencesKey<String>("key_company")
    }
}