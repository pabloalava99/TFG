package com.viamedsalud.gvp.utils

import android.content.Context
import android.content.SharedPreferences
import com.viamedsalud.gvp.R
import com.viamedsalud.gvp.utils.Constants.ID
import com.viamedsalud.gvp.utils.Constants.USER_TOKEN
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class SessionManager @Inject constructor(@ApplicationContext context: Context) {

    private var prefs: SharedPreferences =
        context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

    fun saveToken(token: String) {
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }

    fun getToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }

    fun saveId(id: Int) {
        val editor = prefs.edit()
        editor.putInt(ID, id)
        editor.apply()
    }

    fun getId(): Int? {
        return prefs.getInt(ID, -1)
    }

    fun clearData() {
        val editor = prefs.edit()
        editor.clear()
        editor.apply()
    }

}