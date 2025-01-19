package com.pruden.habits.baseDatos

import android.util.Log
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    @TypeConverter
    fun fromListToString(list: List<Float>): String {
        return Gson().toJson(list) // Convierte la lista a JSON
    }

    @TypeConverter
    fun fromStringToList(value: String?): List<Float> {
        if (value.isNullOrEmpty()) return emptyList()
        return try {
            val listType = object : TypeToken<List<Float>>() {}.type
            Gson().fromJson(value, listType)
        } catch (e: Exception) {
            emptyList()
        }
    }


    @TypeConverter
    fun fromListToStringNotas(list: List<String?>): String {
        return Gson().toJson(list) // Convierte la lista de notas a JSON
    }

    @TypeConverter
    fun fromStringToListNotas(value: String?): List<String?> {
        if (value.isNullOrEmpty()) {
            return emptyList()
        }
        return try {
            val listType = object : TypeToken<List<String?>>() {}.type
            Gson().fromJson(value, listType)
        } catch (e: Exception) {
            emptyList()
        }
    }

}
