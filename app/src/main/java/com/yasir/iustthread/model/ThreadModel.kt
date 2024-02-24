package com.yasir.iustthread.model

data class ThreadModel(
    val thread:String="",
    val image: String="",
    val userId:String="",
    val timeStamp:String="",
    val likedBy: List<String> = emptyList(),
    val likes:Int,
    val comments:String=""
){
    constructor() : this("","", "", "", emptyList(), 0, "")
}
