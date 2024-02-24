package com.yasir.iustthread.viewmodel


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.yasir.iustthread.model.ThreadModel
import com.yasir.iustthread.model.UserModel

class HomeViewModel : ViewModel() {

    private val database = FirebaseDatabase.getInstance()
    private val allThreads = database.getReference("threads")

    private val _threadsAndUsers = MutableLiveData<List<Pair<ThreadModel, UserModel>>>()
    val threadsAndUsers: LiveData<List<Pair<ThreadModel, UserModel>>> = _threadsAndUsers

    init {
        fetchThreadAndUsers{
            _threadsAndUsers.value = it
        }
    }
    private fun fetchThreadAndUsers(onResult: (List<Pair<ThreadModel,UserModel>>) -> Unit) {
        allThreads.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val result = mutableListOf<Pair<ThreadModel, UserModel>>()
                for (threadSnapshot in snapshot.children) {
                    val thread = threadSnapshot.getValue(ThreadModel::class.java)
                    thread?.let{
                        fetchUserFromThread(it){ user ->
                            result.add(0,it to user)
                            if(result.size ==snapshot.childrenCount.toInt()){
                                onResult(result)
                            }
                        }
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.d("iust", "DatabaseError")
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
    fun toggleThreadLike(threadId: String, userId: String, isLiked: Boolean, callback: (Int) -> Unit) {
        val threadRef = allThreads.child(threadId)
        threadRef.get().addOnSuccessListener { dataSnapshot ->
            val currentThread = dataSnapshot.getValue(ThreadModel::class.java)

            currentThread?.let {
                val newLikesCount = if (isLiked) it.likes - 1 else it.likes + 1
                val newLikedBy = if (isLiked) {
                    it.likedBy.toMutableList().apply { remove(userId) }
                } else {
                    it.likedBy.toMutableList().apply { add(userId) }
                }
                val updatedThread = it.copy(
                    likes = newLikesCount,
                    likedBy = newLikedBy
                )
                threadRef.setValue(updatedThread).addOnSuccessListener {
                    callback(newLikesCount)
                }.addOnFailureListener { exception ->
                    println("Failed to update like count: $exception")
                }
            }
        }.addOnFailureListener { exception ->
            println("Error toggling like status: $exception")
        }
    }

}