package com.dev.dailyledger.api


import com.dev.dailyledger.model.GenericResponse
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Query

interface UserAPI {

    @POST("auth/login")
    suspend fun logIn(
        @Query("email") email: String?,
        @Query("password") password: String?
    ): Response<GenericResponse>

    @POST("auth/signup")
    suspend fun signup(
        @Query("name") name: String?,
        @Query("email") email: String?,
        @Query("password") password: String?
    ): Response<GenericResponse>

    @POST("auth/verify-email")
    suspend fun verifyEmail(
        @Query("email") email: String?,
        @Query("code") code: String?
    ): Response<GenericResponse>

    @POST("auth/resend-otp")
    suspend fun resendOTP(
        @Query("email") email: String?
    ): Response<GenericResponse>
}