package com.yasir.iustthread.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.MODE_PRIVATE

object SharedPref {
    @SuppressLint("CommitPrefEdits")
    fun storeData(
        name:String,
        email:String,
        bio:String,
        userName:String,
        imageUrl:String,
        context: Context
    ){
        val sharedPreferences = context.getSharedPreferences("Users",MODE_PRIVATE)
        val  editor = sharedPreferences.edit()
        editor.putString("name",name)
        editor.putString("email",email)
        editor.putString("bio",bio)
        editor.putString("userName",userName)
        editor.putString("imageUrl",imageUrl)
        editor.apply()
    }

    fun getUserName(context:Context):String{
        val sharedPreferences = context.getSharedPreferences("Users",MODE_PRIVATE)
        return sharedPreferences.getString("userName","")!!
    }

    fun getName(context:Context):String{
        val sharedPreferences = context.getSharedPreferences("Users",MODE_PRIVATE)
        return sharedPreferences.getString("name","")!!
    }

    fun getEmail(context:Context):String{
        val sharedPreferences = context.getSharedPreferences("Users",MODE_PRIVATE)
        return sharedPreferences.getString("email","")!!
    }

    fun getBio(context:Context):String{
        val sharedPreferences = context.getSharedPreferences("Users",MODE_PRIVATE)
        return sharedPreferences.getString("Bio","")!!
    }

    fun getImageUrl(context:Context):String{
        val sharedPreferences = context.getSharedPreferences("Users",MODE_PRIVATE)
        return sharedPreferences.getString("imageUrl","")!!
    }

}