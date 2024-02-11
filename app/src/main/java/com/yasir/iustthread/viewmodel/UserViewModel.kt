package com.yasir.iustthread.viewmodel

import android.net.Uri
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.yasir.iustthread.model.ThreadModel
import com.yasir.iustthread.model.UserModel
import java.util.UUID

class UserViewModel : ViewModel() {

    private val database = FirebaseDatabase.getInstance()
    val threadRef = database.getReference("threads")
    val usersRef = database.getReference("Users")

    private val _threads = MutableLiveData(listOf<ThreadModel>())
    val threads:LiveData<List<ThreadModel>> get() = _threads
    //follower ids
    private val _followersList = MutableLiveData(listOf<String>())
    val followersList:LiveData<List<String>> get() = _followersList
    //following ids
    private val _followingList = MutableLiveData(listOf<String>())
    val followingList:LiveData<List<String>> get() = _followingList

    private val _users = MutableLiveData(UserModel())
    val users: MutableLiveData<UserModel> get() = _users

    fun fetchUser(uid:String){
        usersRef.child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(UserModel::class.java)
                _users.postValue(user)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
    fun fetchThreads(uid:String){
        threadRef.orderByChild("userId").equalTo(uid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val threadList = snapshot.children.mapNotNull {
                    it.getValue(ThreadModel::class.java)
                }
                _threads.postValue(threadList)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
    private val firestoreDb = Firebase.firestore
    fun followUsers(userid: String,currentUserId:String){
        val ref = firestoreDb.collection("following").document(currentUserId)
        val followerRef = firestoreDb.collection("followers").document(userid)
        ref.update("followingIds",FieldValue.arrayUnion(userid))
        followerRef.update("followerIds",FieldValue.arrayUnion(currentUserId))
    }
    fun getFollowers(userid: String){
       firestoreDb.collection("followers").document(userid)
           .addSnapshotListener{ value,error->
               val followerIds = value?.get("followerIds") as? List<String>?: listOf()
               _followersList.postValue(followerIds)
           }
    }
    fun getFollowing(userid: String){
        firestoreDb.collection("following").document(userid)
            .addSnapshotListener{ value,error->
                val followerIds = value?.get("followingIds") as? List<String>?: listOf()
                _followingList.postValue(followerIds)
            }
    }
}