package com.example.nitters.fireViewModel


import android.content.Context
import android.provider.ContactsContract
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nitters.models.fireModel.Data
import com.example.nitters.models.repository.fireRep.FireNoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FireNoteViewModel @Inject constructor(fireRepository: FireNoteRepository): ViewModel() {

    private val result = fireRepository

    fun addNote(title: String, description: String) {
        result.addNote(title, description)
    }

    fun status(): Boolean {
        return result.status()
    }

    val context: Context
        get() {
            return context
        }

    fun getNotes() {
        result.getNotes().addOnSuccessListener {
            if(it != null) {
                val title = it.data?.get("title")?.toString()
                val desc = it.data?.get("description")?.toString()
            }
        }
            .addOnFailureListener{
                Toast.makeText(context, "Failed to get Data", Toast.LENGTH_SHORT).show()
            }
    }

    private val _data = MutableLiveData<List<Data>>()
    val data: LiveData<List<Data>> = _data

    fun getData() {
        result.getData {
            data -> _data.value = data
            Log.d("Suckes", data.toString())
        }
    }
}