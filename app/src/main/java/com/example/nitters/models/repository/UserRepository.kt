package com.example.nitters.models.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.nitters.api.UserAPI
import com.example.nitters.di.utils.NetworkResult
import com.example.nitters.models.UserRequest
import com.example.nitters.models.UserResponse
import org.json.JSONObject
import javax.inject.Inject

class UserRepository @Inject constructor(private val userAPI: UserAPI) {

    private val _userResponseLiveData = MutableLiveData<NetworkResult<UserResponse>>()
    val userResponseLiveData: LiveData<NetworkResult<UserResponse>>
        get() = _userResponseLiveData

    suspend fun registerUser(userRequest: UserRequest) {
        _userResponseLiveData.postValue(NetworkResult.Loading())

        val response = userAPI.signup(userRequest)
        if(response.isSuccessful && response.body() != null) {
            _userResponseLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if(response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _userResponseLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            _userResponseLiveData.postValue(NetworkResult.Error("Something went Wrong"))
        }
    }
    suspend fun loginUser(userRequest: UserRequest) {
        val response = userAPI.signin(userRequest)
    }
}
