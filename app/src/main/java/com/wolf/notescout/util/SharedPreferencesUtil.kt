package com.wolf.notescout.util

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys


object SharedPreferencesUtil {

    private lateinit var preferences: SharedPreferences
    private val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

    fun init(context: Context) {
        preferences = EncryptedSharedPreferences.create(
                SharedPreferenceKeys.SHARED_PREFS_FILE.value,
                masterKeyAlias,
                context,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    var isFirstTime: Boolean
        get() {
            return preferences.getBoolean(SharedPreferenceKeys.IS_FIRST_TIME.value, true)
        }
        set(state) {
            preferences.edit().putBoolean(SharedPreferenceKeys.IS_FIRST_TIME.value, state).apply()
        }

    var username: String?
        get() {
            return preferences.getString(SharedPreferenceKeys.CURRENT_USERNAME.value, "")
        }
        set(name) {
            preferences.edit().putString(SharedPreferenceKeys.CURRENT_USERNAME.value, name).apply()
        }

}

private enum class SharedPreferenceKeys(val value: String){
    SHARED_PREFS_FILE("bonuslinksharedprefs"),
    IS_FIRST_TIME("isFirstTime"),
    CURRENT_USERNAME("username")
}