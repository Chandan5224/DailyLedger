package com.example.bluefield.db


import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DataConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromArrayListOfPairsToString(list: ArrayList<Pair<String, String>>): String {
        return gson.toJson(list)
    }

    @TypeConverter
    fun fromStringToArrayListOfPairs(value: String): ArrayList<Pair<String, String>> {
        val type = object : TypeToken<ArrayList<Pair<String, String>>>() {}.type
        return gson.fromJson(value, type)
    }

//    @TypeConverter
//    fun taskDetailsToString(taskDetails: TaskDetails?): String? {
//        return gson.toJson(taskDetails)
//    }
//
//    @TypeConverter
//    fun stringToTaskDetails(data: String?): TaskDetails? {
//        if (data == null) {
//            return null
//        }
//        val type = object : TypeToken<TaskDetails>() {}.type
//        return gson.fromJson(data, type)
//    }
}