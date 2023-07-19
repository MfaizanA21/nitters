package com.example.nitters.models.repository.fireRep

import com.example.nitters.di.utils.NetworkResult
import com.example.nitters.models.utils.await
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(private val firebaseAuth: FirebaseAuth) :
    AuthRepository {

    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    override suspend fun login(email: String, password: String): NetworkResult<FirebaseUser> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            NetworkResult.Success(result.user!!)
        } catch (e: Exception) {
            e.printStackTrace()
            NetworkResult.Error("$e")
        }

    }

    override suspend fun signup(name: String, email: String, password: String): NetworkResult<FirebaseUser> {
       return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            result.user?.updateProfile(UserProfileChangeRequest.Builder().setDisplayName(name).build())?.await()
            NetworkResult.Success(result.user!!)

        } catch (e: Exception) {
            NetworkResult.Error("$e")
        }

    }

    override fun logout() {
        firebaseAuth.signOut()
    }
}