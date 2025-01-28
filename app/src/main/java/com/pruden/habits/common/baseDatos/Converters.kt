package com.pruden.habits.common.baseDatos

import android.util.Log
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    @TypeConverter
    fun fromMutableListToString(list: MutableList<String>): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun fromStringToMutableList(value: String?): MutableList<String> {
        if (value.isNullOrEmpty()) return mutableListOf()
        return try {
            val listType = object : TypeToken<MutableList<String>>() {}.type
            Gson().fromJson(value, listType)
        } catch (e: Exception) {
            mutableListOf()
        }
    }

    @TypeConverter
    fun fromMutableListToStringNullable(list: MutableList<String?>): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun fromStringToMutableListNullable(value: String?): MutableList<String?> {
        if (value.isNullOrEmpty()) return mutableListOf()
        return try {
            val listType = object : TypeToken<MutableList<String?>>() {}.type
            Gson().fromJson(value, listType)
        } catch (e: Exception) {
            mutableListOf()
        }
    }

}
