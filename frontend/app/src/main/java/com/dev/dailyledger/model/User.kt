package com.dev.dailyledger.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int,
    var name: String,
    var email: String,
    var password:String
)
