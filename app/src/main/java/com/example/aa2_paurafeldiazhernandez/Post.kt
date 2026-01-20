package com.example.aa2_paurafeldiazhernandez


// Data class que representa un post en el foro

data class Post(
    val id: String = "",
    val userEmail: String = "",
    val title: String = "",
    val content: String = "",
    val timestamp: Long = 0,
) {

    constructor() : this("", "", "", "", 0 )
}