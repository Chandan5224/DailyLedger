package com.dev.dailyledger.ui.auth

import android.content.Context
import com.dev.dailyledger.api.RetrofitInstance
import com.dev.dailyledger.model.User
import com.example.bluefield.db.AppDatabase

class AuthRepository(val context: Context) {
    private val db = AppDatabase(context)


    suspend fun login(email: String, password: String) = RetrofitInstance.api.logIn(email, password)
    suspend fun signup(name: String, email: String, password: String) =
        RetrofitInstance.api.signup(name, email, password)

    suspend fun resetOTP(email: String) = RetrofitInstance.api.resendOTP(email)
    suspend fun verifyEmail(email: String, code: String) =
        RetrofitInstance.api.verifyEmail(email, code)

    /// Database operations
    //User
    suspend fun insertUser(user: User) = db.userDao().insert(user)
    suspend fun getALlUser() = db.userDao().getAllUser()
    suspend fun getUserById(id: Int) = db.userDao().getUserById(id)
    suspend fun deleteUser(user: User) = db.userDao().delete(user)

}