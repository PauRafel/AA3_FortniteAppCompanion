package com.example.aa2_paurafeldiazhernandez

// Data class que representa una respuesta a un post

data class Reply(
    val id: String = "",
    val postId: String = "",
    val userEmail: String = "",
    val content: String = "",
    val timestamp: Long = 0
) {
    constructor() : this("", "", "", "", 0)
}