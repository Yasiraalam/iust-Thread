package com.yasir.iustthread.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.ktx.storage
import com.yasir.iustthread.model.ThreadModel
import java.util.UUID

class AddThreadViewModel : ViewModel() {

    private val database = FirebaseDatabase.getInstance()
    val userRef = database.getReference("threads")

    private val _isPosted = MutableLiveData<Boolean>()
    val isPosted: LiveData<Boolean> = _isPosted

    private val storageref = com.google.firebase.ktx.Firebase.storage.reference
    private val imageRef = storageref.child("threads/${UUID.randomUUID()}.jpg")

     fun saveImage(
        thread: String,
        userId: String,
        imageUri: Uri,
    ) {
        val uploadTask = imageRef.putFile(imageUri)
        uploadTask.addOnSuccessListener {
            imageRef.downloadUrl.addOnSuccessListener {
                saveData(thread, userId, it.toString())
            }
        }

    }

    fun saveData(
        thread: String,
        userId: String,
        image: String,
    ) {
        val threadData = ThreadModel(thread , image, userId, System.currentTimeMillis().toString())
        userRef.child(userRef.push().key!!).setValue(threadData).addOnSuccessListener {
            _isPosted.postValue(true)
        }.addOnFailureListener {

        }
    }

}