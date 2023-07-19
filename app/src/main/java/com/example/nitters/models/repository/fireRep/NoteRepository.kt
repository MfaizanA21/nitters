package com.example.nitters.models.repository.fireRep

import android.content.Context
import android.provider.ContactsContract
import android.util.Log
import android.widget.Toast
import com.example.nitters.models.fireModel.Data
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import kotlin.properties.Delegates


class FireNoteRepository @Inject constructor(private val firebaseFirestore: FirebaseFirestore){

    //Flows for Toast

    val context: Context
        get() {
          return context
        }

    private val db: FirebaseFirestore = firebaseFirestore

    private var flag: Boolean = true

    fun addNote(title: String, description: String) {

        val note: MutableMap<String, Any> = HashMap()
        note["title"] = title
        note["description"] = description

        db.collection("Notes")
            .add(note)
            .addOnSuccessListener {
                Log.d("notes","success")
//                Toast.makeText(context, "Note Saved!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{
                flag = false
//                Toast.makeText(context, "ERROR Saving Note!", Toast.LENGTH_SHORT).show()
                Log.d("notes","failure")
            }
    }

    fun status(): Boolean {
        return flag
    }

    fun getNotes(): Task<DocumentSnapshot> {
        val ref = db.collection("Notes")
            .document("")
       return ref.get()

    }

    fun getData(callback: (data: List<Data>) -> Unit) {
        val docRef = db.collection("Notes")
            .get()
            .addOnSuccessListener { documents ->
                val data = documents.mapNotNull { document ->
                    val title = document.getString("title")
                    val description = document.getString("description")
                    if (title != null && description != null) {
                        Data(title, description)
                    }
                    else {
                        null
                    }
                }
                callback(data)

            }
            .addOnFailureListener {
                callback(emptyList())
            }
    }

    fun deleteNote() {

    }
}