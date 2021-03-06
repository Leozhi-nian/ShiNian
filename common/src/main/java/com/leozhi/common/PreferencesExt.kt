package com.leozhi.common

import android.content.Context
import java.lang.IllegalArgumentException
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * @author leozhi
 * @date 2021/3/6
 */
class Preference<T>(
    private val context: Context,
    private val name: String,
    private val default: T,
    private val prefName: String = "default"
) : ReadWriteProperty<Any?, T> {
    private val prefs by lazy {
        context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
    }
    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return when (default) {
            is Int -> prefs.getInt(name, default)
            is Long -> prefs.getLong(name, default)
            is Float -> prefs.getFloat(name, default)
            is Boolean -> prefs.getBoolean(name, default)
            is String -> prefs.getString(name, default)
            else -> throw IllegalArgumentException("Unsupported type.")
        } as T
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        with(prefs.edit()) {
            when (value) {
                is Int -> putInt(name, value)
                is Long -> putLong(name, value)
                is Float -> putFloat(name, value)
                is Boolean -> putBoolean(name, value)
                is String? -> putString(name, value)
                else -> throw IllegalArgumentException("Unsupported type.")
            }
            apply()
        }
    }
}