package com.example.nitters.viewModels

import android.text.TextUtils
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nitters.di.utils.NetworkResult
import com.example.nitters.models.UserRequest
import com.example.nitters.models.UserResponse
import com.example.nitters.models.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AuthViewModel @Inject constructor(private val userRepository: UserRepository): ViewModel() {

    val userResponseLiveData: LiveData<NetworkResult<UserResponse>>
        get() = userRepository.userResponseLiveData

    fun registerUser(userRequest: UserRequest) {
        viewModelScope.launch{
            userRepository.registerUser(userRequest)
        }

    }

    fun loginUser(userRequest: UserRequest) {
        viewModelScope.launch{
            userRepository.loginUser(userRequest)
        }

    }

    fun validateCredential(username: String, emailAddress: String, password: String, isLogin: Boolean): Pair<Boolean, String> {
        var result = Pair(true, "")
        if(!isLogin && TextUtils.isEmpty(username) || TextUtils.isEmpty(emailAddress)|| TextUtils.isEmpty(password)) {
            result = Pair(false, "Please Provide Credentials")
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()) {
            result = Pair(false, "Please Provide valid email")
        }
        else if(password.length <= 5) {
            result = Pair(false, "Password should be greater than 5")
        }
        return result
    }

}