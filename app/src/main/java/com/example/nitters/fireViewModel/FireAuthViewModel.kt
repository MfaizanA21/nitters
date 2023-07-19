package com.example.nitters.fireViewModel

import android.text.TextUtils
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nitters.di.utils.NetworkResult
import com.example.nitters.models.repository.fireRep.AuthRepository
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FireAuthViewModel @Inject constructor(private val repository: AuthRepository): ViewModel() {

    private val _loginFlow = MutableLiveData<NetworkResult<FirebaseUser>?>(null)
    val loginFlow: LiveData<NetworkResult<FirebaseUser>?> = _loginFlow

    private val _signupFlow = MutableLiveData<NetworkResult<FirebaseUser>?>(null)
    val signupFlow: LiveData<NetworkResult<FirebaseUser>?> = _signupFlow

    val currentUser: FirebaseUser?
        get() = repository.currentUser

    init {
        if(repository.currentUser != null) {
            _loginFlow.value = NetworkResult.Success(repository.currentUser!! )
        }
    }

    fun login(email: String, password: String) = viewModelScope.launch {
        val result = repository.login(email, password)
        _loginFlow.value = result
    }

    fun signup(name: String, email: String, password: String) = viewModelScope.launch {
        val result = repository.signup(name, email, password)
        _signupFlow.value = result
    }

    fun validation(name: String, email: String, password: String, isLogin: Boolean): Pair<Boolean, String> {
        var result = Pair(true, "")
        if(!isLogin && TextUtils.isEmpty(name) || TextUtils.isEmpty(email)|| TextUtils.isEmpty(password)) {
            result = Pair(false, "Please Provide Credentials")
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            result = Pair(false, "Please Provide valid email")
        }
        else if(password.length <= 5) {
            result = Pair(false, "Password should be greater than 5")
        }
        return result
    }

    fun logout() {
        repository.logout()
        _loginFlow. value = null
        _signupFlow.value = null
    }
}