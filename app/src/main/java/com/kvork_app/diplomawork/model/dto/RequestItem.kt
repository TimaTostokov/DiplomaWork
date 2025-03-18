package com.kvork_app.diplomawork.model.dto

data class RequestItem(
    val id: String = "",
    val dateOfRegistration: String = "",
    val address: String = "",
    val contact: String = "",
    val description: String = "",
    val status: String = "",
    val masterFio: String = ""
)
