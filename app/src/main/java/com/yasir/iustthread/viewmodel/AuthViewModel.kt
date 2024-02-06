package com.yasir.iustthread.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.ktx.storage
import com.yasir.iustthread.model.UserModel
import com.yasir.iustthread.utils.SharedPref
import java.util.UUID

class AuthViewModel : ViewModel() {
    val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()
    val userRef = database.getReference("Users")
    private val _firebaseUser = MutableLiveData<FirebaseUser>()
    val firebaseUser: LiveData<FirebaseUser> = _firebaseUser
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val storageref = com.google.firebase.ktx.Firebase.storage.reference
    private val imageRef = storageref.child("Users/${UUID.randomUUID()}.jpg")

    init {
        _firebaseUser.value = auth.currentUser

    }

    fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                _firebaseUser.postValue(auth.currentUser)
            } else {
                _error.postValue("Something went Wrong.")
            }
        }
    }

    fun register(
        email: String,
        password: String,
        name: String,
        bio: String,
        username: String,
        imageUri: Uri,
        context: Context
    ) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                _firebaseUser.postValue(auth.currentUser)
                saveImage(email, password, name, bio, username, imageUri, auth.currentUser?.uid, context)
            } else {
                _error.postValue("Something went Wrong.")
            }
        }
    }

    private fun saveImage(
        email: String,
        password: String,
        name: String,
        bio: String,
        username: String,
        imageUri: Uri,
        uid: String?,
        context: Context
    ) {
        val uploadTask = imageRef.putFile(imageUri)
        uploadTask.addOnSuccessListener {
            imageRef.downloadUrl.addOnSuccessListener {
                saveData(email, password, name, bio, username, it.toString(), uid,context)
            }
        }

    }

    private fun saveData(
        email: String,
        password: String,
        name: String,
        bio: String,
        username: String,
        imageUri: String,
        uid: String?,
        context:Context
    ) {
        val userData = UserModel(email, password, name, bio, username, imageUri)
        userRef.child(uid!!).setValue(userData).addOnSuccessListener {
            SharedPref.storeData(
                name,
                email,
                bio,
                username,
                imageUri,
                context
            )

        }.addOnFailureListener {

        }
    }
}