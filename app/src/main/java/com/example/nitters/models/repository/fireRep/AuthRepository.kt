package com.example.nitters.models.repository.fireRep

import com.example.nitters.di.utils.NetworkResult
import com.google.firebase.auth.FirebaseUser

interface AuthRepository {

    val currentUser: FirebaseUser?
    suspend fun login(email:String, password: String): NetworkResult<FirebaseUser>
    suspend fun signup(name:String, email:String, password: String): NetworkResult<FirebaseUser>
    fun logout()
}