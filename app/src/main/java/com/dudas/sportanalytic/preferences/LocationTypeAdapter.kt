package com.dudas.sportanalytic.preferences

import com.dudas.sportanalytic.BuildConfig
import com.dudas.sportanalytic.constants.DBConstants
import com.dudas.sportanalytic.utils.decrypt
import com.dudas.sportanalytic.utils.encrypt
import com.google.gson.Gson
import net.orange_box.storebox.adapters.base.BaseStringTypeAdapter

class LocationTypeAdapter : BaseStringTypeAdapter<Location>() {

    override fun adaptForPreferences(value: Location?): String? {
        return if (value == null) {
            null
        } else {
            val json = Gson().toJson(value)
            if (BuildConfig.DEBUG) json else encrypt(json, DBConstants.KEY)
        }
    }

    override fun adaptFromPreferences(value: String?): Location? {
        if (value == null) {
            return null
        } else {
            return Gson().fromJson(if (BuildConfig.DEBUG) value else decrypt(value, DBConstants.KEY), Location::class.java)
        }
    }
}