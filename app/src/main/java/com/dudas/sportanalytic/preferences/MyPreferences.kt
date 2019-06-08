package com.dudas.sportanalytic.preferences

import com.dudas.sportanalytic.constants.PreferencesConstants
import net.orange_box.storebox.annotations.method.KeyByString
import net.orange_box.storebox.annotations.method.RemoveMethod
import net.orange_box.storebox.annotations.method.TypeAdapter

interface MyPreferences {
    /* User start */
    @KeyByString(PreferencesConstants.KEY_USER)
    @TypeAdapter(UserTypeAdapter::class)
    fun getUser(): LoggedInUser?

    @KeyByString(PreferencesConstants.KEY_USER)
    @TypeAdapter(UserTypeAdapter::class)
    fun setUser(value: LoggedInUser)

    @RemoveMethod
    @KeyByString(PreferencesConstants.KEY_USER)
    @TypeAdapter(UserTypeAdapter::class)
    fun removeUser()
    /* User end */
}