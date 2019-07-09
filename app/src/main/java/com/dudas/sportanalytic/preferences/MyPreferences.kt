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

    /* Location start */
    @KeyByString(PreferencesConstants.KEY_LOCATION)
    @TypeAdapter(LocationTypeAdapter::class)
    fun getLocation(): Location?

    @KeyByString(PreferencesConstants.KEY_LOCATION)
    @TypeAdapter(LocationTypeAdapter::class)
    fun setLocation(value: Location)

    @RemoveMethod
    @KeyByString(PreferencesConstants.KEY_LOCATION)
    @TypeAdapter(LocationTypeAdapter::class)
    fun removeLocation()
    /* Location end */

    /* Report start */
    @KeyByString(PreferencesConstants.KEY_REPORT)
    fun getReport(): String?

    @KeyByString(PreferencesConstants.KEY_REPORT)
    fun setReport(value: String)

    @RemoveMethod
    @KeyByString(PreferencesConstants.KEY_REPORT)
    fun removeReport()
    /* Report end */
}