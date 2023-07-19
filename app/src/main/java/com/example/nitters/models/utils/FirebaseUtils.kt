package com.example.nitters.models.utils

import com.google.android.gms.tasks.Task
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resumeWithException

suspend fun <T> Task<T>.await(): T{
    return suspendCancellableCoroutine {
        cont ->addOnCompleteListener {
            if(it.exception != null) {
                cont.resumeWithException(exception!!)
            } else {
                cont.resume(it.result, null)
            }
    }
    }
}