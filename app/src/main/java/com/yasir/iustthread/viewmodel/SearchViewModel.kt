package com.yasir.iustthread.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.ktx.storage
import com.yasir.iustthread.model.ThreadModel
import com.yasir.iustthread.model.UserModel
import java.util.UUID

class SearchViewModel : ViewModel() {

    private val database = FirebaseDatabase.getInstance()
    val users = database.getReference("Users")

    private val _users = MutableLiveData<List<UserModel>>()
    val usersList: LiveData<List<UserModel>> = _users

    init {
        fetchUsers {
            _users.value =it
        }
    }
    private fun fetchUsers(onResult: (List<UserModel>) -> Unit) {
        users.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val result = mutableListOf<UserModel>()
                for (threadSnapshot in snapshot.children) {
                    val thread = threadSnapshot.getValue(UserModel::class.java)
                    result.add(thread!!)
                }
                onResult(result)
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
    fun fetchUserFromThread(
        thread: ThreadModel,
        onResult: (UserModel) -> Unit
    ) {
        database.getReference("Users").child(thread.userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(UserModel::class.java)
                    user?.let(onResult)
                }
                override fun onCancelled(error: DatabaseError) {

                }

            })

    }

}