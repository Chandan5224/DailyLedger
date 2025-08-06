package com.dev.dailyledger.ui.auth

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.dailyledger.model.GenericResponse
import com.dev.dailyledger.model.User
import com.dev.dailyledger.utils.AppPreferences
import com.dev.dailyledger.utils.Resource
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response
import kotlin.math.log

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {
    val signupResponse: MutableLiveData<Resource<String>> = MutableLiveData()
    val loginResponse: MutableLiveData<Resource<GenericResponse>> = MutableLiveData()
    val verifyEmailResponse: MutableLiveData<Resource<String>> = MutableLiveData()
    val resendOTPResponse: MutableLiveData<Resource<String>> = MutableLiveData()

    fun signup(user: User) {
        signupResponse.postValue(Resource.Loading())
        viewModelScope.launch {
            try {
                val response = authRepository.signup(user.name, user.email, user.password)
                handleSignupResponse(response)
            } catch (e: Exception) {
                Log.d("AuthViewModel : signup", e.message.toString())
                signupResponse.postValue(Resource.Error(e.message.toString()))
            }
        }
    }

    private fun handleSignupResponse(response: Response<GenericResponse>) {
        if (response.isSuccessful) {
            // Directly post the success result if the response is successful and non-null
            response.body()?.let { resultResponse ->
                when (resultResponse.header.code) {
                    200 -> {
                        signupResponse.postValue(Resource.Success(resultResponse.body.value.asString))
                    }
                    else -> {
                        signupResponse.postValue(Resource.Error(resultResponse.body.error))
                    }
                }
            }
        } else {
            // Handle non-successful responses more gracefully
            val errorMsg = parseErrorResponse(response)
            signupResponse.postValue(Resource.Error(errorMsg))
        }
    }

    fun login(email: String, password: String) {
        loginResponse.postValue(Resource.Loading())
        viewModelScope.launch {
            try {
                val response = authRepository.login(email, password)
                handleLoginResponse(response)
            } catch (e: Exception) {
                Log.d("AuthViewModel : login", e.message.toString())
                loginResponse.postValue(Resource.Error(e.message.toString()))
            }
        }
    }

    private fun handleLoginResponse(response: Response<GenericResponse>) {
        if (response.isSuccessful) {
            // Directly post the success result if the response is successful and non-null
            response.body()?.let { resultResponse ->
                when (resultResponse.header.code) {
                    200 -> {
                        loginResponse.postValue(Resource.Success(resultResponse))
                    }

                    201 -> {
                        loginResponse.postValue(Resource.Success(resultResponse))
                    }
                }
            }
        } else {
            // Handle non-successful responses more gracefully
            val errorMsg = parseErrorResponse(response)
            loginResponse.postValue(Resource.Error(errorMsg))
        }
    }

    fun verifyEmail(email: String, code: String) {
        verifyEmailResponse.postValue(Resource.Loading())
        viewModelScope.launch {
            try {
                val response = authRepository.verifyEmail(email, code)
                handleVerifyEmailResponse(response)
            } catch (e: Exception) {
                Log.d("AuthViewModel : login", e.message.toString())
                verifyEmailResponse.postValue(Resource.Error(e.message.toString()))
            }
        }
    }

    private fun handleVerifyEmailResponse(response: Response<GenericResponse>) {
        if (response.isSuccessful) {
            // Directly post the success result if the response is successful and non-null
            response.body()?.let { resultResponse ->
                if (resultResponse.header.code == 200) {
                    verifyEmailResponse.postValue(Resource.Success(resultResponse.body.value.asString))
                } else {
                    verifyEmailResponse.postValue(Resource.Error(resultResponse.body.error))
                }
            }
        } else {
            // Handle non-successful responses more gracefully
            val errorMsg = parseErrorResponse(response)
            verifyEmailResponse.postValue(Resource.Error(errorMsg))
        }
    }

    fun resendOTP(email: String) {
        resendOTPResponse.postValue(Resource.Loading())
        viewModelScope.launch {
            try {
                val response = authRepository.resetOTP(email)
                handleResendOTPResponse(response)
            } catch (e: Exception) {
                Log.d("AuthViewModel : login", e.message.toString())
                resendOTPResponse.postValue(Resource.Error(e.message.toString()))
            }
        }
    }

    private fun handleResendOTPResponse(response: Response<GenericResponse>) {
        if (response.isSuccessful) {
            // Directly post the success result if the response is successful and non-null
            response.body()?.let { resultResponse ->
                if (resultResponse.header.code == 200) {
                    resendOTPResponse.postValue(Resource.Success(resultResponse.body.value.asString))
                } else {
                    resendOTPResponse.postValue(Resource.Error(resultResponse.body.error.toString()))
                }
            }
        } else {
            // Handle non-successful responses more gracefully
            val errorMsg = parseErrorResponse(response)
            resendOTPResponse.postValue(Resource.Error(errorMsg))
        }
    }

    fun saveDataInSharePreference(key: String, value: String) {
        AppPreferences.saveDataInSharePreference(key, value)
    }

    fun getDataFromSharePreference(key: String): String? {
        return AppPreferences.getDataFromSharePreference(key)
    }

    fun removeDataSharePreference(key: String) {
        AppPreferences.removeDataSharePreference(key)
    }

    private fun parseErrorResponse(response: Response<*>): String {
        return try {
            val errorBody = response.errorBody()?.string()
            if (!errorBody.isNullOrEmpty()) {
                val errorJson = JSONObject(errorBody)
                errorJson.getJSONObject("body").getString("error") // match your API structure
            } else {
                "Unknown error"
            }
        } catch (e: Exception) {
            "Something went wrong"
        }
    }


}